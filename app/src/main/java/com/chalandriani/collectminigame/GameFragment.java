package com.chalandriani.collectminigame;


import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class GameFragment extends Fragment {


    Player self;

    public GameFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_game, container, false);
        GameView gv = v.findViewById(R.id.gameview_layout);
        gv.setPlayer(self);
        return v;
    }

    public void setSelf(Player self) {
        this.self = self;
    }

}
