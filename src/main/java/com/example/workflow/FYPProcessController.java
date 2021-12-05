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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
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
    public Boolean compareDates(Date date1) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);

        Date firstDate = date1;
        Date secondDate = new Date(System.currentTimeMillis());
        System.out.println("FIRSTDATE AND SECOND DATE");
        secondDate.setYear(121);
        System.out.println(firstDate);
        System.out.println(secondDate);
        long diffInMillies = Math.abs(secondDate.getTime() - firstDate.getTime());
        long diff = TimeUnit.DAYS.convert(diffInMillies,TimeUnit.MILLISECONDS);
        if(diff==0){
            diffInMillies = Math.abs(secondDate.getTime() - firstDate.getTime());
            diff = TimeUnit.HOURS.convert(diffInMillies,TimeUnit.MILLISECONDS);
            if(diff==0){
                diffInMillies = Math.abs(secondDate.getTime() - firstDate.getTime());
                diff = TimeUnit.MINUTES.convert(diffInMillies,TimeUnit.MILLISECONDS);
                if(diff<=1){
                    return true;
                }

            }
            System.out.println(diff);
            diffInMillies = Math.abs(secondDate.getTime() - firstDate.getTime());
            diff = TimeUnit.HOURS.convert(diffInMillies,TimeUnit.MILLISECONDS);
            System.out.println(diff);
            diffInMillies = Math.abs(secondDate.getTime() - firstDate.getTime());
            diff = TimeUnit.MINUTES.convert(diffInMillies,TimeUnit.MILLISECONDS);
            System.out.println(diff);
        }
        return false;
    }
    @PostMapping("/register")
    public void registration(@RequestBody UserRegistered user)  throws IOException, InterruptedException,ParseException{
        isAuthorized=false;
        Object principal= SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println(principal.toString());
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getName());
        System.out.println(((List)SecurityContextHolder.getContext().getAuthentication().getAuthorities()));
        ((List)SecurityContextHolder.getContext().getAuthentication().getAuthorities()).forEach(e->{
            System.out.println(e);
            if(e.toString().equals("ROLE_ADMIN") || (e.toString().equals("ROLE_PROF"))){
                System.out.println("isAuthorized!!!!!?????");
                this.isAuthorized=true;
            }
        });
         lock.lock();
         try{
             Map<String,Object>  hashMap=new HashMap<>();
             hashMap.put("isAuthorized",this.isAuthorized);
             pi=runtimeService.startProcessInstanceByKey("FYP-Process-process",hashMap).getId();
             Task currentUserTask=null;
             List<Task> list=taskService.createTaskQuery().list();
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
                 compareDates(task.getCreateTime());
                 if(task.getName().equals("registrationService") && compareDates(task.getCreateTime())){

                     System.out.println("HEREEEEE");
                     currentUserTask=task;
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
