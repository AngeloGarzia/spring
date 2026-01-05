package fr.diginamic.recencement.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class VilleExceptionHandler {

    @ExceptionHandler(VilleApiException.class)
    public ResponseEntity<String> handleRegleMetier(VilleApiException e) {
        System.out.println("Je suis passé par la");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

}
