package spring.boot.authenauthor.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;
import spring.boot.authenauthor.entities.Users;
import spring.boot.authenauthor.exceptions.AppException;
import spring.boot.authenauthor.models.requests.UserLoginRequest;
import spring.boot.authenauthor.models.requests.UserRegisterRequest;
import spring.boot.authenauthor.models.response.UserLoginResponse;
import spring.boot.authenauthor.models.response.UserRegisterResponse;
import spring.boot.authenauthor.services.IUserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/users")
public class UserController {

    private final IUserService userService;

    @PostMapping("/register")
    public Mono<ResponseEntity<UserRegisterResponse>> createUser(@Valid @RequestBody UserRegisterRequest userRegisterRequest) {
        if (!userRegisterRequest.getRetypePassword().equals(userRegisterRequest.getPassword())) {
            return Mono.error(new RuntimeException("Password không khớp"));
        }
        return userService.createUser(userRegisterRequest)
                .map(result -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(result))
                .onErrorResume(AppException.class, e -> {
                  throw e;
                });
    }

    @PostMapping("/login")
    public  Mono<ResponseEntity<UserLoginResponse>> login(@Valid @RequestBody UserLoginRequest userLoginRequest ,
                                                          ServerHttpRequest request){
        return userService.login(userLoginRequest.getEmail(),userLoginRequest.getPassword(),request).map(
                result -> ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(result))
                 .onErrorResume(AppException.class, e -> {
            throw e;
        });


    }


}
