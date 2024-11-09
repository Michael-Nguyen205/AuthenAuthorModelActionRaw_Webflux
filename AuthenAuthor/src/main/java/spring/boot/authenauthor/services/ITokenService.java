package spring.boot.authenauthor.services;



import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import spring.boot.authenauthor.entities.Tokens;
import spring.boot.authenauthor.entities.Users;


public interface ITokenService {
    Mono<Tokens> addToken(@NotBlank(message = "Phone number is required") String userDetails, String token, boolean isMobileDevice);
    Tokens refreshToken(String refreshToken, Users user) throws Exception;
}
