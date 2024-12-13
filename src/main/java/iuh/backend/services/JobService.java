package iuh.backend.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import iuh.backend.models.Company;
import iuh.backend.models.Job;
import iuh.backend.repositories.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

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

    public Job findById(Long id) {
        return jobRepository.findById(id).orElse(null);
    }

    public Page<Job> findByCompany(Company company, int page, int size) {
        return jobRepository.findByCompany(company, PageRequest.of(page, size));
    }

    public List<Job> findTop10Job() {
        return jobRepository.findTop10ByOrderByIdDesc();
    }

    public List<Job> findRecommendJobsForCandidate(Long canId) {
        String filePath = "recommendedJobs/recommendation_model.json";

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            // Read recommendation model from file
            File file = new File(filePath);
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
            String json = sb.toString();
            Map<Long, List<Long>> recommendationModel = objectMapper.readValue(json, new TypeReference<Map<Long, List<Long>>>() {
            });

            // Get recommended jobs for candidate
            List<Long> jobIds = recommendationModel.get(canId);
            return jobRepository.findAllById(jobIds);
        } catch (IOException e) {
            e.printStackTrace();
            // Return top 10 jobs if error
            return jobRepository.findTop10ByOrderByIdDesc();
        }
    }

    public List<Job> findByCompanyId(Long companyId) {
        return jobRepository.findByCompanyId(companyId);
    }
}
