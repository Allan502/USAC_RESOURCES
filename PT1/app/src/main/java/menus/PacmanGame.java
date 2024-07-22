/*
    Board items:
    X: Wall
    @: Trap or ghost
    0: Simple reward    -> 10 points    
    $: Special reward   -> 15 points
    <: Pacman

    Commands:
    8: Move up
    5: Move down
    4: Move left
    6: Move right
    F: Pause game

    *********  Cartesian plane modeling software solution ***************

                0     1   2   3   4   5   ---------> +X 
        0                 |
                          |  
        1                 |
                          |  
        2 ----------------@
        
        3
        
        4
        
        5
        |
        |
        +Y

        To access a two-dimensional array, we first access the rows, which correspond to the positive Y-axis values 
        based on the Cartesian plane. Therefore, when we use `board[][]`, the first value refers to the row 
        (the Y-axis), and the second value refers to the column (the X-axis). Based on this, to correctly access 
        the position specified by the user, we access the array by first specifying the Y value and then the X value. 
        For example: `board[y][x]`.

*/ 

package menus;

import java.util.Scanner;
import java.io.IOException;
import java.util.Random;


public class PacmanGame {

    // Constructor is not needed
    Scanner scanner = new Scanner(System.in);
    Random random = new Random();

    public void runMenu(){
        int [] pacmanPosition = new int[2];                                             //position (y, x)
        String [][] history = new String[30][2];                                        //history array with 30 rows and 2 columns (name, score)
        int option  = 0;                                                                //condition to exit the loop  
        do {
            int score   = 0;                                                            //score
            int lives   = 3;                                                            //lives
            int totalRewards = 0;                                                       //rewards earned
            int rewardsEarned = 0;                                                      //rewards earned in the last move
            option = mainMenu();                                                        //get the option in the main menu
            switch (option){                        
                case 1:                                                                 //start the game
                    clearConsole();
                    String name = startGameMenu();                                      //get the name of the player
                    int [] boardSettings = setBoard();                                  //get the board settings
                    totalRewards = boardSettings[1];                                    //get the rewards earned
                    String [][] board = createBoard(boardSettings[0], boardSettings[1], 
                                                    boardSettings[2], boardSettings[3]); // (size, rewards, walls, traps)
                    showBoard(board, name, score, lives);                               //show the board
                    pacmanPosition = setPacmanPosition(board);                          // (y, x) -> (row, column)
                    clearConsole();                                                     //clear the console
                    showBoard(board, name, score, lives);                               //show the board
                    while(lives != 0 && totalRewards != rewardsEarned){
                        pacmanPosition = move(pacmanPosition[0], pacmanPosition[1],
                                              score, lives, board);                     // (row "y", column "x", score, lives, board)
                        score = pacmanPosition[2];                                      //update the score
                        lives = pacmanPosition[3];                                      //update the lives
                        rewardsEarned += pacmanPosition[4];                             //update the rewards earned
                        if (lives == 0){
                            clearConsole();                                             //clear the console
                            showBoard(board, name, score, lives);
                            gameOver();                                                 //show the game over message
                            clearConsole();                                             //clear the console
                        } else if (totalRewards == rewardsEarned){
                            clearConsole();
                            showBoard(board, name, score, lives);
                            winMessage(name);                                           //show the win message
                            clearConsole();                                             //clear the console   
                        }else {
                            clearConsole();                                             //clear the console
                            showBoard(board, name, score, lives);                       //show the board
                        }
                    }
                    fillHistory(history, name, score);                                  //fill the history with the last game
                    break;
                case 2:
                    clearConsole();                                                     //clear the console
                    showHistory(history);                                               //show the history
                    clearConsole();                                                     //clear the console
                    break;
                case 3:
                    break;
                default:
                    System.out.println("Invalid option");
            }
        } while (option != 3);
    }

    public void controlPanel(String name, int score, int lives){
        System.out.println("------------PLAYER DATA---------------");
        System.out.println("User Name:     \t" + name);
        System.out.println("Score ($):     \t" + score);
        System.out.println("Lives  <3:     \t" + lives);
    }

    public int mainMenu(){
        System.out.println("======= Main Menu =======");
        System.out.println("1. Start game");
        System.out.println("2. History");
        System.out.println("3. Exit");
        System.out.print("Option: ");
        int option = scanner.nextInt();
        return option;
    }

