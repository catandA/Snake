package com.catand.snake;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.catand.snake.object.Direction;
import com.catand.snake.object.Tile;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;
import javafx.util.Pair;

import java.util.LinkedList;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameTimer;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameWorld;

public class SnakeApp extends GameApplication {

	private Map map;
	private Entity[][] mapEntity;
	private LinkedList<Entity> snakeEntitys;
	private LinkedList<Tile> snakeBody;
	private Entity foodEntity;
	private Direction curDirection, nextDirection;

	@Override
	protected void initSettings(GameSettings settings) {
		Pair<Integer, Integer> mapPixel = Utils.getPixelCoordinateRD(Config.MAP_WIDTH, Config.MAP_HEIGHT);
		settings.setWidth(mapPixel.getKey());
		settings.setHeight(mapPixel.getValue());
		settings.setTitle(Config.GAME_TITLE);
		settings.setVersion("0.0.1");
	}

	@Override
	protected void initGame() {
		getGameWorld().addEntityFactory(new GameEntityFactory());
		initMap();
		initSnake();
		map.updateFood();
		Pair<Integer, Integer> foodPixel = Utils.getPixelCoordinateTL(map.getFood().getX(), map.getFood().getY());
		foodEntity = FXGL.spawn("Content", new SpawnData(foodPixel.getKey(), foodPixel.getValue()).put("contentType", Tile.ContentType.FOOD));
	}

	private void initMap() {
		map = new Map(Config.MAP_WIDTH, Config.MAP_HEIGHT);
		mapEntity = new Entity[Config.MAP_WIDTH][Config.MAP_HEIGHT];
		for (int i = 0; i < Config.MAP_WIDTH; i++) {
			for (int j = 0; j < Config.MAP_HEIGHT; j++) {
				Tile tile = map.getTiles()[i][j];
				Pair<Integer, Integer> pixel = Utils.getPixelCoordinateTL(i, j);
				mapEntity[i][j] = FXGL.spawn("Tile", new SpawnData(pixel.getKey(), pixel.getValue()).put("tileType", tile.getTileType()));
			}
		}
	}

	private void initSnake() {
		snakeBody = map.getSnakeBody();
		snakeEntitys = new LinkedList<>();
		snakeBody.forEach(tile -> {
			Pair<Integer, Integer> pixel = Utils.getPixelCoordinateTL(tile.getX(), tile.getY());
			snakeEntitys.add(FXGL.spawn("Content", new SpawnData(pixel.getKey(), pixel.getValue()).put("contentType", tile.getContentType())));
		});
	}

	private void turn(Direction to) {
		if (curDirection == null) {
			getGameTimer().runAtInterval(this::update, Duration.millis(200));
			curDirection = to;
			nextDirection = curDirection;
		}
		if (to != Utils.getOppositeDirection(curDirection)) {
			nextDirection = to;
		}
	}

	private void update() {
		curDirection = nextDirection;
		Pair<Integer, Integer> nextTileCoordinate = Utils.getNextTile(snakeBody.getFirst().getX(), snakeBody.getFirst().getY(), curDirection);
		Tile nextTile = map.getTiles()[nextTileCoordinate.getKey()][nextTileCoordinate.getValue()];
		Tile.ContentType nextContentType = nextTile.getContentType();
		if (nextTile.getTileType() == Tile.TileType.WALL || nextTile.getContentType() == Tile.ContentType.SNAKE) {
			gameOver();
			return;
		}
		nextTile.setContentType(Tile.ContentType.SNAKE);
		snakeBody.addFirst(nextTile);
		Pair<Integer, Integer> nextPixel = Utils.getPixelCoordinateTL(nextTileCoordinate.getKey(), nextTileCoordinate.getValue());
		snakeEntitys.addFirst(FXGL.spawn("Content", new SpawnData(nextPixel.getKey(), nextPixel.getValue()).put("contentType", Tile.ContentType.SNAKE)));
		if (nextContentType == Tile.ContentType.FOOD) {
			getGameWorld().removeEntity(foodEntity);
			map.updateFood();
			Pair<Integer, Integer> foodPixel = Utils.getPixelCoordinateTL(map.getFood().getX(), map.getFood().getY());
			foodEntity = FXGL.spawn("Content", new SpawnData(foodPixel.getKey(), foodPixel.getValue()).put("contentType", Tile.ContentType.FOOD));
		} else {
			Tile tail = snakeBody.removeLast();
			tail.setContentType(Tile.ContentType.EMPTY);
			getGameWorld().removeEntity(snakeEntitys.removeLast());
		}
		System.out.println("Moving to: " + nextTileCoordinate + ", Pixel: " + nextPixel);
		System.out.println("Snake body size: " + snakeBody.size() + ", Entities size: " + snakeEntitys.size());
	}

	private void gameOver() {
		System.out.println("U DIED");
	}

	@Override
	protected void initInput() {
		FXGL.onKey(KeyCode.W, () -> turn(Direction.UP));
		FXGL.onKey(KeyCode.D, () -> turn(Direction.RIGHT));
		FXGL.onKey(KeyCode.S, () -> turn(Direction.DOWN));
		FXGL.onKey(KeyCode.A, () -> turn(Direction.LEFT));
	}
}