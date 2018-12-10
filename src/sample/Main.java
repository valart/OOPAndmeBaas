package sample;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class Main extends Application {

    private ObservableList<DataItem> data;
    private ObservableList<DataItem> dataOld;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(final Stage peaLava) {
        GridPane root = new GridPane();
        root.setPadding(new Insets(20, 20, 20, 20));
        root.setMinSize(500, 500);
        root.setVgap(20);
        root.setHgap(20);

        Andmebaas baas = new Andmebaas();
        data = baas.tagastaAndmebaas();
        dataOld = baas.tagastaAndmebaas();

        final TableView<DataItem> tableView = new TableView();
        tableView.setMaxHeight(337);
        tableView.setEditable(true);

        tableView.setMinWidth(415);
        tableView.setMaxWidth(415);



        TableColumn firma = new TableColumn("Firma");
        firma.setMinWidth(100);
        firma.setCellValueFactory(new PropertyValueFactory<DataItem,String>("firma"));
        firma.setCellFactory(TextFieldTableCell.forTableColumn());
        firma.setOnEditCommit(
                (EventHandler<TableColumn.CellEditEvent<DataItem, String>>) t -> t.getTableView().getItems().get(
                        t.getTablePosition().getRow()).setFirma(t.getNewValue())
        );


        TableColumn model = new TableColumn("Model");
        model.setMinWidth(100);
        model.setCellValueFactory(new PropertyValueFactory<DataItem,String>("model"));
        model.setCellFactory(TextFieldTableCell.forTableColumn());
        model.setOnEditCommit(
                (EventHandler<TableColumn.CellEditEvent<DataItem, String>>) t -> t.getTableView().getItems().get(
                        t.getTablePosition().getRow()).setModel(t.getNewValue())
        );


        TableColumn suurus = new TableColumn("Suurus");
        suurus.setMinWidth(100);
        suurus.setCellValueFactory(new PropertyValueFactory<DataItem,String>("suurus"));
        suurus.setCellFactory(TextFieldTableCell.forTableColumn());
        suurus.setOnEditCommit(
                (EventHandler<TableColumn.CellEditEvent<DataItem, String>>) t -> t.getTableView().getItems().get(
                        t.getTablePosition().getRow()).setSuurus(t.getNewValue())
        );


        TableColumn hind = new TableColumn("Hind");
        hind.setMinWidth(100);
        hind.setCellValueFactory(new PropertyValueFactory<DataItem,String>("hind"));
        hind.setCellFactory(TextFieldTableCell.forTableColumn());
        hind.setOnEditCommit(
                (EventHandler<TableColumn.CellEditEvent<DataItem, String>>) t -> t.getTableView().getItems().get(
                        t.getTablePosition().getRow()).setHind(t.getNewValue())
        );

        tableView.setItems(data);

        tableView.getColumns().addAll(firma,model,suurus,hind);



        //Sisestused
        final TextField addFirma = new TextField();
        addFirma.setPromptText("Firma");
        addFirma.setMaxWidth(90);

        final TextField addModel = new TextField();
        addModel.setMaxWidth(90);
        addModel.setPromptText("Model");

        final TextField addSuurus = new TextField();
        addSuurus.setMaxWidth(90);
        addSuurus.setPromptText("Suurus");

        final TextField addHind = new TextField();
        addHind.setMaxWidth(90);
        addHind.setPromptText("Hind");

        Button lisaNupp = new Button("Lisa");
        lisaNupp.setMinSize(90,10);

        Button saveNupp = new Button("Salvesta");
        saveNupp.setMinSize(90,10);

        Button kustutaNupp = new Button("Kustuta");
        kustutaNupp.setMinSize(90,10);

        Button kasutamisJuhend = new Button("Abi");
        kasutamisJuhend.setMinSize(90,10);

        saveNupp.setOnAction(event -> {
            for(int i = 0; i<data.size();i++){
                if(!data.get(i).getFirma().equals(dataOld.get(i).getFirma()) //Kui märkan muudatusi reas
                        || !data.get(i).getModel().equals(dataOld.get(i).getModel())
                        || !data.get(i).getSuurus().equals(dataOld.get(i).getSuurus())
                        || !data.get(i).getHind().equals(dataOld.get(i).getHind())) {
                    
                    dataOld = baas.tagastaAndmebaas();
                    String[] vanadAndmed = new String[4];
                    vanadAndmed[0] = dataOld.get(i).getFirma();
                    vanadAndmed[1] = dataOld.get(i).getModel();
                    vanadAndmed[2] = dataOld.get(i).getSuurus();
                    vanadAndmed[3] = dataOld.get(i).getHind();

                    String[] uuedAndmed = new String[4];
                    uuedAndmed[0] = data.get(i).getFirma();
                    uuedAndmed[1] = data.get(i).getModel();
                    uuedAndmed[2] = data.get(i).getSuurus();
                    uuedAndmed[3] = data.get(i).getHind();

                    baas.muudaAndmebaas(baas.leiaIndeks(vanadAndmed),uuedAndmed);
                }
            }
        });


        lisaNupp.setOnAction(e -> {
            String[] lisaAndmebaasi = new String[4];
            if (addFirma.getText().length() > 0 && addModel.getText().length() > 0 && addSuurus.getText().length() > 0 && addHind.getText().length() > 0) {
                data.add(new DataItem(
                        addFirma.getText(),
                        addModel.getText(),
                        addSuurus.getText(),
                        addHind.getText()));

                lisaAndmebaasi[0] = addFirma.getText();
                lisaAndmebaasi[1] = addModel.getText();
                lisaAndmebaasi[2] = addSuurus.getText();
                lisaAndmebaasi[3] = addHind.getText();
                baas.lisaAndmebaasi(lisaAndmebaasi);

                addFirma.clear();
                addModel.clear();
                addSuurus.clear();
                addHind.clear();
                dataOld.add(new DataItem("","","",""));
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Viga");
                alert.setHeaderText(null);
                alert.setContentText("Mingi lahter jäi tühjaks! Proovi uuesti");
                alert.showAndWait();
            }
        });

        kustutaNupp.setOnAction(e -> {
            String[] indeksiLedimiseks = new String[4];
            indeksiLedimiseks[0] = tableView.getSelectionModel().getSelectedItem().getFirma();
            indeksiLedimiseks[1] = tableView.getSelectionModel().getSelectedItem().getModel();
            indeksiLedimiseks[2] = tableView.getSelectionModel().getSelectedItem().getSuurus();
            indeksiLedimiseks[3] = tableView.getSelectionModel().getSelectedItem().getHind();

            baas.kustutaAndmebaasisit(baas.leiaIndeks(indeksiLedimiseks));
            data = baas.tagastaAndmebaas();
            dataOld = baas.tagastaAndmebaas();
            tableView.setItems(data);
        });

        kasutamisJuhend.setOnAction(event -> {
            Label tühik1 = new Label("");
            Label rida1 = new Label(" - Andmete lisamiseks on olemas akna all olevad sisestused (firma,model,");
            Label rida2 = new Label("   suurus,hind). Pärast sisestamist vajuta nuppu \"Lisa\"");

            Label tühik2 = new Label("");
            Label rida3 = new Label(" - Andmete muutmiseks klõpsa kahekordselt sõnale, mida tahad muuta.");
            Label rida4 = new Label("   Pärast andmete sisestamist vajuta Enter ning vajuta nupule \"Salvesta\"");

            Label rida5 = new Label(" - Read kustutamiseks vali sobiv rida ning vajuta selle peale.");
            Label rida6 = new Label("   Pärast vajuta nupule \"Kustuta\"");

            VBox secondRoot = new VBox();
            secondRoot.getChildren().addAll(rida1, rida2, tühik1, rida3, rida4, tühik2, rida5, rida6);

            Scene secondScene = new Scene(secondRoot, 450, 170);

            // New window (Stage)
            Stage newWindow = new Stage();
            newWindow.setTitle("Kasutamisjuhend");
            newWindow.setScene(secondScene);

            newWindow.initModality(Modality.WINDOW_MODAL);
            newWindow.initOwner(peaLava);
            newWindow.setX(peaLava.getX() + 60);
            newWindow.setY(peaLava.getY() + 120);

            newWindow.show();
        });


        root.add(tableView, 0, 0);

        GridPane root1 = new GridPane();
        root1.setPadding(new Insets(20, 20, 20, 20));

        root1.add(addFirma, 0, 0);
        root1.add(addModel, 1, 0);
        root1.add(addSuurus, 2, 0);
        root1.add(addHind, 3, 0);

        root.add(root1, 0, 1);

        root.add(lisaNupp, 1, 1);

        GridPane root2 = new GridPane();
        root2.setPadding(new Insets(20, 0, 0, 0));
        //root2.setVgap(5);
        //root2.setHgap(5);

        root2.add(saveNupp, 0, 1);
        root2.add(kustutaNupp, 0, 2);
        root2.add(kasutamisJuhend, 0, 4);

        root.add(root2, 1, 0);



        //root.getChildren().add(tableView);
        //root.getChildren().addAll(addFirma,addModel,addSuurus,addHind,lisaNupp,saveNupp,kustutaNupp,kasutamisJuhend);

        StackPane root3 = new StackPane();

        root.setMaxWidth(Double.MAX_VALUE);
        root.setAlignment(Pos.CENTER);

        root3.setMaxWidth(Double.MAX_VALUE);
        root3.setAlignment(Pos.CENTER);

        GridPane.setHalignment(root, HPos.CENTER);
        GridPane.setHalignment(root3, HPos.CENTER);

        root.prefWidthProperty().bind(root3.widthProperty());
        root.prefHeightProperty().bind(root3.heightProperty());

        root3.getChildren().add(root);

        Scene scene = new Scene(root3,650,470);
        peaLava.setMinHeight(500);
        peaLava.setMinWidth(650);
        peaLava.setScene(scene);
        peaLava.setTitle("Anmdebaas");
        peaLava.show();
    }
}
