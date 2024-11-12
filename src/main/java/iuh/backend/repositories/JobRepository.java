package iuh.backend.repositories;

import iuh.backend.models.Job;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JobRepository extends PagingAndSortingRepository<Job, Long> {
    Job save(Job job);

    Optional<Job> findById(Long id);
}
