package iuh.frontend.controllers;

import com.neovisionaries.i18n.CountryCode;
import iuh.backend.models.*;
import iuh.backend.services.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequestMapping("/companies")
@Controller
public class CompanyController {
    @Autowired
    private CompanyService companyService;
    @Autowired
    private JobService jobService;
    @Autowired
    private SkillService skillService;
    @Autowired
    private CandidateService candidateService;
    @Autowired
    private CandidateSkillService candidateSkillService;
    @Autowired
    private AddressService addressService;

    @RequestMapping("/list")
    public String listCompanies(Model model) {
        Iterable<Company> companies = companyService.findAll();
        model.addAttribute("companies", companies);
        return "companies/list";
    }

    @GetMapping("/login")
    public ModelAndView toLogin() {
        ModelAndView mv = new ModelAndView("companies/login");
        mv.addObject("message", "");
        return mv;
    }

    @PostMapping("/login")
    public ModelAndView doLogin(@RequestParam("email") String email, @RequestParam("password") String password, HttpSession session) {
        ModelAndView mv = new ModelAndView();
        Company company = companyService.findByEmail(email);
        if (company != null) {
            if (company.getPhone().equals(password)) {
                session.setAttribute("company", company);
                mv.addObject("company", company);
                mv.setViewName("companies/dashboard");
                return mv;
            } else {
                mv.addObject("message", "Invalid password");
                mv.setViewName("companies/login");
                return mv;
            }
        } else {
            mv.addObject("message", "Invalid email");
            mv.setViewName("companies/login");
            return mv;
        }
    }

    @GetMapping("")
    public ModelAndView toDashboard(HttpSession session) {
        if (session.getAttribute("company") == null) {
            return new ModelAndView("companies/login")
                    .addObject("message", "Please login to continue");
        }
        return new ModelAndView("companies/dashboard");
    }

    @GetMapping("/logout")
    public ModelAndView logout(HttpSession session) {
        session.removeAttribute("company");
        return new ModelAndView("companies/login");
    }

    @GetMapping(value = "/company-search")
    public String showCompanySearch(Model model,HttpSession session){
        Company company = (Company) session.getAttribute("company");
        model.addAttribute("skill", skillService.findAll());

        boolean isExecuted = candidateService.executeScript("recommendation_cansForjob_script.py");
        if (isExecuted) {
            model.addAttribute("isExecuted", true);
        }

        List<Job> jobs = jobService.findByCompanyId(company.getId());
        List<Long> jobIds = jobs.stream().map(Job::getId).collect(Collectors.toList());
        List<Candidate> candidates = candidateService.findRecommendedCandidatesForJob(jobIds);

        model.addAttribute("recommendedCandidates", candidates);

        if (company == null) {
            model.addAttribute("message", "Please login to continue");
            return "companies/login";
        }
        else
            return "companies/company-search";
    }
    @PostMapping(value = "/company-search")
    public String searchCandidateBySkills(@RequestParam("skills") List<String> skillIds, Model model, HttpSession session) {
        Company company = (Company) session.getAttribute("company");
        if (company == null) {
            model.addAttribute("message", "Please login to continue");
            return "companies/login";
        }

        List<Long> skillIdsLong = skillIds.stream()
                .map(id -> {
                    try {
                        return skillService.findById(Long.parseLong(id.replaceAll("[\\[\\]]", "")));
                    } catch (Exception e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .map(Skill::getId)
                .collect(Collectors.toList());


        Map<Long, List<CandidateSkill>> candidateSkillsMap = candidateSkillService.findCandidatesBySkills(skillIdsLong);

        model.addAttribute("candidateSkillsMap", candidateSkillsMap);
        model.addAttribute("skill", skillService.findAll());
        return "companies/company-search";
    }

    @GetMapping("/register")
    public String toRegister() {
        return "companies/register";
    }

    @PostMapping("/add")
    public String addCompany(@RequestParam("name") String name,
                             @RequestParam("email") String email,
                             @RequestParam("phone") String phone,
                             @RequestParam("about") String about,
                             @RequestParam("url") String webUrl,
                             @RequestParam("street") String street,
                             @RequestParam("city") String city,
                             @RequestParam("number") String number,
                             Model model) {
        Company company = new Company();
        company.setCompName(name);
        company.setAbout(about);
        company.setEmail(email);
        company.setPhone(phone);
        company.setWebUrl(webUrl);

        Address companyAddress = new Address();
        companyAddress.setStreet(street);
        companyAddress.setCity(city);
        companyAddress.setNumber(number);
        companyAddress.setCountry(CountryCode.VN.getNumeric());
        addressService.save(companyAddress);

        company.setAddress(companyAddress);

        companyService.save(company);
        model.addAttribute("message", "Company registered successfully");
        return "companies/login";
    }
}
