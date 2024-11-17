package iuh.backend.repositories;

import iuh.backend.models.Skill;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SkillRepository extends PagingAndSortingRepository<Skill, Long> {
    Skill save(Skill skill);

    Optional<Skill> findById(Long id);

    Iterable<Skill> findAll();

    @Query("SELECT DISTINCT s FROM Skill s WHERE s NOT IN " +
            "(SELECT cs.skill FROM CandidateSkill cs WHERE cs.can.id = :candidateId) " +
            "AND s IN (SELECT js.skill FROM JobSkill js)")
    List<Skill> findRecommendedSkillsForCandidate(@Param("candidateId") Long canId);

    @Query("SELECT s FROM Skill s WHERE s NOT IN " +
            "(SELECT cs.skill FROM CandidateSkill cs WHERE cs.can.id = :id)")
    List<Skill> findSkillNotInCandidate(@Param("id") Long id);
}
