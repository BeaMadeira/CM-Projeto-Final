package com.cm.projetoFinal;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.cm.projetoFinal.database.entities.Topic;
import com.cm.projetoFinal.database.repositories.TopicRepository;
import com.cm.projetoFinal.tictactoe.Agent;
import com.cm.projetoFinal.tictactoe.Board;
import com.cm.projetoFinal.tictactoe.Computer;
import com.cm.projetoFinal.tictactoe.Player;
import com.cm.projetoFinal.tictactoe.Position;
import com.cm.projetoFinal.ui.main.interfaces.TaskCallback;
import com.cm.projetoFinal.ui.main.interfaces.TaskCallbackTopic;

import java.util.ArrayList;
import java.util.List;

public class MainViewModel extends AndroidViewModel {
    private final TopicRepository topicRepository;
    private Board board;
    private int currentPlayer;
    //array of agents, player or computer, order determines the turn
    private List<Agent> agents;
    private String oponentTopic;
    private char symbol;
    private boolean isPlaying;

    public MainViewModel(@NonNull Application application) {
        super(application);
        topicRepository = new TopicRepository(application);
    }

    public void insertTopic(TaskCallbackTopic callback, Topic... topics) {
        Log.w("DEBUG", "----------------insertTopics--------------");
        topicRepository.insertTopics(callback, topics);
    }

    public void getAllTopics(TaskCallbackTopic callback) {
        Log.w("DEBUG", "----------------getTopics--------------");
        topicRepository.getAllTopics(callback);
    }

    public void getTopic(TaskCallbackTopic callback, String topic) {
        Log.w("DEBUG", "----------------getTopic--------------");
        topicRepository.getTopic(callback, topic);
    }

    public void deleteTopicByName(TaskCallbackTopic callback, String topic) {
        Log.w("DEBUG", "----------------deleteTopics--------------");
        topicRepository.deleteTopic(callback, topic);
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

        // Create a player with the symbol
        Player player = new Player(symbol);
        // Create a computer with the opposite symbol
        Player player2 = new Player(player.getOpponentSymbol());

        agents.add(player);
        agents.add(player2);

        // Whichever agent has the X symbol goes first
        if (player.getSymbol() == 'X' && player2.getSymbol() == 'O') {
            currentPlayer = 0;
        }
        else {
            currentPlayer = 1;
        }

        //create a board with the size 3
        board = new Board(3);

        /*if (currentPlayer == 1) {
            player2.move(board, 0, 0);
            currentPlayer = 0;
        }*/

        // Call the callback method
        callback.onSuccess(player);
    }

    // Reset board
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

    public Agent getPlayer() {
        return agents.get(0);
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

    public char getSymbol() {
        return symbol;
    }

    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public int getCurrentPlayerInt() {
        return this.currentPlayer;
    }

    public void playOpponent(Position position) {
        if (currentPlayer == 1) {
            agents.get(currentPlayer).move(board, position.getX(), position.getY());
            currentPlayer = 0;
        }
    }
}