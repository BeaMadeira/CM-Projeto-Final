package com.cm.projetoFinal.tictactoe;

public interface Agent {
    public Board move(Board board, int x, int y);
    public char getSymbol();
    public void setSymbol(char symbol);
    public char getOpponentSymbol();

}
