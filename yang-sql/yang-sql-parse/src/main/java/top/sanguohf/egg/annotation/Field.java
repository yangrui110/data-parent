package top.sanguohf.egg.annotation;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Field {
    String value() default "";
    String alias() default "";
}
