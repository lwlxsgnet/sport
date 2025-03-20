package com.lwl.config.annotation;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//customize annotation
@RestController
@RequestMapping
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RestControllerMapping {
    @org.springframework.core.annotation.AliasFor("path")
    java.lang.String[] value() default {};

    @org.springframework.core.annotation.AliasFor("value")
    java.lang.String[] path() default {};
}
