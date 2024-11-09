package spring.boot.authenauthor.exceptions;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.NoSuchMessageException;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import spring.boot.authenauthor.models.response.ApiResponse;
import org.springframework.security.access.AccessDeniedException;
import spring.boot.authenauthor.utils.LocalizationUtils;


import java.sql.SQLException;

@Slf4j
@ControllerAdvice
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

  private final  LocalizationUtils localizationUtils;
//
//    public Mono<Void> handleAppException(ServerWebExchange exchange, Throwable ex) {
//        exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
//        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
//        // Tạo nội dung phản hồi JSON chứa thông tin lỗi
//        String errorMessage = "{\"error\": \"An unexpected error occurred\", \"message\": \"" + ex.getMessage() + "\"}";
//        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(errorMessage.getBytes());
//        return exchange.getResponse().writeWith(Mono.just(buffer));
//    }

    @ExceptionHandler(value = Exception.class)
    public Mono<ResponseEntity<ApiResponse>> handlingRuntimeException (RuntimeException exception){
        log.error("An error occurred: ", exception);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
        apiResponse.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage());
        return Mono.just(ResponseEntity.badRequest().body(apiResponse));
    }

    @ExceptionHandler(value = SQLException.class)
    public Mono<ResponseEntity<ApiResponse>> handlingSQLException (SQLException exception){
        log.error("An error occurred: ", exception);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
        apiResponse.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage());
        return Mono.just(ResponseEntity.badRequest().body(apiResponse));
    }


    @ExceptionHandler(value = TransactionException.class)
    public Mono<ResponseEntity<ApiResponse>> handlingTransactionException(TransactionException exception) {
        log.error("An error occurred: ", exception);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
        apiResponse.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage());
        return Mono.just(ResponseEntity.badRequest().body(apiResponse));
    }






    @ExceptionHandler(value = AppException.class)
    public Mono<ResponseEntity<ApiResponse<?>>> handlingAppException(AppException exception,ServerWebExchange exchange) {
        ApiResponse<Object> apiResponse = new ApiResponse<>();
        ErrorCode errorCode = exception.getErrorCode();
//        apiResponse.setMessage(errorCode.getMessage());
        try {
            apiResponse.setMessage(localizationUtils.getLocalizedMessage(errorCode.getMessage(), exchange));
        } catch (NoSuchMessageException e) {
            apiResponse.setMessage("An error occurred."); // Thông báo mặc định
        }
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setDetailMess(exception.getDetailMess());
        // Lấy dữ liệu từ AppException (nếu có) và đặt vào ApiResponse
        Object response = exception.getResponseData();
        apiResponse.setResult(response);
//        return Mono.just(ResponseEntity.status(errorCode.getStatusCode()).body(apiResponse));
        return Mono.just(ResponseEntity.status(exception.getErrorCode().getStatusCode()).body(apiResponse));
    }



    @ExceptionHandler(value = AccessDeniedException.class)
    public Mono<ResponseEntity<ApiResponse>> handlingAccessDeniedException(AccessDeniedException exception) {
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());
        return Mono.just(ResponseEntity.status(errorCode.getStatusCode()).body(apiResponse));
    }



    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Mono<ResponseEntity<ApiResponse>> handlingValidation(MethodArgumentNotValidException exception) {
        String enumKey = exception.getFieldError().getDefaultMessage();
        ErrorCode errorCode;
log.error("loi validateeeeee");
        try {
            errorCode = ErrorCode.valueOf(enumKey);
        } catch (IllegalArgumentException e) {
            errorCode = ErrorCode.INVALID_KEY;
        }
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());

        return Mono.just(ResponseEntity.status(errorCode.getStatusCode()).body(apiResponse));
    }

    @ExceptionHandler(value = WebExchangeBindException.class)
    public Mono<ResponseEntity<ApiResponse>> handlingValidation(WebExchangeBindException exception) {
        FieldError fieldError = exception.getFieldError();
        // Nếu có lỗi, lấy message và enum key
        String errorMessage = fieldError != null ? fieldError.getDefaultMessage() : "Validation error";
        String enumKey = fieldError != null ? fieldError.getField() : "INVALID_KEY"; // Sử dụng field hoặc "INVALID_KEY" làm mặc định

        log.error("WebExchangeBindException occurred: {}", exception.getMessage());
        log.error(" fieldError: {}", fieldError);

        ErrorCode errorCode;
        try {
            // Chuyển đổi từ enumKey nếu cần
            errorCode = ErrorCode.valueOf(enumKey.toUpperCase()); // Chuyển đổi thành chữ hoa nếu enum là chữ hoa
        } catch (IllegalArgumentException e) {
            errorCode = ErrorCode.INVALID_KEY; // Mặc định nếu không tìm thấy
        }



        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorMessage);

        return Mono.just(ResponseEntity.status(errorCode.getStatusCode()).body(apiResponse));
    }





    @PostConstruct
    public void init() {
        log.error("GlobalExceptionHandler is initialized");
    }
}
