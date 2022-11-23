package com.alex.taco;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.Valid;
import org.springframework.validation.Errors;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/design")
@SessionAttributes("tacoOrder")
public class DesignTacoController {
	private final IngredientRepository ingredientRepo;

	@Autowired 
	public DesignTacoController(IngredientRepository ingredientRepo) { 
		this.ingredientRepo = ingredientRepo;
	}

	@ModelAttribute 
	public void addIngredientsToModel(Model model) { 
		Iterable<Ingredient> ingredients = ingredientRepo.findAll(); 
		Ingredient.Type[] types = Ingredient.Type.values();
		for (Ingredient.Type type : types) { 
			model.addAttribute(type.toString().toLowerCase(), filterByType(ingredients, type));
		} 
	}

	@ModelAttribute(name = "tacoOrder")
	public TacoOrder order() {
		return new TacoOrder();
	}

	@ModelAttribute(name = "taco")
	public Taco taco() {
		return new Taco();
	}

	@GetMapping
	public String showDesignForm() {
		return "design";
	}

	/*
	@PostMapping
	public String processTaco(Taco taco, @ModelAttribute TacoOrder tacoOrder) {
		tacoOrder.addTaco(taco); 
		log.info("Processing taco: {}", taco); 
		return "redirect:/orders/current";
	}
	*/

	@PostMapping
  	public String processTaco(@Valid Taco taco, Errors errors, @ModelAttribute TacoOrder tacoOrder) {
    	if (errors.hasErrors()) {
    		log.error("Error: {}", errors);
      		return "design";
    	}

    	tacoOrder.addTaco(taco);
    	log.info("Processing taco: {}", taco);
	    return "redirect:/orders/current";
  	}

  	/*
	private Iterable<Ingredient> filterByType(List<Ingredient> ingredients, Ingredient.Type type) {
		return ingredients.stream()
					      .filter(x -> x.getType().equals(type))
					      .collect(Collectors.toList());
	}
	*/

	private Iterable<Ingredient> filterByType(Iterable<Ingredient> ingredients, Ingredient.Type type) { 
		return StreamSupport
				.stream(ingredients.spliterator(), false)
              	.filter(i -> i.getType().equals(type))
              	.collect(Collectors.toList());
  }
}