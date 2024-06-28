package com.multiplayergame.battleship;

import com.multiplayergame.model.Cell;
import com.multiplayergame.network.HandlerForNetwork;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;


public class GameBoardScreen {

    final int BOARD_SIZE=10;
    final int CELL_SIZE=30;
    boolean isHorizontal=true;

    Cell ships[]=new Cell[5];

    Cell myCells[][]=new Cell[10][10];
    Cell oppCells[][]=new Cell[10][10];
    Cell selectedShip=null;
    Button btnRotate;
    Label lblPlaceShips;
    Label lblStatus;
    Text txtMsg;

    
    AIBoardScreen aiBoardScreen;
    Timeline animation;
    boolean myTurn=false;
    Stage stage;
    boolean isVsComputer;
    HandlerForNetwork handlerForNetwork;

    public GameBoardScreen(AIBoardScreen aiBoardScreen) {
        this.aiBoardScreen = aiBoardScreen;
        this.isVsComputer=true;
        setupBoard();
        playVsComputer();
    }



    GridPane gridPaneOpponent;

    public GameBoardScreen(HandlerForNetwork handlerForNetwork) {

        isVsComputer=false;
        setupBoard();
        this.handlerForNetwork=handlerForNetwork;
        playVsPlayer(handlerForNetwork);
    }


    private void setupBoard()
    {


         gridPaneOpponent= initOpponentBoard();
        GridPane gridPaneMy=initMyBoard();


        VBox box1=new VBox();
        box1.setSpacing(5);
        Label labelOpp=new Label("Opponent battleShip");
        txtMsg=new Text();

        labelOpp.setFont(new Font("Verdana",15));
        labelOpp.setTextFill(Color.RED);

        StackPane msgPane=new StackPane();
        msgPane.getChildren().addAll(txtMsg);
        box1.getChildren().addAll(labelOpp,msgPane,gridPaneOpponent);
        box1.setAlignment(Pos.CENTER);

        VBox box2=new VBox();
        box2.setSpacing(5);
        Label labelMy=new Label("My BattleShip");
        labelMy.setTextFill(Color.BLUE);
        labelMy.setFont(new Font("Verdana",15));
        box2.getChildren().addAll(labelMy,new Text(),gridPaneMy);
        box2.setAlignment(Pos.CENTER);

        HBox mainLayout=new HBox();
        mainLayout.setSpacing(5);

        mainLayout.getChildren().addAll(box1,box2);
        mainLayout.setAlignment(Pos.CENTER);

        BorderPane borderPane=new BorderPane();
        borderPane.setCenter(mainLayout);

        GridPane shipsPane=createShipPane();
        VBox shipsBox=new VBox();
        shipsBox.setSpacing(5);
        shipsBox.setAlignment(Pos.CENTER);
        lblPlaceShips=new Label("Place the ships on your Board: ");
        lblPlaceShips.setFont(new Font("Verdana",14));
        lblPlaceShips.setTextFill(Color.GREEN);
        lblStatus=new Label();
        lblStatus.setFont(new Font("Verdana",14));
        lblStatus.setTextFill(Color.RED);
        shipsBox.getChildren().addAll(lblStatus,lblPlaceShips,shipsPane);
        borderPane.setBottom(shipsBox);
        BorderPane.setAlignment(shipsBox,Pos.TOP_CENTER);

        Scene scene=new Scene(borderPane,2*CELL_SIZE*BOARD_SIZE+200,CELL_SIZE*BOARD_SIZE+300);
         stage=new Stage();
        stage.setScene(scene);
        stage.setX(50);
        stage.setY(100);
        stage.show();

        scene.setOnMousePressed(this::handleMousePressed);


    }

