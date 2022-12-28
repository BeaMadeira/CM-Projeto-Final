package com.cm.projetoFinal.tictactoe;

import androidx.annotation.NonNull;

import java.util.Arrays;
import java.util.Objects;

public class Board {
    private char[][] board;
    private int size;

    public Board(int size) {
        setSize(size);
    }

    //This constructor is a copy constructor
    public Board(@NonNull Board board) {
        this.size = board.getSize();
        this.board = new char[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                this.board[i][j] = board.getBoard()[i][j];
            }
        }
    }

    public void setBoard(char[][] board) {
        this.board = board;
    }

    public char[][] getBoard() {
        return board;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
        this.board = new char[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = ' ';
            }
        }
    }

    public boolean isFull() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board[i][j] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isWinner(char symbol) {
        //check rows
        for (int i = 0; i < size; i++) {
            boolean rowWin = true;
            for (int j = 0; j < size; j++) {
                if (board[i][j] != symbol) {
                    rowWin = false;
                    break;
                }
            }
            if (rowWin) {
                return true;
            }
        }
        //check columns
        for (int i = 0; i < size; i++) {
            boolean colWin = true;
            for (int j = 0; j < size; j++) {
                if (board[j][i] != symbol) {
                    colWin = false;
                    break;
                }
            }
            if (colWin) {
                return true;
            }
        }
        //check diagonals
        boolean diagWin = true;
        for (int i = 0; i < size; i++) {
            if (board[i][i] != symbol) {
                diagWin = false;
                break;
            }
        }
        if (diagWin) {
            return true;
        }
        diagWin = true;
        for (int i = 0; i < size; i++) {
            if (board[i][size - i - 1] != symbol) {
                diagWin = false;
                break;
            }
        }
        if (diagWin) {
            return true;
        }
        return false;
    }

    //apply a move at x,y, returns true if move was valid, false otherwise
    public boolean move(char symbol, int x, int y) {
        if (board[x][y] == ' ') {
            board[x][y] = symbol;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Board)) return false;
        Board board1 = (Board) o;
        return getSize() == board1.getSize() && Arrays.equals(getBoard(), board1.getBoard());
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(getSize());
        result = 31 * result + Arrays.hashCode(getBoard());
        return result;
    }

    //to string should display the board with [ ] around each row and | between each column
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append("[");
            for (int j = 0; j < size; j++) {
                sb.append(board[i][j]);
                if (j != size - 1) {
                    sb.append("|");
                }
            }
            sb.append("]");
            if (i != size - 1) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }

}
