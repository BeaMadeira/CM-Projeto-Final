package com.cm.challenge03.ui.main.interfaces;

import java.util.List;

public interface GenericTaskCallback<T>{
    T onComplete(List<T> result);
}
