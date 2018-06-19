package com.tripmaker.demo.Data;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Set;

//Nie należy używać LOMBOKA!!!

@Entity
@Table(name = "user")
public class User implements Serializable{

    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    @Email(message = "Please provide a valid email")
    @NotEmpty(message = "Please provice an email")
    private String email;

    @Length(min = 5, message = "Your password must have at least 5 characters")
    @NotEmpty(message = "Please provide your password")
    @Column(name = "password")
    private String password;

    @NotEmpty(message = "Please provide your name")
    private String name;

    @NotEmpty(message = "Please provide your last name")
    private String last_name;

    @ManyToOne(cascade = CascadeType.ALL)
    private Locale locale;

    @NotEmpty(message = "Please provide role")
    private String role;

//cos sie tu wali
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_trip_group", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "trip_group_id"))
    private Set<TripGroup> tripGroups;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public Set<TripGroup> getTripGroups() {
        return tripGroups;
    }

    public void setTripGroups(Set<TripGroup> tripGroups) {
        this.tripGroups = tripGroups;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
