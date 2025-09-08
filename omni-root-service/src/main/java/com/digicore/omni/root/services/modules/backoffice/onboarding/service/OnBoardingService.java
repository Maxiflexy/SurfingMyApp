/*
 * Created by Monsuru (7/8/2022)
 */

package com.digicore.omni.root.services.modules.backoffice.onboarding.service;


import com.digicore.omni.root.lib.modules.backoffice.service.BackOfficeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OnBoardingService {

    private final BackOfficeService backOfficeService;


    @Autowired
    public OnBoardingService(BackOfficeService backOfficeService) {
        this.backOfficeService = backOfficeService;
    }


}
