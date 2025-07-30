package sweii.kochchef.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sweii.kochchef.demo.models.Category;
import sweii.kochchef.demo.models.Ingredient;
import sweii.kochchef.demo.repositories.CategoryRepository;
import sweii.kochchef.demo.repositories.IngredientRepository;

import java.util.List;

@Controller
public class AdminViewController {
    private static final int MAX_PAGE_SIZE = 20;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private CategoryRepository categoryRepository;



    private int displayedIngredients = MAX_PAGE_SIZE;
    private int displayedCategories = MAX_PAGE_SIZE;

    // Getters for testing
    public int getDisplayedIngredients() {
        return displayedIngredients;
    }

    public int getDisplayedCategories() {
        return displayedCategories;
    }


    @GetMapping("/adminView")
    public String ingredientList(@RequestParam(name = "categorySize", defaultValue = "20") int categorySize,
                                 @RequestParam(name = "ingredientSize", defaultValue = "20")int ingredientSize,
                                 Model model) {
        Page<Ingredient> recipePage = ingredientRepository.findAll(PageRequest.of(0, ingredientSize));
        List<Ingredient> ingredients = recipePage.getContent();
        Page<Category> categoryPage = categoryRepository.findAll(PageRequest.of(0, categorySize));
        List<Category> categories = categoryPage.getContent();
        model.addAttribute("categories", categories);
        model.addAttribute("categorySize", categorySize);
        model.addAttribute("ingredients", ingredients);
        model.addAttribute("ingredientSize", ingredientSize);
        displayedIngredients = ingredientSize;
        return "adminView";
    }

    @GetMapping("/adminView/load-more-ingredients")
    public String loadMoreIngredients(@RequestParam(name = "currentSize", defaultValue = "20") int currentSize,
                                      @RequestParam(name = "categorySize", defaultValue = "20") int categorySize) {
        int totalIngredients = ingredientRepository.findAll().size();
        // Erweitern nur, wenn es weitere Zutaten gibt
        if (displayedIngredients < totalIngredients) {
            displayedIngredients += 10;
        }

        // Erhöhe die Anzahl der angezeigten Zutaten um 10
        return "redirect:/adminView?ingredientSize=" + displayedIngredients + "&categorySize=" + categorySize; // Umleitung zur Startseite, um die aktualisierte Liste zu laden
    }

    @GetMapping("/adminView/load-more-categories")
    public String loadMoreCategories(@RequestParam(name = "currentSize", defaultValue = "20") int currentSize,
                                     @RequestParam(name = "ingredientSize", defaultValue = "20") int ingredientSize) {
        int totalCategories = categoryRepository.findAll().size();
        // Erweitern nur, wenn es weitere Kategorien gibt
        if (displayedCategories < totalCategories) {
            displayedCategories += 10;
        }

        // Erhöhe die Anzahl der angezeigten Kategorien um 10
        return "redirect:/adminView?ingredientSize=" + ingredientSize + "&categorySize=" + displayedCategories; // Umleitung zur Startseite, um die aktualisierte Liste zu laden
    }

}





































