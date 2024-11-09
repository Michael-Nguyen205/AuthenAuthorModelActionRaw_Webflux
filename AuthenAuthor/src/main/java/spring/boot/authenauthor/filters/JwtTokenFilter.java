package spring.boot.authenauthor.filters;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import spring.boot.authenauthor.exceptions.AppException;
import spring.boot.authenauthor.exceptions.ErrorCode;
import spring.boot.authenauthor.models.pojos.CustomWebAuthenticationDetails;
import spring.boot.authenauthor.utils.JwtTokenUtils;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.web.reactive.function.server.HandlerStrategies;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Log4j2
public class JwtTokenFilter implements WebFilter {

    @Value("${api.prefix}")
    private String apiPrefix;

    private final ReactiveUserDetailsService userDetailsService;
    private final JwtTokenUtils jwtTokenUtil;




    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        long startTime = System.currentTimeMillis();
        final String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

        log.info("Processing request: {}", exchange.getRequest().getURI());

        // Kiểm tra bypass token trước
        return isBypassToken(exchange)
                .flatMap(isBypass -> {
                    if (isBypass) {
                        log.error(" đã isBypass");
                        return chain.filter(exchange).onErrorResume(e->{
                            e.printStackTrace();
                            return null;
                        });
                    }
                    log.info("JWT validation required");

                    // Kiểm tra Authorization header và xử lý nếu header không hợp lệ
                    return Mono.justOrEmpty(authHeader)
                            .filter(header -> header != null && header.startsWith("Bearer"))
                            .switchIfEmpty(Mono.error(new AppException(ErrorCode.UNAUTHENTICATED, "token không hợp lệ")))
                            .map(header -> {
                                String token = header.substring(7); // Lấy phần token sau "Bearer "

                                return token; // Trả về token đã được lấy
                            })
                            .flatMap(token -> {
                                return authenticateUser(token, exchange);
                            })
                            .switchIfEmpty(Mono.error(new AppException(ErrorCode.UNAUTHENTICATED, "lỗi xác thực user với token ")))
                            .flatMap(authentication -> {
                                log.error("authentication:{}",authentication);

                                return chain.filter(exchange)
                                        .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication))
                                        .doOnEach(signal -> {
                                            log.error("SecurityContext authentication: {}", authentication);
                                        });
                            });
                })
                .onErrorResume(AppException.class, e -> {
                    log.error("Authentication error: ", e);
                    throw new AppException(ErrorCode.UNAUTHENTICATED);
                })
                .onErrorResume( e -> {
                    log.error("Authentication error: ", e);
                    try {
                        throw e;
                    } catch (Throwable ex) {
                        throw new RuntimeException(ex);
                    }
                })
                .doFinally(signalType -> logRequestDuration(exchange, startTime));
    }




    private Mono<Authentication> authenticateUser(String token, ServerWebExchange exchange) {
        return jwtTokenUtil.extractByEmail(token)
                .flatMap(username -> userDetailsService.findByUsername(username)
                        .flatMap(userDetails ->
                                jwtTokenUtil.validateToken(token, userDetails)
                                        .filter(valid -> valid) // Chỉ giữ lại khi token hợp lệ
                                        .flatMap(valid -> {
                                            UsernamePasswordAuthenticationToken authenticationToken =
                                                    new UsernamePasswordAuthenticationToken(
                                                            userDetails,
                                                            null,
                                                            userDetails.getAuthorities()
                                                    );
                                            ServerRequest request = ServerRequest.create(exchange, HandlerStrategies.withDefaults().messageReaders());
                                            CustomWebAuthenticationDetails details = new CustomWebAuthenticationDetails(request);
                                            authenticationToken.setDetails(details);
                                            return Mono.just(authenticationToken); // Trả về Mono<Authentication>
                                        })
                                        .switchIfEmpty(Mono.error(new AppException(ErrorCode.UNAUTHENTICATED,"loi validateToken"))) // Xử lý khi không có giá trị


                        )
                );


    }

    private Mono<Boolean> isBypassToken(ServerWebExchange exchange) {
        final List<Pair<String, String>> bypassTokens = Arrays.asList(
                Pair.of("/ws/info", "GET"),
                Pair.of("/ws", "GET"),
                Pair.of("/users/login","POST"),
                Pair.of("/posts", "GET"),

                Pair.of("/users/register", "POST")
        );

        String requestPath = exchange.getRequest().getPath().toString();
        String requestMethod = exchange.getRequest().getMethod().name();

        return Mono.just(bypassTokens.stream()
                .anyMatch(bypassToken -> requestPath.contains(bypassToken.getFirst()) &&
                        requestMethod.equalsIgnoreCase(bypassToken.getSecond())));
    }

    private void logRequestDuration(ServerWebExchange exchange, long startTime) {
        long duration = System.currentTimeMillis() - startTime;
        log.info("{} run time: {} ms", exchange.getRequest().getURI(), duration);
    }


}
