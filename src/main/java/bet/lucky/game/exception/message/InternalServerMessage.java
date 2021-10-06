package bet.lucky.game.exception.message;


public enum InternalServerMessage implements ApplicationMessage {
    INTERNAL_SERVER_ERROR("PV0001", "Internal server error");

    private String message;
    private String description;

    InternalServerMessage(String message, String description) {
        this.message = message;
        this.description = description;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getKey() {
        return this.name();
    }


    @Override
    public int getStatusCode() {
        return 500;
    }
}
