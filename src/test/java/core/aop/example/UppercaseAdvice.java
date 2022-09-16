package core.aop.example;

import core.aop.intercept.MethodInterceptor;
import core.aop.intercept.MethodInvocation;

public class UppercaseAdvice implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) {
        return String.valueOf(invocation.proceed()).toUpperCase();
    }
}
