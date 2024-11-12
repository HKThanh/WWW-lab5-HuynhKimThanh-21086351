package iuh.backend.services;

import iuh.backend.models.Candidate;
import iuh.backend.repositories.CandidateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class CandidateService {
    @Autowired
    private CandidateRepository candidateRepository;

    public Page<Candidate> findAll(int pageNo, int pageSize, String sortBy, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        return candidateRepository.findAll(pageable);
    }

    public Candidate save(Candidate candidate) {
        return candidateRepository.save(candidate);
    }

    public Candidate findById(Long id) {
        return candidateRepository.findById(id).orElse(null);
    }

    public void deleteById(Long id) {
        candidateRepository.deleteById(id);
    }

    public Candidate update(Candidate candidate) {
        return candidateRepository.save(candidate);
    }

    public Page<Candidate> search(String keyword, int pageNo, int pageSize, String sortBy, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        return candidateRepository.findByFullNameContaining(keyword, pageable);
    }
}
