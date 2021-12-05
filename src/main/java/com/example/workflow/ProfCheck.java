package com.example.workflow;

import com.example.workflow.model.Project;
import com.example.workflow.repositories.ProjectRepo;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ProfCheck implements JavaDelegate {
    @Autowired
    ProjectRepo projectRepository;
    public Boolean studyOneProject(Project project){
       return true;
    }
    public TaskService taskService;
    public ProfCheck(TaskService taskService){
        this.taskService=taskService;
    }
    @Override
    public void execute(DelegateExecution execution) throws Exception {
        System.out.println("INSIDE EXECUTE");
    List<Project> projects= (List<Project>) projectRepository.findAll();
    for(int i=0;i<projects.size();++i){
        System.out.print("numberOfVOTES");
        System.out.println(projects.get(i).numberOfVotes);
        if(projects.get(i).numberOfVotes<1){
            System.out.println("FALSE");
              projects.get(i).accepted=false;
              Project newProject=projects.get(i);
              projectRepository.save(newProject);
        }
    }
    System.out.println("AFTYER FOR");
    }
    public void start_execution(DelegateExecution execution) throws Exception {

        System.out.println(execution.getVariable("isAuthorized"));
    }
}
