package com.catand.snake.object;

import javafx.scene.paint.Color;

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