    private GridPane initOpponentBoard() {

        GridPane gridPane=new GridPane();
        gridPane.setPadding(new Insets(10,10,10,10));
        gridPane.setVgap(2);
        gridPane.setHgap(2);
        int rowNumber=1;
        char colNumber='A';
        for (int row=0;row<BOARD_SIZE;row++)
        {
            Label label=new Label((rowNumber++)+"");
            GridPane.setRowIndex(label,row+1);
            GridPane.setColumnIndex(label,0);
            gridPane.getChildren().addAll(label);
            Label labelCol=new Label((colNumber++)+"");
            GridPane.setRowIndex(labelCol,0);
            GridPane.setColumnIndex(labelCol,row+1);
            gridPane.getChildren().addAll(labelCol);

            for (int col=0;col<BOARD_SIZE;col++)
            {

                Cell cell=new Cell(CELL_SIZE,CELL_SIZE);
                cell.setFill(Color.WHITESMOKE);
                cell.setStroke(Color.LIGHTGRAY);
                cell.setRowCol(row,col);
                oppCells[row][col]=cell;


                GridPane.setRowIndex(cell,row+1);
                GridPane.setColumnIndex(cell,col+1);
                gridPane.getChildren().addAll(cell);

            }
        }


        gridPane.setAlignment(Pos.CENTER);
        return gridPane;

    }


    private GridPane initMyBoard() {

        GridPane gridPane=new GridPane();
        gridPane.setPadding(new Insets(10,10,10,10));
        gridPane.setVgap(2);
        gridPane.setHgap(2);
        int rowNumber=1;
        char colNumber='A';
        for (int row=0;row<BOARD_SIZE;row++)
        {
            Label label=new Label((rowNumber++)+"");
            GridPane.setRowIndex(label,row+1);
            GridPane.setColumnIndex(label,0);
            gridPane.getChildren().addAll(label);
            Label labelCol=new Label((colNumber++)+"");
            GridPane.setRowIndex(labelCol,0);
            GridPane.setColumnIndex(labelCol,row+1);
            gridPane.getChildren().addAll(labelCol);

            for (int col=0;col<BOARD_SIZE;col++)
            {

                Cell cell=new Cell(CELL_SIZE,CELL_SIZE);
                cell.setFill(Color.LIGHTGRAY);
                cell.setStroke(Color.BLACK);
                cell.setRowCol(row,col);

                GridPane.setRowIndex(cell,row+1);
                GridPane.setColumnIndex(cell,col+1);
                myCells[row][col]=cell;
                gridPane.getChildren().addAll(cell);

            }
        }

        gridPane.setAlignment(Pos.CENTER);
        return gridPane;

    }




    private GridPane createShipPane()
    {
        GridPane shipPane=new GridPane();
        shipPane.setVgap(10);
        shipPane.setHgap(20);
        shipPane.setAlignment(Pos.CENTER);
        int colIndex=0;
        int shipIndex=0;
        for (int i=5;i>=1;i--)
        {

            Cell ship=createShip(i);

            ships[shipIndex++]=ship;
            Text text = new Text(i+"");
            text.setFont(Font.font(14)); // Set the font and size of the text
            text.setFill(Color.WHITE); // Set the text color

            // Create a StackPane to overlay the Rectangle and Text
            StackPane stackPane = new StackPane();
            stackPane.getChildren().addAll(ship, text);
            GridPane.setColumnIndex(stackPane,colIndex++);
            GridPane.setRowIndex(stackPane,0);
            shipPane.getChildren().addAll(stackPane);

        }
        Button btnRotate=craeteRotateButton();
        GridPane.setColumnIndex(btnRotate,++colIndex);
        GridPane.setRowIndex(btnRotate,0);
        shipPane.getChildren().addAll(btnRotate);

        return shipPane;

    }

