package iuh.backend.repositories;

import iuh.backend.models.Candidate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CandidateRepository extends PagingAndSortingRepository<Candidate, Long> {
    Candidate save(Candidate can);

    Optional<Candidate> findById(Long id);

    void deleteById(Long id);

    Page<Candidate> findByFullNameContaining(String keyword, Pageable pageable);

    Candidate findByEmail(String username);
}
