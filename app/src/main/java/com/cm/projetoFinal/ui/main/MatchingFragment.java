package com.cm.projetoFinal.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.cm.projetoFinal.MainViewModel;
import com.cm.projetoFinal.R;
import com.cm.projetoFinal.ui.main.interfaces.Authentication;
import com.cm.projetoFinal.ui.main.interfaces.FragmentChanger;
import com.cm.projetoFinal.ui.main.interfaces.MQTTInterface;
import com.google.firebase.auth.FirebaseUser;

//TODO Send message with users uid to the topic tiktaktoe/room/enter
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

        MenuItem signOut = menu.findItem(R.id.sign_out);
        signOut.setOnMenuItemClickListener(item -> {
            ((Authentication) requireActivity()).signOut();
            return true;
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser user = ((Authentication) requireActivity()).getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
            ((MQTTInterface) requireActivity()).publish(getResources().getString(R.string.tiktaktoe_room_enter), uid);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        FirebaseUser user = ((Authentication) requireActivity()).getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
            ((MQTTInterface) requireActivity()).publish(getResources().getString(R.string.tiktaktoe_room_leave), uid);
        }
    }
}
