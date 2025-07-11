package com.eviive.personalapi.mapper;

import com.eviive.personalapi.core.config.MapStructConfig;
import com.eviive.personalapi.dto.ImageDTO;
import com.eviive.personalapi.entity.Image;
import com.eviive.personalapi.mapper.util.CollectionsMapper;
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
