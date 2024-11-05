package iuh.frontend.models;

import iuh.backend.models.Candidate;
import iuh.backend.services.CandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class CandidateModel {
    @Autowired
    private CandidateService candidateService;

    public Page<Candidate> findAll(int pageNo, int pageSize, String sortBy, String sortDir) {
        return candidateService.findAll(pageNo, pageSize, sortBy, sortDir);
    }
}
