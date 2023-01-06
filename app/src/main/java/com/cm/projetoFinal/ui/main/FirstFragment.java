package com.cm.projetoFinal.ui.main;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

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

public class FirstFragment extends Fragment {
    private MainViewModel mainViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainViewModel = ((FragmentChanger) requireActivity()).getViewModel(MainViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.inflateMenu(R.menu.menu);
        Menu menu = toolbar.getMenu();

        MenuItem signOut = menu.findItem(R.id.sign_out);
        signOut.setOnMenuItemClickListener(item -> {
            ((Authentication) requireActivity()).signOut();
            return true;
        });

        List<CardView> cards = new ArrayList<>();
        cards.add(view.findViewById(R.id.cardPlay));
        cards.get(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((FragmentChanger) requireActivity()).replaceFragment(SinglePlayerFragment.class, true);
                Log.d("DEBUG","CLICKED1");
            }
        });
        cards.add(view.findViewById(R.id.cardMultiplayer));
        cards.get(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((FragmentChanger) requireActivity()).replaceFragment(MatchingFragment.class, true);
                Log.d("DEBUG","CLICKED2");
            }
        });
        cards.add(view.findViewById(R.id.cardHighscores));
        cards.get(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((FragmentChanger) requireActivity()).replaceFragment(HighScoresFragment.class, true);

                Log.d("DEBUG","CLICKED3");
            }
        });
        cards.add(view.findViewById(R.id.cardAccount));
        cards.get(3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((FragmentChanger) requireActivity()).replaceFragment(AccountFragment.class, true);

                Log.d("DEBUG","CLICKED4");
            }
        });

    }
}