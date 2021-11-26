package com.example.workflow;

import com.example.workflow.model.User;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
@RestController
@RequestMapping("/registrationService")
public class FYPProcessController  {
    private final RuntimeService runtimeService;
    public FYPProcessController(RuntimeService runtimeService){
        this.runtimeService=runtimeService;
    }
    Boolean isAuthorized=false;
    String userType="";
    @PostMapping("/register")
    public void registration(@RequestBody User user){
         if(user.name=="Caline" && user.password=="Caline"){
             isAuthorized=true;
         }
        Map<String,Object>  hashMap=new HashMap<>();
         hashMap.put("Authorized",isAuthorized);
         runtimeService.startProcessInstanceByKey("FYP-Process-process",hashMap);
    }
}
