package guru.springframework.spring5recipeapp.bootstrap;

import guru.springframework.spring5recipeapp.domain.*;
import guru.springframework.spring5recipeapp.repositories.CategoryRepository;
import guru.springframework.spring5recipeapp.repositories.RecipeRepository;
import guru.springframework.spring5recipeapp.repositories.UnitOfMeasureRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class RecipeBootstrap implements ApplicationListener<ContextRefreshedEvent> {

    private final CategoryRepository categoryRepository;
    private final RecipeRepository recipeRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;

    public RecipeBootstrap(CategoryRepository categoryRepository, RecipeRepository recipeRepository, UnitOfMeasureRepository unitOfMeasureRepository) {
        this.categoryRepository = categoryRepository;
        this.recipeRepository = recipeRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        recipeRepository.saveAll(getRecipes());
        log.debug("Loading Bootstrap Data");
    }

    private List<Recipe> getRecipes() {

        List<Recipe> recipes = new ArrayList<>(2);

        Optional<UnitOfMeasure> cupUnitOfMeasureOptional = unitOfMeasureRepository.findByUom("Cup");

        if (!cupUnitOfMeasureOptional.isPresent()) {
            throw new RuntimeException("Expected UOM not found");
        }

        Optional<UnitOfMeasure> tableSpoonUomOptional = unitOfMeasureRepository.findByUom("Tablespoon");

        if (!tableSpoonUomOptional.isPresent()) {
            throw new RuntimeException("Expected UOM not found");
        }

        //get optionals
        UnitOfMeasure eachUnitOfMeasure = cupUnitOfMeasureOptional.get();
        UnitOfMeasure tableSpoonUom = tableSpoonUomOptional.get();

        //get Categories
        Optional<Category> americanCategoryOptional = categoryRepository.findByCategoryName("American");

        if (!americanCategoryOptional.isPresent()) {
            throw new RuntimeException("Expected Category not found");
        }

        Optional<Category> mexicanCategoryOptional = categoryRepository.findByCategoryName("Mexican");

        if (!mexicanCategoryOptional.isPresent()) {
            throw new RuntimeException("Expected Category not found");
        }

        Category americanCategory = americanCategoryOptional.get();
        Category mexicanCategory = mexicanCategoryOptional.get();

        Recipe guacRecipe = new Recipe();
        guacRecipe.setDescription("Pefect Guacamole");
        guacRecipe.setPrepTime(10);
        guacRecipe.setCookTime(0);
        guacRecipe.setDifficulty(Difficulty.EASY);
        guacRecipe.setDirections("jkfajskfjklasjf kjkje klj fkjsr fkj klj dkj kjkejk jfk jksj kj kdlj kflja kesj k j");

        Notes guacNotes = new Notes();
        guacNotes.setRecipeNotes("asf jskfjkasljf kjsafkljklj aklvjamnslfnakljf klj");

        //guacNotes.setRecipe(guacRecipe);
        //ova nam stavka vise nije potrebna zato sto smo u seteru za notes dodali tu stavku da odradi automatski
        guacRecipe.setNotes(guacNotes);

        //guacRecipe.getIngredients().add(new Ingredient("ripe avocados", new BigDecimal(2), eachUnitOfMeasure));
        guacRecipe.addIngredient(new Ingredient("ripe avocados", new BigDecimal(2), eachUnitOfMeasure));
        //guacRecipe.getIngredients().add(new Ingredient("kosher salt", new BigDecimal(5), tableSpoonUom));
        guacRecipe.addIngredient(new Ingredient("kosher salt", new BigDecimal(5), tableSpoonUom));

        guacRecipe.getCategories().add(americanCategory);
        guacRecipe.getCategories().add(mexicanCategory);

        recipes.add(guacRecipe);

        return recipes;
    }
}