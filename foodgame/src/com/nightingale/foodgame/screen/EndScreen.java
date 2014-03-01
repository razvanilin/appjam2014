package com.nightingale.foodgame.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class EndScreen implements Screen {

	private TextureRegion backgroundTexture;
	private BitmapFont white, black;
	private Skin skin;
	private SpriteBatch spriteBatch;
	private int score;

	private TextButton buttonBack;
	private Label heading;
	private Label scoreLabel;
	private Game game;
	private TextureAtlas atlas;
	private Stage stage;
	private Table table;

	public EndScreen(int score, Game game) {
		this.score = score;
		this.game = game;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		spriteBatch.begin();
		spriteBatch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		spriteBatch.end();

		stage.act(delta);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void show() {
		backgroundTexture = new TextureRegion(new Texture(Gdx.files.internal("background/background.png")));
		spriteBatch = new SpriteBatch();

		stage = new Stage();

		Gdx.input.setInputProcessor(stage);

		atlas = new TextureAtlas("ui/blueButtons.pack");
		skin = new Skin(Gdx.files.internal("ui/menuSkin.json"), atlas);

		table = new Table(skin);
		table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		heading = new Label("You ate too much bad food!", skin);
		scoreLabel = new Label("Your score was: "+score, skin);

		buttonBack = new TextButton("Try Again", skin);
		buttonBack.pad(20);
		buttonBack.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new GameScreen(game));
			}
		});

		table.add(heading);
		table.getCell(heading).spaceBottom(250);
		table.row();
		table.add(scoreLabel);
		table.getCell(scoreLabel).spaceBottom(100);
		table.row();
		table.add(buttonBack);
		stage.addActor(table);
	}

	@Override
	public void hide() {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {
		spriteBatch.dispose();
	}

}
