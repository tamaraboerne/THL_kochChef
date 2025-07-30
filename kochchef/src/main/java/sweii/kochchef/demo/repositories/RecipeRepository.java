package sweii.kochchef.demo.repositories;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import sweii.kochchef.demo.models.Recipe;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Integer>, JpaSpecificationExecutor<Recipe> {
    Page<Recipe> findByCategory_Id(Long categoryId, Pageable pageable);
    List<Recipe> findByCategory_Id(Long categoryId);
}
