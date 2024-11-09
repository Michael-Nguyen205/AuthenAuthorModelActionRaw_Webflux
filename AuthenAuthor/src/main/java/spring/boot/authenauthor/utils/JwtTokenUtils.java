package spring.boot.authenauthor.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.security.Key;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
import spring.boot.authenauthor.exceptions.AppException;
import spring.boot.authenauthor.exceptions.ErrorCode;

@Log4j2
@Component
@RequiredArgsConstructor
public class JwtTokenUtils {
    @Value("${jwt.expiration}")
    private int expiration; //save to an environment variable

    @Value("${jwt.secretKey}")
    private String secretKey;

    public Mono<String> generateToken(Authentication authentication) {
        return Mono.fromCallable(() -> {
            // Lấy thông tin từ đối tượng người dùng đã được xác thực
            UserDetails user = (UserDetails) authentication.getPrincipal();

            // Tạo claims chứa các thông tin bạn muốn lưu trữ trong token
            Map<String, Object> claims = new HashMap<>();
            claims.put("username", user.getUsername());
            claims.put("password", user.getPassword());

            // Thêm các quyền của người dùng vào claims (nếu cần)
            List<String> roles = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());
            log.error("roles {}:", roles);
            claims.put("roles", roles);

            // Tạo token với các claims và thông tin cần thiết
            return Jwts.builder()
                    .setClaims(claims) // Thêm claims vào token
                    .setSubject(user.getUsername()) // Đặt chủ đề (thông thường là email hoặc tên đăng nhập)
                    .setIssuedAt(new Date(System.currentTimeMillis())) // Đặt thời gian phát hành token
                    .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000L)) // Đặt thời gian hết hạn token
                    .signWith(getSignInKey(), SignatureAlgorithm.HS256) // Ký token với secret key và thuật toán HS256
                    .compact(); // Hoàn thiện quá trình tạo token
        }).onErrorMap(e -> new AppException(ErrorCode.ERROR_GENERATE_TOKEN, "Lỗi tạo token"));
    }
//public Mono<String> generateToken(Authentication authentication) {
//    return Mono.fromCallable(() -> {
//        // Giả lập một lỗi
//        if (true) { // Điều kiện này luôn đúng, bạn có thể thay đổi theo ý muốn
//            throw new AppException(ErrorCode.ERROR_GENERATE_TOKEN, "Lưu token lỗi");
//        }
//
//        // Lấy thông tin từ đối tượng người dùng đã được xác thực
//        UserDetails user = (UserDetails) authentication.getPrincipal();
//
//        // Tạo claims chứa các thông tin bạn muốn lưu trữ trong token
//        Map<String, Object> claims = new HashMap<>();
//        claims.put("username", user.getUsername());
//        claims.put("password", user.getPassword());
//
//        // Thêm các quyền của người dùng vào claims (nếu cần)
//        List<String> roles = authentication.getAuthorities().stream()
//                .map(GrantedAuthority::getAuthority)
//                .collect(Collectors.toList());
//        log.error("roles {}:", roles);
//        claims.put("roles", roles);
//
//        // Tạo token với các claims và thông tin cần thiết
//        return Jwts.builder()
//                .setClaims(claims) // Thêm claims vào token
//                .setSubject(user.getUsername()) // Đặt chủ đề (thông thường là email hoặc tên đăng nhập)
//                .setIssuedAt(new Date(System.currentTimeMillis())) // Đặt thời gian phát hành token
//                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000L)) // Đặt thời gian hết hạn token
//                .signWith(getSignInKey(), SignatureAlgorithm.HS256) // Ký token với secret key và thuật toán HS256
//                .compact(); // Hoàn thiện quá trình tạo token
//    }).onErrorResume(e ->{ throw new AppException(ErrorCode.ERROR_GENERATE_TOKEN, "Lưu token lỗi");
//    }
//    );
//}


    private Key getSignInKey() {
        byte[] bytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(bytes);
    }

    private String generateSecretKey() {
        SecureRandom random = new SecureRandom();
        byte[] keyBytes = new byte[32]; // 256-bit key
        random.nextBytes(keyBytes);
        return Encoders.BASE64.encode(keyBytes);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public <T> Mono<T> extractClaim(String token, Function<Claims, T> claimsResolver) {
        return Mono.fromCallable(() -> claimsResolver.apply(this.extractAllClaims(token)));
    }

    //check expiration
    public Mono<Boolean> isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration)
                .map(expirationDate -> expirationDate.before(new Date()));
    }

    public Mono<String> extractPhoneNumber(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Mono<String> extractByEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Mono<Boolean> validateToken(String token, UserDetails userDetails) {
        return extractByEmail(token)
                .map(username -> username.equals(userDetails.getUsername()))
                .zipWith(isTokenExpired(token), (isUsernameValid, isExpired) -> isUsernameValid && !isExpired);
    }
}
