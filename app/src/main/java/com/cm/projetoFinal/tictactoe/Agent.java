package com.cm.projetoFinal.tictactoe;

public interface Agent {
    Board move(Board board, int x, int y);

    char getSymbol();

    void setSymbol(char symbol);

    char getOpponentSymbol();
}