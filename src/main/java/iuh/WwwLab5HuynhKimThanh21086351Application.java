package iuh;

import com.neovisionaries.i18n.CountryCode;
import iuh.backend.models.Address;
import iuh.backend.models.Candidate;
import iuh.backend.repositories.AddressRepository;
import iuh.backend.repositories.CandidateRepository;
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

    @Autowired
    private CandidateRepository candidateRepository;
    @Autowired
    private AddressRepository addressRepository;

    @Bean
    CommandLineRunner initData() {
        return args -> {
            Random rnd = new Random();
            for (int i = 1; i < 1000; i++) {
                Address add = new Address(rnd.nextInt(1, 1000) + "", "Quang Trung", "HCM",
                        rnd.nextInt(70000, 80000) + "", CountryCode.VN );
                addressRepository.save(add);

                Candidate can = new Candidate();
                can.setAddress(add);
                can.setDob(LocalDate.of(1998, rnd.nextInt(1, 13), rnd.nextInt(1, 29)));
                can.setAddress(add);
                can.setPhone(rnd.nextLong(1111111111L, 9999999999L) + "");
                can.setEmail("candidate" + i + "@gmail.com");
                can.setFullName("Candidate " + i);
                candidateRepository.save(can);

                System.out.println("Save: " + can);
            }
        };
    }
}
