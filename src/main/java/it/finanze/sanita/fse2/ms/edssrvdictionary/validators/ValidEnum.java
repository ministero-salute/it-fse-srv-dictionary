package it.finanze.sanita.fse2.ms.edssrvdictionary.validators;
import it.finanze.sanita.fse2.ms.edssrvdictionary.validators.impl.EnumValidator;
import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;
@Documented
@Constraint(validatedBy = EnumValidator.class)
@Target({ ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEnum {
    String message() default "Enum Object not valid";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
