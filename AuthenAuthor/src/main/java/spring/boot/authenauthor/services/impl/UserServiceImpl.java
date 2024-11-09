package spring.boot.authenauthor.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.reactive.TransactionalOperator;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;
import spring.boot.authenauthor.dtos.UserDTO;
import spring.boot.authenauthor.entities.Permission;
import spring.boot.authenauthor.entities.UserPermission;
import spring.boot.authenauthor.entities.Users;
import spring.boot.authenauthor.exceptions.AppException;
import spring.boot.authenauthor.exceptions.ErrorCode;
import spring.boot.authenauthor.models.pojos.CustomUserDetails;
import spring.boot.authenauthor.models.pojos.CustomWebAuthenticationDetails;
import spring.boot.authenauthor.models.requests.UserRegisterRequest;
import spring.boot.authenauthor.models.response.UserLoginResponse;
import spring.boot.authenauthor.models.response.UserRegisterResponse;
import spring.boot.authenauthor.models.response.UserResponse;
import spring.boot.authenauthor.repositories.*;
import spring.boot.authenauthor.services.ITokenService;
import spring.boot.authenauthor.services.IUserService;
//import spring.boot.authenauthor.utils.AuthenticationUtils;
import spring.boot.authenauthor.utils.JwtTokenUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;

import static org.springframework.security.core.context.ReactiveSecurityContextHolder.getContext;


@Log4j2
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements IUserService {
    private final UserRepository userRepository;
    private final UsersPermissionRepository usersPermissionRepository;
    private final PasswordEncoder passwordEncoder;
    private final TransactionalOperator operator;
    private final ReactiveAuthenticationManager authenticationManager;
    private final JwtTokenUtils jwtTokenUtil;
    private final ITokenService tokenService;




//    @Transactional
//@Transactional
    @Override
    public Mono<UserRegisterResponse> createUser(UserRegisterRequest userRegisterRequest) {
        Integer permissionId = userRegisterRequest.getPermissionId() == null ? 1 : userRegisterRequest.getPermissionId();
        String email = userRegisterRequest.getEmail();
        log.error( "userRegisterRequest {}:",userRegisterRequest.toString());

        // Kiểm tra sự tồn tại của username
        return userRepository.existsByEmail(email)
                .flatMap(exists -> {
                    if (exists) {
                        // Trả về lỗi nếu username đã tồn tại
                        return Mono.error(new AppException(ErrorCode.USER_EXISTED));
                    }
//                    return Mono.error(new AppException(ErrorCode.USER_EXISTED, "email đã tồn tại: " + email));

//                    log.error("userRegisterRequest :{}", userRegisterRequest);

                    // Tạo user mới và lưu user cùng quyền trong một giao dịch

//
                    Users newUser = Users.builder()
                            .name(userRegisterRequest.getName())
                            .phoneNumber(userRegisterRequest.getPhoneNumber())
                            .email(email)
                            .build();

                    String encodedPassword = passwordEncoder.encode(userRegisterRequest.getPassword());
                    newUser.setPassword(encodedPassword);

                    // Sử dụng .as(operator::transactional)
                    return userRepository.save(newUser)
                            .onErrorResume(e -> {
                                e.printStackTrace();
                                return Mono.error(new AppException(ErrorCode.DATABASE_SAVE_ERROR, "Lỗi khi lưu người dùng"));
                            })
                            .flatMap(savedUser -> {
                                // Tạo quyền cho user
                                UserPermission userPermissions = new UserPermission();
                                userPermissions.setUserId(savedUser.getId());
                                userPermissions.setPermissionId(permissionId);

                                // Lưu quyền vào database và trả về kết quả cuối cùng
                                return usersPermissionRepository.save(userPermissions)
                                        .onErrorResume(e -> {
                                            e.printStackTrace();
                                            return Mono.error(new AppException(ErrorCode.DATABASE_SAVE_ERROR, "Lỗi khi lưu permission"));
                                        })
                                        .thenReturn(UserRegisterResponse.builder()
                                                .id(savedUser.getId())
                                                .name(savedUser.getName())
                                                .username(savedUser.getUsername())
                                                .phoneNumber(savedUser.getPhoneNumber())
                                                .email(savedUser.getEmail())
                                                .build());
                            })
//                            .as(operator::transactional)
                            .onErrorResume(AppException.class,e -> {
                                e.printStackTrace();
                             throw e;
                            });
                }) ;
    }




//    @Transactional
    @Override
    public Mono<UserLoginResponse> login(String email, String password, ServerHttpRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);

        return authenticationManager.authenticate(authenticationToken)
                .onErrorResume(AppException.class,ex -> {
                    ex.printStackTrace();
                    log.error("Login error: {}", ex.getMessage());
                     throw ex;
//                                return Mono.error(new AppException(ErrorCode.DATABASE_SAVE_ERROR,"loi tao token")); // Trả về lỗi
                })
                .flatMap(authentication -> {
                    log.error("user : {}", authentication.getAuthorities());
                    log.error("user : {}", authentication.getPrincipal().toString());
                    log.error("user : {}", authentication);

                    List<String> permisstionList = authentication.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .toList(); // Thu thập vào List<String>

                    // Chuyển đổi principal về CustomUserDetails
                    CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
                    Users users = customUserDetails.getUser();

                    // Tạo token JWT
                    return jwtTokenUtil.generateToken(authentication)
                            .onErrorResume(ex -> {
                                ex.printStackTrace();
                                log.error("Login error: {}", ex.getMessage());
                                return Mono.error(ex);
//                                return Mono.error(new AppException(ErrorCode.DATABASE_SAVE_ERROR,"loi tao token")); // Trả về lỗi
                            })
                            .flatMap(token -> {
                                String userAgent = request.getHeaders().getFirst(HttpHeaders.USER_AGENT);
                                log.error("headers: {}", userAgent);
                                // Kiểm tra nếu là thiết bị di động
                                return isMobileDevice(userAgent)
                                        .flatMap(checkMobile -> {
                                            // Gọi dịch vụ để thêm token
                                            return tokenService.addToken(email, token, checkMobile)
                                                    .thenReturn(
                                                            UserLoginResponse.builder()
                                                                    .user(UserResponse.toUserResponse(users))
                                                                    .token(token)
                                                                    .permissionList(permisstionList)
                                                                    .build() // Đảm bảo bạn build đối tượng UserLoginResponse
                                                    ).onErrorResume( AppException.class, e->{
                                                        e.printStackTrace();
                                                        return Mono.error(e);

                                                    });
                                        });
                            });
                })   ;
//                .as(operator::transactional);

    }

    private Mono<Boolean> isMobileDevice(String userAgent) {
        return Mono.just(userAgent != null && userAgent.toLowerCase().contains("mobile")); // Kiểm tra User-Agent
    }

}








