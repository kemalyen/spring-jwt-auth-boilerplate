package com.gazatem.corvus.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

@Entity
@Table(name = "roles")
public class Role  implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    private String name;

    private String display_name;

    @ManyToMany(mappedBy = "roles")
    private Collection<User> users;

    @Temporal(TemporalType.DATE)
    private Date creationDate;

    public Role() {
        super();
    }

    public Role(final String name, String display_name) {
        super();
        this.name = name;
        this.display_name = display_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

}
