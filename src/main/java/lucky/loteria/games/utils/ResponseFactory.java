package lucky.loteria.games.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;

import java.io.Serializable;

public final class ResponseFactory {

    public ResponseFactory() {
    }

    public static <T extends Serializable> ResponseEntity<T> accepted(@Nullable T data) {
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(data);
    }

    public static <T extends Serializable> ResponseEntity<T> success(@Nullable T data) {
        return ResponseEntity.status(HttpStatus.OK).body(data);
    }

    public static <T extends Serializable> ResponseEntity<T> success() {
        return ResponseEntity.status(HttpStatus.OK).build();
    }
//
//    public static ResponseEntity<ErrorResponse> error(ApplicationMessage error) {
//        ErrorResponse errorResponse = new ErrorResponse(error.getMessage(), error.getDescription());
//        return ResponseEntity.status(error.getStatusCode()).body(errorResponse);
//    }
//
//    public static ResponseEntity<ErrorResponse> error(int status, String error, String description) {
//        ErrorResponse errorResponse = new ErrorResponse(error, description);
//        return ResponseEntity.status(status).body(errorResponse);
//    }
}
