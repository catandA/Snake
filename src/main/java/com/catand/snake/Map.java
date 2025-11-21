package com.catand.snake;

import com.catand.snake.object.Tile;
import lombok.Getter;

import java.util.LinkedList;

@Getter
public class Map {
	private Tile[][] tiles;
	private LinkedList<Tile> snakeBody;
	private Tile food;
	private int mapWidth = 20;
	private int mapHeight = 20;

	public Map(int width, int height) {
		// 参数赋值
		if (width <= 5 || height <= 5) {
			throw new IllegalArgumentException("地图宽高必须大于5");
		}
		mapWidth = width;
		mapHeight = height;

		// 初始化地图
		tiles = new Tile[mapWidth][mapHeight];
		for (int i = 1; i < mapWidth - 1; i++) {
			for (int j = 1; j < mapHeight - 1; j++) {
				tiles[i][j] = new Tile(i, j, Tile.TileType.EMPTY);
			}
		}
		for (int i = 0; i < mapWidth; i++) {
			tiles[0][i] = new Tile(0, i, Tile.TileType.WALL);
			tiles[mapWidth - 1][i] = new Tile(mapWidth - 1, i, Tile.TileType.WALL);
		}
		for (int i = 1; i < mapWidth - 1; i++) {
			tiles[i][0] = new Tile(i, 0, Tile.TileType.WALL);
			tiles[i][mapHeight - 1] = new Tile(i, mapHeight - 1, Tile.TileType.WALL);
		}

		// 初始化蛇
		snakeBody = new LinkedList<>();
		int startX = Math.toIntExact(Math.round(Math.random() * (mapWidth - 5) + 2));
		int startY = Math.toIntExact(Math.round(Math.random() * (mapHeight - 5) + 2));
		Tile body1 = tiles[startX][startY];
		Tile body2 = tiles[startX + 1][startY];
		Tile body3 = tiles[startX + 1][startY + 1];
		body1.setContentType(Tile.ContentType.SNAKE);
		body2.setContentType(Tile.ContentType.SNAKE);
		body3.setContentType(Tile.ContentType.SNAKE);
		snakeBody.add(body1);
		snakeBody.add(body2);
		snakeBody.add(body3);

		updateFood();
	}

	public void updateFood() {
		while (food == null || food.getContentType() != Tile.ContentType.FOOD) {
			int x = Math.toIntExact(Math.round(Math.random() * (mapWidth - 1)));
			int y = Math.toIntExact(Math.round(Math.random() * (mapHeight - 1)));
			if (tiles[x][y].getTileType() != Tile.TileType.EMPTY || tiles[x][y].getContentType() != Tile.ContentType.EMPTY) {
				continue;
			}
			food = tiles[x][y];
			food.setContentType(Tile.ContentType.FOOD);
		}
	}
}
