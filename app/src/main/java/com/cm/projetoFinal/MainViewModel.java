package com.cm.projetoFinal;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.cm.projetoFinal.database.entities.Profile;
import com.cm.projetoFinal.database.repositories.ProfileRepository;
import com.cm.projetoFinal.tictactoe.Agent;
import com.cm.projetoFinal.tictactoe.Board;
import com.cm.projetoFinal.tictactoe.Computer;
import com.cm.projetoFinal.tictactoe.Player;
import com.cm.projetoFinal.ui.main.interfaces.TaskCallback;

import java.util.ArrayList;
import java.util.List;

public class MainViewModel extends AndroidViewModel{
    private final ProfileRepository profileRepository;

    private Profile profile;
    private Board board;
    private int currentPlayer;
    //array of agents, player or computer, order determines the turn
    private List<Agent> agents;

    private char getRandomSymbol() {
        if (Math.random() < 0.5)
            return 'X';
        else return 'O';
    }
    public MainViewModel(@NonNull Application application) {
        super(application);
        profileRepository = new ProfileRepository(application);


    }


    public void createProfile(TaskCallback tc, Profile profile){
        profileRepository.insertProfile(tc,profile);
    }

    public void setProfile(Profile profile){
        this.profile = profile;
    }

    public Profile getProfile(){
        return profile;
    }

    public Profile getAllProfile(TaskCallback tc){

        profileRepository.getProfile(tc);
        return getProfile();
    }

    public void updateProfile(TaskCallback tc, Profile profile) {

        profileRepository.updateProfile(tc,profile);
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
        if(player.getSymbol() == 'X' && computer.getSymbol() == 'O')
            currentPlayer = 0;
        else currentPlayer = 1;

        //create a board with the size 3
        board = new Board(3);

        if (currentPlayer==1) {
            computer.move(board, 0, 0);
            currentPlayer = 0;
        }

        //call the callback method
        callback.onSuccess(player);
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
}