package com.proway_upskilling.clinica_veterinaria_api.exception;

import com.proway_upskilling.clinica_veterinaria_api.model.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleNotFound(ResourceNotFoundException ex){
        Map<String, String> body =  new HashMap<>();
        body.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex){
        Map<String, String> erros =  new HashMap<>();
        for(FieldError fe : ex.getBindingResult().getFieldErrors()){
            erros.put(fe.getField(), fe.getDefaultMessage());
        }
        return new ResponseEntity<>(erros, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleAll(Exception ex){
        log.error(ex.getMessage(), ex);
        Message message = Message.builder()
                .title(ex.getMessage())
                .description(ex.getCause().getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleJsonParseError(HttpMessageNotReadableException ex) {
        Map<String, String> error = new HashMap<>();
        String message = ex.getMessage();

        if (message != null && message.contains("LocalDate")) {
            error.put("dataNascimento", "Formato inválido para data. Use o padrão yyyy-MM-dd ou deixe nulo.");
        } else {
            error.put("erro", "Formato inválido para campo(s). Verifique se os dados estão corretos.");
        }

        return ResponseEntity.badRequest().body(error);
    }
}
