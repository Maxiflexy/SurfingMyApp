package com.digicore.omni.root.services.modules.merchants.authentication.apiModel;

import com.digicore.common.validator.RequestBodyChecker;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Nov-13(Sun)-2022
 */

@Getter
@Setter
public class LoginWith2faRequest {
  @RequestBodyChecker(message = "username is required")
  private   String username;
  @RequestBodyChecker(message = "loginKey is required")
  private   String loginKey;
  @RequestBodyChecker(message = "otp is required")
  private   String otp;
}
