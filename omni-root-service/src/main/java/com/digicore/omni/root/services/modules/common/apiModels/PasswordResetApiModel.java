package com.digicore.omni.root.services.modules.common.apiModels;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Oct-05(Wed)-2022
 */

@Getter
@Setter
@Builder
public class PasswordResetApiModel {

    private String recoveryKey;
}
