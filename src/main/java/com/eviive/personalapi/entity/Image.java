package com.eviive.personalapi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name = "API_IMAGE")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Image {

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "IMAGE_GEN")
    @SequenceGenerator(name = "IMAGE_GEN", sequenceName = "IMAGE_SEQ", allocationSize = 1)
    private Long id;

    private UUID uuid;

    @Column(nullable = false)
    private String altEn;

    @Column(nullable = false)
    private String altFr;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "PROJECT_ID")
    @ToString.Exclude
    private Project project;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "SKILL_ID")
    @ToString.Exclude
    private Skill skill;

}
