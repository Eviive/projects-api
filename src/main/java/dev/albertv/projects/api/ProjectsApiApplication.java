package dev.albertv.projects.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan("dev.albertv.projects.api.core.properties")
public class ProjectsApiApplication {

    public static void main(final String[] args) {
        SpringApplication.run(ProjectsApiApplication.class, args);
    }

}
