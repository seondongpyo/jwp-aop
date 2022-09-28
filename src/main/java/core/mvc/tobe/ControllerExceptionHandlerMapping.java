package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.di.context.ApplicationContext;

import java.util.Map;

public class ControllerExceptionHandlerMapping implements ExceptionHandlerMapping {

    private final Map<Class<?>, HandlerExecution> handlers = Maps.newHashMap();

    public ControllerExceptionHandlerMapping(ApplicationContext applicationContext, ExceptionHandlerConverter converter) {
        Map<Class<?>, Object> controllers = applicationContext.getBeansAnnotatedWith(Controller.class);
        handlers.putAll(converter.convert(controllers));
    }

    @Override
    public HandlerExecution getExceptionHandler(Object handler, Throwable throwable) {
        HandlerExecution handlerExecution = (HandlerExecution) handler;
        return handlers.get(handlerExecution.getTargetClass());
    }
}
