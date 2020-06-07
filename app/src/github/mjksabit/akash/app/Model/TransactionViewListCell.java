package github.mjksabit.akash.app.Model;

import github.mjksabit.akash.app.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;

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
                textIn.setStyle("-fx-background-color: #ff4c6b; -fx-background-radius: 5px");
            }
            else {
                textIn.setText("+");
                textIn.setStyle("-fx-background-color: #2cff2c; -fx-background-radius: 5px");
            }

            textType.setText(transaction.getType());
            textAmount.setText(transaction.getAmount() + "");

            textId.setText(transaction.getId());

            if(transaction.getOtherThanMe().isEmpty()) {
                String[] args = transaction.getReference().split(":");

                textOtherUser.setText(args[0]);
                textReference.setText(args[1]);
            }
            else {
                textOtherUser.setText(transaction.getOtherThanMe());
                textReference.setText(transaction.getReference());
            }

            setText(null);
            setGraphic(root);
        }

    }
}
