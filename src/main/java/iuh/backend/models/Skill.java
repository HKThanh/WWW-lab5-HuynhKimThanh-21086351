package iuh.backend.models;

import iuh.backend.enums.SkillType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "skill")
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "skill_id", nullable = false)
    private Long id;

    @Column(name = "skill_description")
    private String skillDescription;

    @Column(name = "skill_name")
    private String skillName;

    @Column(name = "type")
    @Enumerated(EnumType.ORDINAL)
    private SkillType type;

    @Override
    public String toString() {
        return skillName + ": " + skillDescription;
    }
}