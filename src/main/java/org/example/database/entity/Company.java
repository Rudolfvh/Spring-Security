package org.example.database.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// для создание нового запроса
//@NamedQuery(
//        name = "Company.findByName",
//        query = "select c from Company c where lower(c.name) = lower(:name)"
//)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "company", schema = "public")
public class Company implements BaseEntity<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String name;

    @Builder.Default
    // превращаем вторую таблицу в мапу (можно в лист)
    @ElementCollection
    @CollectionTable(name = "company_locales",
            joinColumns = @JoinColumn(name = "company_id"))
    @MapKeyColumn(name = "lang")
    @Column(name = "description")
    private Map<String, String> locales = new HashMap<>();

    @ToString.Exclude
    @Builder.Default
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private List<User> users = new ArrayList<>();

}
