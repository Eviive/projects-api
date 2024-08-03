package com.eviive.personalapi.service;

import com.eviive.personalapi.config.exception.PersonalApiException;
import com.eviive.personalapi.dto.SkillDTO;
import com.eviive.personalapi.dto.SortUpdateDTO;
import com.eviive.personalapi.entity.Skill;
import com.eviive.personalapi.mapper.SkillMapper;
import com.eviive.personalapi.repository.SkillRepository;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

import static com.eviive.personalapi.config.exception.PersonalApiErrorsEnum.API400_SKILL_ID_NOT_ALLOWED;
import static com.eviive.personalapi.config.exception.PersonalApiErrorsEnum.API404_SKILL_ID_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class SkillService {

    private final SkillRepository skillRepository;

    private final SkillMapper skillMapper;

    private final ImageService imageService;

    @Transactional(readOnly = true)
    public List<SkillDTO> findAll() {
        return skillMapper.toListDTO(skillRepository.findAll());
    }

    @Transactional(readOnly = true)
    public Slice<SkillDTO> findAll(final Pageable pageable, final String search) {
        return skillRepository.findAll(pageable, search)
            .map(skillMapper::toDTO);
    }

    @Transactional
    public SkillDTO create(final SkillDTO skillDTO, @Nullable final MultipartFile file) {
        if (skillDTO.id() != null) {
            throw new PersonalApiException(API400_SKILL_ID_NOT_ALLOWED);
        }

        final Skill skill = skillMapper.toEntity(skillDTO);

        final Integer newSort = skillRepository
            .findMaxSort()
            .map(sort -> sort + 1)
            .orElse(0);

        skill.setSort(newSort);

        return save(skill, file);
    }

    @Transactional
    public SkillDTO update(
        final Long id,
        final SkillDTO skillDTO,
        @Nullable final MultipartFile file
    ) {
        if (!skillRepository.existsById(id)) {
            throw PersonalApiException.format(API404_SKILL_ID_NOT_FOUND, id);
        }

        final Skill skill = skillMapper.toEntity(skillDTO);

        skill.setId(id);

        return save(skill, file);
    }

    private SkillDTO save(final Skill skill, @Nullable final MultipartFile file) {
        UUID oldUuid = null;

        if (file != null) {
            oldUuid = skill.getImage().getUuid();
            skill.getImage().setUuid(UUID.randomUUID());
        }

        final Skill savedSkill = skillRepository.save(skill);

        if (file != null) {
            imageService.upload(savedSkill.getImage(), oldUuid, file);
        }

        return skillMapper.toDTO(savedSkill);
    }

    @Transactional
    public void delete(final Long id) {
        final Skill skill = skillRepository.findById(id)
            .orElseThrow(() -> PersonalApiException.format(API404_SKILL_ID_NOT_FOUND, id));

        skill.getProjects().forEach(project -> project.getSkills().remove(skill));

        skillRepository.deleteById(id);

        if (skill.getImage().getUuid() != null) {
            imageService.delete(skill.getImage());
        }
    }

    @Transactional
    public void sort(final List<SortUpdateDTO> sorts) {
        for (SortUpdateDTO sort : sorts) {
            skillRepository.updateSortById(sort.id(), sort.sort());
        }
    }

}
