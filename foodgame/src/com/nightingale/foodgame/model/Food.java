package com.nightingale.foodgame.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Food {

	public enum FoodState {
		GOOD,
		BAD,
		COVERED,
		GOOD_UNCOVERED,
	}
	private static final float SIZE = .0f;
	
	private FoodState state;	
	private int textureIndex;

	private Rectangle bounds = new Rectangle();

	public Food(Vector2 position, FoodState state) {		
		textureIndex = -1;
		
		bounds.x = position.x;
		bounds.y = position.y;
		bounds.width = SIZE * Gdx.graphics.getPpcX();
		bounds.height = SIZE * Gdx.graphics.getPpcY();
		
		this.state = state;
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
}
