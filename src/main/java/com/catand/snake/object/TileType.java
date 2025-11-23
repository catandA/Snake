package com.catand.snake.object;

import javafx.scene.paint.Color;

public enum TileType {
	EMPTY, WALL;

	public Color getColor() {
		return switch (this) {
			case EMPTY -> Color.WHITE;
			case WALL -> Color.BROWN;
		};
	}
}
