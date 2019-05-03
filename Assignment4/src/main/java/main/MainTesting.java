package main;

import java.math.BigDecimal;

import data.RestaurantSerializer;
import model.BaseProduct;
import model.CompositeProduct;
import model.Restaurant;

public class MainTesting {
	
	public static void main(String[] args) {

		BaseProduct soup = new BaseProduct("Soup", new BigDecimal("3.14"));
		BaseProduct chips = new BaseProduct("Chips", new BigDecimal("4.12"));
		BaseProduct fish = new BaseProduct("Fish", new BigDecimal("5.77"));
		
		CompositeProduct fishNChips = new CompositeProduct("Fish 'n Chips");
		fishNChips.addSubproduct(fish);
		fishNChips.addSubproduct(chips);
		
		CompositeProduct britMeal = new CompositeProduct("Brit Meal");
		britMeal.addSubproduct(soup);
		britMeal.addSubproduct(fishNChips);
		
		Restaurant res = new Restaurant();
		res.addToMenu(soup);
		res.addToMenu(chips);
		res.addToMenu(fish);
		res.addToMenu(fishNChips);
		res.addToMenu(britMeal);
		
		new RestaurantSerializer().serialize(res);
	}
}
