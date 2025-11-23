package com.catand.snake;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.ViewComponent;
import com.catand.snake.object.ContentType;
import com.catand.snake.object.Direction;
import javafx.scene.Node;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

public class SnakeRender {
	private static final double SNAKE_WIDTH = (Config.TILE_SIZE - 2 * Config.TILE_MARGIN) * 0.6;
	private static final double SNAKE_LENGTH = (double) Config.TILE_SIZE / 2;

	public static void snakeHead(Entity snake, Direction nextBody) {
		ViewComponent view = snake.getViewComponent();
		view.clearChildren();

		// 头部
		double radius = SNAKE_WIDTH / 2;
		Node circle = new Circle(radius, ContentType.SNAKE.getColor());
		circle.setTranslateX((double) Config.TILE_SIZE / 2);
		circle.setTranslateY((double) Config.TILE_SIZE / 2);
		view.addChild(circle);
		view.addChild(getHalfBodyRect(nextBody, view));
	}

	public static void snakeBody(Entity snake, Direction prevBody, Direction nextBody) {
		ViewComponent view = snake.getViewComponent();
		view.clearChildren();

		view.addChild(getHalfBodyRect(prevBody, view));
		Circle centerCircle = new Circle(SNAKE_WIDTH / 2, ContentType.SNAKE.getColor());
		centerCircle.setTranslateX((double) Config.TILE_SIZE / 2);
		centerCircle.setTranslateY((double) Config.TILE_SIZE / 2);
		view.addChild(centerCircle);
		view.addChild(getHalfBodyRect(nextBody, view));
	}

	public static void snakeTail(Entity snake, Direction prevBody) {
		ViewComponent view = snake.getViewComponent();
		view.clearChildren();

		Polygon triangle = new Polygon();
		triangle.setFill(ContentType.SNAKE.getColor());
		double tailLength = (Config.TILE_SIZE - Config.TILE_MARGIN) * 0.8;
		switch (prevBody) {
			case UP -> {
				triangle.getPoints().addAll(
						0.0, 0.0,
						SNAKE_WIDTH, 0.0,
						SNAKE_WIDTH / 2, tailLength
				);
				triangle.setTranslateX((Config.TILE_SIZE - SNAKE_WIDTH) / 2);
			}
			case RIGHT -> {
				triangle.getPoints().addAll(
						tailLength, 0.0,
						tailLength, SNAKE_WIDTH,
						0.0, SNAKE_WIDTH / 2
				);
				triangle.setTranslateX(Config.TILE_SIZE - tailLength);
				triangle.setTranslateY((Config.TILE_SIZE - SNAKE_WIDTH) / 2);
			}
			case DOWN -> {
				triangle.getPoints().addAll(
						0.0, tailLength,
						SNAKE_WIDTH, tailLength,
						SNAKE_WIDTH / 2, 0.0
				);
				triangle.setTranslateX((Config.TILE_SIZE - SNAKE_WIDTH) / 2);
				triangle.setTranslateY(Config.TILE_SIZE - tailLength);
			}
			case LEFT -> {
				triangle.getPoints().addAll(
						0.0, 0.0,
						0.0, SNAKE_WIDTH,
						tailLength, SNAKE_WIDTH / 2
				);
				triangle.setTranslateY((Config.TILE_SIZE - SNAKE_WIDTH) / 2);
			}
			default -> throw new IllegalArgumentException("Invalid direction: " + prevBody);
		}
		view.addChild(triangle);
	}

	private static Rectangle getHalfBodyRect(Direction prevBody, ViewComponent view) {
		Rectangle bodyRect;
		switch (prevBody) {
			case UP -> {
				bodyRect = new Rectangle(SNAKE_WIDTH, SNAKE_LENGTH, ContentType.SNAKE.getColor());
				bodyRect.setTranslateX((Config.TILE_SIZE - SNAKE_WIDTH) / 2);
			}
			case RIGHT -> {
				bodyRect = new Rectangle(SNAKE_LENGTH, SNAKE_WIDTH, ContentType.SNAKE.getColor());
				bodyRect.setTranslateX(SNAKE_LENGTH);
				bodyRect.setTranslateY((Config.TILE_SIZE - SNAKE_WIDTH) / 2);
			}
			case DOWN -> {
				bodyRect = new Rectangle(SNAKE_WIDTH, SNAKE_LENGTH, ContentType.SNAKE.getColor());
				bodyRect.setTranslateX((Config.TILE_SIZE - SNAKE_WIDTH) / 2);
				bodyRect.setTranslateY(SNAKE_LENGTH);
			}
			case LEFT -> {
				bodyRect = new Rectangle(SNAKE_LENGTH, SNAKE_WIDTH, ContentType.SNAKE.getColor());
				bodyRect.setTranslateY((Config.TILE_SIZE - SNAKE_WIDTH) / 2);
			}
			default -> throw new IllegalArgumentException("Invalid direction: " + prevBody);
		}
		return bodyRect;
	}
}
