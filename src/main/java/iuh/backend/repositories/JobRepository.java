package iuh.backend.repositories;

import iuh.backend.models.Company;
import iuh.backend.models.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobRepository extends PagingAndSortingRepository<Job, Long> {
    Job save(Job job);

    Optional<Job> findById(Long id);

    Page<Job> findByCompany(Company company,
                            Pageable pageable);

    List<Job> findTop10ByOrderByIdDesc();

    @Query("SELECT DISTINCT j FROM Job j JOIN JobSkill js on j.id = js.job.id " +
            "JOIN js.skill s WHERE s IN " +
            "(SELECT s FROM CandidateSkill cs join cs.skill s WHERE cs.can.id = :candidateId)")
    List<Job> findRecommendJobsForCandidate(@Param("candidateId") Long canId);
}
