package com.nightingale.foodgame.view;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.nightingale.foodgame.model.Food;
import com.nightingale.foodgame.model.Food.FoodState;
import com.nightingale.foodgame.screen.EndScreen;

public class GameRenderer {
	
	private Game game;
	
	private TextureRegion backgroundTexture;
	private TextureRegion[] goodTexture = new TextureRegion[5];
	private TextureRegion[] badTexture = new TextureRegion[5];
	private TextureRegion coveredTexture;
	private SpriteBatch batch;
	private Vector2 touchedArea = new Vector2();
	private BitmapFont gameFont;
	private Sound eatSound;
	private Sound ewSound;
	private Sound bkSound;
	private ParticleEffect effect;
	private ArrayList<ParticleEffect> effects = new ArrayList<ParticleEffect>();
	
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
		
		gameFont = new BitmapFont(Gdx.files.internal("fonts/title.fnt"));
		
		eatSound = Gdx.audio.newSound(Gdx.files.internal("sounds/eat.wav"));
		ewSound = Gdx.audio.newSound(Gdx.files.internal("sounds/eww.wav"));
		//bkSound = Gdx.audio.newSound(Gdx.files.internal("sounds/background.MP3"));
		//bkSound.play(0.5f);
		
		shape = new ShapeRenderer();
		
