package com.eviive.personalapi.repository;

import com.eviive.personalapi.dto.ProjectLightDTO;
import com.eviive.personalapi.entity.Project;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface ProjectRepository extends JpaRepository<Project, Long> {

    // Find

    @NotNull
    @EntityGraph(value = "project-image")
    @Query("select p from Project p where :#{#search} is null or lower(p.title) like lower('%' || :#{#search} || '%')")
    Page<Project> findAll(@NotNull Pageable pageable, @Nullable String search);

    @NotNull
    @Query("select new com.eviive.personalapi.dto.ProjectLightDTO(p.id, p.title, p.featured, p.sort) from Project p")
    List<ProjectLightDTO> findAllLight();

    @NotNull
    @EntityGraph(value = "project-image-skills-image")
    List<Project> findAllByFeaturedIsTrue();

    @NotNull
    @EntityGraph(value = "project-image")
    Page<Project> findAllByFeaturedIsFalse(Pageable pageable);

    @NotNull
    @Query("select max(p.sort) from Project p")
    Optional<Integer> findMaxSort();

    // Update

    @Transactional
    @Modifying
    @Query("update Project p set p.sort = :sort where p.id = :id")
    void updateSortById(@NotNull Long id, @NotNull Integer sort);

}
