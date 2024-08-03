package com.eviive.personalapi.repository;

import com.eviive.personalapi.entity.Skill;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.springframework.transaction.annotation.Propagation.MANDATORY;

@Repository
@Transactional(propagation = MANDATORY)
public interface SkillRepository extends JpaRepository<Skill, Long> {

    // Find

    @NotNull
    @EntityGraph(value = "skill-image")
    List<Skill> findAll();

    @NotNull
    @EntityGraph(value = "skill-image")
    @Query("select s from Skill s where :#{#search} is null or lower(s.name) like lower('%' || :#{#search} || '%')")
    Slice<Skill> findAll(@NotNull Pageable pageable, @Nullable String search);

    @NotNull
    @Query("select max(s.sort) from Skill s")
    Optional<Integer> findMaxSort();

    // Update

    @Modifying
    @Query("update Skill s set s.sort = :sort where s.id = :id")
    void updateSortById(@NotNull Long id, @NotNull Integer sort);

}
