package com.catand.snake;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.catand.snake.object.Tile;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import org.jetbrains.annotations.NotNull;

import static com.almasb.fxgl.dsl.FXGLForKtKt.entityBuilder;

public class GameEntityFactory implements EntityFactory {
	@Spawns("Tile")
	public Entity newTile(SpawnData data) {
		Tile.TileType tileType = data.get("tileType");
		return getTileEntity(data, tileType);
	}

	@Spawns("Content")
	public Entity newContent(SpawnData data) {
		Tile.ContentType contentType = data.get("contentType");
		return getContentEntity(data, contentType);
	}

	@NotNull
	private Entity getTileEntity(SpawnData data, Tile.TileType tileType) {
		return entityBuilder(data)
				.at(data.getX() + Config.TILE_MARGIN, data.getY() + Config.TILE_MARGIN)
				.type(tileType)
				.view(new Rectangle(Config.TILE_SIZE - 2 * Config.TILE_MARGIN,
						Config.TILE_SIZE - 2 * Config.TILE_MARGIN, tileType.getColor()))
				.build();
	}

	@NotNull
	private Entity getContentEntity(SpawnData data, Tile.ContentType contentType) {
		return entityBuilder(data)
				.at(data.getX() + (double) Config.TILE_SIZE /2, data.getY() + (double) Config.TILE_SIZE /2)
				.type(contentType)
				.view(new Circle((double) Config.TILE_SIZE / 2 - Config.TILE_MARGIN, contentType.getColor()))
				.build();
	}

}
