package iuh.frontend.controllers;

import iuh.backend.models.Candidate;
import iuh.backend.services.CandidateService;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Controller
public class CandidateController {
    @Autowired
    private CandidateService candidateService;

    @RequestMapping("/candidates")
    public String showCandidateListPaging(Model model, @PathVariable("/pageNo") Optional<Integer> pageNo, @PathVariable("/pageSize") Optional<Integer> pageSize) {
        int currentPage = pageNo.orElse(1);
        int currentPageSize = pageSize.orElse(10);

        Page<Candidate> candidatePage = candidateService.findAll(currentPage - 1, currentPageSize, "id", "asc");

        model.addAttribute("candidatePage", candidatePage);
        int totalPages = candidatePage.getTotalPages();

        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .toList();

            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "candidates/candidate-paging";
    }
}
