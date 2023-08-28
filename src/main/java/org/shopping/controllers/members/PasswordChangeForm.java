package org.shopping.controllers.members;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PasswordChangeForm {
    private String userId;
    private String currentPassword;
    private String newPassword;
    private String confirmNewPassword;
}
