package sweii.kochchef.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sweii.kochchef.demo.models.Category;
import sweii.kochchef.demo.models.Ingredient;
import sweii.kochchef.demo.models.Recipe;
import sweii.kochchef.demo.models.User;
import sweii.kochchef.demo.repositories.CategoryRepository;
import sweii.kochchef.demo.repositories.IngredientRepository;
import sweii.kochchef.demo.repositories.RecipeRepository;
import sweii.kochchef.demo.service.UserService;
import java.security.Principal;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Controller
public class HomePageController {

    private static final int DEFAULT_PAGE_SIZE = 20;
    private int displayedRecipes = DEFAULT_PAGE_SIZE;

    private static final Category DEFAULT_CATEGORY = null;
    private Category categoryFilter = DEFAULT_CATEGORY;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private UserService userService;

    @GetMapping("/main")
    public String redMain(Model model,Principal principal, @RequestParam(name = "categoryFilterPar", required = false) Long categoryFilterPar, @RequestParam(name = "pageSize", defaultValue = "20") int pageSize) {
        if (principal != null) {
            String username = principal.getName();
            model.addAttribute("username", username);

            if (principal instanceof Authentication) {

                UserDetails userDetails = (UserDetails) ((Authentication) principal).getPrincipal();
                String email = userDetails.getUsername(); // Beispiel: Benutzername als E-Mail speichern
                model.addAttribute("email", email);
                // Füge weitere Benutzerdaten hinzu, falls nötig
            }

            model.addAttribute("userPrincipal", principal);
        }

        Page<Recipe> recipePage;
        if (categoryFilterPar != null) {
            recipePage = recipeRepository.findByCategory_Id(categoryFilterPar, PageRequest.of(0, pageSize));
        } else {
            recipePage = recipeRepository.findAll(PageRequest.of(0, pageSize));
        }

        List<Recipe> recipes = recipePage.getContent();
        List<Category> categories = categoryRepository.findAll();

        model.addAttribute("recipes", recipes);
        model.addAttribute("categories", categories);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("categoryFilterPar", categoryFilterPar);

        // Reset displayedRecipes and categoryFilter on page load
        displayedRecipes = pageSize;
        categoryFilter = categoryFilterPar != null ? categoryRepository.findById(Math.toIntExact(categoryFilterPar)).orElse(null) : null;

        return "main";
    }


    @GetMapping("/")
    public String getAllRecipes(Model model,Principal principal, @RequestParam(name = "categoryFilterPar", required = false) Long categoryFilterPar, @RequestParam(name = "pageSize", defaultValue = "20") int pageSize) {
        if (principal != null) {
            String username = principal.getName();
            model.addAttribute("username", username);

            if (principal instanceof Authentication) {

                UserDetails userDetails = (UserDetails) ((Authentication) principal).getPrincipal();
                String email = userDetails.getUsername(); // Beispiel: Benutzername als E-Mail speichern
                model.addAttribute("email", email);
                // Füge weitere Benutzerdaten hinzu, falls nötig
            }

            model.addAttribute("userPrincipal", principal);
        }

        Page<Recipe> recipePage;
        if (categoryFilterPar != null) {
            recipePage = recipeRepository.findByCategory_Id(categoryFilterPar, PageRequest.of(0, pageSize));
        } else {
            recipePage = recipeRepository.findAll(PageRequest.of(0, pageSize));
        }

        List<Recipe> recipes = recipePage.getContent();
        List<Category> categories = categoryRepository.findAll();

        model.addAttribute("recipes", recipes);
        model.addAttribute("categories", categories);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("categoryFilterPar", categoryFilterPar);

        // Reset displayedRecipes and categoryFilter on page load
        displayedRecipes = pageSize;
        categoryFilter = categoryFilterPar != null ? categoryRepository.findById(Math.toIntExact(categoryFilterPar)).orElse(null) : null;

        return "redirect:/main";
    }


    @GetMapping("/protected")
    public String protectedPage() {
        // Dies löst die HTTP Basic Authentifizierung aus
        return "redirect:/"; // Weiterleitung nach erfolgreicher Authentifizierung
    }

    @GetMapping("/logout")
    public String logout() {
        // Dies löst die HTTP Basic Authentifizierung aus
        return "redirect:/"; // Weiterleitung nach erfolgreicher Authentifizierung
    }

    @GetMapping("/load-more")
    public String loadMoreRecipes(@RequestParam(name = "categoryFilterPar", required = false) Long categoryFilterPar, @RequestParam(name = "pageSize", defaultValue = "20") int pageSize) {
        int totalRecipes = recipeRepository.findAll().size();
        // erweitert nur, wen es weitere Recipes gibt
        if(displayedRecipes < totalRecipes) {
            displayedRecipes += 10;
        }

        // Redirect to start page with updated pageSize and category filter
        return "redirect:/main?pageSize=" + displayedRecipes + "&categoryFilterPar=" + (categoryFilterPar != null ? categoryFilterPar : "");
    }

