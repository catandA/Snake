package com.catand.snake;

import com.almasb.fxgl.dsl.EntityBuilder;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.catand.snake.object.ContentComponent;
import com.catand.snake.object.ContentType;
import com.catand.snake.object.TileComponent;
import com.catand.snake.object.TileType;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import static com.almasb.fxgl.dsl.FXGLForKtKt.entityBuilder;

public class GameEntityFactory implements EntityFactory {
	@Spawns("Tile")
	public Entity newTile(SpawnData data) {
		TileType tileType = data.get("type");
		int x = data.get("x");
		int y = data.get("y");
		Rectangle hollowRect = new Rectangle(Config.TILE_SIZE, Config.TILE_SIZE);
		if (tileType == TileType.EMPTY) {
			hollowRect.setStroke(Color.LIGHTGREEN);
		}
		if (tileType == TileType.WALL) {
			hollowRect.setStroke(Color.BURLYWOOD);
		}
		hollowRect.setFill(Color.TRANSPARENT);
		hollowRect.setStrokeWidth(2 * Config.TILE_MARGIN);
		Rectangle rect = new Rectangle(Config.TILE_SIZE - 2 * Config.TILE_MARGIN,
				Config.TILE_SIZE - 2 * Config.TILE_MARGIN, tileType.getColor());
		rect.setTranslateX(Config.TILE_MARGIN);
		rect.setTranslateY(Config.TILE_MARGIN);
		return entityBuilder(data)
				.at(data.getX(), data.getY())
				.type(tileType)
				.view(hollowRect)
				.view(rect)
				.with(new TileComponent(x, y, tileType))
				.build();
	}

	@Spawns("Content")
	public Entity newContent(SpawnData data) {
		ContentType contentType = data.get("type");
		int x = data.get("x");
		int y = data.get("y");
		EntityBuilder eb = entityBuilder(data)
				.at(data.getX(), data.getY())
				.type(contentType)
				.with(new ContentComponent(x, y, contentType));
		if (contentType == ContentType.FOOD) {
			Circle circle = new Circle((double) Config.TILE_SIZE / 2 - Config.TILE_MARGIN, contentType.getColor());
			circle.setTranslateX((double) Config.TILE_SIZE / 2);
			circle.setTranslateY((double) Config.TILE_SIZE / 2);
			eb.view(circle);
		}
		return eb.build();
	}
}
