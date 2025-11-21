module com.catand.snake {
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.web;

	requires org.controlsfx.controls;
	requires net.synedra.validatorfx;
	requires org.kordamp.ikonli.javafx;
	requires eu.hansolo.tilesfx;
	requires com.almasb.fxgl.all;

	opens com.catand.snake to javafx.fxml;
	exports com.catand.snake;
}