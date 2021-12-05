package com.example.workflow.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="Projets")
public class Project {
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Id
    public Integer id;
    public Project(Integer id,String title,String supervisor,String studentName,Boolean isCheck,String description,Integer numberOfVotes,Boolean accepted){
        this.title=title;
        this.supervisor=supervisor;
        this.studentName=studentName;
        this.isCheck=isCheck;
        this.numberOfVotes=numberOfVotes;
        this.description=description;
        this.accepted=accepted;
    }
    public Project(Project p){
        this.title=p.title;
        this.supervisor=p.supervisor;
        this.studentName=p.studentName;
        this.isCheck=p.isCheck;
        this.description=p.description;
        this.numberOfVotes=p.numberOfVotes;
        this.accepted=p.accepted;
    }
    public Project(){

    }
    public String title;
    public Boolean isCheck=false;
    private String supervisor;
    private String studentName;
    public String description;
    public Boolean accepted=true;
    public Integer numberOfVotes=0;
    public String getTitle(){
        return this.title;
    }
    public String getSupervisor(){
        return this.supervisor;
    }
    public String getStudentName(){
        return this.studentName;
    }
    public Boolean getIsCheck(){ return this.isCheck;}
}
