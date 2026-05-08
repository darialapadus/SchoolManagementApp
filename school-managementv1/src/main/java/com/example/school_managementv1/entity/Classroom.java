package com.example.school_managementv1.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "classrooms",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"building", "room_number"})
        }
)
public class Classroom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 1, max = 50)
    @Column(nullable = false)
    private String building;

    @NotBlank
    @Size(min = 1, max = 20)
    @Column(name = "room_number", nullable = false)
    private String roomNumber;

    @NotNull
    @Min(1)
    @Column(nullable = false)
    private Integer capacity;

    @OneToMany(mappedBy = "classroom", cascade = CascadeType.ALL, orphanRemoval = false)
    @JsonIgnore
    private List<Course> courses = new ArrayList<>();

    public Classroom() {
    }

    public Long getId() {
        return id;
    }

    public String getBuilding() {
        return building;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }
}
