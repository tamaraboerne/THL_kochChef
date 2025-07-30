package sweii.kochchef.demo.controllerTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.Mockito.verify;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.ui.Model;
import sweii.kochchef.demo.controller.AdminViewController;
import sweii.kochchef.demo.models.Category;
import sweii.kochchef.demo.models.Ingredient;
import sweii.kochchef.demo.repositories.CategoryRepository;
import sweii.kochchef.demo.repositories.IngredientRepository;

import java.util.List;
import java.util.ArrayList;

@ExtendWith(MockitoExtension.class)
public class AdminViewControllerTest {

    @Mock
    private IngredientRepository ingredientRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private Model model;

    @InjectMocks
    private AdminViewController adminViewController;

    private List<Ingredient> ingredientList;
    private List<Category> categoryList;

    /*
    @BeforeEach
    void setUp() {
        // Initialize your lists with sample data
        ingredientList = List.of(new Ingredient(), new Ingredient());
        categoryList = List.of(new Category(), new Category());
    }

     */


    @BeforeEach
    void setUp() {
        // Initialize your lists with sample data
        ingredientList =  new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            ingredientList.add(new Ingredient());
        }
        categoryList = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            categoryList.add(new Category());
        }
    }

    @Test
    void testIngredientList() {
        // Mock the behavior of your repositories
        when(ingredientRepository.findAll(any(PageRequest.class)))
                .thenReturn(new PageImpl<>(ingredientList));
        when(categoryRepository.findAll(any(PageRequest.class)))
                .thenReturn(new PageImpl<>(categoryList));

        // Call the method
        String viewName = adminViewController.ingredientList(20, 20, model);

        // Verify interactions and assert the model attributes
        assertThat(viewName).isEqualTo("adminView");
        verify(model).addAttribute("categories", categoryList);
        verify(model).addAttribute("categorySize", 20);
        verify(model).addAttribute("ingredients", ingredientList);
        verify(model).addAttribute("ingredientSize", 20);
    }

    @Test
    void testLoadMoreIngredients() {
        // Set initial displayed ingredients
        //adminViewController.displayedIngredients = 20;

        // Mock the behavior of the repository
        when(ingredientRepository.findAll()).thenReturn(ingredientList);

        // Call the method
        String redirectUrl = adminViewController.loadMoreIngredients(20, 20);

        // Verify the expected changes
        assertThat(redirectUrl).isEqualTo("redirect:/adminView?ingredientSize=30&categorySize=20");
        assertThat(adminViewController.getDisplayedIngredients()).isEqualTo(30);
    }

    @Test
    void testLoadMoreCategories() {
        // Set initial displayed categories
        //adminViewController.displayedCategories = 20;

        // Mock the behavior of the repository
        when(categoryRepository.findAll()).thenReturn(categoryList);

        // Call the method
        String redirectUrl = adminViewController.loadMoreCategories(20, 20);

        // Verify the expected changes
        assertThat(redirectUrl).isEqualTo("redirect:/adminView?ingredientSize=20&categorySize=30");
        assertThat(adminViewController.getDisplayedCategories()).isEqualTo(30);
    }

}