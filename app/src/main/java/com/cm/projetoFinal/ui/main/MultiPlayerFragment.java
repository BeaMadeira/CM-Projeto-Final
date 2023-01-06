package com.cm.projetoFinal.ui.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;

import com.cm.projetoFinal.MainViewModel;
import com.cm.projetoFinal.R;
import com.cm.projetoFinal.tictactoe.Agent;
import com.cm.projetoFinal.tictactoe.Board;
import com.cm.projetoFinal.tictactoe.Computer;
import com.cm.projetoFinal.ui.main.interfaces.FragmentChanger;

public class MultiPlayerFragment extends Fragment {
    private MainViewModel mainViewModel;

    public MultiPlayerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainViewModel = ((FragmentChanger) requireActivity()).getViewModel(MainViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_multiplayer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.inflateMenu(R.menu.menu);
        Menu menu = toolbar.getMenu();

        MenuItem settings = menu.findItem(R.id.settings);
        settings.setOnMenuItemClickListener(item -> {
            ((FragmentChanger) requireActivity()).replaceFragment(SettingsFragment.class, true);
            return true;
        });

        final GridLayout gameBoardView = view.findViewById(R.id.game_board_multiplayer);

        // Attach a View.OnClickListener to each button on the game board
        for (int i = 0; i < gameBoardView.getChildCount(); i++) {
            final Button button = (Button) gameBoardView.getChildAt(i);
            button.setOnClickListener(view12 -> {
                Board board = mainViewModel.getBoard();
                // Get the row and column indices of the button that was clicked
                int row = gameBoardView.indexOfChild(view12) / board.getSize();
                int col = gameBoardView.indexOfChild(view12) % board.getSize();

                Agent agent = mainViewModel.getCurrentPlayer();
                board = agent.move(board, row, col);
                updateBoard(board);
                if(!verifyGameCondition(agent, board)) {

                    agent = mainViewModel.next();
                    board = mainViewModel.getBoard();
                    if (agent instanceof Computer) {
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

    // Update text on all buttons based on values from board
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