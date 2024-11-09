//package spring.boot.authenauthor.configurations.routes;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.reactive.function.server.RouterFunction;
//import org.springframework.web.reactive.function.server.ServerResponse;
//import spring.boot.authenauthor.handler.AuthorHandler;
//import spring.boot.authenauthor.handler.ProductHandler;
//
//import static org.springframework.web.reactive.function.server.RouterFunctions.route;
//
//@Configuration
//public class AuthorRouter {
//@Value("${api.prefix}")
//private String apiPrefix;
//
//    @Bean
//    public RouterFunction<ServerResponse> authorRoutes(AuthorHandler authorHandler) {
//        return route()
////                .POST(apiPrefix + "/author/createPermission", authorHandler::createPermission) // Sử dụng apiPrefix đúng cách
//                .build();
//    }
//}
