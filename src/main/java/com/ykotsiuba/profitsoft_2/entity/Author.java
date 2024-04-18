package com.ykotsiuba.profitsoft_2.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "authors")
public class Author {
    @Id
    @Column(name = "id",unique=true, nullable = false)
    private UUID id;

    @Column(name = "first_name", columnDefinition = "varchar(50)")
    private String firstName;

    @Column(name = "last_name", columnDefinition = "varchar(50)")
    private String lastName;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "powerStation")
    private List<Article> articles;
}
