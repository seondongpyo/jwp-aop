package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.di.context.ApplicationContext;

import java.util.Map;

public class ControllerExceptionHandlerMapping implements ExceptionHandlerMapping {

    private final Map<Class<? extends Throwable>, HandlerExecution> handlers = Maps.newHashMap();
    private final ApplicationContext applicationContext;
    private final ExceptionHandlerConverter converter;

    public ControllerExceptionHandlerMapping(ApplicationContext applicationContext, ExceptionHandlerConverter converter) {
        this.applicationContext = applicationContext;
        this.converter = converter;
    }

    @Override
    public void initialize() {
        Map<Class<?>, Object> controllers = applicationContext.getBeansAnnotatedWith(Controller.class);
        handlers.putAll(converter.convert(controllers));
    }

    @Override
    public boolean supports(Object handler) {
        if (!(handler instanceof HandlerExecution)) {
            return false;
        }
        return ((HandlerExecution) handler).isAnnotationPresent(Controller.class);
    }

    @Override
    public HandlerExecution getExceptionHandler(Class<? extends Throwable> exceptionClass) {
        return handlers.get(exceptionClass);
    }
}
