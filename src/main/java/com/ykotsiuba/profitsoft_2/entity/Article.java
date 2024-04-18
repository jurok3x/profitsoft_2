package com.ykotsiuba.profitsoft_2.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "articles")
public class Article {
    @Id
    @Column(name = "id",unique=true, nullable = false)
    private UUID id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

    @Column(name = "field")
    @Enumerated(EnumType.STRING)
    private Field field;

    @Column(name = "year", columnDefinition = "integer")
    private Integer year;

    @Column(name = "journal", columnDefinition = "varchar(50)")
    private String journal;
}
