package uk.co.malbec.cascade.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface StepPostHandler {
    Class[] value();
}