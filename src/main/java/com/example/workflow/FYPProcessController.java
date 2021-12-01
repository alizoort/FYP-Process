package com.example.workflow;

import com.example.workflow.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.camunda.bpm.engine.ManagementService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.task.Task;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.OutputStream;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
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
    public void registration(@RequestBody User user)  throws IOException, InterruptedException{
        isAuthorized=false;
        System.out.println(user.name);
        System.out.println(user.password);
         if(user.name.equals("Caline") && user.password.equals("Caline")){
             System.out.println(user.name);
             System.out.println(user.password);
             this.isAuthorized=true;
         }
         lock.lock();
         try{
             Map<String,Object>  hashMap=new HashMap<>();
             hashMap.put("isAuthorized",this.isAuthorized);
             pi=runtimeService.startProcessInstanceByKey("FYP-Process-process",hashMap).getId();
             Task currentUserTask=null;
             List<Task> list=taskService.createTaskQuery().list();
             Boolean checkOnlyFirst=false;
             for(Task task :list){
                 //System.out.println(task.getCamundaFormRef());
                 System.out.println(task.getId());
                 System.out.println(task.getName());
                 if(task.getName().equals("registrationService") && checkOnlyFirst==false){
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
