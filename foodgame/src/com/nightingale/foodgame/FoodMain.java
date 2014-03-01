package com.nightingale.foodgame;

import com.badlogic.gdx.Game;
import com.nightingale.foodgame.screen.MenuScreen;

public class FoodMain extends Game {

	@Override
	public void create() {
		MenuScreen menuScreen = new MenuScreen(this);
		setScreen(menuScreen);
	}

}
