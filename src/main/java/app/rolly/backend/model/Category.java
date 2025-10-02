package app.rolly.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Data
@Table(name="category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "category_seq")
    @SequenceGenerator(
            name = "category_seq",
            sequenceName = "category_sequence",
            allocationSize = 1
    )
    private Long id;

    public Category(String name){
        this.name = name;
    }

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Trick> tricks = new ArrayList<>();

    @Column(nullable = false)
    private String name;
}
