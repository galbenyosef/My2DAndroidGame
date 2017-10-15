package com.chalandriani.collectminigame;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;


public class Main extends Activity {

    public static Resources resources;
    public static AnimationManagerWalkingSealed animator;
    public static FragmentManager fragmentizer;
    public static GameLoop gameLoop;
    public static Player player;
    public static HashMap<String,Player> players;

    long backPressedStartTime;
    boolean backPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        backPressed=false;
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.fragment_main);

        resources = getResources();
        animator = new AnimationManagerWalkingSealed(getAssets());
        fragmentizer = getFragmentManager();
        players = new HashMap<>();

        FirebaseHandler.initialize(getApplicationContext());
        FragmentHandler.switchFragment(R.id.fragment_container, new LoginFragment());
    }

    @Override
    public void onBackPressed() {
        if (!backPressed) {
            backPressedStartTime = System.nanoTime();
            Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();
            backPressed=true;
        }
        else {
            if (((System.nanoTime() - backPressedStartTime)/1000000) > 2000)
                backPressed=false;
            else{
                if (FirebaseHandler.player_reference != null) {
                    FirebaseHandler.players_reference.removeEventListener(FirebaseHandler.EventListeners.playerUpdaterv2);
                    players.clear();
                    gameLoop.setRunning(false);
                    FirebaseHandler.player_reference.setValue(null);
                    FirebaseHandler.authentication.signOut();
                  //  FirebaseHandler.database.goOffline();
                }
                finish();
            }
        }
    }


}