package com.disha.taskmanager.entity;

import jakarta.persistence.*;

@Entity
@Table(name="tasks", indexes={
        @Index(name="idx_title",columnList = "title")
})

    public class TaskEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String title;
        private String description;
        private boolean completed;

        @ManyToOne
        @JoinColumn(name="user_id")
        private UserEntity user;

        public TaskEntity() {
        }

        public TaskEntity(Long id, String title, String description, boolean completed) {
            this.id = id;
            this.title = title;
            this.description = description;
            this.completed = completed;

        }

        public void setId(Long id) {
            this.id = id;
        }
        public void setTitle(String title) {
            this.title = title;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setCompleted(boolean completed) {
            this.completed = completed;
        }

        public void setUser(UserEntity user){
            this.user=user;
        }

        public Long getId() {
            return id;
        }
        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public boolean isCompleted() {
            return completed;
        }

        public UserEntity getUser(){
            return user;
        }
    }

