package com.ykotsiuba.profitsoft_2.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "authors")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Author {

    @Id
    @Column(name = "id",unique=true, nullable = false)
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    private UUID id;

    @Column(name = "email", unique=true, nullable = false, columnDefinition = "varchar(50)")
    private String email;

    @Column(name = "first_name", columnDefinition = "varchar(50)")
    private String firstName;

    @Column(name = "last_name", columnDefinition = "varchar(50)")
    private String lastName;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "author")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<Article> articles;

    public void dismissArticle(Article article) {
        this.articles.remove(article);
    }
}
