package iuh.backend.repositories;

import iuh.backend.models.CandidateSkill;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CandidateSkillRepository extends CrudRepository<CandidateSkill, Long> {
    List<CandidateSkill> findByCanId(Long id);
}
