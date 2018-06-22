package com.tripmaker.demo.data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "locale")
public class Locale implements Serializable{

    @Id
    @GeneratedValue
    @Column(name = "locale_id")
    private Long id;

    private String name;

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
}
