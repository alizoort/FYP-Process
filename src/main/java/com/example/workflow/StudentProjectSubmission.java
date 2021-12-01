package com.example.workflow;

import com.example.workflow.model.Project;
import com.example.workflow.repositories.ProjectRepo;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/submission")
public class StudentProjectSubmission {
    TaskService taskService;
    @Autowired
   ProjectRepo projectRepository ;
    public StudentProjectSubmission(TaskService taskService){
        this.taskService=taskService;
    }
   @ResponseBody
@GetMapping("/allSubmittedProjects")
public List<Project> getAllProjects(){
return (List<Project>) projectRepository.findAll();
}
@ResponseBody
@PostMapping("")
    public Project createProject(@RequestBody Project project){
        Task currentUserTask=null;
        Boolean checkOnlyFirst=false;
    List<Task> list=taskService.createTaskQuery().list();
    for(Task task :list){
        //System.out.println(task.getCamundaFormRef());
        System.out.println(task.getId());
        System.out.println(task.getName());
        System.out.println(task.getTaskDefinitionKey());
        System.out.println(task.getDescription());
        System.out.println(task.getCreateTime());
        System.out.println("!!!!!!!!!");
        if(task.getName().equals("project submission by students") && checkOnlyFirst==false){
            currentUserTask=task;
            checkOnlyFirst=true;
        }
        //  taskService.claim(task.getId(),null);
        //taskService.complete(task.getId());
    }
    taskService.claim(currentUserTask.getId(),null);
    taskService.complete(currentUserTask.getId());
    Project newProject=new Project(project);
    return this.projectRepository.save(newProject);
}
@ResponseBody
@GetMapping("/{id}")
    public Project getProject(@PathVariable("id") Integer id){
       return projectRepository.findById(id).orElseThrow(()->new StudentProjectSubmission.ResourceNotFoundException("No Project found with id"+id));
}
@PutMapping("/{id}")
public Project updateProject(@PathVariable("id") Integer id,@RequestBody Project project){
      projectRepository.findById(id).orElseThrow(()->new StudentProjectSubmission.ResourceNotFoundException("No Project was found with id "+id));
      return projectRepository.save(project);
}
@DeleteMapping("/{id}")
public void deleteProject(@PathVariable("id") Integer id){
       Project project=projectRepository.findById(id).orElseThrow(()->new StudentProjectSubmission.ResourceNotFoundException("No project found with id "+id));
       projectRepository.deleteById(project.id);
}
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public class ResourceNotFoundException extends RuntimeException{
        public ResourceNotFoundException(){
            this("Resource not found");
        }
        public ResourceNotFoundException(String message){
            this(message,null);
        }
        public ResourceNotFoundException(String message,Throwable cause){
            super(message,cause);
        }
    }

}
