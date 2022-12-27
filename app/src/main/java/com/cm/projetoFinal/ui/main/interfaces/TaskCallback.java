package com.cm.projetoFinal.ui.main.interfaces;

import java.util.List;

public interface TaskCallback {
    <T> void onCompleted(T result);

}