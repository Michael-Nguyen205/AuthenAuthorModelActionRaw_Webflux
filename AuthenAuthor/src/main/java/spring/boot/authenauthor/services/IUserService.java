package spring.boot.authenauthor.services;


import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;
import spring.boot.authenauthor.dtos.UserDTO;
import spring.boot.authenauthor.entities.Users;
import spring.boot.authenauthor.models.requests.UserRegisterRequest;
import spring.boot.authenauthor.dtos.UserDTO;
import spring.boot.authenauthor.models.response.UserLoginResponse;
import spring.boot.authenauthor.models.response.UserRegisterResponse;


public interface IUserService {
//    Users createUser(UserRegisterRequest userRegisterRequest) throws Exception;

    Mono<UserRegisterResponse> createUser(UserRegisterRequest userRegisterRequest);

    Mono<UserLoginResponse> login(String email, String password , ServerHttpRequest request ) ;


//    String changePass( String phoneNumber , String password) throws Exception;
//    Users getUserDetailsFro
}