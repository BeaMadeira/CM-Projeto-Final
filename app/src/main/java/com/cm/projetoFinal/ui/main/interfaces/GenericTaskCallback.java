package com.cm.projetoFinal.ui.main.interfaces;

import java.util.List;

public interface GenericTaskCallback<T>{
    T onComplete(List<T> result);
}
