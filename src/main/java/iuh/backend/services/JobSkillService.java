package iuh.backend.services;

import iuh.backend.models.JobSkill;
import iuh.backend.repositories.JobSkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobSkillService {
    @Autowired
    JobSkillRepository jobSkillRepository;

    public List<JobSkill> findAll() {
        return (List<JobSkill>) jobSkillRepository.findAll();
    }

    public List<JobSkill> findByJobId(Long jobId) {
        return jobSkillRepository.findByJobId(jobId);
    }
}
