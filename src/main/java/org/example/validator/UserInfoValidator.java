package org.example.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.example.database.repository.CompanyRepository;
import org.example.dto.UserCreateEditDto;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;

import static org.springframework.util.StringUtils.hasText;

@Component
@RequiredArgsConstructor
public class UserInfoValidator implements ConstraintValidator<UserInfo, UserCreateEditDto> {

    private final CompanyRepository companyRepository;

    @Override
    public boolean isValid(UserCreateEditDto value, ConstraintValidatorContext context) {
        return hasText(value.getFirstname()) || hasText(value.getLastname());
    }

}
