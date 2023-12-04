package com.institutosermelhor.ManagerCore.controller.validade.custom;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PDFValidator implements ConstraintValidator<PDFTester, MultipartFile> {

    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
        try {
            System.out.println(value);
            System.out.println("no report " + value.getContentType());
            return "application/pdf".equals(value.getContentType());
        } catch (Exception e) {
            // Registre a exceção para depuração
            e.printStackTrace();
            return false;
        }
    }
    
}
