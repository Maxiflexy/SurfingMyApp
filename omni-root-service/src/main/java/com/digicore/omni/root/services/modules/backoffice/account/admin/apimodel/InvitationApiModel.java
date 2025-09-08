/*
 * Created by Monsuru (5/9/2022)
 */

package com.digicore.omni.root.services.modules.backoffice.account.admin.apimodel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Monsuru <br/>
 * @since Sep-05(Mon)-2022
 */
@AllArgsConstructor
@Getter
@Setter
@Builder
public class InvitationApiModel {
    private String backOfficeUserId;
}