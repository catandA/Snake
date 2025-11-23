package com.catand.snake.object;

import com.almasb.fxgl.entity.component.Component;
import lombok.Getter;
import lombok.Setter;

@Getter
public class TileComponent extends Component {
	private final TileType tileType;
	private final int x, y;
	@Setter
	private int age;

	public TileComponent(int x, int y, TileType tileType) {
		this.x = x;
		this.y = y;
		this.tileType = tileType;
	}

}