package iuh.frontend.controllers;

import iuh.backend.models.Candidate;
import iuh.backend.services.CandidateService;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@RequestMapping("/candidates")
@Controller
public class CandidateController {
    @Autowired
    private CandidateService candidateService;

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
        return new ModelAndView("candidates/add");
    }

    @PostMapping("/add")
    public String addCandidate(@ModelAttribute Candidate candidate) {
        candidateService.save(candidate);
        return "redirect:/candidates";
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
}
