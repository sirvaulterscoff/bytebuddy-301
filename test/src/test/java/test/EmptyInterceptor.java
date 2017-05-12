package test;

import net.bytebuddy.implementation.bind.annotation.Origin;

import java.lang.reflect.Method;

/**
 * @author pobedenniy.alexey
 * @since 12.05.17
 */
public class EmptyInterceptor {
    public void intercept(@Origin Method method) {

    }
}
