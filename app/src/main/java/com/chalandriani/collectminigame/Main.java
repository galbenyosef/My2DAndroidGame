package com.chalandriani.collectminigame;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.ArrayList;


public class Main extends Activity {

    public static Resources resources;
    public static AnimationManager animator;
    public static FragmentManager fragmentizer;
    public static GameLoop gameLoop;
    public static Player player;
    public static ArrayList<Player> players;

    long backPressedStartTime;
    boolean backPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        backPressed=false;
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.fragment_main);

        FirebaseHandler.initialize(getApplicationContext());
        resources = getResources();
        animator = new AnimationManager(getAssets());
        fragmentizer = getFragmentManager();
        players = new ArrayList<>();

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
                    gameLoop.setRunning(false);
                    FirebaseHandler.player_reference.setValue(null);
                }
                finish();
            }
        }
    }
}