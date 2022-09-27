package core.mvc.tobe;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.Optional;

public class ExceptionHandlerMappingRegistry {

    private final List<ExceptionHandlerMapping> mappings = Lists.newArrayList();

    public void addExceptionHandlerMapping(ExceptionHandlerMapping mapping) {
        mapping.initialize();
        mappings.add(mapping);
    }

    public Optional<HandlerExecution> getExceptionHandler(Object handler, Throwable throwable) {
        return mappings.stream()
            .filter(mapping -> mapping.supports(handler))
            .map(mapping -> mapping.getExceptionHandler(throwable.getClass()))
            .findFirst();
    }
}
