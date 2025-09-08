package com.digicore.omni.root.services.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * @author Oluwatobi Ogunwuyi
 * @createdOn Sep-05(Mon)-2022
 */
@Configuration
public class CORSFilter implements WebMvcConfigurer {
        @Override
        public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/**")
                    .allowedOrigins("*")
                    .allowedHeaders("Content-Type", "Access-Control-Allow-Headers", "Access-Control-Expose-Headers", "Content-Disposition",
                            "Authorization"," X-Requested-With")
                    .exposedHeaders("Content-Disposition")
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "HEAD", "PATCH");
        }

}


