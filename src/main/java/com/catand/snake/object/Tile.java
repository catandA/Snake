package com.catand.snake.object;

import javafx.scene.paint.Color;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Tile {
	private final TileType tileType;
	private final int x, y;
	@Setter
	private ContentType contentType;
	@Setter
	private int age;

	public Tile(int x, int y, TileType tileType) {
		this.x = x;
		this.y = y;
		this.tileType = tileType;
		this.contentType = ContentType.EMPTY;
	}

	public enum TileType {
		EMPTY, WALL;
		Color color;

		public Color getColor() {
			return switch (this) {
				case EMPTY -> Color.WHITE;
				case WALL -> Color.BROWN;
			};
		}
	}

	public enum ContentType {
		EMPTY, SNAKE, FOOD;
		Color color;

		public Color getColor() {
			return switch (this) {
				case EMPTY -> Color.TRANSPARENT;
				case SNAKE -> Color.GREEN;
				case FOOD -> Color.RED;
			};
		}
	}
}
