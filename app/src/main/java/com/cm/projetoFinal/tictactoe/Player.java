package com.cm.projetoFinal.tictactoe;

public class Player implements Agent {
    private char symbol;

    public Player(char symbol) {
        this.symbol = symbol;
    }

    public char getSymbol() {
        return symbol;
    }

    public void setSymbol(char symbol) {
        //check if symbol is valid
        if (symbol == 'X' || symbol == 'O') {
            this.symbol = symbol;
        } else throw new IllegalArgumentException("Symbol must be X or O");
    }

    @Override
    public char getOpponentSymbol() {
        if (symbol == 'X') {
            return 'O';
        } else {
            return 'X';
        }
    }

    @Override
    public Board move(Board board, int x, int y) {
        //save board in a variable
        char[][] boardArray = board.getBoard();
        //called when player executes a move, needs to check if position is valid
        if (boardArray[x][y] == ' ') {
            boardArray[x][y] = symbol;
            board.setBoard(boardArray);
            return board;
        } else {
            return null;
        }
    }
}
