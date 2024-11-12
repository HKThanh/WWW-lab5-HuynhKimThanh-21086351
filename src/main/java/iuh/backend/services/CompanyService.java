package iuh.backend.services;

import iuh.backend.models.Company;
import iuh.backend.repositories.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompanyService {
    @Autowired
    private CompanyRepository companyRepository;

    public Iterable<Company> findAll() {
        return companyRepository.findAll();
    }

    public void save(Company company) {
        companyRepository.save(company);
    }

    public Company findById(long i) {
        if (companyRepository.findById(i).isEmpty()) {
            return null;
        }
        return companyRepository.findById(i).get();
    }
}