    @GetMapping("/recipe/{id}")
    public String getRecipeDetails(@PathVariable int id, Model model,
                                   @RequestParam(name = "categoryFilterPar", required = false) Long categoryFilterPar,
                                   @RequestParam(name = "pageSize", defaultValue = "20") int pageSize) {
        Recipe recipe = recipeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid recipe Id:" + id));
        model.addAttribute("recipe", recipe);
        model.addAttribute("categoryFilterPar", categoryFilterPar);
        model.addAttribute("pageSize", pageSize);
        return "recipedetails";
    }

    // Providing all ingredients and categories from the database
    // Adding an empty recipe object
    @GetMapping("/create_recipe")
    public String showCreateRecipeForm(Model model,
                                       Principal principal,
                                       @RequestParam(name = "categoryFilterPar", required = false) Long categoryFilterPar,
                                       @RequestParam(name = "pageSize", defaultValue = "20") int pageSize) {
        User creator = userService.findByEmail("MaxMustermann@gmail.com");

        if (principal != null) {
            String username = principal.getName();
            model.addAttribute("username", username);

            if (principal instanceof Authentication) {

                UserDetails userDetails = (UserDetails) ((Authentication) principal).getPrincipal();
                String email = userDetails.getUsername(); // Beispiel: Benutzername als E-Mail speichern
                model.addAttribute("email", email);

                creator = userService.findByEmail(email);
            }

            model.addAttribute("userPrincipal", principal);
        }

        Recipe recipe = new Recipe();
        List<Category> categories = categoryRepository.findAll();
        List<Ingredient> ingredients = ingredientRepository.findAll();

        model.addAttribute("categories", categories);
        model.addAttribute("recipe", recipe);
        model.addAttribute("ingredients", ingredients);
        model.addAttribute("creator", creator);
        model.addAttribute("categoryFilterPar", categoryFilterPar);
        model.addAttribute("pageSize", pageSize);
        return "create_recipe";
    }

    // Validating the recipe and adding it to database
    @PostMapping("/save_recipe")
    public String saveRecipe(@ModelAttribute("recipe") Recipe recipe, @RequestParam("creatorEmail") String creatorEmail, BindingResult result,
                             @RequestParam("image") MultipartFile file, Model model) {
        // If any errors restart
        if (result.hasErrors()) {
            List<Category> categories = categoryRepository.findAll();
            List<Ingredient> ingredients = ingredientRepository.findAll();
            model.addAttribute("categories", categories);
            model.addAttribute("ingredients", ingredients);
            return "create_recipe";
        } // dawojdwaljdaowjdowa213141

        // Get the category to add it to the recipe (easier filter on mainpage)
        if (recipe.getCategory() != null && recipe.getCategory().getId() != null) {
            Category category = categoryRepository.findById(Math.toIntExact(recipe.getCategory().getId()))
                    .orElseThrow(() -> new IllegalArgumentException("Ungültige Kategorie-ID: " + recipe.getCategory().getId()));
            recipe.setCategory(category);
        } else {
            throw new IllegalArgumentException("Kategorie darf nicht null sein");
        }

        // Saving file to local storage and adding imagePath to recipe object
        if (!file.isEmpty()) {
            try {
                String fileName = file.getOriginalFilename();
                String fileExtension = fileName.substring(fileName.lastIndexOf("."));
                String uniqueFileName = UUID.randomUUID() + fileExtension;

                String imagePath = Paths.get("external-resources", "images").toString();
                File directory = new File(imagePath);
                if (!directory.exists()) {
                    directory.mkdirs();
                }

                String filePath = Paths.get(imagePath, uniqueFileName).toAbsolutePath().toString();
                Files.copy(file.getInputStream(), Paths.get(filePath));

                recipe.setImagePath("/images/" + uniqueFileName);
            } catch (IOException e) {
                e.printStackTrace();
                model.addAttribute("imageUploadError", "Bild konnte nicht hochgeladen werden");
                return "create_recipe";
            }
        }

        // Adding ingredient and recipe mapping to ingredients
        recipe.getIngredients().stream().filter(ingredient -> ingredient.getIngredientName() != null).toList().forEach(ingredient -> ingredient.setRecipe(recipe));

        // Adding the creator (cur. dummy user) to the recipe
        User creator = userService.findByEmail(creatorEmail);
        recipe.setCreator(creator);
        creator.addRecipes(recipe);

        // Saving to repository
        recipeRepository.save(recipe);
        return "redirect:/recipe/" + recipe.getId() + "?pageSize=" + displayedRecipes + "&categoryFilterPar=" + (categoryFilter != null ? categoryFilter : "");
    }
}
