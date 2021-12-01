package com.example.workflow;

import com.example.workflow.model.Project;
import com.example.workflow.repositories.ProjectRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ProfController {
    @Autowired
    ProjectRepo projectRepository;
    @GetMapping("/projectform")
    public String projectform(Model model){
        System.out.println("hello");
        model.addAttribute("projects",projectRepository.findAll());
        return "projectform.html";
    }
    @PostMapping("/submitted")
    public String postprojects(@ModelAttribute("projects") Iterable<Project> project){
        System.out.println(project);
        return "signin.html";
    }
}