		effect = new ParticleEffect();
		effect.load(Gdx.files.internal("effects/mist.p"), Gdx.files.internal("effects"));
		effect.setPosition(0, 0);
		effect.start();
	}
	
	/* 
	 * Render fields
	 */
	
	float timePassed = 0.0f;
	float timeGood = 0.0f;
	float timeBad = 0.0f;
	float timeCoveredAppearance = 0.0f;
	float timeCoveredUnvield = 0.0f;
	Rectangle[] removableRect = new Rectangle[100];
	Rectangle removeRect = new Rectangle();
	int score = 0;
	int ending = 3;
	boolean flash = false;
	float flashDuration = 0.0f;
	ShapeRenderer shape;
	
	public void render(float delta)
	{
		timePassed += delta;
		timeGood += delta;
		timeBad+=delta;
		timeCoveredAppearance+=delta;
		
		batch.begin();
		batch.draw(backgroundTexture, 0, 0);
		//batch.draw(coveredTexture, touchedArea.x, touchedArea.y, 100.0f, 100.0f);
		drawFood();
		gameFont.draw(batch, String.valueOf(score), 20.0f, Gdx.graphics.getHeight()-20.0f);
		for (int i = ending;i>0;i--){
			if (i == 3)
				batch.draw(goodTexture[1], Gdx.graphics.getWidth()/1.3f, Gdx.graphics.getHeight()/1.1f, 0.5f*Gdx.graphics.getPpcX(), 0.5f*Gdx.graphics.getPpcY());
			if (i == 2)
				batch.draw(goodTexture[1], Gdx.graphics.getWidth()/1.2f, Gdx.graphics.getHeight()/1.1f, 0.5f*Gdx.graphics.getPpcX(), 0.5f*Gdx.graphics.getPpcY());
			if (i == 1)
				batch.draw(goodTexture[1], Gdx.graphics.getWidth()/1.11f, Gdx.graphics.getHeight()/1.1f, 0.5f*Gdx.graphics.getPpcX(), 0.5f*Gdx.graphics.getPpcY());

		}
		for (ParticleEffect eff : effects) {
			eff.draw(batch, delta/2);
		}
		if (flash) {
			
			  shape.begin(ShapeType.Filled);
			  shape.setColor(Color.WHITE);
			  shape.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			  shape.end();
			  flashDuration +=delta;
			  if (flashDuration > 0.15f)
			  flash = false;
		}
		batch.end();
		
		if (timeGood > .8f) {
			food.addFood(
					new Vector2(
							(int)(Math.random()*(Gdx.graphics.getWidth()-food.getBounds().width)),
							(int)(Math.random()*(Gdx.graphics.getHeight()-food.getBounds().height))), 
					FoodState.GOOD
					);
			timeGood = 0.0f;
		}
		
		if (timeBad > 2.0f) {
			food.addFood(
					new Vector2(
							(int)(Math.random()*(Gdx.graphics.getWidth()-food.getBounds().width)),
							(int)(Math.random()*(Gdx.graphics.getHeight()-food.getBounds().height))), 
					FoodState.BAD
					);
			timeBad = 0.0f;
		}
		
		if (timeCoveredAppearance > 3.5f) {
			food.addFood(
					new Vector2(
							(int)(Math.random()*(Gdx.graphics.getWidth()-food.getBounds().width)),
							(int)(Math.random()*(Gdx.graphics.getHeight()-food.getBounds().height))), 
					FoodState.COVERED
					);
			timeCoveredAppearance = 0.0f;
		}
		
		int k = 0;
		
		food.animate(delta*100);
		
		touchedArea.x = Gdx.input.getX();
		touchedArea.y = Gdx.graphics.getHeight()-Gdx.input.getY();
		for (Rectangle rect : food.getFood().keySet()) {
			if (touchedArea.x >= rect.x && touchedArea.x <= rect.x+rect.width &&
					touchedArea.y >= rect.y && touchedArea.y <= rect.y+rect.height &&
					food.getFood().get(rect).containsKey(FoodState.GOOD)) {
				
				System.out.println("works");
				effect.setPosition(rect.x, rect.y);
				effect.start();
				effects.add(effect);
				removableRect[k] = rect;
				k++;
				score++;
				eatSound.play();
			}
			
			if (touchedArea.x >= rect.x && touchedArea.x <= rect.x+rect.width &&
					touchedArea.y >= rect.y && touchedArea.y <= rect.y+rect.height &&
					food.getFood().get(rect).containsKey(FoodState.COVERED)) {
				if (food.getFood().get(rect).get(FoodState.COVERED) != -1) {
					removableRect[k] = rect;
					k++;
					score+=10;
				}
			}
			
			if (touchedArea.x >= rect.x && touchedArea.x <= rect.x+rect.width &&
					touchedArea.y >= rect.y && touchedArea.y <= rect.y+rect.height &&
					food.getFood().get(rect).containsKey(FoodState.BAD)) {
				removableRect[k] = rect;
				effect.setPosition(rect.x, rect.y);
				effect.start();
				effects.add(effect);
				k++;
				ending--;
				flash = true;
				ewSound.play(0.5f);
				Gdx.input.vibrate(300);
			
			}
		}
		
		
		for (int i=0;i<k;i++){
			HashMap<Rectangle, HashMap<FoodState, Integer>> tempMap = new HashMap<Rectangle, HashMap<FoodState, Integer>>();
			tempMap = food.getFood();
			tempMap.remove(removableRect[i]);
			food.setFood(tempMap);
		}
		k = 0;
		
		
		if (ending == 0) {
			game.setScreen(new EndScreen(score, game));
		}
		touchedArea = new Vector2(0, 0);
		
	}
	
	private void drawFood() {
		Rectangle[] removableRect = new Rectangle[100];
		int k = 0;
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
				if (map.get(FoodState.BAD) != -1)
					index = map.get(FoodState.BAD);
				else
				{
					index = (int)(Math.random()*5);
					map.remove(FoodState.BAD);
					map.put(FoodState.BAD, index);
				}
				batch.draw(badTexture[index],
						rect.x,
						rect.y,
						rect.width,
						rect.height
						);
			}
			
			else if (map.containsKey(FoodState.COVERED)) {
				if (rect.width < .8f * Gdx.graphics.getPpcX())
					batch.draw(coveredTexture,
							rect.x,
							rect.y,
							rect.width,
							rect.height
							);
				else {
					int rand = (int)(Math.random()*2);
					if (rand % 2 == 0){ 
						map.put(FoodState.BAD, (int)(Math.random()*5));
						HashMap<Rectangle, HashMap<FoodState, Integer>> temp = new HashMap<Rectangle, HashMap<FoodState, Integer>>();
						temp = food.getFood();
						temp.put(rect, map);
						food.setFood(temp);
						
						batch.draw(badTexture[map.get(FoodState.BAD)],
								rect.x,
								rect.y,
								rect.width,
								rect.height
								);
					}
					
					else {
						map.put(FoodState.GOOD, (int)(Math.random()*5));
						HashMap<Rectangle, HashMap<FoodState, Integer>> temp = new HashMap<Rectangle, HashMap<FoodState, Integer>>();
						temp = food.getFood();
						temp.put(rect, map);
						food.setFood(temp);
						
						batch.draw(goodTexture[map.get(FoodState.GOOD)],
								rect.x,
								rect.y,
								rect.width,
								rect.height
								);
					}
				}
			}
			HashMap<Rectangle, HashMap<FoodState, Integer>> tempMap = new HashMap<Rectangle, HashMap<FoodState, Integer>>();
			tempMap = food.getFood();
			tempMap.put(rect, map);
			food.setFood(tempMap);
		}
	}
	
	public void setTouchedArea(Vector2 area) {
		this.touchedArea = area;
	}
	
	public void dispose() {
		batch.dispose();
		eatSound.dispose();
		ewSound.dispose();
		effect.dispose();
		gameFont.dispose();
	}

}
