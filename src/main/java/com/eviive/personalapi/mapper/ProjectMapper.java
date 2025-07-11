package com.eviive.personalapi.mapper;

import com.eviive.personalapi.core.config.MapStructConfig;
import com.eviive.personalapi.dto.ProjectDTO;
import com.eviive.personalapi.dto.ProjectLightDTO;
import com.eviive.personalapi.entity.Project;
import com.eviive.personalapi.mapper.util.CollectionsMapper;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Mapper(
    config = MapStructConfig.class,
    uses = {SkillMapper.class, ImageMapper.class}
)
public interface ProjectMapper extends CollectionsMapper<Project, ProjectDTO> {

    // to Entity

    Project toEntity(ProjectDTO projectDTO);

    @AfterMapping
    default void afterMapping(@MappingTarget Project project) {
        if (project.getImage() != null) {
            project.getImage().setProject(project);
        }
        if (project.getSkills() != null) {
            project.getSkills().forEach(skill -> skill.getProjects().add(project));
        }
    }

    // to DTO

    ProjectDTO toDTO(Project project);

    // to Light DTO

    ProjectLightDTO toLightDTO(Project project);

    List<ProjectLightDTO> toLightListDTO(Collection<Project> projects);

    Set<ProjectLightDTO> toLightSetDTO(Collection<Project> projects);

}
