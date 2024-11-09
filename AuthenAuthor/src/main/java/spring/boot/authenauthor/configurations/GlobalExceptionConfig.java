//package spring.boot.authenauthor.configurations;
//
//import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.annotation.Order;
//import org.springframework.core.io.buffer.DataBuffer;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//import spring.boot.authenauthor.exceptions.GlobalExceptionHandler;
//
//@Configuration
//public class GlobalExceptionConfig {
//
//    @Bean
//    @Order(-2)  // Đảm bảo GlobalErrorHandler được ưu tiên hơn các handler khác
//    public ErrorWebExceptionHandler globalErrorHandler(GlobalExceptionHandler globalExceptionHandler) {
//        return (ServerWebExchange exchange, Throwable ex) -> {
//            exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
//            exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
//            // Tạo nội dung phản hồi JSON chứa thông tin lỗi
//            String errorMessage = "{\"error\": \"An unexpected error occurred\", \"message\": \"" + ex.getMessage() + "\"}";
//            DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(errorMessage.getBytes());
//            return exchange.getResponse().writeWith(Mono.just(buffer));
//        };
//    }
//}
//
