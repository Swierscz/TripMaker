package com.tripmaker.demo.Data;


import org.omg.PortableServer.ServantActivator;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Entity
@Table(name = "role")
public class Role implements Serializable{
    @Id
    @GeneratedValue
    @Column(name = "role_id")
    private Long id;

    @NotEmpty(message = "Please provide a role")
    private String role;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
