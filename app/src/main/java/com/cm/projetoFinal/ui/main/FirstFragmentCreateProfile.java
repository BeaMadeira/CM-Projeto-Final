package com.cm.projetoFinal.ui.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.cm.projetoFinal.R;
import com.cm.projetoFinal.ui.main.interfaces.FragmentChanger;

import java.util.ArrayList;
import java.util.List;

public class FirstFragmentCreateProfile extends Fragment {

    public FirstFragmentCreateProfile() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first_create_profile, container, false);
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
        cards.add(view.findViewById(R.id.cardAccount));
        cards.get(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((FragmentChanger) requireActivity()).replaceFragment(AccountFragment.class, true);

            }
        });


    }

}