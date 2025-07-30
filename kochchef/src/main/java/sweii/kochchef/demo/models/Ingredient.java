package sweii.kochchef.demo.models;

import jakarta.persistence.*;

import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "INGREDIENT")
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @NotNull
    @Column(name = "NAME")
    private String name;

    // Getter und Setter
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
