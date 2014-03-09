package com.nightingale.foodgame.model;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Food {

	public enum FoodState {
		GOOD,
		BAD,
		COVERED,
		GOOD_UNCOVERED,
		BAD_UNCOVERED
	}
	private FoodState state;
	
	private FoodHandler handler = FoodHandler.getInstance();
	
	private int textureIndex;

	private static final float SIZE = .0f;

	private HashMap<Rectangle, HashMap<FoodState, Integer> > foods;
	private Rectangle bounds = new Rectangle();

	public Food(Vector2 position, FoodState state) {
		foods = new HashMap<Rectangle, HashMap<FoodState, Integer>>();
		
		textureIndex = -1;
		
		bounds.x = position.x;
		bounds.y = position.y;
		bounds.width = SIZE * Gdx.graphics.getPpcX();
		bounds.height = SIZE * Gdx.graphics.getPpcY();
		
		this.state = state;
	}

	public void addFood(Vector2 position, FoodState state)
	{
		// Setting the food rectangle
		bounds.x = position.x;
		bounds.y = position.y;
		bounds.width = SIZE * Gdx.graphics.getPpcX();
		bounds.height = SIZE * Gdx.graphics.getPpcY();
		
		this.state = state;
		textureIndex = -1;
		// Giving the food to the handler
//		FoodHandler.getInstance().addFood(this);
	}

	public void animate(float delta) {
		Rectangle[] removableRect = new Rectangle[100];
		int i = 0;
		for (Rectangle rect : foods.keySet()) {
			if (rect.width < 1.2f*Gdx.graphics.getPpcX()){

				rect.width += delta;
				rect.height += delta;			
			}
			
			else {
				removableRect[i] = rect;
				i++;
			}
		}
		
		for (int k =0; k<i; k++) {
			foods.remove(removableRect[k]);
		}
		
	}
	
	public Rectangle getBounds() {
		return bounds;
	}
	
	public void setBounds(Rectangle bounds) {
		this.bounds = bounds;
	}

	public FoodState getState() {
		return state;
	}

	public void setState(FoodState state) {
		this.state = state;
	}
	
	public int getIndex() {
		return textureIndex;
	}
	
	public void setIndex(int index) {
		textureIndex = index;
	}

	public HashMap<Rectangle, HashMap<FoodState, Integer>> getFood() {
		return foods;
	}

	public void setFood(HashMap<Rectangle, HashMap<FoodState, Integer>> foods) {
		this.foods = foods;
	}


}
