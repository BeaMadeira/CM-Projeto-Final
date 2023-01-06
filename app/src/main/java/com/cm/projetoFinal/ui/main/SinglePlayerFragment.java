package com.cm.projetoFinal.ui.main;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.cm.projetoFinal.MainViewModel;
import com.cm.projetoFinal.R;
import com.cm.projetoFinal.tictactoe.Agent;
import com.cm.projetoFinal.tictactoe.Board;
import com.cm.projetoFinal.tictactoe.Computer;
import com.cm.projetoFinal.ui.main.interfaces.Authentication;
import com.cm.projetoFinal.ui.main.interfaces.FragmentChanger;
import com.cm.projetoFinal.ui.main.interfaces.TaskCallback;


public class SinglePlayerFragment extends Fragment {
    private MainViewModel mainViewModel;
    private final TaskCallback tc = new TaskCallback() {
        @Override
        public <T> void onSuccess(T result) {
            Agent plyr = (Agent) result;
            if (plyr.getSymbol() == 'X') {
                Toast.makeText(requireContext(), "Your turn", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "Computer's turn", Toast.LENGTH_SHORT).show();
            }
            updateBoard(mainViewModel.getBoard());
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainViewModel = ((FragmentChanger) requireActivity()).getViewModel(MainViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_play, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.inflateMenu(R.menu.menu);
        Menu menu = toolbar.getMenu();

        MenuItem signOut = menu.findItem(R.id.sign_out);
        signOut.setOnMenuItemClickListener(item -> {
            ((Authentication) requireActivity()).signOut();
            return true;
        });


        final GridLayout gameBoardView = view.findViewById(R.id.game_board);
        mainViewModel.startSinglePlayerGame(tc);

        // Get a reference to the reset button
        Button resetButton = view.findViewById(R.id.reset_button);

        // Attach a View.OnClickListener to the reset button
        resetButton.setOnClickListener(view1 -> mainViewModel.resetBoard(tc));

        // Attach a View.OnClickListener to each button on the game board
        for (int i = 0; i < gameBoardView.getChildCount(); i++) {
            final Button button = (Button) gameBoardView.getChildAt(i);
            button.setOnClickListener(view12 -> {
                Board board = mainViewModel.getBoard();
                // Get the row and column indices of the button that was clicked
                int row = gameBoardView.indexOfChild(view12) / board.getSize();
                int col = gameBoardView.indexOfChild(view12) % board.getSize();

                Agent agent = mainViewModel.getCurrentPlayer();
                board = agent.move(board,row,col);
                Log.d("DEBUG", agent.getSymbol() + " class:" + agent.getClass().getName());
                updateBoard(board);
                if(!verifyGameCondition(agent,board)) {

                    agent = mainViewModel.next();
                    board = mainViewModel.getBoard();
                    if (agent instanceof Computer) {
                        Log.d("DEBUG", agent.getSymbol() + " class:" + agent.getClass().getName());
                        board = agent.move(board, 0, 0);
                        if (verifyGameCondition(agent, board))
                            return;
                        updateBoard(board);
                        mainViewModel.next();
                    }
                }

            });
        }


    }
    //update text on all buttons based on values from board
    private void updateBoard(Board board) {
        final GridLayout gameBoardView = this.requireView().findViewById(R.id.game_board);

        for (int i = 0; i < gameBoardView.getChildCount(); i++) {
            Button button = (Button) gameBoardView.getChildAt(i);
            int row = gameBoardView.indexOfChild(button) / board.getSize();
            int col = gameBoardView.indexOfChild(button) % board.getSize();
            button.setText(String.valueOf(board.getBoard()[row][col]));
            //disable button if it has a symbol
            if(board.getBoard()[row][col] != ' ') {
                button.setEnabled(false);
            } else {
                button.setEnabled(true);
            }
        }
    }


    //disable all buttons
    private void disableButtons() {
        final GridLayout gameBoardView = this.requireView().findViewById(R.id.game_board);

        for (int i = 0; i < gameBoardView.getChildCount(); i++) {
            Button button = (Button) gameBoardView.getChildAt(i);
            button.setEnabled(false);
        }
    }

    private boolean verifyGameCondition(Agent agent, @NonNull Board board) {
        if(!board.getWinnerToast(agent).equals("Continue playing")) {
            Toast.makeText(requireContext(),board.getWinnerToast(agent),Toast.LENGTH_SHORT).show();
            updateBoard(board);
            disableButtons();
            return true;
        }
        return false;
    }
}