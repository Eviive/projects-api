package com.eviive.personalapi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.NamedSubgraph;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.SEQUENCE;

@Entity
@Table(name = "API_PROJECT")
@NamedEntityGraph(name = "project-image", attributeNodes = @NamedAttributeNode("image"))
@NamedEntityGraph(
    name = "project-image-skills-image",
    attributeNodes = {
        @NamedAttributeNode(value = "skills", subgraph = "skills-image"),
        @NamedAttributeNode("image")
    },
    subgraphs = @NamedSubgraph(name = "skills-image", attributeNodes = @NamedAttributeNode("image"))
)
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Project {

    @Id
    @GeneratedValue(strategy = SEQUENCE, generator = "PROJECT_GEN")
    @SequenceGenerator(name = "PROJECT_GEN", sequenceName = "PROJECT_SEQ", allocationSize = 1)
    private Long id;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(nullable = false, length = 510)
    private String descriptionEn;

    @Column(nullable = false, length = 510)
    private String descriptionFr;

    @Column(nullable = false)
    private LocalDate creationDate;

    @Column(nullable = false)
    private String repoUrl;

    @Column(nullable = false)
    private String demoUrl;

    @Column(nullable = false)
    private Boolean featured;

    @Column(nullable = false)
    private Integer sort;

    @OneToOne(cascade = ALL, mappedBy = "project", orphanRemoval = true)
    @ToString.Exclude
    private Image image;

    @ManyToMany(fetch = LAZY)
    @JoinTable(
        name = "API_PROJECT_SKILL_MAP",
        joinColumns = @JoinColumn(name = "PROJECT_ID"),
        inverseJoinColumns = @JoinColumn(name = "SKILL_ID")
    )
    @ToString.Exclude
    private Set<Skill> skills = new HashSet<>();

}
