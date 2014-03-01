package com.nightingale.foodgame.model;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Food {
	
	public enum FoodState {
		GOOD,
		BAD,
		COVERED
	}
	
	private FoodState state;
	private int textureIndex = -1;
	
	private static final float SIZE = 1.0f;
	
	private HashMap<Rectangle, HashMap<FoodState, Integer> > foods;
	private Vector2 position;
	private Rectangle bounds = new Rectangle();
	
	public Food() {
		foods = new HashMap<Rectangle, HashMap<FoodState, Integer>>();
		
		bounds.x = 0;
		bounds.y = 0;
		bounds.width = SIZE * Gdx.graphics.getPpcX();
		bounds.height = SIZE * Gdx.graphics.getPpcY();
	}
	
	public void addFood(Vector2 position, FoodState state)
	{
		Rectangle newFood = new Rectangle();
		newFood.width = bounds.width;
		newFood.height = bounds.height;
		newFood.x = position.x;
		newFood.y = position.y;
		
		HashMap<FoodState, Integer> map = new HashMap<FoodState, Integer>();
		map.put(state, textureIndex);
		
		foods.put(newFood, map);
	}
	
	public Vector2 getPosition() {
		return position;
	}
	
	public void setPosition(Vector2 position) {
		this.position = position;
	}
	
	public Rectangle getBounds() {
		return bounds;
	}
	
	public FoodState getState() {
		return state;
	}
	
	public void setState(FoodState state) {
		this.state = state;
	}
	
	public HashMap<Rectangle, HashMap<FoodState, Integer>> getFood() {
		return foods;
	}
	
	public void setFood(HashMap<Rectangle, HashMap<FoodState, Integer>> foods) {
		this.foods = foods;
	}
	

}
