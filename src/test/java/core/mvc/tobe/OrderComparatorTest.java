package core.mvc.tobe;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

class OrderComparatorTest {

    @Test
    void compare() {
        AbstractExceptionHandlerMapping mapping1 = new ControllerExceptionHandlerMapping(null, null);
        mapping1.setOrder(1);
        AbstractExceptionHandlerMapping mapping2 = new ControllerAdviceExceptionHandlerMapping(null, null);
        mapping2.setOrder(0);

        List<AbstractExceptionHandlerMapping> list = Arrays.asList(mapping1, mapping2);
        OrderComparator.sort(list);
        System.out.println("list = " + list);
    }
}
