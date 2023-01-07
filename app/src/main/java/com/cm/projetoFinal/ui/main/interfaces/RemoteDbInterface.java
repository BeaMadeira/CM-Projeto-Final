package com.cm.projetoFinal.ui.main.interfaces;

public interface RemoteDbInterface {
    void addWin();

    void addLoss();

    void addDraw();

    void getUsername(TaskCallback taskCallback);
}
