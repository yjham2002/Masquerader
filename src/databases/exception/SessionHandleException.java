package databases.exception;

public class SessionHandleException extends Exception {

    public SessionHandleException(String message){
        super(message);
    }

    public SessionHandleException(){
        this("Session Handling is not sound.");
    }

}
