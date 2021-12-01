package com.example.workflow.entities;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name="user",schema="public")
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;
    @Column(name="username")
    private String username;
    @Column(name="password")
    private String password;
    @Column(name="algorithm")
    @Enumerated(EnumType.STRING)
    private EncryptionAlgorithm algorithm;
    @OneToMany(mappedBy="user",fetch=FetchType.EAGER)
    private List<Authority> authorities;
    public enum EncryptionAlgorithm{
        BCRYPT,SCRYPT
    }
    public Integer getId(){
        return this.id;
    }
    public String getPassword(){
        return this.password;
    }
    public  EncryptionAlgorithm getAlgorithm(){
        return this.algorithm;
    }
    public String getName(){
        return this.username;
    }
    public List<Authority> getAuthorities(){
        return this.authorities;
    }
    public String getUsername(){
        return this.username;
    }
}
