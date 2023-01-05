package com.cm.projetoFinal.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.cm.projetoFinal.MainViewModel;
import com.cm.projetoFinal.R;
import com.cm.projetoFinal.ui.main.interfaces.FragmentChanger;
import com.cm.projetoFinal.ui.main.interfaces.MQTTInterface;

//TODO Send message with users uid to the topic tiktaktoe/room
//TODO Wait for message sent to topic tiktaktoe/<uid> with the <uid> of the other player
//TODO Change to Multiplayer Fragment

public class MatchingFragment extends Fragment {
    private MainViewModel mainViewModel;

    public MatchingFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainViewModel = ((FragmentChanger) requireActivity()).getViewModel(MainViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_matching, container, false);
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

        //TODO Get user profile uid
        //String uid = mainViewModel.getProfile().getUid().toString();
        String uid = "user1";
        //Toast.makeText(requireActivity().getApplicationContext(), uid, Toast.LENGTH_SHORT).show();
        // Publish user id to the topic tiktaktoe/room
        ((MQTTInterface) requireActivity()).publish(getResources().getString(R.string.tiktaktoe_room), uid);
    }
}
