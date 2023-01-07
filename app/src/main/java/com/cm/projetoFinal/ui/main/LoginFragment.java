package com.cm.projetoFinal.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.cm.projetoFinal.MainViewModel;
import com.cm.projetoFinal.R;
import com.cm.projetoFinal.ui.main.interfaces.Authentication;
import com.cm.projetoFinal.ui.main.interfaces.FragmentChanger;

public class LoginFragment extends Fragment {
    private MainViewModel mainViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainViewModel = ((FragmentChanger) requireActivity()).getViewModel(MainViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        EditText email = view.findViewById(R.id.email_login);
        EditText password = view.findViewById(R.id.password_login);
        Button login = view.findViewById(R.id.login);
        Button register = view.findViewById(R.id.go_to_register);

        toolbar.setTitle(R.string.app_name);

        login.setOnClickListener(v -> {
            if (email.getText().toString().equals("")) {
                Toast.makeText(requireActivity(), "Email Empty", Toast.LENGTH_SHORT).show();
            }
            if (password.getText().toString().equals("")) {
                Toast.makeText(requireActivity(), "Password Empty", Toast.LENGTH_SHORT).show();
            }
            else {
                ((Authentication) requireActivity()).signIn(email.getText().toString(), password.getText().toString());
            }
        });

        register.setOnClickListener(v -> ((FragmentChanger) requireActivity()).replaceFragment(RegisterFragment.class, true));
    }
}
