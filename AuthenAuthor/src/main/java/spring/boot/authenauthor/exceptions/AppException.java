package spring.boot.authenauthor.exceptions;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Log4j2
@Getter
@Setter
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class AppException extends RuntimeException {

    private ErrorCode errorCode;
    private Object data;
    private Object responseData;
    private String detailMess;
//    private String systemErrorMess;


    // Constructor có tham số dữ liệu
    public AppException(ErrorCode errorCode, String mess) {
//        super(mess);
        log.error("mess",mess);
        this.errorCode = errorCode;
        this.detailMess = mess;


//        this.systemErrorMess = mess;
    }

    // Constructor không có tham số dữ liệu
    public AppException(ErrorCode errorCode) {
//        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }



    public AppException(ErrorCode errorCode,Object object) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.responseData = object;
    }



}
