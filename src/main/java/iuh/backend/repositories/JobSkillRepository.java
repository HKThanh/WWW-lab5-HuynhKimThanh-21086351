package iuh.backend.repositories;

import iuh.backend.models.JobSkill;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface JobSkillRepository extends CrudRepository<JobSkill, Long> {
    List<JobSkill> findByJobId(Long jobId);
}
