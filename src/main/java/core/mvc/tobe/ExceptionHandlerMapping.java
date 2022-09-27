package core.mvc.tobe;

public interface ExceptionHandlerMapping {

    void initialize();

    boolean supports(Object handler);

    HandlerExecution getExceptionHandler(Class<? extends Throwable> exceptionClass);
}
