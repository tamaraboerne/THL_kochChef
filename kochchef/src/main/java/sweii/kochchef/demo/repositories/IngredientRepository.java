package sweii.kochchef.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import sweii.kochchef.demo.models.Ingredient;

@Repository
public interface IngredientRepository  extends JpaRepository<Ingredient, Integer>, JpaSpecificationExecutor<Ingredient> {
}
