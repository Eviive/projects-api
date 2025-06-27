package com.eviive.personalapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan("com.eviive.personalapi.properties")
@SuppressWarnings("checkstyle:HideUtilityClassConstructor")
public class PersonalApiApplication {

    public static void main(final String[] args) {
        SpringApplication.run(PersonalApiApplication.class, args);
    }

}
