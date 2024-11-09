//package spring.boot.authenauthor.handler;
//
//
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.reactive.function.server.ServerRequest;
//import org.springframework.web.reactive.function.server.ServerResponse;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Component;
//
//
//import reactor.core.publisher.Mono;
//import spring.boot.authenauthor.entities.Users;
//import spring.boot.authenauthor.exceptions.AppException;
//import spring.boot.authenauthor.models.requests.UserRegisterRequest;
//import spring.boot.authenauthor.services.IUserService;
//
//@Component
//@Validated
//@RequiredArgsConstructor
//public class UsersHandler {
//
//    private final IUserService userService;
//
//    public Mono<ServerResponse> createUser(@Valid ServerRequest request) {
//        return request.bodyToMono(UserRegisterRequest.class).flatMap(
//                userRegisterRequest ->{
//                    if(!userRegisterRequest.getRetypePassword().equals(userRegisterRequest.getPassword()) ){
//                        return  Mono.error(new RuntimeException(" password khong khop"));
//                    }
//                    return userService.createUser(userRegisterRequest)
//                            .flatMap(result -> ServerResponse.ok()
//                                    .contentType(MediaType.APPLICATION_JSON).bodyValue(result)) // Trả về ServerResponse nếu thành công
//                            .onErrorResume(AppException.class, e -> {
//                                // Trả về Mono.error hoặc ném lại ngoại lệ
//                                throw e;
//                            });
//                }
//        );
//    }
//
//
//
//
//
//
//
////    @PostMapping("/register")
////    @Transactional
////    public ResponseEntity<?> createUser(
////            @Valid @RequestBody UserRegisterRequest userRegisterRequest,
////            BindingResult result
////    ) {
////        if (result.hasErrors()) {
////            throw new RuntimeException("loix");
////        }
////
////        if (!userRegisterRequest.getPassword().equals(userRegisterRequest.getRetypePassword())) {
////            throw new RuntimeException("loix");
////        }
////
////        try {
////            Users user = userService.createUser(userRegisterRequest);
////            return ResponseEntity.ok(user);
////        } catch (Exception e) {
////            e.printStackTrace();
////            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("loi");
////        }
////    }
//
//}
