package com.nightingale.foodgame;

import com.badlogic.gdx.Game;
import com.nightingale.foodgame.screen.GameScreen;

public class FoodMain extends Game {

	@Override
	public void create() {
		GameScreen gameScreen = new GameScreen(this);
		setScreen(gameScreen);
	}

}