    private Button craeteRotateButton() {


         btnRotate=new Button("Rotate");
        btnRotate.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {


                isHorizontal = !isHorizontal;
                for (Cell ship:ships) {
                    double height = ship.getHeight();
                    double width = ship.getWidth();
                    ship.setHeight(width);
                    ship.setWidth(height);
                }

            }
        });

        return btnRotate;
    }

    private Cell createShip(int size) {

        Cell rectangle=new Cell(CELL_SIZE*size,CELL_SIZE);
        rectangle.setFill(Color.BLUE);

        return rectangle;
    }

    List<Cell> markedCells=new ArrayList<>();
    private void handleMousePressed(MouseEvent event) {
        lblStatus.setText("");
        if (event.getTarget() instanceof Cell) {

            {
                Cell selectedRectangle=(Cell) event.getTarget();
                boolean isShip=false;
                for (Cell ship:ships)
                {
                    if(ship.equals(selectedRectangle))
                        isShip=true;
                }
                if(isShip) {
                    if (selectedShip!=null)
                        selectedShip.setFill(Color.BLUE);
                    selectedShip = (Cell) event.getTarget();
                    selectedShip.setFill(Color.GREEN);
                }

                else {
                    boolean isOnMyGrid=false;
                    int selectedRow=0;
                    int selectedCol=0;
                    for (int row=0;row< myCells.length;row++)
                    {
                        for (int col=0;col<10;col++)
                        {
                            Cell rectangle=myCells[row][col];
                            if(selectedRectangle.equals(rectangle)) {
                                isOnMyGrid = true;
                                selectedRow=row;
                                selectedCol=col;
                                break;
                            }
                        }
                    }

                    if(isOnMyGrid)
                    {
                        if(selectedShip!=null)
                        {
                            if(isHorizontal)
                            {

                                int width=(int)(selectedShip.getWidth()/(double) CELL_SIZE);
                                if(selectedCol+width<= myCells[0].length) {

                                    boolean hasShip=false;
                                    for (int i = selectedCol; i < selectedCol + width; i++) {
                                        if( myCells[selectedRow][i].isHasShip())
                                            hasShip=true;
                                    }

                                    if(!hasShip) {
                                        for (int i = selectedCol; i < selectedCol + width; i++) {

                                            myCells[selectedRow][i].setFill(Color.BLUE);
                                            myCells[selectedRow][i].setHasShip(true);
                                        }
                                        selectedShip.setVisible(false);
                                        selectedShip = null;
                                        if (isAllShipsPlaced()) {
                                            btnRotate.setVisible(false);
                                            lblPlaceShips.setVisible(false);

                                            activateOpponentGrid();
                                            //create alert dialog
                                            Alert alert=new Alert(Alert.AlertType.INFORMATION);
                                            alert.setTitle("All Done");
                                            alert.setHeaderText("Vs AI");
                                            alert.setContentText("Click ok to start the game");
                                            alert.showAndWait();
                                            createAnimation();

                                        }
                                    }
                                    else
                                        lblStatus.setText("Cannot place here");


                                }
                                else
                                {

                                    lblStatus.setText("Cannot place here");

                                }

                            }
                            else {
                                int height = (int) (selectedShip.getHeight() / (double) CELL_SIZE);
                                if (selectedRow + height <= myCells.length) {

                                    boolean hasShip = false;
                                    for (int i = selectedRow; i < selectedRow + height; i++) {
                                        if (myCells[i][selectedCol].isHasShip())
                                            hasShip = true;
                                    }

                                    if (!hasShip) {
                                        for (int i = selectedRow; i < selectedRow + height; i++) {

                                            myCells[i][selectedCol].setFill(Color.BLUE);
                                            myCells[i][selectedCol].setHasShip(true);
                                        }
                                        selectedShip.setVisible(false);
                                        selectedShip = null;
                                        if (isAllShipsPlaced()) {
                                            btnRotate.setVisible(false);
                                            lblPlaceShips.setVisible(false);
                                            activateOpponentGrid();
                                            //create alert dialog
                                            Alert alert=new Alert(Alert.AlertType.INFORMATION);
                                            alert.setTitle("All Done");
                                            alert.setHeaderText("Vs AI");
                                            alert.setContentText("Click ok to start the game");
                                            alert.showAndWait();
                                            createAnimation();
                                        }
                                    } else
                                    if (!isAllShipsPlaced())
                                        lblStatus.setText("Cannot place here");
                                }
                            }
                        }
                        else
                        if (!isAllShipsPlaced())
                            lblStatus.setText("Please Select a Ship");


                    }

                    else
                    {

                        if(myTurn && isVsComputer) {
                            if (!selectedRectangle.isMarked()) {
                                int row = selectedRectangle.getRow();
                                int col = selectedRectangle.getCol();
                                boolean isOnOppShip = aiBoardScreen.hasShip(row, col);
                                selectedRectangle.setHit(isOnOppShip);
                                selectedRectangle.setMarked(true);
                                markedCells.add(selectedRectangle);



                                gridPaneOpponent.getChildren().remove(selectedRectangle);
                                Label txt;
                                if (isOnOppShip) {
                                    txt = new Label("X");
                                    txt.setBackground(Background.fill(Color.GREEN));

                                } else {
                                    txt = new Label("O");
                                    txt.setBackground(Background.fill(Color.RED));
                                }
                                txt.setPrefWidth(CELL_SIZE);
                                txt.setPrefHeight(CELL_SIZE);
                                txt.setAlignment(Pos.CENTER);
                                txt.setTextFill(Color.WHITE);
                                animation.pause();
                                txtMsg.setOpacity(0);
                                myTurn=false;
                                aiBoardScreen.computerTurn();

                                GridPane.setColumnIndex(txt, col + 1);
                                GridPane.setRowIndex(txt, row + 1);
                                gridPaneOpponent.getChildren().add(txt);
                                isWin();
                            }


                        }
                        else if(myTurn && !isVsComputer)
                        {
                            if (!selectedRectangle.isMarked()) {
                                int row = selectedRectangle.getRow();
                                int col = selectedRectangle.getCol();
                                boolean isOnOppShip = handlerForNetwork.sendHasShipCommand(row,col);
                                selectedRectangle.setHit(isOnOppShip);
                                selectedRectangle.setMarked(true);
                                markedCells.add(selectedRectangle);



                                gridPaneOpponent.getChildren().remove(selectedRectangle);
                                Label txt;
                                if (isOnOppShip) {
                                    txt = new Label("X");
                                    txt.setBackground(Background.fill(Color.GREEN));

                                } else {
                                    txt = new Label("O");
                                    txt.setBackground(Background.fill(Color.RED));
                                }
                                txt.setPrefWidth(CELL_SIZE);
                                txt.setPrefHeight(CELL_SIZE);
                                txt.setAlignment(Pos.CENTER);
                                txt.setTextFill(Color.WHITE);
                                animation.pause();
                                txtMsg.setOpacity(0);
                                myTurn=false;
                                handlerForNetwork.sendEnableTurnCommand();

                                GridPane.setColumnIndex(txt, col + 1);
                                GridPane.setRowIndex(txt, row + 1);
                                gridPaneOpponent.getChildren().add(txt);
                                isWin();
                            }

                        }
                    }

                }
            }
        }
    }

    private void activateOpponentGrid() {
        if(isVsComputer)
        myTurn=true;
        for (Cell rectangles[]:oppCells)
        {
            for (Cell rectangle:rectangles)
            {
                rectangle.setFill(Color.LIGHTGRAY);
                rectangle.setStroke(Color.BLACK);
            }
        }
    }


    public boolean isAllShipsPlaced()
    {
        boolean allInvisible=true;
        for (Cell ship:ships)
        {
            if(ship.isVisible())
                allInvisible=false;
        }
       return allInvisible;
    }



    //create opponent Grid for Computer

    private void playVsComputer()
    {


    }

    private void playVsPlayer(HandlerForNetwork handlerForNetwork) {


    }


    private void createAnimation()
    {
        txtMsg.setText("Your Turn");
        txtMsg.setFill(Color.RED);
        txtMsg.setFont(new Font("Verdana",22));
        DropShadow glow = new DropShadow();
        glow.setColor(Color.YELLOW); // Set the color of the glow
        glow.setWidth(50); // Adjust the width of the glow effect
        glow.setHeight(50); // Adjust the height of the glow effect
        txtMsg.setEffect(glow);




        animation=new Timeline(
                new KeyFrame(Duration.seconds(0.3), event -> {
                    if (txtMsg.isVisible()) {
                        txtMsg.setVisible(false);
                    } else {
                        txtMsg.setVisible(true);
                    }
                })
        );

        animation.setCycleCount(Animation.INDEFINITE);
        animation.setRate(0.5);
        animation.play();

    }


    public void enableMyTurn()
    {
        myTurn=true;
        txtMsg.setOpacity(1.0);
        animation.play();
    }

    public boolean hasShip(int row, int col) {
        Cell cell=myCells[row][col];
        return cell.isHasShip();
    }

    public void isWin()
    {
        int sum=0;//to calculate hits, if hist equal to 15 then win
        for (Cell cell:markedCells)
        {
            if(cell.isHit())
                sum++;
        }
        if(sum>=15)
        {
            animation.stop();
            Alert alert=new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("You Win");
            alert.setContentText("You Win");
            alert.setHeaderText("Click ok to restart");
            alert.showAndWait();
            stage.close();
            aiBoardScreen.close();
            new MainMenuScreen();
        }
    }


    public void close() {
        stage.close();
        new MainMenuScreen();
    }
}
