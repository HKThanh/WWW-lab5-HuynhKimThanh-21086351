package iuh.backend.services;

import iuh.backend.models.Job;
import iuh.backend.repositories.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class JobService {
    @Autowired
    private JobRepository jobRepository;

    public Page<Job> findAll(int pageNo, int pageSize, String sortBy, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        return jobRepository.findAll(PageRequest.of(pageNo, pageSize, sort));
    }

    public Job save(Job job) {
        return jobRepository.save(job);
    }
}
