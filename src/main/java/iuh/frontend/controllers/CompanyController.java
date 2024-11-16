package iuh.frontend.controllers;

import iuh.backend.models.Company;
import iuh.backend.models.Job;
import iuh.backend.services.CompanyService;
import iuh.backend.services.JobService;
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

    @RequestMapping("/list")
    public String listCompanies(Model model) {
        Iterable<Company> companies = companyService.findAll();
        model.addAttribute("companies", companies);
        return "companies/list";
    }

    @GetMapping("/login")
    public ModelAndView toLogin() {
        return new ModelAndView("companies/login");
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
                mv.addObject("error", "Invalid password");
                mv.setViewName("companies/login");
                return mv;
            }
        } else {
            mv.addObject("error", "Invalid email");
            mv.setViewName("companies/login");
            return mv;
        }
    }

    @GetMapping("")
    public ModelAndView toDashboard(HttpSession session) {
        if (session.getAttribute("company") == null) {
            return new ModelAndView("companies/login");
        }
        return new ModelAndView("companies/dashboard");
    }

    @GetMapping("/logout")
    public ModelAndView logout(HttpSession session) {
        session.removeAttribute("company");
        return new ModelAndView("companies/login");
    }
}