    public String startGameMenu(){
        System.out.println("======= Start Game =======");
        System.out.print("Name: ");
        String name = scanner.next();
        return name;
    }

    public int[] setBoard(){
        int sizeNumer   = 0;
        int rewards     = 0;
        int walls       = 0;
        int traps       = 0;
        System.out.println("======= Set Board =======");
        System.out.println("Please set the board:");
        System.out.print("Board size (P or G):");
        String size = scanner.next().toUpperCase();
        if (size.equals("P")){
            sizeNumer   = 0;
            rewards     = getInputBoard("Rewards [1-12]: ", 12);
            walls       = getInputBoard("Walls   [1-6] : ", 6);
            traps       = getInputBoard("Traps   [1-6] : ", 6);
        } else if (size.equals("G")){
            sizeNumer   = 1;
            rewards     = getInputBoard("Rewards [1-40]: ", 40);
            walls       = getInputBoard("Walls   [1-20]: ", 20);
            traps       = getInputBoard("Traps   [1-20]: ", 20);
        }
        System.out.println("");
        clearConsole();
        return new int[]{sizeNumer, rewards, walls, traps};
    }

    private int getInputBoard(String phrase, int max){
        int option = 0;
        do {
            try{
                System.out.print(phrase);
                option = scanner.nextInt();
            } catch (Exception e){
                System.out.println("Invalid input");
                scanner.next();
            }
            
        } while (option < 1 || option > max);
        return option; 
    }

    public String[][] createBoard(int size, int rewards, int walls, int traps){
        // Calculate the total items
        int totalItems = rewards + walls + traps;

        // Create a reward's array and fill it with random rewards
        String [] rewardsList  = new String[rewards];  
        for (int i=0; i<rewards; i++){
            int rewardType = random.nextInt(2);
            if (rewardType == 0){
                rewardsList[i] = "0";
            } else {
                rewardsList[i] = "$";
            }
        }
        // Select the board size
        if (size == 0){
            String [][] board = new String[5][6];
            return fillBoard(5, 6, totalItems, board, walls, traps, rewardsList);
        } else {
            String [][] board = new String[10][10];
            return fillBoard(10, 10, totalItems, board, walls, traps, rewardsList);
        }
    }

    public String [][] fillBoard(int xlimit, int ylimit, int totalItems, String[][] board, int walls, int traps, String[] rewardsList){
        int itemCounter = 0;
        int rewardCounter = 0;
        int trapCounter = 0;
        int wallCounter = 0;
        // We will fill first with walls
        while (itemCounter < totalItems) {
            // Create 2 variables to store position x and y && dont exceed the limits
            int x = random.nextInt(xlimit);
            int y = random.nextInt(ylimit);
            if (itemCounter == 0){
                board[x][y] = "X";
                wallCounter++;
                itemCounter++;
            } else {
                if (board[x][y] == null){
                    if (wallCounter != walls){
                        board[x][y] = "X";
                        wallCounter++;
                        itemCounter++;
                    } else if (trapCounter != traps)    {
                        board[x][y] = "@";
                        trapCounter++;
                        itemCounter++;
                    } else if (rewardCounter != rewardsList.length){
                        board[x][y] = rewardsList[rewardCounter];
                        rewardCounter++;
                        itemCounter++;
                    }
                }
            }
        }
        // Fill the rest of the board with empty spaces
        for (int i=0; i<xlimit; i++){
            for (int j=0; j<ylimit; j++){
                if (board[i][j] == null){
                    board[i][j] = "";
                }
            }
        }
        return board;
    }

    public void showBoard(String[][] board, String name, int score, int lives){
        controlPanel(name, score, lives);
        System.out.println(" --------------------------------------------------------------------------------------      \n");
        for (int i=0; i<board.length; i++){
            System.out.print("|\t");
            for (int j=0; j<board[i].length; j++){
                System.out.print(board[i][j] + "\t");
            }
            System.out.print("|\t");
            System.out.println();
        }
        System.out.println(" \n--------------------------------------------------------------------------------------\n      ");
    }

    public int[] getPacmanPosition(){
        System.out.println("======= Set Position =======");
        System.out.println("Please set the position:");
        System.out.print("Row: ");
        int row = scanner.nextInt();
        System.out.print("Column: ");
        int column = scanner.nextInt();
        return new int[]{row-1, column-1, }; // (y, x)
    }

