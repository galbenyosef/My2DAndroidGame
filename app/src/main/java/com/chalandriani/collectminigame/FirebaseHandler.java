package com.chalandriani.collectminigame;

import android.content.Context;
import android.content.Intent;
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
import java.util.HashMap;
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
        // database.purgeOutstandingWrites();

        authentication = FirebaseAuth.getInstance();
        players_reference = database.getReference("players");
        players_reference.keepSynced(true);

    }

    public static class EventListeners{

        public static ChildEventListener playerUpdaterv2 = new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                GenericTypeIndicator<Player> player_db = new GenericTypeIndicator<Player>(){};
                final Player newPlayer = dataSnapshot.getValue(player_db);
                if (!newPlayer.getPlayerName().equalsIgnoreCase(Main.player.getPlayerName())) {
                    newPlayer.setAnimation(Main.animator.getCharacter().getWalkAnimation(JoystickView.CENTER));
                    Main.players.put(newPlayer.getPlayerName(),newPlayer);
                    players_reference.child(newPlayer.getPlayerName()).keepSynced(true);
                    players_reference.child(newPlayer.getPlayerName()).addChildEventListener(new ChildEventListener() {

                        final String playerName = newPlayer.getPlayerName();

                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                            final String key = dataSnapshot.getKey();
                            final Object value = dataSnapshot.getValue();

                            switch (key){
                                case "changedDirection":
                                    Main.players.get(playerName).setChangedDirection((boolean)value);
                                    break;
                                case "direction":
                                    Main.players.get(playerName).setDirection(((Long)value).intValue());
                                    break;
                                case "dx":
                                    Main.players.get(playerName).setDx(((Long)value).intValue());
                                    break;
                                case "dy":
                                    Main.players.get(playerName).setDy(((Long)value).intValue());
                                    break;
                                case "hp":
                                    Main.players.get(playerName).setHp(((Long)value).intValue());
                                    break;
                                case "jumping":
                                    Main.players.get(playerName).setJumping((boolean)value);
                                    break;
                                case "rectangle":
                                    final HashMap<String,Long> rect = (HashMap<String,Long>)value;
                                    Log.d("H",value.toString());
                                    Main.players.get(playerName).setRectangle(new Rectangle(rect.get("left").intValue(),rect.get("top").intValue(),rect.get("right").intValue(),rect.get("bottom").intValue()));
                                    break;
                                case "slashing":
                                    Main.players.get(playerName).setSlashing((boolean)value);
                                    break;
                                case "speed":
                                    Main.players.get(playerName).setSpeed(((Long)value).intValue());
                                    break;
                                case "walking":
                                    Main.players.get(playerName).setWalking((boolean)value);
                                    break;
                                case "x":
                                    Main.players.get(playerName).setX(((Long)value).intValue());
                                    break;
                                case "y":
                                    Main.players.get(playerName).setY(((Long)value).intValue());
                                    break;
                            }
                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                //FUCKING NOT WANTED NETWORK CALL :[
               // handle(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<Player> player_db = new GenericTypeIndicator<Player>() {};
                final Player removePlayer = dataSnapshot.getValue(player_db);
                if (Main.players.containsKey(removePlayer.getPlayerName())){
                    Main.players.remove(removePlayer.getPlayerName());
                }
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
                                current.setRectangle(update.getRectangle());
                                current.setSpeed(update.getSpeed());
                                current.setSlashing(update.isSlashing());
                                current.setDirection(update.getDirection());
                                current.setWalking(update.isWalking());
                                current.setJumping(update.isJumping());
                            }
                        }
                    }
                }
            });
        }
    }
}
