package dev.albertv.projects.api.core.config.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Permission {

    READ_PROJECT("read:project"),
    CREATE_PROJECT("create:project"),
    UPDATE_PROJECT("update:project"),
    DELETE_PROJECT("delete:project"),

    READ_SKILL("read:skill"),
    CREATE_SKILL("create:skill"),
    UPDATE_SKILL("update:skill"),
    DELETE_SKILL("delete:skill"),

    REVALIDATE_PORTFOLIO("revalidate:portfolio");

    private final String name;

}