    public int[] setPacmanPosition(String[][] board){
        boolean validation = false;
        int x = 0;
        int y = 0;
        while (!validation){
            int [] pacmanPosition = getPacmanPosition();
            y = pacmanPosition[0]; // Rows Value
            x = pacmanPosition[1]; // Column Value
            if (board[y][x] == ""){
                board[y][x] = "<";
                validation = true;
            } else {
                System.out.println("Invalid position");
            }
        }
        System.out.println("");
        return new int[]{y, x};
    }

    public int pauseGame(){
        int option = 0;
        do{
            clearConsole();
            System.out.println("======= Game Paused =======");
            System.out.println("1. Continue");
            System.out.println("2. Exit");
            System.out.print("Option: ");
            option = scanner.nextInt();
        } while (option != 2 && option != 1);
        return option;
    }

    public int[] move(int initialY, int initialX, int score, int lives, String[][] board){
        String move = "";
        int rewardsEarned = 0;
        int [] newPositions = new int[5]; // (y, x, score, lives, rewardsEarned)
        int [] localData = new int[]{initialY, initialX, score, lives, rewardsEarned};
        int exit = 0;
        do{
            System.out.print("\nMove: ");
            move = scanner.next();
            System.out.println();
        }while(!move.equals("8") && !move.equals("5") && !move.equals("4") && !move.equals("6") && !move.toUpperCase().equals("F"));
        
        if (move.equals("8")){
            newPositions = validateMove(true, initialY, initialX, -1, board, lives, score, "v");           //v
        } else if (move.equals("5")){
            newPositions = validateMove(true, initialY, initialX, 1, board, lives, score, "A");      //Ʌ
        } else if (move.equals("4")){
            newPositions = validateMove(false, initialY, initialX, -1, board, lives, score, ">");          //<
        } else if (move.equals("6")){
            newPositions = validateMove(false, initialY, initialX, 1, board, lives, score, "<");     //>
        } else if (move.toUpperCase().equals("F")){
            exit = pauseGame();
            if (exit == 2){
                newPositions[2] = score;
                newPositions[3] = 0;
            } else {
                return localData;
            }
        }
        return newPositions;
    }

