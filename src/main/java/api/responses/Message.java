package api.responses;

public class Message {
    private String message;
    static final public String CANT_FIND_USER = "Can't find user with nickname: ";

    public Message(String message) {
        this.message = message;
    }

    public Message(String template, String message) {
        this.message = template + message;
    }

    public String getMessage() {
        return message;
    }
}
