package github.mjksabit.akash.app.Model;

import github.mjksabit.akash.app.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

import java.io.IOException;

public class TransactionViewListCell extends ListCell<Transaction> {
    AnchorPane rootPane;

    @FXML
    private Label textIn;

    @FXML
    private Label textOtherUser;

    @FXML
    private Label textReference;

    @FXML
    private Label textAmount;

    @FXML
    private Label textType;

    @FXML
    private Label textId;

    @FXML
    private AnchorPane root;

    private FXMLLoader mLLoader;

    @Override
    protected void updateItem(Transaction transaction, boolean empty) {
        super.updateItem(transaction, empty);

        if (empty || transaction == null) {
            setText(null);
            setGraphic(null);
        } else {
            if (mLLoader == null) {
                mLLoader = Main.loadFXML("transactionListItem");
                mLLoader.setController(this);

                try {
                    mLLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (transaction.isCashOut()){
                textIn.setText("-");
                textIn.setTextFill(Color.RED);
            }
            else {
                textIn.setText("+");
                textIn.setTextFill(Color.GREEN);
            }

            textType.setText(transaction.getType());
            textAmount.setText(transaction.getAmount() + "");

            textOtherUser.setText(transaction.getOtherThanMe());
            textId.setText(transaction.getId());
            textReference.setText(transaction.getReference());

            setText(null);
            setGraphic(root);
        }

    }
}
