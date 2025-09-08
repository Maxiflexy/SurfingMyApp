package com.digicore.omni.root.services.util;

import com.digicore.omni.data.lib.modules.common.models.LocationInfo;
import com.digicore.omni.data.lib.modules.common.repository.LocationInfoRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

/**
 * @author Ibrahim Lawal
 * @createdOn 22/04/2023
 */

@Component
@RequiredArgsConstructor
@Slf4j
public class LocationInfoLoader implements CommandLineRunner {

    private final LocationInfoRepository locationInfoRepository;

    @Override
    public void run(String... args) throws Exception {

        if (locationInfoRepository.count() == 0) {
            ObjectMapper mapper = new ObjectMapper();
            TypeReference<List<LocationInfo>> typeReference = new TypeReference<>() {
            };
            InputStream inputStream = TypeReference.class.getResourceAsStream("/locationinfo.json");
            List<LocationInfo> locationInfos = mapper.readValue(inputStream, typeReference);
            locationInfoRepository.saveAll(locationInfos);

            log.trace("<<<<<<<<<< states saved to database! <<<<<<<<<<<<");
        } else {
            log.trace("<<<<<<<<<< states already exist in the database! <<<<<<<<<<");
        }
    }
}
