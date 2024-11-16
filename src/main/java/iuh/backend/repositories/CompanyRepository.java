package iuh.backend.repositories;

import iuh.backend.models.Company;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends PagingAndSortingRepository<Company, Long> {
    Company save(Company company);
    Optional<Company> findById(long id);
    List<Company> findAll();

    Company findByEmail(String email);
}
