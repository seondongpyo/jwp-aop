package core.mvc.tobe;

import org.springframework.core.Ordered;

public abstract class AbstractExceptionHandlerMapping implements ExceptionHandlerMapping, Ordered {

    private int order;

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public int getOrder() {
        return order;
    }
}
