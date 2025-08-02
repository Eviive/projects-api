package dev.albertv.projects.api.mapper;

import dev.albertv.projects.api.core.config.MapStructConfig;
import dev.albertv.projects.api.dto.SkillDTO;
import dev.albertv.projects.api.entity.Skill;
import dev.albertv.projects.api.mapper.util.CollectionsMapper;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(
    config = MapStructConfig.class,
    uses = ImageMapper.class
)
public interface SkillMapper extends CollectionsMapper<Skill, SkillDTO> {

    // to Entity

    @Mapping(target = "projects", ignore = true)
    Skill toEntity(SkillDTO skill);

    @AfterMapping
    default void afterMapping(@MappingTarget Skill skill) {
        if (skill.getImage() != null) {
            skill.getImage().setSkill(skill);
        }
        if (skill.getProjects() != null) {
            skill.getProjects().forEach(project -> project.getSkills().add(skill));
        }
    }

    // to DTO

    SkillDTO toDTO(Skill skill);

}
