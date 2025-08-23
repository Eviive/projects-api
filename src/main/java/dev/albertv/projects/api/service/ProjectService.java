package dev.albertv.projects.api.service;

import dev.albertv.projects.api.core.exception.ProjectsApiException;
import dev.albertv.projects.api.dto.ProjectDTO;
import dev.albertv.projects.api.dto.ProjectLightDTO;
import dev.albertv.projects.api.dto.SortUpdateDTO;
import dev.albertv.projects.api.entity.Project;
import dev.albertv.projects.api.mapper.ProjectMapper;
import dev.albertv.projects.api.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

import static dev.albertv.projects.api.core.exception.ProjectsApiErrorsEnum.API400_PROJECT_ID_NOT_ALLOWED;
import static dev.albertv.projects.api.core.exception.ProjectsApiErrorsEnum.API404_PROJECT_ID_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ImageService imageService;

    private final ProjectRepository projectRepository;

    private final ProjectMapper projectMapper;

    @Transactional(readOnly = true)
    public Page<ProjectDTO> findAll(final Pageable pageable, final String search) {
        return projectRepository.findAll(pageable, search)
            .map(projectMapper::toDTO);
    }

    @Transactional(readOnly = true)
    public List<ProjectLightDTO> findAllLight() {
        return projectRepository.findAllLight();
    }

    @Transactional(readOnly = true)
    public List<ProjectDTO> findAllFeatured() {
        return projectMapper.toListDTO(projectRepository.findAllByFeaturedIsTrue());
    }

    @Transactional(readOnly = true)
    public Page<ProjectDTO> findAllNotFeatured(final Pageable pageable) {
        return projectRepository.findAllByFeaturedIsFalse(pageable)
            .map(projectMapper::toDTO);
    }

    @Transactional
    public ProjectDTO create(final ProjectDTO projectDTO, @Nullable final MultipartFile file) {
        if (projectDTO.id() != null) {
            throw new ProjectsApiException(API400_PROJECT_ID_NOT_ALLOWED);
        }

        final Project project = projectMapper.toEntity(projectDTO);

        final Integer newSort = projectRepository
            .findMaxSort()
            .map(sort -> sort + 1)
            .orElse(0);

        project.setSort(newSort);

        return save(project, file);
    }

    @Transactional
    public ProjectDTO update(final Long id, final ProjectDTO projectDTO, @Nullable final MultipartFile file) {
        if (!projectRepository.existsById(id)) {
            throw ProjectsApiException.format(API404_PROJECT_ID_NOT_FOUND, id);
        }

        final Project project = projectMapper.toEntity(projectDTO);

        project.setId(id);

        return save(project, file);
    }

    private ProjectDTO save(final Project project, final @Nullable MultipartFile file) {
        UUID oldUuid = null;

        if (file != null) {
            oldUuid = project.getImage().getUuid();
            project.getImage().setUuid(UUID.randomUUID());
        }

        final Project savedProject = projectRepository.save(project);

        if (file != null) {
            imageService.upload(savedProject.getImage(), oldUuid, file);
        }

        return projectMapper.toDTO(savedProject);
    }

    @Transactional
    public void delete(final Long id) {
        final Project project = projectRepository.findById(id)
            .orElseThrow(() -> ProjectsApiException.format(API404_PROJECT_ID_NOT_FOUND, id));

        projectRepository.deleteById(id);

        if (project.getImage().getUuid() != null) {
            imageService.delete(project.getImage());
        }
    }

    @Transactional
    public void sort(final List<SortUpdateDTO> sorts) {
        for (SortUpdateDTO sort : sorts) {
            projectRepository.updateSortById(sort.id(), sort.sort());
        }
    }

}
