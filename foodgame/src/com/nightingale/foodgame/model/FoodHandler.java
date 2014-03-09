package com.nightingale.foodgame.model;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.nightingale.foodgame.model.Food.FoodState;

public class FoodHandler {
	
	private static FoodHandler handler = new FoodHandler();
	private ArrayList<Food> foodList = new ArrayList<Food>();
	
	private FoodHandler() {
		
	}
	
	public static FoodHandler getInstance() {
		return handler;
	}
	
	public ArrayList<Food> getList() {
		return foodList;
	}
	
	public void setList(ArrayList<Food> foodList) {
		this.foodList = foodList;
	}
	
	public void addFood(Vector2 position, FoodState state) {
		foodList.add(new Food(position, state));
	}
	
	public void animate(float delta) {
		Food[] removableFood = new Food[100];
		int i = 0;
		for (Food food : foodList) {
			if (food.getBounds().width < 1.2f*Gdx.graphics.getPpcX()){
				
				food.setBounds(new Rectangle(
						food.getBounds().x,
						food.getBounds().y,
						food.getBounds().width + delta,
						food.getBounds().height + delta
						)
				);			
			}
			else {
				removableFood[i] = food;
				i++;
			}
		}
		
		for (int k =0; k<i; k++) {
			foodList.remove(k);
			i--;
		}
		
	}
}
