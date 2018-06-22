package com.tripmaker.demo.data;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Set;

@Entity
@Table(name = "user")
public class User implements Serializable{

    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    @Email(message = "Please provide a valid email")
    @NotEmpty(message = "Please provide an email")
    private String email;

    @Length(min = 5, message = "Your password must have at least 5 characters")
    @NotEmpty(message = "Please provide your password")
    @Column(name = "password")
    private String password;

    @NotEmpty(message = "Please provide your username")
    @Column(name = "user_name", unique = true)
    private String userName;

    private String name;

    @Column(name = "last_name")
    private String lastName;

    @ManyToOne(cascade = CascadeType.ALL)
    private Locale locale;

    @NotEmpty(message = "Please provide role")
    private String role;

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "users")
    @JsonIgnore
    @JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@user_generator_id")
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

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
