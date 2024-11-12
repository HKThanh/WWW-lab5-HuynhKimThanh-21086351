package iuh.backend.repositories;

import iuh.backend.models.Skill;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SkillRepository extends PagingAndSortingRepository<Skill, Long> {
    Skill save(Skill skill);
}
