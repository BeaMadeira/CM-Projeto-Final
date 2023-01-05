package com.cm.projetoFinal.tictactoe;

public class Computer implements Agent {
    private char symbol;


    public Computer(char symbol) {
        this.symbol = symbol;
    }

    @Override
    public Board move(Board board, int x, int y) {
        //make a random computer move
        char[][] boardArray = board.getBoard();
        //check if board is full
        if (board.isFull()) {
            return null;
        } else {
            //check if there is a winning move
            for (int i = 0; i < board.getSize(); i++) {
                for (int j = 0; j < board.getSize(); j++) {
                    if (boardArray[i][j] == ' ') {
                        boardArray[i][j] = symbol;
                        if (board.isWinner(symbol)) {
                            board.setBoard(boardArray);
                            return board;
                        } else {
                            boardArray[i][j] = ' ';
                        }
                    }
                }
            }
            //check if there is a blocking move
            for (int i = 0; i < board.getSize(); i++) {
                for (int j = 0; j < board.getSize(); j++) {
                    if (boardArray[i][j] == ' ') {
                        boardArray[i][j] = getOpponentSymbol();
                        if (board.isWinner(getOpponentSymbol())) {
                            boardArray[i][j] = symbol;
                            board.setBoard(boardArray);
                            return board;
                        } else {
                            boardArray[i][j] = ' ';
                        }
                    }
                }
            }
            //check if there is a center move
            if (boardArray[board.getSize() / 2][board.getSize() / 2] == ' ') {
                boardArray[board.getSize() / 2][board.getSize() / 2] = symbol;
                board.setBoard(boardArray);
                return board;
            }
            //check if there is a corner move
            if (boardArray[0][0] == ' ') {
                boardArray[0][0] = symbol;
                board.setBoard(boardArray);
                return board;
            }
            if (boardArray[0][board.getSize() - 1] == ' ') {
                boardArray[0][board.getSize() - 1] = symbol;
                board.setBoard(boardArray);
                return board;
            }
            if (boardArray[board.getSize() - 1][0] == ' ') {
                boardArray[board.getSize() - 1][0] = symbol;
                board.setBoard(boardArray);
                return board;
            }
            if (boardArray[board.getSize() - 1][board.getSize() - 1] == ' ') {
                boardArray[board.getSize() - 1][board.getSize() - 1] = symbol;
                board.setBoard(boardArray);
                return board;
            }
            //check if there is a side move
            for (int i = 0; i < board.getSize(); i++) {
                if (boardArray[0][i] == ' ') {
                    boardArray[0][i] = symbol;
                    board.setBoard(boardArray);
                    return board;
                }
                if (boardArray[i][0] == ' ') {
                    boardArray[i][0] = symbol;
                    board.setBoard(boardArray);
                    return board;
                }
                if (boardArray[board.getSize() - 1][i] == ' ') {
                    boardArray[board.getSize() - 1][i] = symbol;
                    board.setBoard(boardArray);
                    return board;
                }
                if (boardArray[i][board.getSize() - 1] == ' ') {
                    boardArray[i][board.getSize() - 1] = symbol;
                    board.setBoard(boardArray);
                    return board;
                }
            }
        }
        return null;
    }

    public char getSymbol() {
        return symbol;
    }

    public void setSymbol(char symbol) {
        //check if symbol is matching X or O
        if (symbol == 'X' || symbol == 'O') {
            this.symbol = symbol;
        } else {
            throw new IllegalArgumentException("Symbol must be X or O");
        }
    }

    @Override
    public char getOpponentSymbol() {
        if (symbol == 'X') {
            return 'O';
        } else {
            return 'X';
        }
    }


}
