package com.sapo.mock_project.inventory_receipt.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// Định nghĩa annotation cho số điện thoại
@Constraint(validatedBy = PhoneValidator.class)
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPhone {
    String message() default "Số điện thoại không hợp lệ. Phải có 10 số và bắt đầu bằng số 0.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
