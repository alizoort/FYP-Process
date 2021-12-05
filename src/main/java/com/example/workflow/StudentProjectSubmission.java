package com.example.workflow;

import com.example.workflow.model.Project;
import com.example.workflow.repositories.ProjectRepo;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/submission")
public class StudentProjectSubmission {
    public static Date deadline=new Date(2021,12,5,6,45,0);
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
        project.numberOfVotes=0;
    List<Task> list=taskService.createTaskQuery().list();
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    LocalDateTime now = LocalDateTime.now();
    System.out.println("DATE");
    System.out.println(dtf.format(now));
    for(Task task : list){
        //System.out.println(task.getCamundaFormRef());
        System.out.println(task.getId());
        System.out.println(task.getName());
        System.out.println(task.getTaskDefinitionKey());
        System.out.println(task.getDescription());
        System.out.println(task.getCreateTime());
        System.out.println("!!!!!!!!!");
    }
    for(Task task : list){
        if(task.getName().equals("project submission by students") ){
            currentUserTask=task;
            System.out.println("CURRENTUSERTASK");
            System.out.println(currentUserTask);
        }
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
    List<Task> list=taskService.createTaskQuery().list();
    Date secondDate = new Date(System.currentTimeMillis());
     if(secondDate.compareTo(deadline)<0){
         for(Task task : list){
             if(task.getName().equals("Prof check for projects submitted")){
                 taskService.claim(task.getId(),null);
                 taskService.complete(task.getId());
             }
         }
     }
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
