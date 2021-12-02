package com.example.workflow;

import com.example.workflow.model.UserRegistered;
import org.camunda.bpm.engine.ManagementService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.userdetails.UserDetails;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

@RestController
@RequestMapping("/registrationService")
public class FYPProcessController  {
    public  String pi;
    private final RuntimeService runtimeService;
    private final TaskService taskService;
    private final ManagementService managementService;
    public FYPProcessController(RuntimeService runtimeService, TaskService taskService,ManagementService managementService){
        this.runtimeService=runtimeService;
        this.taskService=taskService;
        this.managementService=managementService;
    }
    public static ReentrantLock lock = new ReentrantLock();
    public static Map<String,List<Task>> mapOfTasks=new HashMap();
    public Boolean isAuthorized;
    String userType="";
    @PostMapping("/register")
    public void registration(@RequestBody UserRegistered user)  throws IOException, InterruptedException{
        isAuthorized=false;
        Object principal= SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println(principal.toString());
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getName());
        System.out.println(((List)SecurityContextHolder.getContext().getAuthentication().getAuthorities()));
        if(((List)SecurityContextHolder.getContext().getAuthentication().getAuthorities()).contains("ROLE_ADMIN") || ((List)SecurityContextHolder.getContext().getAuthentication().getAuthorities()).contains("ROLE_PROF")){
            isAuthorized=true;
        }
         lock.lock();
         try{
             Map<String,Object>  hashMap=new HashMap<>();
             hashMap.put("isAuthorized",this.isAuthorized);
             pi=runtimeService.startProcessInstanceByKey("FYP-Process-process",hashMap).getId();
             Task currentUserTask=null;
             List<Task> list=taskService.createTaskQuery().list();
             Boolean checkOnlyFirst=false;
             DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
             LocalDateTime now = LocalDateTime.now();
             System.out.println("current time");

             for(Task task :list){
                 System.out.println("!!!ICI");
                 System.out.println(dtf.format(now));
                 System.out.println(task.getCreateTime().toString().split(" ")[3]);
                 //System.out.println(task.getCamundaFormRef());
                 System.out.println(task.getId());
                 System.out.println(task.getName());
                 System.out.println(dtf.format(now).split(" ")[0]);
                 if(task.getName().equals("registrationService") && task.getCreateTime().toString().split(" ")[3].equals(dtf.format(now).split(" ")[1])){

                     System.out.println("HEREEEEE");
                     currentUserTask=task;
                     checkOnlyFirst=true;
                 }
                 System.out.println(task.getTaskDefinitionKey());
                 System.out.println(task.getDescription());
                 System.out.println(task.getCreateTime());
                 System.out.println("!!!!!!!!!");
             }
             taskService.claim(currentUserTask.getId(),null);
             taskService.complete(currentUserTask.getId());

         }finally{
             lock.unlock();
         }
    }

}
