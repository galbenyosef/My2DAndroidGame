package com.chalandriani.collectminigame;

import android.content.Context;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Gal on 02/10/2017.
 */

public class FirebaseHandler {

    public static FirebaseDatabase database ;
    public static FirebaseAuth authentication;
    public static DatabaseReference player_reference,players_reference;

    public static void setup(Context context){
        FirebaseApp.initializeApp(context);
        database  = FirebaseDatabase.getInstance();
        authentication = FirebaseAuth.getInstance();
        players_reference = database.getReference("players");
    }

    public static class Listeners {

        ValueEventListener playerUpdater = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<Map<String,Player>> player_db = new GenericTypeIndicator<Map<String,Player>>(){};
                if (dataSnapshot.getValue(player_db) != null) {
                    ArrayList<Player> players_updates = new ArrayList<>(dataSnapshot.getValue(player_db).values());
                    if (players_updates.size() == 0) return;
                    for (int i = 0; i < players_updates.size(); i++) {
                        Player currentPlayer = players_updates.get(i);
                        if (!currentPlayer.getPlayerName().equals(Main.player.getPlayerName())) {
                            player2.setX(currentPlayer.getX());
                            player2.setY(currentPlayer.getY());
                            player2.setDirection(currentPlayer.getDirection());
                            player2.setWidth(currentPlayer.getWidth());
                            player2.setHeight(currentPlayer.getHeight());
                            player2.setWalking(currentPlayer.isWalking());
                            player2.setSlashing(currentPlayer.isSlashing());
                            player2.setSpeed(currentPlayer.getSpeed());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        };

        ValueEventListener playerReader = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<Map<String,Player>> player_db = new GenericTypeIndicator<Map<String,Player>>(){};
                if (dataSnapshot.getValue(player_db) != null) {
                    ArrayList<Player> players_updates = new ArrayList<>(dataSnapshot.getValue(player_db).values());
                    if (players_updates.size() == 0) return;
                    for (int i = 0; i < players_updates.size(); i++) {
                        Player currentPlayer = players_updates.get(i);
                        if (!currentPlayer.getPlayerName().equals(Main.player.getPlayerName())) {
                            player2.setX(currentPlayer.getX());
                            player2.setY(currentPlayer.getY());
                            player2.setDirection(currentPlayer.getDirection());
                            player2.setWidth(currentPlayer.getWidth());
                            player2.setHeight(currentPlayer.getHeight());
                            player2.setWalking(currentPlayer.isWalking());
                            player2.setSlashing(currentPlayer.isSlashing());
                            player2.setSpeed(currentPlayer.getSpeed());
                        }
//                    else {
//                        player.setX(currentPlayer.getX());
//                        player.setY(currentPlayer.getY());
//                        player.setDirection(currentPlayer.getDirection());
//                        player.setWidth(currentPlayer.getWidth());
//                        player.setHeight(currentPlayer.getHeight());
//                        player.setWalking(currentPlayer.isWalking());
//                        player.setSlashing(currentPlayer.isSlashing());
//                        player.setSpeed(currentPlayer.getSpeed());
//                    }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

    }

}
