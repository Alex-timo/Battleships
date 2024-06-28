package com.multiplayergame.battleship;

import com.multiplayergame.model.Cell;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class AIBoardScreen {
    final int BOARD_SIZE=10;
    final int CELL_SIZE=30;
    Cell oppCellsForComputer[][]=new Cell[10][10];
    Cell myCells[][]=new Cell[10][10];
    GameBoardScreen gameBoardScreen;
    Text txtMsg;
    Timeline animation;
    boolean isShipsPlaced=false;
    Stage stage;



    public AIBoardScreen() {


       openBoard();
        //place ships for computer

        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                placeShips();

                isShipsPlaced=true;

            }
        });
        thread.start();

        try {

            Thread.sleep(200);
            int restart=0;

            while (!isShipsPlaced && restart<=5) {
                if (!isShipsPlaced) {
                    System.out.println("Restarting thread");
                    restart++;
                    if(restart>=5) {
                        System.exit(0);
                    }
                    thread.interrupt();
                    thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            placeShips();

                            isShipsPlaced = true;

                        }
                    });
                    thread.start();
                    Thread.sleep(200);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    private void placeShips() {

        for (int k = 5; k >= 1; k--) {
            int random = (int) (Math.random() * 2);
            boolean isHorizontal = random == 1 ? true : false;
            if (isHorizontal) {

                int width = k;
                int selectedCol = (int) (Math.random() * 10);
                int selectedRow = (int) (Math.random() * 10);
                while (!(selectedCol + width < myCells[0].length)) {
                    selectedCol = (int) (Math.random() * 10);
                    selectedRow = (int) (Math.random() * 10);
                }

                if (selectedCol + width < myCells[0].length) {

                    boolean hasShip = false;
                    for (int i = selectedCol; i < selectedCol + width; i++) {
                        if (myCells[selectedRow][i].isHasShip())
                            hasShip = true;
                    }

                    while (hasShip)
                    {
                        hasShip = false;
                        while (!(selectedCol + width < myCells[0].length)) {
                            selectedCol = (int) (Math.random() * 10);
                            selectedRow = (int) (Math.random() * 10);
                        }
                        for (int i = selectedCol; i < selectedCol + width; i++) {
                            if (myCells[selectedRow][i].isHasShip())
                                hasShip = true;
                        }
                    }

                    if (!hasShip) {
                        for (int i = selectedCol; i < selectedCol + width; i++) {

                            myCells[selectedRow][i].setHasShip(true);
                        }



                    }

                }
            }else {
                int height = k;
                int selectedCol = (int) (Math.random() * 10);
                int selectedRow = (int) (Math.random() * 10);
                while (!(selectedRow + height <= myCells.length)) {
                    selectedCol = (int) (Math.random() * 10);
                    selectedRow = (int) (Math.random() * 10);
                }
                if (selectedRow + height <= myCells.length) {

                    boolean hasShip = false;
                    for (int i = selectedRow; i < selectedRow + height; i++) {
                        if (myCells[i][selectedCol].isHasShip())
                            hasShip = true;
                    }

                    while (hasShip)
                    {
                        hasShip=false;
                        while (!(selectedRow + height <= myCells.length)) {
                            selectedCol = (int) (Math.random() * 10);
                            selectedRow = (int) (Math.random() * 10);
                        }
                        for (int i = selectedRow; i < selectedRow + height; i++) {
                            if (myCells[i][selectedCol].isHasShip())
                                hasShip = true;
                        }
                    }

                    if (!hasShip) {
                        for (int i = selectedRow; i < selectedRow + height; i++) {

                            myCells[i][selectedCol].setFill(Color.BLUE);
                            myCells[i][selectedCol].setHasShip(true);
                        }

                    }
                }
            }
                }
            }


    GridPane gridPane;

    private void openBoard()
    {
         gridPane=new GridPane();
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
                oppCellsForComputer[row][col]=cell;
                GridPane.setRowIndex(cell,row+1);
                GridPane.setColumnIndex(cell,col+1);
                gridPane.getChildren().addAll(cell);

            }
        }

        gridPane.setAlignment(Pos.CENTER);
        VBox box1=new VBox();
        box1.setSpacing(5);
        Label labelOpp=new Label("Computer Player");
        txtMsg=new Text();

        labelOpp.setFont(new Font("Verdana",15));
        labelOpp.setTextFill(Color.RED);

        box1.getChildren().addAll(labelOpp,txtMsg,gridPane);
        box1.setAlignment(Pos.CENTER);

        Scene scene=new Scene(box1,CELL_SIZE*BOARD_SIZE+200,CELL_SIZE*BOARD_SIZE+200);
         stage=new Stage();
        stage.setScene(scene);

        stage.setX(2*CELL_SIZE*BOARD_SIZE+300);
        stage.setY(100);
        stage.show();


        //create Computer cells
        for (int row=0;row<BOARD_SIZE;row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {

                Cell cell = new Cell(CELL_SIZE, CELL_SIZE);
                cell.setRowCol(row,col);
                myCells[row][col] = cell;

            }
        }

        createAnimation();
        txtMsg.setOpacity(0);
    }

    private void displayShips()
    {
        for (int i=0;i< myCells.length;i++)
        {
            for (int j=0;j<myCells[0].length;j++) {
                if(myCells[i][j].isHasShip())
                System.out.println("Ship: "+myCells[i][j].getRow()+": "+myCells[i][j].getCol());
            }
        }
    }
    public void setPlayerBoard(GameBoardScreen gameBoardScreen) {
        this.gameBoardScreen=gameBoardScreen;
    }

    public boolean hasShip(int row, int col) {
        Cell cell=myCells[row][col];
        return cell.isHasShip();
    }



    public void computerTurn()
    {
        txtMsg.setOpacity(1);
        animation.play();
        PauseTransition action=new PauseTransition(Duration.seconds(1));
        action.setOnFinished(e->{
            playComputer();
            gameBoardScreen.enableMyTurn();
            action.stop();
            animation.pause();
            txtMsg.setOpacity(0);
        });
        action.play();
    }


    List<Cell> markedCells=new ArrayList<>();
    private void playComputer()
    {
        Cell selectedRectangle=null;
        int row;
        int col;


             row=(int)(Math.random()*10);
             col=(int)(Math.random()*10);



            for (int i=0;i<markedCells.size();i++)
            {

                Cell cell=markedCells.get(i);
                if(cell.isHit())
                {
                    if(cell.getRow()+1<oppCellsForComputer.length)
                    {
                        row=cell.getRow()+1;
                        col=cell.getCol();



                        break;

                    }
                    else if(cell.getCol()+1<oppCellsForComputer[0].length)
                    {
                        col=cell.getCol()+1;
                        row=cell.getRow();
                        selectedRectangle=oppCellsForComputer[row][col];
                        while (selectedRectangle.isMarked() )
                        {
                            if(selectedRectangle.isHit()) {
                                if(row+1<oppCellsForComputer.length)
                               row++;
                                break;
                            }
                            else
                            {
                                if(col+1<oppCellsForComputer[0].length)
                                    col++;
                            }
                        }

                        break;



                    }


                }


            }





        selectedRectangle= oppCellsForComputer[row][col];
            while (markedCells.contains(selectedRectangle))
            {
                row=(int)(Math.random()*10);
                col=(int)(Math.random()*10);
                selectedRectangle= oppCellsForComputer[row][col];

            }

        selectedRectangle.setMarked(true);
        boolean isOnOppShip= gameBoardScreen.hasShip(row,col);
        selectedRectangle.setHit(isOnOppShip);
        selectedRectangle.isMarked();
        markedCells.add(selectedRectangle);

        gridPane.getChildren().remove(selectedRectangle);
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



        GridPane.setColumnIndex(txt, col + 1);
        GridPane.setRowIndex(txt, row + 1);
        gridPane.getChildren().add(txt);
        checkWin();


    }


    private void createAnimation()
    {
        txtMsg.setText("Computer Turn");
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


    }


    private void checkWin()
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
         alert.setTitle("Computer Wins");
         alert.setContentText("Computer win");
         alert.setHeaderText("Click ok to restart");
         alert.showAndWait();
         stage.close();
         gameBoardScreen.close();
     }
    }

    public void close()
    {
        stage.close();
    }
}
