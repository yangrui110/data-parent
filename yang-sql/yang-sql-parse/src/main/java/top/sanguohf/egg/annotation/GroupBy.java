package top.sanguohf.egg.annotation;

import java.lang.annotation.*;

@Target({ElementType.FIELD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repeatable(GroupBys.class)
public @interface GroupBy {
    String column() default "";
    String tableAlias() default "";
}
