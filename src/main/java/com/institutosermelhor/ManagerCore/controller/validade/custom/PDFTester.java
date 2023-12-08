package com.institutosermelhor.ManagerCore.controller.validade.custom;


import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.validation.Constraint;

@Documented
@Constraint(validatedBy = PDFValidator.class)
@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface PDFTester {

  String message() default "File type not supported. Accept only PDF.";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
