package com.institutosermelhor.ManagerCore.util.customValidators;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.auth0.jwt.interfaces.Payload;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.validation.Constraint;

@Constraint(validatedBy={PDFValidator.class})
@Target({ METHOD })
@Retention(RUNTIME)
public @interface PDFTester {
    String message() default "File type not supported. Accept only PDF.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
