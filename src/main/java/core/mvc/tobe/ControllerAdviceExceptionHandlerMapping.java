package core.mvc.tobe;

import com.google.common.collect.Maps;
import core.annotation.web.Controller;
import core.annotation.web.ControllerAdvice;
import core.di.context.ApplicationContext;

import java.util.Map;

public class ControllerAdviceExceptionHandlerMapping implements ExceptionHandlerMapping {

    private final Map<Class<?>, HandlerExecution> handlers = Maps.newHashMap();

    public ControllerAdviceExceptionHandlerMapping(ApplicationContext applicationContext, ExceptionHandlerConverter converter) {
        Map<Class<?>, Object> controllerAdvices = applicationContext.getBeansAnnotatedWith(ControllerAdvice.class);
        handlers.putAll(converter.convert(controllerAdvices));
    }

    @Override
    public HandlerExecution getExceptionHandler(Object handler, Throwable throwable) {
        return handlers.get(throwable.getClass());
    }
}
