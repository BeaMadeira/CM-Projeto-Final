package com.cm.projetoFinal.ui.main;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cm.projetoFinal.MainViewModel;
import com.cm.projetoFinal.R;
import com.cm.projetoFinal.ui.main.interfaces.Authentication;
import com.cm.projetoFinal.ui.main.interfaces.FragmentChanger;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class HighScoresFragment extends Fragment {

    private MainViewModel mainViewModel;
    private RecyclerView recyclerView;
    private ScoreboardAdapter adapter;
    private List<User> users;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainViewModel = ((FragmentChanger) requireActivity()).getViewModel(MainViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_high_scores, container, false);
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
        users = new ArrayList<>();
        adapter = new ScoreboardAdapter(users);
        recyclerView = view.findViewById(R.id.scoreRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        adapter = new ScoreboardAdapter(users);
        recyclerView.setAdapter(adapter);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error != null){
                            return;
                        }
                        users.clear();
                        for(QueryDocumentSnapshot doc : value){
                            User user = doc.toObject(User.class);
                            users.add(user);
                        }
                        Collections.sort(users, new Comparator<User>() {
                            @Override
                            public int compare(User o1, User o2) {
                                return Integer.compare(o2.getScore(), o1.getScore());
                            }
                        });
                        adapter.notifyDataSetChanged();
                    }
                });

    }
}

class ScoreboardAdapter extends RecyclerView.Adapter<ScoreboardAdapter.ViewHolder> {

    private List<User> users;

    public ScoreboardAdapter(List<User> users) {
        this.users = users;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User user = users.get(position);
        holder.bind(user,position);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView nameTextView;
        private TextView scoreTextView;

        private TextView lossTextView;

        Drawable[] throphy = new Drawable[3];
        public ViewHolder(View itemView) {
            super(itemView);

            throphy[0] = ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.trophy_award_gold, null);
            throphy[1] = ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.trophy_award_silver, null);
            throphy[2] = ResourcesCompat.getDrawable(itemView.getResources(), R.drawable.trophy_award_bronze, null);

            nameTextView = itemView.findViewById(R.id.username);
            scoreTextView = itemView.findViewById(R.id.content_preview);
            lossTextView = itemView.findViewById(R.id.sub_content_preview);
        }

        public void bind(User user, int position) {
            if(position == 0)
                ((ImageView) itemView.findViewById(R.id.imageView)).setImageDrawable(throphy[0]);
            else if(position == 1)
                ((ImageView) itemView.findViewById(R.id.imageView)).setImageDrawable(throphy[1]);
            else if(position == 2)
                ((ImageView) itemView.findViewById(R.id.imageView)).setImageDrawable(throphy[2]);
            nameTextView.setText("Username: "+user.getUsername());
            scoreTextView.setText("Score: " + String.valueOf(user.getScore()));
            lossTextView.setText("Losses: " + String.valueOf(user.getLoss()));
        }
    }
}
class User {
    private String username;
    private int win;
    private int loss;
    private int draw;
    private int score;

    public User(String name, int win, int loss, int draw) {
        this.username = name;
        this.loss = loss;
        this.score = win*2 + draw;
    }
    public User()  {}
    public String getUsername() {
        return username;
    }
    public void setUsername(String name) {
        this.username = name;
    }
    public int getScore() {
        this.score = win*2 + draw;
        return win*2 + draw;
    }

    public int getWin() {
        return win;
    }

    public void setWin(int win) {
        this.win = win;
    }

    public int getLoss() {
        return loss;
    }

    public void setLoss(int loss) {
        this.loss = loss;
    }

    public int getDraw() {
        return draw;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return getWin() == user.getWin() && getLoss() == user.getLoss() && getDraw() == user.getDraw() && getScore() == user.getScore() && Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, getWin(), getLoss(), getDraw(), getScore());
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", win=" + win +
                ", loss=" + loss +
                ", draw=" + draw +
                ", score=" + score +
                '}';
    }
}