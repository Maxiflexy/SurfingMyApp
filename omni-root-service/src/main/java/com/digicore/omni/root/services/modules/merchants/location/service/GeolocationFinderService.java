package com.digicore.omni.root.services.modules.merchants.location.service;


import com.digicore.omni.root.lib.modules.common.services.GeolocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class GeolocationFinderService {

    private final GeolocationService geolocationService;

    public Set<Object[]> fetchAllCountry() {
        return geolocationService.findAllCountries();
    }

    public Set<Object[]> fetchAllStatesByCountry(String countryCode) {
        return geolocationService.findAllStatesByCountry(countryCode);
    }

    public Set<String> fetchAllCitiesByCountryAndState(String countryCode, String stateCode) {
        return geolocationService.findAllCitiesByCountryAndState(countryCode, stateCode);
    }

    public List<String> getCitiesByStateName(String stateName) {
        return geolocationService.getCitiesByStateName(stateName);
    }

}
