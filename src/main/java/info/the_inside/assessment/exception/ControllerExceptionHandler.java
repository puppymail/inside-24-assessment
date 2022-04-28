package info.the_inside.assessment.exception;

import static info.the_inside.assessment.exception.ErrorMessages.FAILED_LOGIN_EX;
import static info.the_inside.assessment.exception.ErrorMessages.INVALID_HISTORY_SIZE_EX;
import static info.the_inside.assessment.exception.ErrorMessages.SENDER_ALREADY_EXISTS_EX;
import static info.the_inside.assessment.exception.ErrorMessages.SENDER_NOT_FOUND_EX;
import static org.springframework.http.HttpHeaders.WARNING;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.security.auth.login.AccountException;
import javax.security.auth.login.AccountNotFoundException;
import javax.security.auth.login.FailedLoginException;

@ControllerAdvice(basePackages = "info.the_inside.assessment.controller")
public class ControllerExceptionHandler {

    @ExceptionHandler(AccountNotFoundException.class)
    public final ResponseEntity<Exception> handleSenderNotFoundEx(AccountNotFoundException anfe) {
        return ResponseEntity.status(NOT_FOUND)
                .header(WARNING, SENDER_NOT_FOUND_EX)
                .body(anfe);
    }

    @ExceptionHandler(FailedLoginException.class)
    public final ResponseEntity<Exception> handleFailedLoginEx(FailedLoginException fle) {
        return ResponseEntity.status(BAD_REQUEST)
                .header(WARNING, FAILED_LOGIN_EX)
                .body(fle);
    }

    @ExceptionHandler(NumberFormatException.class)
    public final ResponseEntity<Exception> handleNumberFormatEx(NumberFormatException nfe) {
        return ResponseEntity.status(BAD_REQUEST)
                .header(WARNING, INVALID_HISTORY_SIZE_EX)
                .body(nfe);
    }

    @ExceptionHandler(AccountException.class)
    public final ResponseEntity<Exception> handleAccountEx(AccountException ae) {
        return ResponseEntity.status(CONFLICT)
                .header(WARNING, SENDER_ALREADY_EXISTS_EX)
                .body(ae);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Exception> handleOtherEx(Exception e) {
        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .body(e);
    }

}
