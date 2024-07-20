package ar.com.dno.ai.rag.controlplane.models.adapters.input.web;


import ar.com.dno.ai.rag.controlplane.models.usecases.exceptions.ModelAlreadySupportedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class SupportedModelsExceptionHandler {

    @ExceptionHandler({ModelAlreadySupportedException.class})
    protected ResponseEntity<ApiError> handleConflict(ModelAlreadySupportedException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ApiError("model_already_supported", HttpStatus.CONFLICT, e.getMessage()));
    }


    public record ApiError(String code, HttpStatus status, String message) {
    }
}
