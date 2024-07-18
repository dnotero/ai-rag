package ar.com.dno.ai.embeddings.spaces.adapters.input.web;


import ar.com.dno.ai.embeddings.spaces.usecases.exceptions.SpaceAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class SpaceExceptionHandler {

    @ExceptionHandler({SpaceAlreadyExistsException.class})
    protected ResponseEntity<ApiError> handleConflict(SpaceAlreadyExistsException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ApiError("space_already_exists", HttpStatus.CONFLICT, e.getMessage()));
    }


    public record ApiError(String code, HttpStatus status, String message) {
    }
}
