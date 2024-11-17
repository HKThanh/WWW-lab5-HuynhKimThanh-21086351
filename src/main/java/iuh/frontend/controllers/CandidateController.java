package iuh.frontend.controllers;

import com.neovisionaries.i18n.CountryCode;
import iuh.backend.enums.SkillLevel;
import iuh.backend.models.*;
import iuh.backend.services.*;
import jakarta.servlet.http.HttpSession;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequestMapping("/candidates")
@Controller
public class CandidateController {
    @Autowired
    private CandidateService candidateService;
    @Autowired
    private SkillService skillService;
    @Autowired
    private CandidateSkillService candidateSkillService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private JobService jobService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ModelAndView showCandidateListPaging(@RequestParam("pageNo") Optional<Integer> pageNo, @RequestParam("pageSize") Optional<Integer> pageSize) {
        ModelAndView mav = new ModelAndView("candidates/candidate-paging");

        int currentPage = pageNo.orElse(1);
        int currentPageSize = pageSize.orElse(20);

        Page<Candidate> candidatePage = candidateService.findAll(currentPage - 1, currentPageSize, "id", "asc");

        return getModelAndView(candidatePage, mav);
    }

    @GetMapping("/add")
    public ModelAndView addCandidate() {
        ModelAndView mv = new ModelAndView();
        Iterable<Skill> skills = skillService.findAll();
        SkillLevel[] skillLevels = SkillLevel.values();
        mv.addObject("skills", skills);
        mv.addObject("skillLevels", skillLevels);
        mv.setViewName("candidates/register");
        return mv;
    }

    @GetMapping("/search")
    public ModelAndView searchCandidates(@RequestParam("keyword") String keyword,
                                         @RequestParam("pageNo") Optional<Integer> pageNo,
                                         @RequestParam("pageSize") Optional<Integer> pageSize) {

        int currentPage = pageNo.orElse(1);
        int currentPageSize = pageSize.orElse(20);

        Page<Candidate> candidatePage = candidateService.search(keyword, currentPage - 1, currentPageSize, "id", "asc");
        ModelAndView mav = new ModelAndView("candidates/candidate-paging");
        return getModelAndView(candidatePage, mav);
    }

    @NotNull
    private ModelAndView getModelAndView(Page<Candidate> candidatePage, ModelAndView mav) {
        mav.addObject("candidatePage", candidatePage);

        int totalPages = candidatePage.getTotalPages();

        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            mav.addObject("pageNumbers", pageNumbers);
        }

        return mav;
    }

    @PostMapping("add-candidate")
    public ModelAndView doAddCandidate(@RequestParam("email") String email,
                                       @RequestParam("phone") String phone,
                                       @RequestParam("fullName") String fullName,
                                        @RequestParam("dob") String dob,
                                        @RequestParam("number") String number,
                                        @RequestParam("street") String street,
                                        @RequestParam("city") String city,
                                       @RequestParam("skills") List<Long> skillIds,
                                       @RequestParam("skillLevels") List<String> skillLevels) {
        ModelAndView mv = new ModelAndView();

        Address address = new Address();
        address.setNumber(number);
        address.setStreet(street);
        address.setCity(city);
        address.setCountry(CountryCode.VN.getNumeric());
        addressService.save(address);

        Candidate candidate = new Candidate();
        candidate.setEmail(email);
        candidate.setPhone(phone);
        candidate.setFullName(fullName);
        candidate.setDob(LocalDate.parse(dob));
        candidate.setAddress(address);
        candidateService.save(candidate);

        for (int i = 0; i < skillIds.size(); i++) {
            CandidateSkill candidateSkill = new CandidateSkill();
            CandidateSkillId candidateSkillId = new CandidateSkillId();
            candidateSkillId.setCanId(candidate.getId());
            candidateSkillId.setSkillId(skillIds.get(i));
            Skill skill = skillService.findById(skillIds.get(i));
            candidateSkill.setId(candidateSkillId);
            candidateSkill.setSkill(skill);
            candidateSkill.setSkillLevel(SkillLevel.valueOf(skillLevels.get(i)));
            candidateSkill.setCan(candidate);
            candidateSkillService.save(candidateSkill);
        }

        mv.setViewName("redirect:/login");
        return mv;
    }

    @GetMapping("/dashboard")
    public ModelAndView showDashboard(HttpSession session) {
        Candidate candidate = (Candidate) session.getAttribute("candidate");

        if (candidate == null) {
            ModelAndView mv = new ModelAndView();
            mv.setViewName("redirect:/login");
            return mv;
        }

        ModelAndView mv = new ModelAndView();
        mv.addObject("candidate", candidate);

        List<Job> recommendedJobs = jobService.findRecommendJobsForCandidate(candidate.getId());
        mv.addObject("recommendedJobs", recommendedJobs);

        List<CandidateSkill> candidateSkills = candidateSkillService.getCandidateSkills(candidate.getId());

        Map<Skill, String> skillsForCandidate = new HashMap<>();
        for (CandidateSkill candidateSkill : candidateSkills) {
            Skill skill = skillService.findById(candidateSkill.getId().getSkillId());
            skillsForCandidate.put(skill, candidateSkill.getSkillLevel().name());
        }

        mv.addObject("skillsForCandidate", skillsForCandidate);
        mv.addObject("candidateSkills", candidateSkills);

        List<Skill> recommendedSkills = skillService.findRecommendedSkillsForCandidate(candidate.getId());
        mv.addObject("recommendedSkills", recommendedSkills);

        SkillLevel[] skillLevels = SkillLevel.values();
        mv.addObject("skillLevels", skillLevels);

        List<Skill> skillNotInCandidate = skillService.findSkillNotInCandidate(candidate.getId());
        mv.addObject("skillNotInCandidate", skillNotInCandidate);

        mv.setViewName("candidates/dashboard");
        return mv;
    }

    @PostMapping("/update-skills")
    public ModelAndView updateCandidateSkills(@RequestParam("skills") List<Long> skillIds,
                                              @RequestParam("skillLevels") List<String> skillLevels,
                                              HttpSession session) {
        ModelAndView mv = new ModelAndView();
        Candidate candidate = (Candidate) session.getAttribute("candidate");

        if (candidate == null) {
            mv.setViewName("redirect:/login");
            return mv;
        }

        for (int i = 0; i < skillIds.size(); i++) {
            CandidateSkill candidateSkill = new CandidateSkill();
            CandidateSkillId candidateSkillId = new CandidateSkillId();
            candidateSkillId.setCanId(candidate.getId());
            candidateSkillId.setSkillId(skillIds.get(i));
            Skill skill = skillService.findById(skillIds.get(i));
            candidateSkill.setId(candidateSkillId);
            candidateSkill.setSkill(skill);
            candidateSkill.setSkillLevel(SkillLevel.valueOf(skillLevels.get(i)));
            candidateSkill.setCan(candidate);
            candidateSkillService.save(candidateSkill);
        }

        mv.setViewName("redirect:/candidates/dashboard");
        return mv;
    }
}
