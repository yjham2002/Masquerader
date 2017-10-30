package server.exception;

public class LogicallyImpossibleException extends Exception{

    public static final String DEFAULT_MSG = "[Not A Java Level Error] This situation cannot be occurred, Check if there is any error on other aspects.";

    public LogicallyImpossibleException(String message){
        super(message);
    }

    public LogicallyImpossibleException(){
        super(DEFAULT_MSG);
    }

}
