package spring.boot.authenauthor.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import spring.boot.authenauthor.entities.Tokens;
import spring.boot.authenauthor.entities.Users;
import spring.boot.authenauthor.exceptions.AppException;
import spring.boot.authenauthor.exceptions.ErrorCode;
import spring.boot.authenauthor.repositories.TokenRepository;
import spring.boot.authenauthor.repositories.UserRepository;
import spring.boot.authenauthor.services.ITokenService;
import spring.boot.authenauthor.utils.JwtTokenUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements ITokenService {
//    private static final int MAX_TOKENS = 3;

    @Value("${jwt.max-token}")
    private int MAX_TOKENS; //save to an environment variable

    @Value("${jwt.expiration}")
    private int expiration; //save to an environment variable

    @Value("${jwt.expiration-refresh-token}")
    private int expirationRefreshToken;

    private final UserRepository userRepository;

    private final TokenRepository tokenRepository;

    private final JwtTokenUtils jwtTokenUtil;



    @Override
    public Mono<Tokens> addToken(String email, String token, boolean isMobileDevice) {
        return userRepository.findByEmail(email)
                .switchIfEmpty(Mono.error(new RuntimeException("User not found")))
                .onErrorMap(e -> {
                    // Chuyển đổi ngoại lệ thành AppException
                    return new AppException(ErrorCode.DATABASE_SAVE_ERROR, "Lỗi khi lưu vào cơ sở dữ liệu: " + e.getMessage());
                })                .flatMap(user -> {
                    return tokenRepository.findByUserId(user.getId())
                            .collectList()
                            .flatMap(userTokens -> {
                                log.error("đang ở trong dâyyyy");
                                int tokenCount = userTokens.size();
                                if (tokenCount >= MAX_TOKENS) {
                                    log.error("MAX_TOKENS: {}",MAX_TOKENS);
                                    // Xóa token cũ
                                    return deleteOldToken(userTokens);
                                }
                                return Mono.empty(); // Không cần xóa token
                            })
                            .then(saveNewToken(user.getId(), token, isMobileDevice));
                });
    }

    private Mono<Void> deleteOldToken(List<Tokens> userTokens) {

        log.error("List<Tokens> : {} ",userTokens);
        Tokens tokenToDelete = userTokens.stream()
                .filter(userToken -> !userToken.isMobile())
                .findFirst()
                .orElse(userTokens.getFirst());
        return tokenRepository.delete(tokenToDelete).then(); // Xóa token
    }



    private Mono<Tokens> saveNewToken(String userId, String token, boolean isMobileDevice) {
        log.error("đang lưu token");
        LocalDateTime expirationDateTime = LocalDateTime.now().plusSeconds(expiration);
        Tokens newToken = Tokens.builder()
                .id(UUID.randomUUID().toString())
                .userId(userId)
                .token(token)
                .revoked(false)
                .expired(false)
                .tokenType("Bearer")
                .expirationDate(expirationDateTime)
                .isMobile(isMobileDevice)
                .refreshToken(UUID.randomUUID().toString())
                .refreshExpirationDate(LocalDateTime.now().plusSeconds(expirationRefreshToken))
                .build();
        return tokenRepository.save(newToken)
                .onErrorResume(e ->{
//            throw new AppException(ErrorCode.DATABASE_SAVE_ERROR,"lưu token lỗi");
                    e.printStackTrace();
            return Mono.error(new AppException(ErrorCode.DATABASE_SAVE_ERROR, "Lưu token lỗi"));
        });
    }



//    @Override
//    public Tokens addToken(String username, String token, boolean isMobileDevice) {
//
//         Optional<Users> userEXistting = userRepository.findByUsername(username);
//         Users user = userEXistting.get();
//
//
//         List<Tokens> userTokens = tokenRepository.findByUserId(user.getId());
//
//        int tokenCount = userTokens.size();
//        // Số lượng token vượt quá giới hạn, xóa một token cũ
//        if (tokenCount >= MAX_TOKENS) {
//            //kiểm tra xem trong danh sách userTokens có tồn tại ít nhất
//            //một token không phải là thiết bị di động (non-mobile)
//            boolean hasNonMobileToken = !userTokens.stream().allMatch(Tokens::isMobile);
//            Tokens tokenToDelete;
//            if (hasNonMobileToken) {
//                tokenToDelete = userTokens.stream()
//                        .filter(userToken -> !userToken.isMobile())
//                        .findFirst()
//                        .orElse(userTokens.get(0));
//            } else {
//                //tất cả các token đều là thiết bị di động,
//                //chúng ta sẽ xóa token đầu tiên trong danh sách
//                tokenToDelete = userTokens.get(0);
//            }
//            tokenRepository.delete(tokenToDelete);
//        }
//
//
//        long expirationInSeconds = expiration;
//        LocalDateTime expirationDateTime = LocalDateTime.now().plusSeconds(expirationInSeconds);
//
//
//        // Tạo mới một token cho người dùng
//        Tokens newToken = Tokens.builder()
//                .userId( user.getId())
//                .token(token)
//                .revoked(false)
//                .expired(false)
//                .tokenType("Bearer")
//                .expirationDate(expirationDateTime)
//                .isMobile(isMobileDevice)
//                .build();
//
//        newToken.setRefreshToken(UUID.randomUUID().toString());
//        newToken.setRefreshExpirationDate(LocalDateTime.now().plusSeconds(expirationRefreshToken));
//        tokenRepository.save(newToken);
//        return newToken;
//    }
//




    @Override
    public Tokens refreshToken(String refreshToken, Users user) throws Exception{
        Tokens existingToken = tokenRepository.findByRefreshToken(refreshToken);
        if(existingToken == null) {
            throw new RuntimeException(" loi");
        }
//
//
//        if(existingToken.getRefreshExpirationDate().compareTo(LocalDateTime.now()) < 0){
//            tokenRepository.delete(existingToken);
//            throw new AppException(ErrorCode.TOKEN_AUTHEN_ERRO);
//        }
//
//
//        String token = jwtTokenUtil.generateToken(user);
//        LocalDateTime expirationDateTime = LocalDateTime.now().plusSeconds(expiration);
//        existingToken.setExpirationDate(expirationDateTime);
//        existingToken.setToken(token);
//        existingToken.setRefreshToken(UUID.randomUUID().toString());
//        existingToken.setRefreshExpirationDate(LocalDateTime.now().plusSeconds(expirationRefreshToken));

        return existingToken;
    }

}
