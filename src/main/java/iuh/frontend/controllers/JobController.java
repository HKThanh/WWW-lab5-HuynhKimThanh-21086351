package iuh.frontend.controllers;

import iuh.backend.models.Job;
import iuh.backend.models.JobSkill;
import iuh.backend.models.Skill;
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

    @RequestMapping("/list-jobs")
    public String listJobs(@ModelAttribute("account") String account, Model model, @RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);

        Page<Job> jobs = jobService.findAll(currentPage - 1, pageSize, "id", "asc");

        model.addAttribute("jobs", jobs);

        int totalPages = jobs.getTotalPages();

        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream
                    .rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());

            model.addAttribute("pageNumbers", pageNumbers);
        }

        model.addAttribute("account", account);
        return "jobs/list-jobs";
    }

    @RequestMapping("/job-detail/{id}")
    public String getJobById(Model model, @PathVariable("id") Long id) {
        Job job = jobService.findById(id);
        List<JobSkill> jobSkills = jobSkillService.findByJobId(job.getId());
        List<Skill> skills = new ArrayList<>();
        for (JobSkill jobSkill : jobSkills) {
            Skill skill = skillService.findById(jobSkill.getSkill());
            skills.add(skill);
        }

        model.addAttribute("skills", skills);

        model.addAttribute("job", job);
        return "jobs/job-detail";
    }

    @RequestMapping("/add-job")
    public String addJob() {
        return "jobs/add-job";
    }
}
