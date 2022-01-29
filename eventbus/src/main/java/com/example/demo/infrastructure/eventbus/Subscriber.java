package com.example.demo.infrastructure.eventbus;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Wrapper class holding the method to invoke and the target object,
 * serving as invokable values to be user inside the EventBus
 */
record Subscriber(Method handler,
                  Object targetObject) {

    public void invoke(Object object) {
        try {
            handler.invoke(targetObject, object);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}