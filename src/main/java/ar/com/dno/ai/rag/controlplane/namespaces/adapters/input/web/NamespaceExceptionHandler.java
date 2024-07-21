package ar.com.dno.ai.rag.controlplane.namespaces.adapters.input.web;


import ar.com.dno.ai.rag.controlplane.namespaces.usecases.exceptions.NamespaceAlreadyExistException;
import ar.com.dno.ai.rag.controlplane.spaces.usecases.exceptions.SpaceAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class NamespaceExceptionHandler {

    @ExceptionHandler({NamespaceAlreadyExistException.class})
    protected ResponseEntity<ApiError> handleConflict(NamespaceAlreadyExistException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ApiError("namespace_already_exists", HttpStatus.CONFLICT, e.getMessage()));
    }


    public record ApiError(String code, HttpStatus status, String message) {
    }
}
