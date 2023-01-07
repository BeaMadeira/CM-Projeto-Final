package com.cm.projetoFinal.ui.main.interfaces;

import com.google.firebase.auth.FirebaseUser;

import java.util.Map;

public interface Authentication {
    void createAccount(String email, String password);

    void createAccount(String email, String password, Map<String, Object> data);

    void sendEmailVerification();

    void signIn(String email, String password);

    void signOut();

    void reload();

    void updateUI(FirebaseUser user);

    void checkSignIn();

    boolean isSignIn();

    FirebaseUser getCurrentUser();
}