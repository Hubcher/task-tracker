package com.example.aop;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CheckTaskStateExists {
    String paramName() default "taskStateId";
}

