package iuh.backend.services;

import iuh.backend.models.Skill;
import iuh.backend.repositories.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkillService {
    @Autowired
    private SkillRepository skillRepository;

    public List<Skill> findSkillNotInCandidate(Long id) {
        return skillRepository.findSkillNotInCandidate(id);
    }

    public Skill save(Skill skill) {
        return skillRepository.save(skill);
    }

    public Page<Skill> findAll(int page, int size, String sortBy, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        return skillRepository.findAll(PageRequest.of(page, size, sort));
    }

    public Skill update(Skill skill) {
        return skillRepository.save(skill);
    }

    public Skill findById(Long skill) {
        return skillRepository.findById(skill).orElse(null);
    }

    public Iterable<Skill> findAll() {
        return skillRepository.findAll();
    }

    public List<Skill> findRecommendedSkillsForCandidate(Long candidateId) {
        return skillRepository.findRecommendedSkillsForCandidate(candidateId);
    }
}
