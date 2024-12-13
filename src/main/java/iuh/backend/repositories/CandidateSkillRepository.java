package iuh.backend.repositories;

import iuh.backend.models.CandidateSkill;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CandidateSkillRepository extends CrudRepository<CandidateSkill, Long> {
    List<CandidateSkill> findByCanId(Long id);

    @Query("SELECT cs.can.id FROM CandidateSkill cs WHERE cs.skill.id IN :skillIds")
    List<Long> findCandidatesBySkill(@Param("skillIds") List<Long> skillIds);
}