    public int[] validateMove(boolean orientation, int posY, int posX, int shift, String[][] board, int lives, int score, String direction){ // (row, column)
        int rowsNumber = board.length;
        int columnsNumber = board[0].length;
        int rewardsEarned = 0;
        // VERTICAL
        if (orientation){
            int newYPostion = posY + shift;
            int yLimit = rowsNumber-1;
            if (newYPostion >= 0 && newYPostion <= yLimit){
                String item1 = board[newYPostion][posX];
                if (item1.equals("@") && lives > 0){
                    updatePosition(board, posY, posX, newYPostion, posX, direction);
                    posY = newYPostion;
                    lives--;
                } else if (item1.equals("0") || item1.equals("$")){
                    score += validateScore(item1);
                    updatePosition(board, posY, posX, newYPostion, posX, direction);
                    posY = newYPostion;
                    rewardsEarned++;
                } else if(item1.equals("")){
                    updatePosition(board, posY, posX, newYPostion, posX, direction);
                    posY = newYPostion;
                } else if (item1.equals("X")){
                    invalidMove();
                }
            } else {
                if (newYPostion < 0){  
                    String item2 = board[yLimit][posX];
                    if (item2.equals("0") || item2.equals("$")){
                        score += validateScore(item2);
                        updatePosition(board, posY, posX, yLimit, posX, direction);
                        posY = yLimit;
                        rewardsEarned++;
                    } else if(item2.equals("@")){
                        updatePosition(board, posY, posX, yLimit, posX, direction);
                        posY = yLimit;
                        lives--;
                    } else if(item2.equals("")){
                        updatePosition(board, posY, posX, yLimit, posX, direction);
                        posY = yLimit;
                    } else if (item2.equals("X")){
                        invalidMove();
                    }
                } else if(newYPostion > yLimit){
                    String item3 = board[0][posX];
                    if(item3.equals("0") || item3.equals("$")){
                        score += validateScore(item3);
                        updatePosition(board, posY, posX, 0, posX, direction);
                        posY = 0;
                        rewardsEarned++;
                    } else if (item3.equals("@")){
                        updatePosition(board, posY, posX, 0, posX, direction);
                        posY = 0;
                        lives--;
                    } else if (item3.equals("")){
                        updatePosition(board, posY, posX, 0, posX, direction);
                        posY = 0;
                    } else if (item3.equals("X")){
                        invalidMove();
                    }
                }
            } 
        } else {
            int newXPosition = posX + shift;
            int xLimit = columnsNumber-1;
            if (newXPosition >= 0 && newXPosition <= xLimit){
                String item1 = board[posY][newXPosition];
                if (item1.equals("@") && lives > 0) {
                    updatePosition(board, posY, posX, posY, newXPosition, direction);
                    posX = newXPosition;
                    lives--;
                } else if (item1.equals("0") || item1.equals("$")){
                    score += validateScore(item1);
                    updatePosition(board, posY, posX, posY, newXPosition, direction);
                    posX = newXPosition;
                    rewardsEarned++;
                } else if(item1.equals("")){
                    updatePosition(board, posY, posX, posY, newXPosition, direction);
                    posX = newXPosition;
                } else if (item1.equals("X")){
                    invalidMove();
                }
            } else {
                if (newXPosition < 0){ 
                    String item2 = board[posY][xLimit];
                    if (item2.equals("0") || item2.equals("$")){
                        score += validateScore(item2);
                        updatePosition(board, posY, posX, posY, xLimit, direction);
                        posX = xLimit;
                        rewardsEarned++;
                    } else if(item2.equals("@")){
                        updatePosition(board, posY, posX, posY, xLimit, direction);
                        posX = xLimit;
                        lives--;
                    } else if(item2.equals("")){
                        updatePosition(board, posY, posX, posY, xLimit, direction);
                        posX = xLimit;
                    } else if(item2.equals("X")){
                        invalidMove();
                    }
                } else if(newXPosition > xLimit){
                    String item3 = board[posY][0];
                    if(item3.equals("0") || item3.equals("$")){
                        score += validateScore(item3);
                        updatePosition(board, posY, posX, posY, 0, direction);
                        posX = 0;
                        rewardsEarned++;
                    } else if (item3.equals("@")){
                        updatePosition(board, posY, posX, posY, 0, direction);
                        posX = 0;
                        lives--;
                    } else if (item3.equals("")){
                        updatePosition(board, posY, posX, posY, 0, direction);
                        posX = 0;
                    } else if (item3.equals("X")){
                        invalidMove();
                    }
                }
            } 
        }
        
        return new int[]{posY, posX, score, lives, rewardsEarned};
    }

    public void invalidMove(){
        System.out.print("Invalid input. Press any key to continue");
        scanner.next();
    }

    public void updatePosition(String[][] board, int posY, int posX, int newY, int newX, String direction){
        board[posY][posX] = "";
        board[newY][newX] = direction;
    }

    public int validateScore(String reward){
        return reward.equals("0") ? 10 : 15;
    } 

    public void fillHistory(String[][] history, String name, int score){
        for (int i=0; i < history.length; i++){
            if(history[i][0] == null){
                history[i][0] = name;
                history[i][1] = String.valueOf(score);
                break;
            }
        }
    }

    public void showHistory(String[][] history){
        System.out.println("========== History ==========");
        System.out.println("No. \t Name \t\t Score");
        int counter = 1;
        for (int i = history.length - 1; i >= 0; i--){
            if (history[i][0] != null) {
                System.out.printf("%-8d %-15s %-5s%n", counter, history[i][0], history[i][1]);
                counter++;
            }
        }
        System.out.print("Write ok to continue: ");
        scanner.next();

    }

    public void gameOver(){
        System.out.println("======= Game Over T___T' =======");
        System.out.print("Press any key to continue: ");
        scanner.next();
    }

    public void winMessage(String name){
        System.out.println("Congratulations: " + name + ", you won the game! :3 :3 :3");
        System.out.print("Press any key to continue: ");
        scanner.next();
    }

    public  void clearConsole() {
        String os = System.getProperty("os.name").toLowerCase();
        
        try {
            if (os.contains("win")) {
                // Comando para Windows
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else if (os.contains("nix") || os.contains("nux") || os.contains("mac")) {
                // Comando para Unix y macOS
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            } else {
                // Si el sistema operativo no es reconocido
                System.out.println("Sistema operativo no soportado para limpieza de consola.");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
