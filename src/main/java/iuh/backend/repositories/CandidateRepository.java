package iuh.backend.repositories;

import iuh.backend.models.Candidate;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CandidateRepository extends PagingAndSortingRepository<Candidate, Long> {
    void save(Candidate can);
}
