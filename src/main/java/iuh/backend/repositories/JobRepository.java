package iuh.backend.repositories;

import iuh.backend.models.Company;
import iuh.backend.models.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
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
}
