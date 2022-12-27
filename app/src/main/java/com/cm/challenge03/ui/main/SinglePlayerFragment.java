package com.cm.challenge03.ui.main;

import android.os.Bundle;
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

import com.cm.challenge03.MainViewModel;
import com.cm.challenge03.R;
import com.cm.challenge03.ui.main.interfaces.FragmentChanger;

// TODO: Add charts and plots
// TODO: Change plot type?
// TODO: Select time interval?
public class SinglePlayerFragment extends Fragment {
    private MainViewModel mainViewModel;

    // The game board is represented as a 2D array, with a value of 0 for an empty space,
    // 1 for a space occupied by the first player, and 2 for a space occupied by the second player
    int[][] gameBoard = new int[3][3];

    // This variable keeps track of which player's turn it is
    int currentPlayer = 1;

    // This function is called when a player makes a move on the game board
    public void makeMove(int player, int x, int y) {
        gameBoard[x][y] = player;
    }

    // This function checks the game board to see if the game has been won by a player
    public int checkForWin() {
        // Check for horizontal wins
        for (int i = 0; i < 3; i++) {
            if (gameBoard[i][0] == gameBoard[i][1] && gameBoard[i][1] == gameBoard[i][2] && gameBoard[i][0] != 0) {
                return gameBoard[i][0];
            }
        }

        // Check for vertical wins
        for (int i = 0; i < 3; i++) {
            if (gameBoard[0][i] == gameBoard[1][i] && gameBoard[1][i] == gameBoard[2][i] && gameBoard[0][i] != 0) {
                return gameBoard[0][i];
            }
        }

        // Check for diagonal wins
        if (gameBoard[0][0] == gameBoard[1][1] && gameBoard[1][1] == gameBoard[2][2] && gameBoard[0][0] != 0) {
            return gameBoard[0][0];
        }
        if (gameBoard[2][0] == gameBoard[1][1] && gameBoard[1][1] == gameBoard[0][2] && gameBoard[2][0] != 0) {
            return gameBoard[2][0];
        }

        // If there are no wins, return 0
        return 0;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainViewModel = ((FragmentChanger) requireActivity()).getViewModel(MainViewModel.class);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_line_graph, container, false);
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


        final GridLayout gameBoardView = view.findViewById(R.id.game_board);

        // Get a reference to the reset button
        Button resetButton = view.findViewById(R.id.reset_button);

        // Attach a View.OnClickListener to the reset button
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Clear the game board array
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        gameBoard[i][j] = 0;
                    }
                }

                // Reset the text of each button on the game board to an empty string
                for (int i = 0; i < gameBoardView.getChildCount(); i++) {
                    Button button = (Button) gameBoardView.getChildAt(i);
                    button.setText("");
                }

                // Reset the current player to player 1
                currentPlayer = 1;
            }
        });

        // Attach a View.OnClickListener to each button on the game board
        for (int i = 0; i < gameBoardView.getChildCount(); i++) {
            final Button button = (Button) gameBoardView.getChildAt(i);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Get the row and column indices of the button that was clicked
                    int row = gameBoardView.indexOfChild(view) / 3;
                    int col = gameBoardView.indexOfChild(view) % 3;

                    // Make a move on the game board at the specified location
                    makeMove(currentPlayer, row, col);

                    // Set the text of the button to 'X' or 'O' depending on the player number
                    if (gameBoard[row][col] == 1) {
                        button.setText("X");
                    } else {
                        button.setText("O");
                    }

                    // Check for a win
                    int winner = checkForWin();
                    if (winner != 0) {
                        // If there is a win, display a message to the player indicating who won the game
                        Toast.makeText(getContext(), "Player " + winner + " wins!", Toast.LENGTH_SHORT).show();
                    } else {
                        // If there is no win, switch to the other player's turn
                        currentPlayer = currentPlayer == 1 ? 2 : 1;
                    }
                }
            });
        }



    }

}