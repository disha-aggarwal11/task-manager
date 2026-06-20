package com.disha.taskmanager.entity;

import jakarta.persistence.*;

@Entity
@Table(name="tasks")

    public class TaskEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String title;
        private String description;
        private boolean completed;

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
    }

