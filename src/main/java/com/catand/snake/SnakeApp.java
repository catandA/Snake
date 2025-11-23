package com.catand.snake;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.catand.snake.object.*;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;
import javafx.util.Pair;

import java.util.LinkedList;

import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameTimer;
import static com.almasb.fxgl.dsl.FXGLForKtKt.getGameWorld;

public class SnakeApp extends GameApplication {

	private Entity[][] mapTiles;
	private Entity[][] mapContent;
	private LinkedList<Entity> snakeBody;
	private Entity food;
	private Direction curDirection, nextDirection;
	int width;
	int height;

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
		updateFood();
	}

	private void initMap() {
		// 参数赋值
		if (Config.MAP_WIDTH < 5 || Config.MAP_HEIGHT < 5) {
			throw new IllegalArgumentException("地图宽高至少为5");
		}
		width = Config.MAP_WIDTH;
		height = Config.MAP_HEIGHT;

		mapTiles = new Entity[width][height];
		// 初始化地图
		for (int i = 1; i < width - 1; i++) {
			for (int j = 1; j < height - 1; j++) {
				mapTiles[i][j] = spawn(i, i, EntityType.TILE, TileType.EMPTY);
			}
		}
		for (int i = 0; i < width; i++) {
			mapTiles[0][i] = spawn(0, i, EntityType.TILE, TileType.WALL);
			mapTiles[width - 1][i] = spawn(width - 1, i, EntityType.TILE, TileType.WALL);
		}
		for (int i = 1; i < width - 1; i++) {
			mapTiles[i][0] = spawn(i, 0, EntityType.TILE, TileType.WALL);
			mapTiles[i][height - 1] = spawn(i, height - 1, EntityType.TILE, TileType.WALL);
		}
	}

	private void initSnake() {
		snakeBody = new LinkedList<>();
		mapContent = new Entity[width][height];
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				mapContent[x][y] = spawn(x, y, EntityType.CONTENT, ContentType.EMPTY);
			}
		}
		int startX = Math.toIntExact(Math.round(Math.random() * (Config.MAP_WIDTH - 5) + 2));
		int startY = Math.toIntExact(Math.round(Math.random() * (Config.MAP_HEIGHT - 5) + 2));
		snakeBody.add(spawn(startX, startY, EntityType.CONTENT, ContentType.SNAKE));
		mapContent[startX][startY] = snakeBody.getLast();
		snakeBody.add(spawn(startX + 1, startY, EntityType.CONTENT, ContentType.SNAKE));
		mapContent[startX + 1][startY] = snakeBody.getLast();
		snakeBody.add(spawn(startX + 1, startY + 1, EntityType.CONTENT, ContentType.SNAKE));
		mapContent[startX + 1][startY + 1] = snakeBody.getLast();
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
		ContentComponent snakeHead = snakeBody.getFirst().getComponent(ContentComponent.class);

		// 获取下一步格子
		Pair<Integer, Integer> nextTileCoordinate = Utils.getNextTile(snakeHead.getX(), snakeHead.getY(), curDirection);
		int nextX = nextTileCoordinate.getKey();
		int nextY = nextTileCoordinate.getValue();
		Entity nextContent = mapContent[nextX][nextY];
		// 此处额外保留类型方便下一步判断
		TileType tileType = mapTiles[nextX][nextY].getComponent(TileComponent.class).getTileType();
		ContentType nextContentType = nextContent.getComponent(ContentComponent.class).getContentType();

		// 判断是否撞墙
		if (tileType == TileType.WALL || nextContentType == ContentType.SNAKE) {
			gameOver();
			return;
		}

		// 头部移动到下一格
		Entity newSnakeHead = spawn(nextX, nextY, EntityType.CONTENT, ContentType.SNAKE);
		snakeBody.addFirst(newSnakeHead);
		getGameWorld().removeEntity(nextContent);
		mapContent[nextX][nextY] = newSnakeHead;

		// 检查吃到食物
		if (nextContentType == ContentType.FOOD) {
			updateFood();
		} else {
			Entity tail = snakeBody.removeLast();
			mapContent[tail.getComponent(ContentComponent.class).getX()][tail.getComponent(ContentComponent.class).getY()] = spawn(tail.getComponent(ContentComponent.class).getX(), tail.getComponent(ContentComponent.class).getY(), EntityType.CONTENT, ContentType.EMPTY);
			getGameWorld().removeEntity(tail);
		}
	}

	public void updateFood() {
		if (food != null) {
			getGameWorld().removeEntity(food);
			food = null;
		}
		while (food == null) {
			int x = Math.toIntExact(Math.round(Math.random() * (width - 1)));
			int y = Math.toIntExact(Math.round(Math.random() * (height - 1)));
			TileComponent tileComponent = mapTiles[x][y].getComponent(TileComponent.class);
			if (tileComponent.getTileType() != TileType.EMPTY || mapContent[x][y].getComponent(ContentComponent.class).getContentType() != ContentType.EMPTY) {
				continue;
			}
			food = spawn(x, y, EntityType.CONTENT, ContentType.FOOD);
			mapContent[x][y] = food;
		}
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

	private Entity spawn(int x, int y, EntityType entityType, Enum<?> type) {
		Pair<Integer, Integer> pixel = Utils.getPixelCoordinateTL(x, y);
		return FXGL.spawn(entityType.getName(), new SpawnData(pixel.getKey(), pixel.getValue()).put("type", type).put("x", x).put("y", y));
	}

	public enum EntityType {
		TILE, CONTENT;

		public String getName() {
			return switch (this) {
				case TILE -> "Tile";
				case CONTENT -> "Content";
			};
		}
	}
}