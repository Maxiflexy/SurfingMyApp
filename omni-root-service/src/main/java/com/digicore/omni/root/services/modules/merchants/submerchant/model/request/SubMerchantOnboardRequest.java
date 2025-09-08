package com.digicore.omni.root.services.modules.merchants.submerchant.model.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author mosh
 * @role software engineer
 * @createdOn 13 Wed Aug, 2025
 */

@Setter
@Getter
public class SubMerchantOnboardRequest {

 @NotBlank(message = "First name is required")
 private String firstName;

 @NotBlank(message = "Email is required")
 private String email;

 @NotBlank(message = "Gender is required")
 private String gender;

 @NotBlank(message = "Last name is required")
 private String lastName;

 @NotBlank(message = "Phone number is required")
 @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid phone number format")
 private String phoneNumber;

 @NotBlank(message = "Password is required")
 @Size(min = 8, message = "Password must be at least 8 characters")
 private String password;

 @NotBlank(message = "Password confirmation is required")
 private String confirmPassword;

 @NotBlank(message = "Verification code is required")
 private String verificationCode;

 public boolean isPasswordConfirmed() {
  return password != null && password.equals(confirmPassword);
 }
}
