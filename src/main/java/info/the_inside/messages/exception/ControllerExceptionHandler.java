package info.the_inside.messages.exception;

import static info.the_inside.messages.exception.ErrorMessages.FAILED_LOGIN_EX;
import static info.the_inside.messages.exception.ErrorMessages.INVALID_HISTORY_SIZE_EX;
import static info.the_inside.messages.exception.ErrorMessages.SENDER_ALREADY_EXISTS_EX;
import static info.the_inside.messages.exception.ErrorMessages.SENDER_NOT_FOUND_EX;
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
    public final ResponseEntity<String> handleSenderNotFoundEx(AccountNotFoundException anfe) {
        // If no sender found, we return 404
        return ResponseEntity.status(NOT_FOUND)
                .header(WARNING, SENDER_NOT_FOUND_EX)
                .body(anfe.getMessage());
    }

    @ExceptionHandler(FailedLoginException.class)
    public final ResponseEntity<String> handleFailedLoginEx(FailedLoginException fle) {
        // If login fails, we return 400
        return ResponseEntity.status(BAD_REQUEST)
                .header(WARNING, FAILED_LOGIN_EX)
                .body(fle.getMessage());
    }

    @ExceptionHandler(NumberFormatException.class)
    public final ResponseEntity<String> handleNumberFormatEx(NumberFormatException nfe) {
        // If historySize is invalid, we return 400
        return ResponseEntity.status(BAD_REQUEST)
                .header(WARNING, INVALID_HISTORY_SIZE_EX)
                .body(nfe.getMessage());
    }

    @ExceptionHandler(AccountException.class)
    public final ResponseEntity<String> handleAccountEx(AccountException ae) {
        // If sender already exists, we return 409
        return ResponseEntity.status(CONFLICT)
                .header(WARNING, SENDER_ALREADY_EXISTS_EX)
                .body(ae.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<String> handleOtherEx(Exception e) {
        // Otherwise, return 500
        return ResponseEntity.status(INTERNAL_SERVER_ERROR)
                .body(e.getMessage());
    }

}
