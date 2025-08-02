package dev.albertv.projects.api.mapper;

import dev.albertv.projects.api.core.config.MapStructConfig;
import dev.albertv.projects.api.dto.ImageDTO;
import dev.albertv.projects.api.entity.Image;
import dev.albertv.projects.api.mapper.util.CollectionsMapper;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapStructConfig.class)
public interface ImageMapper extends CollectionsMapper<Image, ImageDTO> {

    // to Entity

    @Mapping(target = "project", ignore = true)
    @Mapping(target = "skill", ignore = true)
    Image toEntity(ImageDTO projectDTO);

    @AfterMapping
    default void afterMapping(@MappingTarget Image image) {
        if (image.getProject() != null) {
            image.getProject().setImage(image);
        }
        if (image.getSkill() != null) {
            image.getSkill().setImage(image);
        }
    }

    // to DTO

    ImageDTO toDTO(Image project);

}
