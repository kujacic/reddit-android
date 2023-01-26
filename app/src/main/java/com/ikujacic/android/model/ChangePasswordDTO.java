package com.ikujacic.android.model;

public class ChangePasswordDTO {
    private String oldPassword;
    private String newPassword;

    public ChangePasswordDTO(String oldPassword, String newPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }
}