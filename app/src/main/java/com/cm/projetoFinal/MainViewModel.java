package com.cm.projetoFinal;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.cm.projetoFinal.tictactoe.Agent;
import com.cm.projetoFinal.tictactoe.Board;
import com.cm.projetoFinal.tictactoe.Computer;
import com.cm.projetoFinal.tictactoe.Player;
import com.cm.projetoFinal.ui.main.interfaces.TaskCallback;

import java.util.ArrayList;
import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private Board board;
    private int currentPlayer;
    //array of agents, player or computer, order determines the turn
    private List<Agent> agents;
    private String oponentTopic;

    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    private char getRandomSymbol() {
        if (Math.random() < 0.5) {
            return 'X';
        } else {
            return 'O';
        }
    }

    //this method is called when the user clicks on the single player button
    public void startSinglePlayerGame(@NonNull TaskCallback callback) {
        agents = new ArrayList<>();
        //get a random symbol for the player
        char symbol = getRandomSymbol();
        //create a player with the symbol
        Player player = new Player(symbol);
        //create a computer with the opposite symbol
        Computer computer = new Computer(player.getOpponentSymbol());
        // whichever agent has the X symbol goes first
        agents.add(player);
        agents.add(computer);
        if (player.getSymbol() == 'X' && computer.getSymbol() == 'O') currentPlayer = 0;
        else currentPlayer = 1;

        //create a board with the size 3
        board = new Board(3);

        if (currentPlayer == 1) {
            computer.move(board, 0, 0);
            currentPlayer = 0;
        }

        //call the callback method
        callback.onSuccess(player);
    }

    //this method is called when the user clicks on the single player button
    public void startMultiPlayerGame(@NonNull TaskCallback callback) {
        agents = new ArrayList<>();
        // Get a random symbol for the player
        char symbol = getRandomSymbol();
        // Create a player with the symbol
        Player player = new Player(symbol);
        // Create a computer with the opposite symbol
        Player player2 = new Player(player.getOpponentSymbol());
        // Whichever agent has the X symbol goes first
        agents.add(player);
        agents.add(player2);
        if (player.getSymbol() == 'X' && player2.getSymbol() == 'O') currentPlayer = 0;
        else currentPlayer = 1;

        //create a board with the size 3
        board = new Board(3);

        if (currentPlayer == 1) {
            player2.move(board, 0, 0);
            currentPlayer = 0;
        }

        //call the callback method
        callback.onSuccess(player);
    }

    public void multiplayerGame() {

    }

    //reset board
    public void resetBoard(TaskCallback callback) {
        startSinglePlayerGame(callback);
    }

    public Agent next() {
        currentPlayer = currentPlayer == 0 ? 1 : 0;
        return agents.get(currentPlayer);
    }

    public Agent getCurrentPlayer() {
        return agents.get(currentPlayer);
    }

    public Board getBoard() {
        return board;
    }

    public String getOponentTopic() {
        return oponentTopic;
    }

    public void setOponentTopic(String oponentTopic) {
        this.oponentTopic = oponentTopic;
    }
}