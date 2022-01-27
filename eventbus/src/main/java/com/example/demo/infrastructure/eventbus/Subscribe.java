package com.example.demo.infrastructure.eventbus;

import java.lang.annotation.*;

/**
 * Annotation to annotate method which we wants to be invokable by the EventBus identified by its value
 */
@Repeatable(Subscribe.List.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Subscribe {

    String value() default "";

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    @interface List {

        Subscribe[] value();
    }
}
