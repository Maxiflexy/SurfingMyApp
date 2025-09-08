package com.digicore.omni.root.services.modules.merchants.location.controller;


import com.digicore.omni.root.services.modules.common.utils.CommonUtils;
import com.digicore.omni.root.services.modules.merchants.location.service.GeolocationFinderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/merchant-location-retriever/process")
@RequiredArgsConstructor
public class GeolocationFinderController {

    private final GeolocationFinderService geolocationFinderService;

    @GetMapping("fetch-all-countries")
    public ResponseEntity<Object> retrieveAllCountry() {
        return CommonUtils.buildSuccessResponse(geolocationFinderService.fetchAllCountry());
    }

    @GetMapping("fetch-all-states")
    public ResponseEntity<Object> retrieveAllStates(@RequestParam String countryCode) {
        return CommonUtils.buildSuccessResponse(geolocationFinderService.fetchAllStatesByCountry(countryCode));
    }

    @GetMapping("fetch-all-cities")
    public ResponseEntity<Object> retrieveAllCities(@RequestParam String countryCode, @RequestParam String stateCode) {
        return CommonUtils.buildSuccessResponse(geolocationFinderService.fetchAllCitiesByCountryAndState(countryCode, stateCode));
    }

    @GetMapping("fetch-all-cities-by-state")
    public ResponseEntity<Object> retrieveAllCitiesByState(@RequestParam String stateName) {
        return CommonUtils.buildSuccessResponse(geolocationFinderService.getCitiesByStateName(stateName));
    }
}
