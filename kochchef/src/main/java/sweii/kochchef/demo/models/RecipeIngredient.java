package sweii.kochchef.demo.models;

import jakarta.persistence.*;

@Entity
@Table(name = "RECIPE_INGREDIENT")
public class RecipeIngredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "INGREDIENT_NAME")
    private String ingredientName;

    @ManyToOne
    @JoinColumn(name = "RECIPE_ID")
    private Recipe recipe;

    @Column(name = "QUANTITY")
    private float quantity;

    @Column(name = "UNIT")
    private String unit;

    // Getter & Setter

    public Long getId() {
        return id;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public float getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
