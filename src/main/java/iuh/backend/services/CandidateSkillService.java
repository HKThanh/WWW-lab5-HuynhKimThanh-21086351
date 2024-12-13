package iuh.backend.services;

import iuh.backend.models.CandidateSkill;
import iuh.backend.repositories.CandidateSkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public Map<Long, List<CandidateSkill>> findCandidatesBySkills(List<Long> skillIds) {
        List<Long> canIds = candidateSkillRepository.findCandidatesBySkill(skillIds);
        List<CandidateSkill> candidateSkills = (List<CandidateSkill>) candidateSkillRepository.findAllById(canIds);

        return candidateSkills.stream().collect(
            Collectors.groupingBy(
                candidateSkill -> candidateSkill.getCan().getId()
            )
        );
    }
}
