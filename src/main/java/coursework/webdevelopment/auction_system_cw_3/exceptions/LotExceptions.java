package coursework.webdevelopment.auction_system_cw_3.exceptions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.io.IOException;

@RestControllerAdvice

public class LotExceptions {

   @ExceptionHandler(LotNotFoundException.class)
   public ResponseEntity<?> handleException (Exception exception){
       return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
   }

    @ExceptionHandler(LotStartsException.class)
    public ResponseEntity<?> handleRequestException(IOException ioException) {
        return new ResponseEntity<>(ioException.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
