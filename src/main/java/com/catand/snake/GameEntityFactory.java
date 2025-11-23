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
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import static com.almasb.fxgl.dsl.FXGLForKtKt.entityBuilder;

public class GameEntityFactory implements EntityFactory {
	@Spawns("Tile")
	public Entity newTile(SpawnData data) {
		TileType tileType = data.get("type");
		int x = data.get("x");
		int y = data.get("y");
		return entityBuilder(data)
				.at(data.getX() + Config.TILE_MARGIN, data.getY() + Config.TILE_MARGIN)
				.type(tileType)
				.view(new Rectangle(Config.TILE_SIZE - 2 * Config.TILE_MARGIN,
						Config.TILE_SIZE - 2 * Config.TILE_MARGIN, tileType.getColor()))
				.with(new TileComponent(x, y, tileType))
				.build();
	}

	@Spawns("Content")
	public Entity newContent(SpawnData data) {
		ContentType contentType = data.get("type");
		int x = data.get("x");
		int y = data.get("y");
		EntityBuilder eb = entityBuilder(data)
				.at(data.getX() + (double) Config.TILE_SIZE / 2, data.getY() + (double) Config.TILE_SIZE / 2)
				.type(contentType)
				.with(new ContentComponent(x, y, contentType));
		if (contentType == ContentType.FOOD || contentType == ContentType.SNAKE) {
			eb.view(new Circle((double) Config.TILE_SIZE / 2 - Config.TILE_MARGIN, contentType.getColor()));
		}
		return eb.build();
	}
}
