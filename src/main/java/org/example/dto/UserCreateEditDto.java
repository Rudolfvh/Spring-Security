package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Value;
import lombok.experimental.FieldNameConstants;
import org.example.database.entity.Role;
import org.example.validator.UserInfo;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Value
@UserInfo
@FieldNameConstants
public class UserCreateEditDto {
    @NotBlank
    String username;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    LocalDate birthDate;
    @NotBlank
    String firstname;
    @NotBlank
    String lastname;
    Role role;
    Integer companyId;

    MultipartFile image;
}
