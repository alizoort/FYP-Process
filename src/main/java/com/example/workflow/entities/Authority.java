package com.example.workflow.entities;

import javax.persistence.*;
@Entity
@Table(name="authority",schema="public")
public class Authority {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;
    @Column(name="name")
    private String name;
    @JoinColumn(name="user")
    @ManyToOne
    private User user;
    public String getName(){
        return this.name;
    }
}
