package sweii.kochchef.demo.models;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "RECIPE")
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;

    @NotNull
    @Column(name = "NAME", unique = true)
    @Pattern(regexp = "^[a-zA-Z0-9 ]+$", message = "'${validatedValue}' entspricht nicht dem Muster {regexp}")
    @Size(min = 3, max = 60, message = "'${validatedValue}' darf {min} bis {max} Zeichen lang sein.")
    @NotEmpty(message = "'${validatedValue}' darf nicht leer sein.")
    private String name;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "CATEGORY")
    private Category category;

    @NotNull
    @Min(value = 1, message = "'${validatedValue}' muss mindestens {value} Portion(en) haben.")
    @Max(value = 12, message = "'${validatedValue}' darf höchstens {value} Portionen haben.")
    @Column(name = "SERVINGS")
    private int servings;

    @NotNull
    @Min(value = 1, message = "'${validatedValue}' muss mindestens {value} Minute(n) dauern.")
    @Column(name = "DURATION")
    private int duration;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATION_DATE", updatable = false)
    private Date creationdate;

    @Column(name = "DELETION_DATE")
    private Date deletiondate;

    @Column(name = "IMAGE_PATH")
    private String imagePath;

    @Column(name = "RECIPE_STEPS")
    @Size(min = 150, max = 3000, message = "'${validatedValue}' muss mindestens {min} Zeichen haben und darf höchstens {max} Zeichen haben.")
    @NotEmpty(message = "'${validatedValue}' darf nicht leer sein.")
    private String steps;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecipeIngredient> ingredients = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User creator;

    public Recipe() {
    }

    public Recipe(final int id, final String name, final Date creationdate, final Date deletiondate) {
        this.id = id;
        this.name = name;
        this.creationdate = creationdate;
        this.deletiondate = deletiondate;
    }

    // Getter & Setter

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Category getCategory() {
        return category;
    }

    public int getServings() {
        return servings;
    }

    public int getDuration() {
        return duration;
    }

    public Date getCreationdate() {
        return creationdate;
    }

    public Date getDeletiondate() {
        return deletiondate;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getSteps() {
        return steps;
    }

    public List<RecipeIngredient> getIngredients() {
        return ingredients;
    }

    public User getCreator() {
        return creator;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setCreationdate(Date creationdate) {
        this.creationdate = creationdate;
    }

    public void setDeletiondate(Date deletiondate) {
        this.deletiondate = deletiondate;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public void setIngredients(List<RecipeIngredient> ingredients) {
        this.ingredients = ingredients;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }
}
