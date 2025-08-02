package dev.albertv.projects.api.util;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Component
public final class UriUtils {

    public URI buildLocation(final Long id, final String pathToRemove) {
        String path = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(id)
            .getPath();

        if (path != null && pathToRemove != null) {
            path = path.replace(
                pathToRemove.startsWith("/") ? pathToRemove : "/%s".formatted(pathToRemove),
                ""
            );
        }

        return URI.create(path != null ? path : "");
    }

    public URI buildLocation(final Long id) {
        return buildLocation(id, null);
    }

}
