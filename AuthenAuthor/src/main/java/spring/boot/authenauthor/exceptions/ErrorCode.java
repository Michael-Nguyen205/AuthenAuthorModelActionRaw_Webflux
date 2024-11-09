package spring.boot.authenauthor.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    USER_EXISTED(1002, "error.user_existed", HttpStatus.BAD_REQUEST),
    DATABASE_DELETE_ERROR(1003, "error.sql_error", HttpStatus.BAD_REQUEST),
    DUPLICATE_DATA(1006, "error.duplicate_data", HttpStatus.CONFLICT),
    UNCATEGORIZED_EXCEPTION(9999, "error.uncategorized", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "error.invalid_key", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(1003, "error.username_invalid", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1004, "error.invalid_password", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1005, "error.user_not_existed", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1006, "error.unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, "error.unauthorized", HttpStatus.FORBIDDEN),
    OUT_OF_STOCK(1009, "error.out_of_stock", HttpStatus.BAD_REQUEST),
    DATA_NOT_FOUND(400, "error.data_not_found", HttpStatus.BAD_REQUEST),
    ERROR_GENERATE_TOKEN(402, "error.error_generate_token", HttpStatus.BAD_REQUEST),
    DATABASE_SAVE_ERROR(2001, "error.database_save_error", HttpStatus.INTERNAL_SERVER_ERROR),
    JSON_MAP_ERRO(1000, "error.json_map_error", HttpStatus.BAD_REQUEST);

    private final int code;
    private final String message;
    private final HttpStatus statusCode;

    ErrorCode(int code, String message, HttpStatus statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}
