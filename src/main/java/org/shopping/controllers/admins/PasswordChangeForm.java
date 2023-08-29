package org.shopping.controllers.admins;

import lombok.Data;

@Data
public class PasswordChangeForm {
    private String userId;
    private String currentPassword;
    private String newPassword;
    private String confirmNewPassword;
}