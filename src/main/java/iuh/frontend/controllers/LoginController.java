package iuh.frontend.controllers;

import iuh.backend.models.Candidate;
import iuh.backend.services.CandidateService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {
    @Autowired
    private CandidateService candidateService;

    @RequestMapping("/login")
    public String login(Model model) {
        String message = "";
        if (model.containsAttribute("message")) {
            message = (String) model.getAttribute("message");
        }
        model.addAttribute("message", message);
        return "candidates/login";
    }

    @RequestMapping("/check")
    public ModelAndView checkLogin(@RequestParam("username") String username,
                                   @RequestParam("password") String password,
                                   HttpSession session) {

        ModelAndView mv = new ModelAndView();

        Candidate candidate = candidateService.findByEmail(username);

       if (candidate.getEmail().equals(username)) {
            if (candidate.getPhone().equals(password)) {
                session.setAttribute("candidate", candidate);
                mv.addObject("candidate", candidate);
                mv.setViewName("redirect:/jobs/list-jobs");
            } else {
                mv.addObject("message", "Invalid password");
                mv.setViewName("candidates/login");
            }
       } else {
            mv.addObject("message", "Invalid username");
            mv.setViewName("candidates/login");
       }

        return mv;
    }

    @RequestMapping("/logout")
    public String logout(SessionStatus sessionStatus, HttpSession session, Model model) {
        session.removeAttribute("candidate");
        sessionStatus.setComplete();
        return "redirect:/login";
    }
}
