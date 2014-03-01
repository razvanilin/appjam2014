package com.nightingale.foodgame.view;

import java.util.HashMap;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.nightingale.foodgame.model.Food;
import com.nightingale.foodgame.model.Food.FoodState;

public class GameRenderer {
	
	private Game game;
	
	private TextureRegion backgroundTexture;
	private TextureRegion[] goodTexture = new TextureRegion[5];
	private TextureRegion[] badTexture = new TextureRegion[5];
	private TextureRegion coveredTexture;
	private SpriteBatch batch;
	
	private Food food;
	
	public GameRenderer(Game game) {
		this.game = game;
		loadItems();
	}
	
	private void loadItems() {
		backgroundTexture = new TextureRegion(new Texture(
				Gdx.files.internal("background/background.png")),
				0,
				0, 
				Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		batch = new SpriteBatch();
		
		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("models/models.pack"));
		for (int i = 0; i<5; i++) {
			goodTexture[i] = atlas.findRegion("food"+i);
		}
		for (int i = 0; i < 5; i++) {
			badTexture[i] = atlas.findRegion("bfood"+i);
		}
		coveredTexture = atlas.findRegion("cfood");
		
		food = new Food();
	}
	
	/* 
	 * Render fields
	 */
	
	float timePassed = 0.0f;
	float timeGood = 0.0f;
	float timeBad = 0.0f;
	float timeCoveredAppearance = 0.0f;
	float timeCoveredUnvield = 0.0f;
	
	public void render(float delta)
	{
		timePassed += delta;
		timeGood += delta;
		
		if (timeGood > 2.0f) {
			food.addFood(
					new Vector2(
							(int)(Math.random()*(Gdx.graphics.getWidth()-food.getBounds().width)),
							(int)(Math.random()*(Gdx.graphics.getHeight()-food.getBounds().height))), 
					FoodState.GOOD
					);
			timeGood = 0.0f;
		}
		
		batch.begin();
		batch.draw(backgroundTexture, 0, 0);
		drawFood();
		batch.end();
		
	}
	
	private void drawFood() {
		for (Rectangle rect : food.getFood().keySet()) {
			int index;
			HashMap<FoodState, Integer> map = new HashMap<FoodState, Integer>();
			map = food.getFood().get(rect);
			if (map.containsKey(FoodState.GOOD)){
				if (map.get(FoodState.GOOD) != -1)
					index = map.get(FoodState.GOOD);
				else {
					index = (int)(Math.random()*5);
					map.remove(FoodState.GOOD);
					map.put(FoodState.GOOD, index);
				}
				
				batch.draw(goodTexture[index], 
						rect.x,
						rect.y,
						rect.width,
						rect.height
						);
			}
			
			else if (map.containsKey(FoodState.BAD)) {
				batch.draw(badTexture[(int)(Math.random()*5)],
						rect.x,
						rect.y,
						rect.width,
						rect.height
						);
			}
			
			else if (map.containsKey(FoodState.COVERED)) {
				batch.draw(coveredTexture,
						rect.x,
						rect.y,
						rect.width,
						rect.height
						);
			}
			HashMap<Rectangle, HashMap<FoodState, Integer>> tempMap = new HashMap<Rectangle, HashMap<FoodState, Integer>>();
			tempMap = food.getFood();
			//tempMap.remove(rect);
			tempMap.put(rect, map);
			food.setFood(tempMap);
		}
	}

}
