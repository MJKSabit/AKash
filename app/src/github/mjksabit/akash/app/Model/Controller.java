package github.mjksabit.akash.app.Model;

import javafx.scene.Node;
import javafx.stage.Stage;

public abstract class Controller {
    private Node rootNode = null;

    public void setRootNode(Node rootNode) {
        this.rootNode = rootNode;
    }

    protected Stage getStage() {
        if(rootNode == null) return null;
        return (Stage) rootNode.getScene().getWindow();
    }
}
