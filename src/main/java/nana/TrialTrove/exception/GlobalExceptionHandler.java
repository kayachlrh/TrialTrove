package nana.TrialTrove.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CapacityExceededException.class)
    public ResponseEntity<String> handleCapacityExceeded(CapacityExceededException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("정원이 초과되었습니다.");
    }
}

