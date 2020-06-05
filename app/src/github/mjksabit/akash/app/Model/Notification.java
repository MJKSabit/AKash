package github.mjksabit.akash.app.Model;

import javafx.scene.control.Label;

public class Notification extends Label {
    public Notification(String text) {
        super(text);
        setWidth(300);
        setHeight(50);
        setWrapText(true);
    }
}
