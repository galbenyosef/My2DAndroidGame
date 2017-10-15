package com.chalandriani.collectminigame;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

/**
 * A simple {@link Fragment} subclass.
 */
public class GameFragment extends Fragment {

    public GameFragment() {
        // Required empty public constructor
    }


    public GameFragment initialize(String name,int id){

        Main.player = new Player();
        Main.player.setPlayerName(name);
        Main.player.setCharacterId(id);
        Main.player.setAnimation(Main.animator.getCharacter().getWalkAnimation(JoystickView.CENTER));
        Main.player.setWidth(64);
        Main.player.setHeight(64);
        Main.player.setHpMax(100);
        Main.player.setHp(100);
        Main.player.setSpeed(3);
        Main.player.setX(250);
        Main.player.setY(300);
        Main.player.setDirection(JoystickView.CENTER);
        Main.player.setRectangle(new Rectangle(Main.player.getX()+Main.player.getWidth()/3,
                Main.player.getY()+Main.player.getHeight()-Main.player.getHeight()/6
                ,Main.player.getX()+Main.player.getWidth()-Main.player.getWidth()/3
                ,Main.player.getY()+Main.player.getHeight()));
        return this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_game, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final View v = view;
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                showUI(v);
            }
        });
        FirebaseHandler.player_reference = FirebaseHandler.players_reference.child(Main.player.getPlayerName());
        FirebaseHandler.players_reference.addChildEventListener(FirebaseHandler.EventListeners.playerUpdaterv2);
    }

    void showUI(View view){

        final RelativeLayout buttonsView = view.findViewById(R.id.buttonview_layout);

        final RelativeLayout joystickView = view.findViewById(R.id.joystickview_layout);
        final int width = view.getWidth();
        final int height =  view.getHeight();

        FrameLayout.LayoutParams resizeJoystick = new FrameLayout.LayoutParams(width/4,height/4, Gravity.BOTTOM | Gravity.START);
        resizeJoystick.setMargins((int)(24*GameView.scaleFactorX),0,0,(int)(24*GameView.scaleFactorY));
        joystickView.setLayoutParams(resizeJoystick);
        JoystickView joyview = new JoystickView(getActivity());
        joyview.setOnJoystickMoveListener(new JoystickView.OnJoystickMoveListener() {
            @Override
            public void onValueChanged(int angle, int power, int direction) {

                Main.player.setDirection(direction);
                Main.player.setWalking(true);

            }

            @Override
            public void onReleased() {

                Main.player.setWalking(false);

            }
        });
        joystickView.addView(joyview);

        FrameLayout.LayoutParams resizeButtons = new FrameLayout.LayoutParams(width/4,height/4,Gravity.BOTTOM | Gravity.END);
        resizeButtons.setMargins(0,0,0,(int)(24*GameView.scaleFactorX));
        ButtonsView bn = new ButtonsView(getActivity(),width/16);
        bn.setLayoutParams(resizeButtons);
        bn.setOnButtonPressedListener(new ButtonsView.OnButtonPressedListener() {
            @Override
            public void onButtonClick(Button clcked) {
                if (clcked.getTag() == "A") {

                    Main.player.setSlashing(true);

                }
                else if (clcked.getTag() == "B"){


                }
                else if (clcked.getTag() == "C"){
                    Main.player.setJumping(true);

                }
                else
                    return;
            }
        });
        buttonsView.addView(bn);
    }

}
