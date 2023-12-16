package com.tyb6.java2lab4;

import graphics.Drawable;
import graphics.Geogroup;
import graphics.JSONParser;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Controller {
    @FXML
    public Label label_open;
    @FXML
    public Button delete_btn;
    @FXML
    private Canvas main_canvas;
    @FXML
    private ListView<String> shapes_list;
    @FXML
    private Button open_btn;
    private File input_file;
    private Drawable shapes;

    @FXML
    public void open() throws IOException {
        FileChooser file_chooser = new FileChooser();
        file_chooser.setTitle("Choose JSON/BIN file!");
        file_chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.JSON", "*.bin"));
        input_file = file_chooser.showOpenDialog(open_btn.getScene().getWindow());
        if (input_file != null) {
            String file_name = input_file.getName();
            String file_extension = file_name.substring(file_name.lastIndexOf(".") + 1);
            if (file_extension.equals("JSON"))
                shapes = JSONParser.deserializeShapes(new FileInputStream(input_file));
            else
                shapes = Drawable.readBinary(new FileInputStream(input_file));
        }
        draw();
    }

    @FXML
    public void draw() {
        ArrayList<String> shapes_string = new ArrayList<>();
        shapes_list.getItems().clear();
        for (Drawable s : ((Geogroup) shapes).getList())
            shapes_string.add(s.toString());
        shapes_list.getItems().addAll(shapes_string);
        GraphicsContext gc;
        gc = main_canvas.getGraphicsContext2D();
        gc.setStroke(Color.WHEAT);
        gc.setLineWidth(4);
        gc.clearRect(0, 0, main_canvas.getWidth(), main_canvas.getHeight());
        shapes.draw(gc);
    }

    @FXML
    public void delete() throws IOException {
        for (Integer i : shapes_list.getSelectionModel().getSelectedIndices())
            ((Geogroup) shapes).remove(i);
        draw();
    }
}
