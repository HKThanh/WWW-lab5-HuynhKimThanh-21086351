package iuh.backend.services;

import iuh.backend.models.CandidateSkill;
import iuh.backend.repositories.CandidateSkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CandidateSkillService {
    @Autowired
    private CandidateSkillRepository candidateSkillRepository;

    public void save(CandidateSkill candidateSkill) {
        candidateSkillRepository.save(candidateSkill);
    }

    public List<CandidateSkill> getCandidateSkills(Long id) {
        return candidateSkillRepository.findByCanId(id);
    }
}
