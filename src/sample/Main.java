package sample;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.concurrent.atomic.AtomicReference;


public class Main extends Application {

    private ObservableList<DataItem> data;
    private ObservableList<DataItem> dataOld;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage peaLava) {
        Pane root = new Pane();

        Andmebaas baas = new Andmebaas();
        data = baas.tagastaAndmebaas();
        dataOld = baas.tagastaAndmebaas();

        TableView<DataItem> tableView = new TableView();
        tableView.setMaxHeight(337);
        tableView.setEditable(true);

        tableView.setMinWidth(415);
        tableView.setMaxWidth(415);

        TableColumn firma = new TableColumn("Firma");
        firma.setMinWidth(100);
        firma.setCellValueFactory(new PropertyValueFactory<DataItem,String>("firma"));
        firma.setCellFactory(TextFieldTableCell.forTableColumn());
        firma.setOnEditCommit(
                (EventHandler<CellEditEvent<DataItem, String>>) t -> t.getTableView().getItems().get(
                        t.getTablePosition().getRow()).setFirma(t.getNewValue())
        );

        TableColumn model = new TableColumn("Model");
        model.setMinWidth(100);
        model.setCellValueFactory(new PropertyValueFactory<DataItem,String>("model"));
        model.setCellFactory(TextFieldTableCell.forTableColumn());
        model.setOnEditCommit(
                (EventHandler<CellEditEvent<DataItem, String>>) t -> t.getTableView().getItems().get(
                        t.getTablePosition().getRow()).setModel(t.getNewValue())
        );

        TableColumn suurus = new TableColumn("Suurus");
        suurus.setMinWidth(100);
        suurus.setCellValueFactory(new PropertyValueFactory<DataItem,String>("suurus"));
        suurus.setCellFactory(TextFieldTableCell.forTableColumn());
        suurus.setOnEditCommit(
                (EventHandler<CellEditEvent<DataItem, String>>) t -> t.getTableView().getItems().get(
                        t.getTablePosition().getRow()).setSuurus(t.getNewValue())
        );

        TableColumn hind = new TableColumn("Hind");
        hind.setMinWidth(100);
        hind.setCellValueFactory(new PropertyValueFactory<DataItem,String>("hind"));
        hind.setCellFactory(TextFieldTableCell.forTableColumn());
        hind.setOnEditCommit(
                (EventHandler<CellEditEvent<DataItem, String>>) t -> t.getTableView().getItems().get(
                        t.getTablePosition().getRow()).setHind(t.getNewValue())
        );

        tableView.setItems(data);

        tableView.getColumns().addAll(firma,model,suurus,hind);


        //Sisestused
        TextField addFirma = new TextField();
        addFirma.setPromptText("Firma");
        addFirma.setMaxWidth(120);
        addFirma.setLayoutX(7);
        addFirma.setLayoutY(360);

        TextField addModel = new TextField();
        addModel.setMaxWidth(120);
        addModel.setLayoutX(135);
        addModel.setLayoutY(360);
        addModel.setPromptText("Model");

        TextField addSuurus = new TextField();
        addSuurus.setMaxWidth(60);
        addSuurus.setLayoutX(263);
        addSuurus.setLayoutY(360);
        addSuurus.setPromptText("Suurus");

        TextField addHind = new TextField();
        addHind.setMaxWidth(60);
        addHind.setLayoutX(330);
        addHind.setLayoutY(360);
        addHind.setPromptText("Hind");

        Button lisaNupp = new Button("Lisa anmdebaasi");
        lisaNupp.setLayoutX(430);
        lisaNupp.setLayoutY(360);

        Button saveNupp = new Button("Salvesta muudatused");
        saveNupp.setLayoutX(425);
        saveNupp.setLayoutY(180);

        Button kustutaNupp = new Button("Kustuta");
        kustutaNupp.setLayoutX(465);
        kustutaNupp.setLayoutY(240);

        Button kasutamisJuhend = new Button("Kasutamis juhend");
        kasutamisJuhend.setLayoutX(435);
        kasutamisJuhend.setLayoutY(50);

        saveNupp.setOnAction(event -> {

            for(int i = 0; i<data.size();i++){
                if(!data.get(i).getFirma().equals(dataOld.get(i).getFirma()) //Kui märkan muudatusi reas
                        || !data.get(i).getModel().equals(dataOld.get(i).getModel())
                        || !data.get(i).getSuurus().equals(dataOld.get(i).getSuurus())
                        || !data.get(i).getHind().equals(dataOld.get(i).getHind())) {

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
            if (addFirma.getText().length()>0 && addModel.getText().length()>0 && addSuurus.getText().length()>0 && addHind.getText().length()>0) {
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
                dataOld=data;
            }
            else{
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Viga");
                alert.setHeaderText(null);
                alert.setContentText("Mingi lahter jäi tühjaks! Proovi uuesti");
                alert.showAndWait();
            }
        });

        kustutaNupp.setOnAction(event -> {
            String[] indeksiLedimiseks = new String[4];
            indeksiLedimiseks[0] = tableView.getSelectionModel().getSelectedItem().getFirma();
            indeksiLedimiseks[1] = tableView.getSelectionModel().getSelectedItem().getModel();
            indeksiLedimiseks[2] = tableView.getSelectionModel().getSelectedItem().getSuurus();
            indeksiLedimiseks[3] = tableView.getSelectionModel().getSelectedItem().getHind();

            baas.kustutaAndmebaasisit(baas.leiaIndeks(indeksiLedimiseks));
            data=baas.tagastaAndmebaas();
            dataOld=baas.tagastaAndmebaas();
            tableView.setItems(data);
        });

        kasutamisJuhend.setOnAction(event -> {
            Label tühik1 = new Label("");
            Label rida1 = new Label(" - Andmete lisamiseks on olemas akna all olevad sisestused (firma,model,");
            Label rida2 = new Label("   suurus,hind). Pärast sisestamist vajuta nuppu \"Lisa andmebaasi\"");

            Label tühik2 = new Label("");
            Label rida3 = new Label(" - Andmete muutmiseks klõpsa kahekordselt sõnale, mida tahad muuta.");
            Label rida4 = new Label("   Pärast andmete sisestamist vajuta Enter ning vajuta nupule \"Salvesta muudatused\"");

            Label rida5 = new Label(" - Read kustutamiseks vali sobiv rida ning vajuta selle peale.");
            Label rida6 = new Label("   Pärast vajuta nupule \"Kustuta\"");

            VBox secondRoot = new VBox();
            secondRoot.getChildren().addAll(rida1,rida2,tühik1,rida3,rida4,tühik2,rida5,rida6);

            Scene secondScene = new Scene(secondRoot, 450, 170);

            // New window (Stage)
            Stage newWindow = new Stage();
            newWindow.setTitle("Kasutamisjuhend");
            newWindow.setScene(secondScene);

            newWindow.initModality(Modality.WINDOW_MODAL);
            newWindow.initOwner(peaLava);
            newWindow.setX(peaLava.getX()+60);
            newWindow.setY(peaLava.getY()+120);

            newWindow.show();
        });



        root.getChildren().add(tableView);
        root.getChildren().addAll(addFirma,addModel,addSuurus,addHind,lisaNupp,saveNupp,kustutaNupp,kasutamisJuhend);



        Scene scene = new Scene(root,570,420);
        peaLava.setScene(scene);
        peaLava.setTitle("Anmdebaas");
        peaLava.show();
    }
}
