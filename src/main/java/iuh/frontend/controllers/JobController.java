package iuh.frontend.controllers;

import iuh.backend.models.Job;
import iuh.backend.services.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequestMapping("/jobs")
@Controller
public class JobController {
    @Autowired
    private JobService jobService;

    @RequestMapping("/list-jobs")
    public String listJobs(Model model, @RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size) {
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
        return "jobs/list-jobs";
    }
}
