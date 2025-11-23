package com.catand.snake.object;

import com.almasb.fxgl.entity.component.Component;
import lombok.Getter;
import lombok.Setter;

@Getter
public class ContentComponent extends Component {
	private final int x, y;
	@Setter
	private ContentType contentType;
	@Setter
	private int age;

	public ContentComponent(int x, int y, ContentType contentType) {
		this.x = x;
		this.y = y;
		this.contentType = contentType;
	}

}
