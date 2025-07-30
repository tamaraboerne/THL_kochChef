package sweii.kochchef.demo.createRecipeTest;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sweii.kochchef.demo.models.Category;
import sweii.kochchef.demo.models.Recipe;
import sweii.kochchef.demo.models.User;

import java.util.Date;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
public class CreateRecipeTest {

    private Recipe recipe;
    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        recipe = new Recipe();
        recipe.setName("Test Recipe");
        recipe.setCategory(new Category());
        recipe.setServings(4);
        recipe.setDuration(30);
        recipe.setCreationdate(new Date());
        recipe.setSteps("This is a step-by-step guide to make the recipe. Hallo dieses Rezept ist ein Rezept aus meiner Kindheit welches sehr sehr lecker ist. Bitte kocht es nach.");
        recipe.setCreator(new User());
    }

    @Test
    public void newRecipe() {
        assertEquals(recipe.getName(), "Test Recipe");
        assertEquals(recipe.getServings(), 4);
        assertEquals(recipe.getDuration(), 30);
        assertEquals(recipe.getSteps(), "This is a step-by-step guide to make the recipe. Hallo dieses Rezept ist ein Rezept aus meiner Kindheit welches sehr sehr lecker ist. Bitte kocht es nach.");
    }

    @Test
    public void testNameNotNull() {
        recipe.setName(null);
        Set<ConstraintViolation<Recipe>> violations = validator.validate(recipe);
        assertFalse(violations.isEmpty());
        assertEquals("'' darf nicht leer sein.", violations.iterator().next().getMessage());
    }

    @Test
    public void testNamePattern() {
        recipe.setName("Invalid@Name!");
        Set<ConstraintViolation<Recipe>> violations = validator.validate(recipe);
        assertFalse(violations.isEmpty());
        assertEquals("'Invalid@Name!' entspricht nicht dem Muster ^[a-zA-Z0-9 ]+$", violations.iterator().next().getMessage());
    }

    @Test
    public void testNameSize() {
        recipe.setName("ab");
        Set<ConstraintViolation<Recipe>> violations = validator.validate(recipe);
        assertFalse(violations.isEmpty());
        assertEquals("'ab' darf 3 bis 60 Zeichen lang sein.", violations.iterator().next().getMessage());

        recipe.setName("a".repeat(61));
        violations = validator.validate(recipe);
        assertFalse(violations.isEmpty());
        assertEquals("'" + "a".repeat(61) + "' darf 3 bis 60 Zeichen lang sein.", violations.iterator().next().getMessage());
    }

    @Test
    public void testServingsRange() {
        recipe.setServings(0);
        Set<ConstraintViolation<Recipe>> violations = validator.validate(recipe);
        assertFalse(violations.isEmpty());
        assertEquals("'0' muss mindestens 1 Portion(en) haben.", violations.iterator().next().getMessage());

        recipe.setServings(13);
        violations = validator.validate(recipe);
        assertFalse(violations.isEmpty());
        assertEquals("'13' darf höchstens 12 Portionen haben.", violations.iterator().next().getMessage());
    }

    @Test
    public void testDurationRange() {
        recipe.setDuration(0);
        Set<ConstraintViolation<Recipe>> violations = validator.validate(recipe);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("'0' muss mindestens 1 Minute(n) dauern.", violations.iterator().next().getMessage());
    }

    @Test
    public void testStepsSize() {
        recipe.setSteps("a".repeat(149));
        Set<ConstraintViolation<Recipe>> violations = validator.validate(recipe);
        assertFalse(violations.isEmpty());
        assertEquals("'"+ "a".repeat(149) + "'" + " muss mindestens 150 Zeichen haben und darf höchstens 3000 Zeichen haben.", violations.iterator().next().getMessage());

        recipe.setSteps("a".repeat(3001));
        violations = validator.validate(recipe);
        assertFalse(violations.isEmpty());
        assertEquals("'"+ "a".repeat(3001) + "'" + " muss mindestens 150 Zeichen haben und darf höchstens 3000 Zeichen haben.", violations.iterator().next().getMessage());
    }

    @Test
    public void testCategoryNotNull() {
        recipe.setCategory(null);
        Set<ConstraintViolation<Recipe>> violations = validator.validate(recipe);
        assertFalse(violations.isEmpty());
        assertEquals("must not be null", violations.iterator().next().getMessage());
    }


}
