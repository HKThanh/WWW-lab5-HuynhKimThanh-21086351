package iuh.backend.repositories;

import iuh.backend.models.Candidate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface CandidateRepository extends PagingAndSortingRepository<Candidate, Long> {
    Candidate save(Candidate can);

    Optional<Candidate> findById(Long id);

    void deleteById(Long id);

    Page<Candidate> findByFullNameContaining(String keyword, Pageable pageable);

    Candidate findByEmail(String username);

    @Query("SELECT c FROM Candidate c WHERE c.id IN :candidateIds")
    List<Candidate> findAllById(Set<Long> candidateIds);

    List<Candidate> findTop10ByOrderByIdDesc();
}
