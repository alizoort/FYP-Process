package com.example.workflow;

import com.example.workflow.model.Project;
import com.example.workflow.repositories.ProjectRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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
    FYPProcessController fypController;
    @GetMapping("/projectform")
    public String projectform(Model model){
        System.out.println("hello");
        model.addAttribute("projects",projectRepository.findAll());
        return "projectform.html";
    }
    @GetMapping("/boot")
    public String bootProcess(){
        Boolean check=false;
        for(GrantedAuthority l : SecurityContextHolder.getContext().getAuthentication().getAuthorities()){
             if(l.toString().equals("ROLE_ADMIN") || l.toString().equals("ROLE_PROF")){
                 return "projectform.html";
             }
        }
        return "signin.html";
    }
    @GetMapping("/submitted")
    public String postprojects(){

        return "signin.html";
    }
}
