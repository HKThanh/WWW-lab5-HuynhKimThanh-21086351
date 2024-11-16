package iuh;

import com.neovisionaries.i18n.CountryCode;
import iuh.backend.models.*;
import iuh.backend.repositories.AddressRepository;
import iuh.backend.repositories.CandidateRepository;
import iuh.backend.services.AddressService;
import iuh.backend.services.CompanyService;
import iuh.backend.services.JobService;
import iuh.backend.services.SkillService;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.util.Random;

@SpringBootApplication
public class WwwLab5HuynhKimThanh21086351Application  {

    public static void main(String[] args) {
        SpringApplication.run(WwwLab5HuynhKimThanh21086351Application.class, args);
    }

//    @Autowired
//    CompanyService companyService;
//    @Autowired
//    JobService jobService;
//    @Autowired
//    SkillService skillService;
//
//    @Bean
//    CommandLineRunner initData() {
//        return args -> {
//            Faker faker = new Faker();
//            for (int i = 1; i < 11; i++) {
//                Skill skill = new Skill();
//                skill.setSkillName(faker.job().keySkills());
//                skill.setSkillDescription("Description for " + skill.getSkillName());
//                skill.setType((byte) 0);
//                skillService.save(skill);
//            }
//        };
//    }
}
