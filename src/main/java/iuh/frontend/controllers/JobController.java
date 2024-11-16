package iuh.frontend.controllers;

import iuh.backend.enums.SkillLevel;
import iuh.backend.enums.SkillType;
import iuh.backend.models.*;
import iuh.backend.services.CompanyService;
import iuh.backend.services.JobService;
import iuh.backend.services.JobSkillService;
import iuh.backend.services.SkillService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequestMapping("/jobs")
@Controller
public class JobController {
    @Autowired
    private JobService jobService;
    @Autowired
    private JobSkillService jobSkillService;
    @Autowired
    private SkillService skillService;
    @Autowired
    private CompanyService companyService;

    @RequestMapping("/list-jobs")
    public String listJobs(HttpSession session, Model model, @RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);

        Page<Job> jobs = jobService.findAll(currentPage - 1, pageSize, "id", "asc");

        Candidate candidate = (Candidate) session.getAttribute("candidate");

        if (candidate != null) {
            model.addAttribute("candidate", candidate);
        } else {
            model.addAttribute("message", "Please login!");
        }

        model.addAttribute("jobs", jobs);

        int totalPages = jobs.getTotalPages();

        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream
                    .rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());

            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "jobs/list-jobs";
    }

    @RequestMapping("/job-detail/{id}")
    public String getJobById(Model model, @PathVariable("id") Long id) {
        Job job = jobService.findById(id);
        List<JobSkill> jobSkills = jobSkillService.findByJobId(job.getId());
        List<Skill> skills = new ArrayList<>();
        for (JobSkill jobSkill : jobSkills) {
            Skill skill = skillService.findById(jobSkill.getSkill().getId());
            skills.add(skill);
        }

        model.addAttribute("skills", skills);

        model.addAttribute("job", job);
        return "jobs/job-detail";
    }

    @RequestMapping("/add-job")
    public String addJob(String username, Model model, HttpSession session) {
        Company company = session.getAttribute("company") != null ? (Company) session.getAttribute("company") : null;

        if (company == null) {
            return "redirect:/companies/login";
        }

        Iterable<Company> companies = companyService.findAll();
        Iterable<Skill> skills = skillService.findAll();

        model.addAttribute("skillLevels", SkillLevel.values());
        model.addAttribute("company", company);
        model.addAttribute("skills", skills);
        return "jobs/add-job";
    }

    @PostMapping("/add")
    public String addJob(@RequestParam("title") String title,
                         @RequestParam("description") String description,
                         @RequestParam("skills") List<Long> skillIds,
                         @RequestParam("skillLevels") List<SkillLevel> skillLevels,
                         HttpSession session,
                         RedirectAttributes redirectAttributes) {

        Company company = (Company) session.getAttribute("company");

        if (company == null) {
            return "redirect:/companies/login";
        }

        Job job = new Job();
        job.setJobName(title);
        job.setJobDesc(description);
        job.setCompany(company);
        jobService.save(job);

        for (Long skillId : skillIds) {
            Skill skill = skillService.findById(skillId);
            JobSkill jobSkill = new JobSkill();
            JobSkillId jobSkillId = new JobSkillId();
            jobSkillId.setJobId(job.getId());
            jobSkillId.setSkillId(skill.getId());
            jobSkill.setSkillLevel(skillLevels.get(skillIds.indexOf(skillId)));
            jobSkill.setId(jobSkillId);
            jobSkill.setJob(job);
            jobSkill.setSkill(skill);
            jobSkillService.save(jobSkill);
        }

        redirectAttributes.addFlashAttribute("message", "Job added successfully!");
        return "redirect:/jobs/company";
    }

    @GetMapping("/company")
    public ModelAndView findJobsByCompany(@RequestParam("page") Optional<Integer> page,
                                          @RequestParam("size") Optional<Integer> size,
                                          HttpSession session) {

        ModelAndView mv = new ModelAndView();

        Company company = (Company) session.getAttribute("company");

        if (company == null) {
            mv.setViewName("redirect:/companies/login");
            return mv;
        }

        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);

        Page<Job> jobs = jobService.findByCompany(company, currentPage - 1, pageSize);

        mv.addObject("jobs", jobs);

        int totalPages = jobs.getTotalPages();

        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream
                    .rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());

            mv.addObject("pageNumbers", pageNumbers);
        }

        mv.addObject("company", company);
        mv.setViewName("jobs/list-jobs-by-company");

        return mv;
    }
}
