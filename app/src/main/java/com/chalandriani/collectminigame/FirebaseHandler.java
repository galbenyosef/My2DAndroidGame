package com.chalandriani.collectminigame;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Created by Gal on 02/10/2017.
 */

public class FirebaseHandler {

    public static FirebaseDatabase database ;
    public static FirebaseAuth authentication;
    public static DatabaseReference player_reference,players_reference;

    public static void initialize(Context context){

        FirebaseApp.initializeApp(context);
        database  = FirebaseDatabase.getInstance();
        database.setPersistenceEnabled(true);
        authentication = FirebaseAuth.getInstance();
        players_reference = database.getReference("players");

    }

    public static class EventListeners{

        public static ChildEventListener playerUpdaterv2 = new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                GenericTypeIndicator<Player> player_db = new GenericTypeIndicator<Player>(){};
                final Player newPlayer = dataSnapshot.getValue(player_db);
                if (!newPlayer.getPlayerName().equalsIgnoreCase(Main.player.getPlayerName())) {
                    Main.players.add(newPlayer);
                    Log.d("H", "added " + newPlayer.getPlayerName());
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                handle(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d("H","Child Removed");

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.d("H","Child Moved");

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("H","Child Canceled");

            }
        };

        static void handle(DataSnapshot ds) {
            final DataSnapshot dataSnapshot = ds;
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    GenericTypeIndicator<Player> player_db = new GenericTypeIndicator<Player>(){};
                    final Player update = dataSnapshot.getValue(player_db);
                    if (!update.getPlayerName().equalsIgnoreCase(Main.player.getPlayerName())) {
                        for (int i = 0 ; i < Main.players.size() ; i++) {
                            Player current = Main.players.get(i);
                            if (update.getPlayerName().equalsIgnoreCase(current.getPlayerName())){
                                current.setX(update.getX());
                                current.setY(update.getY());
                                current.setSpeed(update.getSpeed());
                                current.setSlashing(update.isSlashing());
                                current.setDirection(update.getDirection());
                                current.setWalking(update.isWalking());
                                Log.d("H","Updated "+current.getPlayerName());
                            }
                        }
                    }
                }
            });
        }
    }
}
