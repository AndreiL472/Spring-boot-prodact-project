package sda.academy.restdemo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice // daca doresc sa gestionez exceptiile pentru toate controllerele din aplicatie
// ca sa nu mai adaug @ExceptionHandler in fiecare controller
public class GlobalExceptionHandler {


    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CategoryNotFoundException.class)
    public ErrorResponse handleCategoryNotFound(CategoryNotFoundException ex){
        return new ErrorResponse(ex.getMessage());
    }


    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ProductNotFoundException.class)
    public ErrorResponse handleProductNotFound(ProductNotFoundException ex){
        return new ErrorResponse(ex.getMessage());
    }



    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleValidationException(MethodArgumentNotValidException ex){
        System.out.println("getBindingResult print: " + ex.getBindingResult());
        System.out.println("getFieldErrors  print: " + ex.getBindingResult().getFieldErrors());
             String message =    ex.getBindingResult()
                .getFieldErrors()
                .stream()
                        .map(fieldError -> fieldError.getField() + " : " + fieldError.getDefaultMessage())
                        .collect(Collectors.joining(", "));
             return new ErrorResponse(message);
    }

//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ErrorResponse handleSBValidationException(MethodArgumentNotValidException ex){
//        System.out.println("getBindingResult print: " + ex.getBindingResult());
//        System.out.println("getFieldErrors  print: " + ex.getBindingResult().getFieldErrors());
//        StringBuilder sb = new StringBuilder();
//       for(FieldError fe: ex.getBindingResult().getFieldErrors() ) {
//           if (sb.length() > 0) sb.append(" ");
//           sb.append(fe.getField()).append(" : ").append(fe.getDefaultMessage());
//       }
//        return new ErrorResponse(sb.toString());
//    }



// String fields = "name, price"




}
// pentru centralizare : folosim o clasa cu adnotarea @ControllerAdvice
// ca sa nu definim aceleasi metode in mai multe controllere
