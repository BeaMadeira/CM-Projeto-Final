package com.cm.projetoFinal.ui.main.interfaces;

import com.google.firebase.auth.FirebaseUser;

public interface Authentication {
    void createAccount(String email, String password);

    void sendEmailVerification();

    void signIn(String email, String password);

    void reload();

    void updateUI(FirebaseUser user);

    void checkSignIn();

    boolean isSignIn();

    FirebaseUser getCurrentUser();
}