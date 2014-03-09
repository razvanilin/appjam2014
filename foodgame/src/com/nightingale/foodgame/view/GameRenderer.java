package com.nightingale.foodgame.view;

import java.util.ArrayList;

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
import com.badlogic.gdx.math.Vector2;
import com.nightingale.foodgame.model.Food;
import com.nightingale.foodgame.model.Food.FoodState;
import com.nightingale.foodgame.model.FoodHandler;
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
	private ParticleEffect effect;
	private ArrayList<ParticleEffect> effects = new ArrayList<ParticleEffect>();
	ShapeRenderer shape;
	private FoodHandler foodHandler;
	
	
	public GameRenderer(Game game) {
		this.game = game;
		loadItems();
	}
	
	// Loading all the necessary items
	private void loadItems() {
		backgroundTexture = new TextureRegion(new Texture(
				Gdx.files.internal("background/background.png")),
				0,
				0, 
				Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());
		batch = new SpriteBatch();
		
		// Loading the atlas which contains the spritesheet
		TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("models/models.pack"));
		// getting the sprites with for good food
		for (int i = 0; i<5; i++) {
			goodTexture[i] = atlas.findRegion("food"+i);
		}
		// getting the sprites for bad food
		for (int i = 0; i < 5; i++) {
			badTexture[i] = atlas.findRegion("bfood"+i);
		}
		// getting the sprite for covered food
		coveredTexture = atlas.findRegion("cfood");
		
		foodHandler = FoodHandler.getInstance();
		
		gameFont = new BitmapFont(Gdx.files.internal("fonts/title.fnt"));
		
		eatSound = Gdx.audio.newSound(Gdx.files.internal("sounds/eat.wav"));
		ewSound = Gdx.audio.newSound(Gdx.files.internal("sounds/eww.wav"));
		
		shape = new ShapeRenderer();
		
		// Loading the particle effect used when snatching the food
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
	int score = 0;
	int ending = 3;
	boolean flash = false;
	float flashDuration = 0.0f;
	
	// Render the game
	public void render(float delta)
	{
		// Starting the timers used to place food on the screen at certain intervals of time
		timePassed += delta;
		timeGood += delta;
		timeBad+=delta;
		timeCoveredAppearance+=delta;
		
		// Placing the food on-screen
		if (timeGood > .8f) {
			foodHandler.addFood(
					new Vector2(
							(int)(Math.random()*Gdx.graphics.getWidth()),
							(int)(Math.random()*Gdx.graphics.getHeight())), 
					FoodState.GOOD
					);
			timeGood = 0.0f;
		}
		
		if (timeBad > 2.0f) {
			foodHandler.addFood(
					new Vector2(
							(int)(Math.random()*Gdx.graphics.getWidth()),
							(int)(Math.random()*Gdx.graphics.getHeight())), 
					FoodState.BAD
					);
			timeBad = 0.0f;
		}
		
		if (timeCoveredAppearance > 3.5f) {
			foodHandler.addFood(
					new Vector2(
							(int)(Math.random()*Gdx.graphics.getWidth()),
							(int)(Math.random()*Gdx.graphics.getHeight())), 
					FoodState.COVERED
					);
			timeCoveredAppearance = 0.0f;
		}

		// Animating the food
		foodHandler.animate(delta*100);

		// Checking if the covered food should show the contents
		for (Food food : foodHandler.getList()){
			if (food.getState() == FoodState.COVERED)
				if (food.getBounds().width > .8f * Gdx.graphics.getPpcX()){
					int rand = (int)(Math.random()*2);
					if (rand % 2 == 0)
						food.setState(FoodState.GOOD_UNCOVERED);
					else
						food.setState(FoodState.BAD);
				}
		}

		// Checking the collision between the touched area and the food
		ArrayList<Food> tempList = foodHandler.getList();
		for (int i = 0; i < tempList.size(); i++) {
			Food tempFood = tempList.get(i);
			if (touchedArea.x >= tempFood.getBounds().x && 
					touchedArea.x <= tempFood.getBounds().x+tempFood.getBounds().width &&
					touchedArea.y >= tempFood.getBounds().y && 
					touchedArea.y <= tempFood.getBounds().y+tempFood.getBounds().height) {
			
					switch (tempFood.getState()) {
					case GOOD:
						effect.setPosition(tempFood.getBounds().x, tempFood.getBounds().y);
						effect.start();
						effects.add(effect);
						// tagging the food to be removed
						score++;
						// playing the sound
						eatSound.play();
						// Remove the food from the list
						tempList.remove(i);
						i--;
						break;
						
					case BAD:
						effect.setPosition(tempFood.getBounds().x, tempFood.getBounds().y);
						effect.start();
						effects.add(effect);
						// tagging the food to be removed
						ending--;
						flash = true;
						ewSound.play(0.5f);
						// the device will vibrate for x milliseconds
						Gdx.input.vibrate(300);

						tempList.remove(i);
						i--;
						break;
						
					case COVERED:
						break;
						
					case GOOD_UNCOVERED:
						effect.setPosition(tempFood.getBounds().x, tempFood.getBounds().y);
						effect.start();
						effects.add(effect);
						// tagging the food to be removed
						score+=10;
						// playing the sound
						eatSound.play();
						// Remove the food from the list
						tempList.remove(i);
						i--;
						break;

					default:
						break;
					}
			}
		}
		foodHandler.setList(tempList);
		
		// cleaning the effects list
		for (int i=0; i< effects.size(); i++) {
			if (effects.get(i).isComplete()) {
				effects.remove(i);
				i--;
			}
		}
		
		// if ending reaches 0 the lives are gone and it's gameOver
		// the Ending Screen will be called
		if (ending == 0) {
			dispose();
			game.setScreen(new EndScreen(score, game));
		}
		
		// Drawing everything on the screen
		batch.begin();
		batch.draw(backgroundTexture, 0, 0);
		//batch.draw(coveredTexture, touchedArea.x, touchedArea.y, 100.0f, 100.0f);
		drawFood();
		gameFont.draw(batch, String.valueOf(score), 20.0f, Gdx.graphics.getHeight()-20.0f);
		// Drawing the lives on screen
		for (int i = ending;i>0;i--){
			if (i == 3)
				batch.draw(goodTexture[1], Gdx.graphics.getWidth()/1.3f, Gdx.graphics.getHeight()/1.1f, 0.5f*Gdx.graphics.getPpcX(), 0.5f*Gdx.graphics.getPpcY());
			if (i == 2)
				batch.draw(goodTexture[1], Gdx.graphics.getWidth()/1.2f, Gdx.graphics.getHeight()/1.1f, 0.5f*Gdx.graphics.getPpcX(), 0.5f*Gdx.graphics.getPpcY());
			if (i == 1)
				batch.draw(goodTexture[1], Gdx.graphics.getWidth()/1.11f, Gdx.graphics.getHeight()/1.1f, 0.5f*Gdx.graphics.getPpcX(), 0.5f*Gdx.graphics.getPpcY());

		}
		// Displaying the particle effects
		for (ParticleEffect eff : effects) {
			eff.draw(batch, delta/2);
		}
		// Displaying a flash when the user snatched bad food
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

	}

	// Draw the food
	private void drawFood() {
		for (Food f : foodHandler.getList()) {
			// If the index is -1 (texture not set), assign a number between 0 and 4
			if (f.getIndex() == -1) {
				int index = (int)(Math.random()*goodTexture.length);
				f.setIndex(index);
			}
			// draw the food according to its state
			switch (f.getState()) {
			case GOOD: case GOOD_UNCOVERED:
				batch.draw(goodTexture[f.getIndex()], 
						f.getBounds().x,
						f.getBounds().y,
						f.getBounds().width,
						f.getBounds().height
						);
				break;

			case BAD:
				batch.draw(badTexture[f.getIndex()], 
						f.getBounds().x,
						f.getBounds().y,
						f.getBounds().width,
						f.getBounds().height
						);
				break;

			case COVERED:
				batch.draw(coveredTexture, 
						f.getBounds().x,
						f.getBounds().y,
						f.getBounds().width,
						f.getBounds().height
						);
				break;

			default:
				break;
			}
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
		foodHandler.clearList();
	}

}
