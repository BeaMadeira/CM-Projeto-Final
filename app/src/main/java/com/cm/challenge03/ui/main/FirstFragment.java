package com.cm.challenge03.ui.main;

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

import com.cm.challenge03.MainViewModel;
import com.cm.challenge03.R;
import com.cm.challenge03.ui.main.interfaces.FragmentChanger;

import java.util.ArrayList;
import java.util.List;

// TODO: Add charts and plots
// TODO: Change plot type?
// TODO: Select time interval?
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

        MenuItem settings = menu.findItem(R.id.settings);
        settings.setOnMenuItemClickListener(item -> {
            ((FragmentChanger) requireActivity()).replaceFragment(SettingsFragment.class, true);
            return true;
        });

        List<CardView> cards = new ArrayList<>();
        cards.add(view.findViewById(R.id.cardChanges));
        cards.get(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((FragmentChanger) requireActivity()).replaceFragment(LineGraphFragment.class, true);
                Log.d("DEBUG","CLICKED1");
            }
        });
        cards.add(view.findViewById(R.id.cardAverages));
        cards.get(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("DEBUG","CLICKED2");
            }
        });
        cards.add(view.findViewById(R.id.cardTotals));
        cards.get(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("DEBUG","CLICKED3");
            }
        });
        cards.add(view.findViewById(R.id.cardValues));
        cards.get(3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("DEBUG","CLICKED4");
            }
        });

    }
}