package com.example.workflow;

import com.example.workflow.model.Project;
import com.example.workflow.repositories.ProjectRepo;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ProfCheck implements JavaDelegate {
    @Autowired
    ProjectRepo projectRepository ;
    public Boolean studyOneProject(Project project){
       return true;
    }
    @Override
    public void execute(DelegateExecution execution) throws Exception {
      //  System.out.println(execution.getVariable("isAuthorized"));
    List<Project> projects= (List<Project>) projectRepository.findAll();
        System.out.println("projects filtered!!!!!!!");
        for(int i=0;i<projects.size();++i){
            studyOneProject(projects.get(i));
        }

    }
    public void start_execution(DelegateExecution execution) throws Exception {

        System.out.println(execution.getVariable("isAuthorized"));
    }

}
