package com.disha.taskmanager.entity;

import jakarta.persistence.*;

@Entity
@Table(name="users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;

    private String name;
    private String email;
    private String password;

    public UserEntity(){}

    public UserEntity(long id,String name,String email,String password){
        this.id=id;
        this.name=name;
        this.email=email;
        this.password=password;
    }
    public void setId(long id){
        this.id=id;
    }
    public void setName(String name){
        this.name=name;
    }
    public void setEmail(String email){
        this.email=email;
    }
    public void setPassword(String password){
        this.password=password;
    }
    public long getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    public String getEmail(){
        return email;
    }
    public String getPassword(){
        return password;
    }
}
