package com.cm.projetoFinal.ui.main;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.cm.projetoFinal.MainViewModel;
import com.cm.projetoFinal.R;
import com.cm.projetoFinal.ui.main.interfaces.Authentication;
import com.cm.projetoFinal.ui.main.interfaces.FragmentChanger;

import java.util.ArrayList;
import java.util.List;

public class RegisterFragment extends Fragment {
    private MainViewModel mainViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainViewModel = ((FragmentChanger) requireActivity()).getViewModel(MainViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        EditText email = view.findViewById(R.id.email_register);
        EditText password = view.findViewById(R.id.password_register);
        Button register = view.findViewById(R.id.register);

        toolbar.setTitle(R.string.app_name);
        toolbar.inflateMenu(R.menu.menu);
        Menu menu = toolbar.getMenu();

        register.setOnClickListener(v -> ((Authentication) requireActivity()).createAccount(email.getText().toString(), password.getText().toString()));
    }
}