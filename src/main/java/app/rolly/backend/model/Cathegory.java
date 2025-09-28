package app.rolly.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Data
@Table(name="cathegory")
public class Cathegory {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cathegory_seq")
    @SequenceGenerator(
            name = "cathegory_seq",
            sequenceName = "cathegory_sequence",
            allocationSize = 1
    )
    private long id;

    // TODO czy to ma znaczenie gdzie daję mapped a gdzie tworzę tabelę?
    @OneToMany(mappedBy = "trick_id", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Trick> tricks = new ArrayList<>();

    @Column(nullable = false)
    private String name;
}
