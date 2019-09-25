package com.example.demo.vo;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tutorials_tbl")
@NamedQuery(name="Tutorials.testFindByTitle",
        query = "select t from Tutorials t where t.title =?1")
public class Tutorials {
    @Id
    @Column(name = "tutorial_id")
    private Long id;

    @Column(name = "tutorial_title")
    private String title;

    @Column(name = "tutorial_author")
    private String author;

    @Column(name = "submission_date")
    private Date subDate;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getSubDate() {
        return subDate;
    }

    public void setSubDate(Date subDate) {
        this.subDate = subDate;
    }
}
