package com.example.database.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.Instant;


@Entity
@Table(name = "task")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskModel implements BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private String teg;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Builder.Default
    Instant created = Instant.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_state_id", nullable = false) // внешний ключ
    private TaskStateModel taskState;




    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTeg() {
        return teg;
    }

    public void setTeg(String teg) {
        this.teg = teg;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Instant getCreated() {
        return created;
    }

    public void setCreated(Instant created) {
        this.created = created;
    }
}
