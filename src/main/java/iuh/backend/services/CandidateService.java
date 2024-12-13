package iuh.backend.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import iuh.backend.models.Candidate;
import iuh.backend.models.CandidateSkill;
import iuh.backend.repositories.CandidateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    public Candidate findByEmail(String username) {
        return candidateRepository.findByEmail(username);
    }

    public boolean executeScript(String fileName) {
        StringBuilder output = new StringBuilder();
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("python", "recommendedJobs/" + fileName);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public List<Candidate> findRecommendedCandidatesForJob(List<Long> jobIds) {
        String filePath = "recommendedJobs/recommendation_jobs_model.json";

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

            // Get recommended candidates for job
            Set<Long> candidateIds = new HashSet<>();
            for (Long jobId : jobIds) {
                List<Long> candidateIdList = recommendationModel.get(jobId);
                if (candidateIdList != null) {
                    candidateIds.addAll(candidateIdList);
                }
            }
            return candidateRepository.findAllById(candidateIds);
        } catch (IOException e) {
            e.printStackTrace();
            // Return top 10 candidates if error
            return candidateRepository.findTop10ByOrderByIdDesc();
        }
    }
}
