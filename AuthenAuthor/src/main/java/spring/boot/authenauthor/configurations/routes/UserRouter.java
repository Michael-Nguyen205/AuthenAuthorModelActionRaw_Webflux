//package spring.boot.authenauthor.configurations.routes;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.reactive.function.server.RouterFunction;
//import org.springframework.web.reactive.function.server.ServerResponse;
//import spring.boot.authenauthor.handler.UsersHandler;
//
//import static org.springframework.web.reactive.function.server.RouterFunctions.route;
//
//@Configuration
//public class UserRouter {
//    @Value("${api.prefix}")
//    private String apiPrefix;
//    @Bean
//    public RouterFunction<ServerResponse> userRoutes(UsersHandler userHandler) {
//        return route()
////                .GET("/users", userHandler::getAllUsers) // Route để lấy tất cả người dùng
////                .GET("/users/{id}", userHandler::getUserById) // Route để lấy người dùng theo id
//                .POST(apiPrefix + "/users/register", userHandler::createUser) // Sử dụng apiPrefix đúng cách
//
//
//                .build();
//    }
//}
