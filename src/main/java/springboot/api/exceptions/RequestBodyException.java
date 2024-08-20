package springboot.api.exceptions;

public class RequestBodyException extends RuntimeException {
    public RequestBodyException(String message) {
      super(message);
    }
}
