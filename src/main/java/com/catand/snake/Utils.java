package com.catand.snake;

import com.catand.snake.object.Direction;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.Map;

public class Utils {
	// 对应方向的下一个坐标
	public static Pair<Integer, Integer> getNextTile(int x, int y, Direction direction) {
		return switch (direction) {
			case UP -> new Pair<>(x, y - 1);
			case RIGHT -> new Pair<>(x + 1, y);
			case DOWN -> new Pair<>(x, y + 1);
			case LEFT -> new Pair<>(x - 1, y);
		};
	}

	public static Direction getRelativeDirection(int x1, int y1, int x2, int y2) {
		int dx = x2 - x1;
		int dy = y2 - y1;

		if (dx == 0 && dy == 0) {
			return null;
		}

		if (Math.abs(dx) > Math.abs(dy)) {
			return dx > 0 ? Direction.RIGHT : Direction.LEFT;
		}
		else {
			return dy > 0 ? Direction.DOWN : Direction.UP;
		}
	}

	private static final Map<Direction, Direction> OPPOSITE = new HashMap<Direction, Direction>() {{
		put(Direction.UP, Direction.DOWN);
		put(Direction.RIGHT, Direction.LEFT);
		put(Direction.DOWN, Direction.UP);
		put(Direction.LEFT, Direction.RIGHT);
	}};

	public static Direction getOppositeDirection(Direction direction) {
		return OPPOSITE.get(direction);
	}

	// 获取此格左上角的像素坐标
	public static Pair<Integer, Integer> getPixelCoordinateTL(int x, int y) {
		return new Pair<>(Config.PADDING + (x * (Config.TILE_SIZE)),
				Config.PADDING + (y * (Config.TILE_SIZE)));
	}

	// 获取此格中心像素坐标
	public static Pair<Integer, Integer> getPixelCoordinateCenter(int x, int y) {
		return new Pair<>(Config.PADDING + (x * (Config.TILE_SIZE)) + (Config.TILE_SIZE / 2),
				Config.PADDING + (y * (Config.TILE_SIZE)) + (Config.TILE_SIZE / 2));
	}

	// 获取此格右下角的像素坐标
	public static Pair<Integer, Integer> getPixelCoordinateRD(int x, int y) {
		return new Pair<>(Config.PADDING + (x * (Config.TILE_SIZE + 1)),
				Config.PADDING + (y * (Config.TILE_SIZE + 1)));
	}
}
