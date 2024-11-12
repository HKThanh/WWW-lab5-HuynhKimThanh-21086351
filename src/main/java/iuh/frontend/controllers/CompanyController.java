package iuh.frontend.controllers;

import iuh.backend.models.Company;
import iuh.backend.services.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/companies")
@Controller
public class CompanyController {
    @Autowired
    private CompanyService companyService;

    @RequestMapping("/list")
    public String listCompanies(Model model) {
        Iterable<Company> companies = companyService.findAll();
        model.addAttribute("companies", companies);
        return "companies/list";
    }
}
