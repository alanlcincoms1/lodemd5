package bet.lucky.game.exception.message;

public interface ApplicationMessage {

  String getKey();

  String getMessage();

  String getDescription();

  default int getStatusCode() {
    return 200;
  }
}
