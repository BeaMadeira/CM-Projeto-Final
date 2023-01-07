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
import com.cm.projetoFinal.tictactoe.Position;
import com.cm.projetoFinal.ui.main.interfaces.Authentication;
import com.cm.projetoFinal.ui.main.interfaces.FragmentChanger;
import com.cm.projetoFinal.ui.main.interfaces.MQTTInterface;
import com.cm.projetoFinal.ui.main.interfaces.TaskCallback;

public class MultiPlayerFragment extends Fragment {
    private MainViewModel mainViewModel;
    private final TaskCallback tc = new TaskCallback() {
        @Override
        public <T> void onSuccess(T result) {
            Agent player = (Agent) result;
            if (player.getSymbol() == 'X') {
                Toast.makeText(requireContext(), "Your turn", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "Opponents' turn", Toast.LENGTH_SHORT).show();
            }
            updateBoard(mainViewModel.getBoard());
        }
    };

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

        MenuItem signOut = menu.findItem(R.id.sign_out);
        signOut.setOnMenuItemClickListener(item -> {
            ((Authentication) requireActivity()).signOut();
            return true;
        });

        final GridLayout gameBoardView = view.findViewById(R.id.game_board_multiplayer);
        mainViewModel.startMultiPlayerGame(tc);

        if (mainViewModel.getCurrentPlayerInt() != 0) {
            disableButtons();
        }

        // Attach a View.OnClickListener to each button on the game board
        for (int i = 0; i < gameBoardView.getChildCount(); i++) {
            final Button button = (Button) gameBoardView.getChildAt(i);
            button.setOnClickListener(view12 -> {
                // Get Board
                Board board = mainViewModel.getBoard();

                // Get the row and column indices of the button that was clicked
                int row = gameBoardView.indexOfChild(view12) / board.getSize();
                int col = gameBoardView.indexOfChild(view12) % board.getSize();

                // Get Current Player
                Agent agent = mainViewModel.getCurrentPlayer();

                // Put player's symbol on (row, col) position
                board = agent.move(board, row, col);

                // Change Current Player
                //mainViewModel.setCurrentPlayer(mainViewModel.getCurrentPlayerInt() == 0 ? 1 : 0);

                // Update Board
                updateBoard(board);

                // After player's move disable all buttons
                disableButtons();

                // Send move to opponent
                ((MQTTInterface) requireActivity()).publish(mainViewModel.getOponentTopic(), new Position(row, col));

                if(!verifyGameCondition(agent, board)) {
                    agent = mainViewModel.next();
                    board = mainViewModel.getBoard();
                }
            });
        }
    }

    // Update text on all buttons based on values from board
    public void updateBoard(Board board) {
        final GridLayout gameBoardView = this.requireView().findViewById(R.id.game_board_multiplayer);

        for (int i = 0; i < gameBoardView.getChildCount(); i++) {
            Button button = (Button) gameBoardView.getChildAt(i);
            int row = gameBoardView.indexOfChild(button) / board.getSize();
            int col = gameBoardView.indexOfChild(button) % board.getSize();
            button.setText(String.valueOf(board.getBoard()[row][col]));
            // Disable button if it has a symbol
            button.setEnabled(board.getBoard()[row][col] == ' ');
        }
    }

    // Disable all buttons
    private void disableButtons() {
        final GridLayout gameBoardView = this.requireView().findViewById(R.id.game_board_multiplayer);
        for (int i = 0; i < gameBoardView.getChildCount(); i++) {
            Button button = (Button) gameBoardView.getChildAt(i);
            button.setEnabled(false);
        }
    }

    // Enable all free buttons
    public void enableButtons() {
        final GridLayout gameBoardView = this.requireView().findViewById(R.id.game_board_multiplayer);
        for (int i = 0; i < gameBoardView.getChildCount(); i++) {
            Button button = (Button) gameBoardView.getChildAt(i);
            String text = button.getText().toString();
            boolean enable = text.equals(" ");
            button.setEnabled(enable);
        }
    }

    // Verify if it is Game Over
    private boolean verifyGameCondition(Agent agent, @NonNull Board board) {
        if(!board.getWinnerToast(agent).equals("Continue playing")) {
            Toast.makeText(requireContext(),board.getWinnerToast(agent),Toast.LENGTH_SHORT).show();
            updateBoard(board);
            disableButtons();
            mainViewModel.setPlaying(false);
            return true;
        }
        mainViewModel.setPlaying(true);
        return false;
    }
}