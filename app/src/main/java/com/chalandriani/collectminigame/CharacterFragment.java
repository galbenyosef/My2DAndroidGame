package com.chalandriani.collectminigame;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 */
public class CharacterFragment extends Fragment {

    CharacterView viewer;
    Button left,right,ok;
    TextView characterNumber;
    EditText username;
    View.OnClickListener clickListener;
    View main;

    public CharacterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        main = inflater.inflate(R.layout.fragment_character, container, false);
        return main;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        viewer=view.findViewById(R.id.characterview);
        left=view.findViewById(R.id.arrow_left);
        right=view.findViewById(R.id.arrow_right);
        ok=view.findViewById(R.id.button_ok);
        username=view.findViewById(R.id.username);
        characterNumber=view.findViewById(R.id.characternum);

        clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view == right) {
                    viewer.nextCharacter();
                    characterNumber.setText("Character #" + (viewer.getCharacterId() + 1));
                }
                else if (view == left) {
                    viewer.prevCharacter();
                    characterNumber.setText("Character #" + (viewer.getCharacterId() + 1));
                }
                else{
                    String name = username.getText().toString();
                    int id = viewer.getCharacterId();
                    if (name.length() < 3 || !isLegal(name)) {
                        Toast.makeText(getActivity(), "Bad username syntax", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    FragmentHandler.switchFragment(R.id.fragment_container,new GameFragment().initialize(name,id));
                }
            }
        };
        left.setOnClickListener(clickListener);
        right.setOnClickListener(clickListener);
        ok.setOnClickListener(clickListener);
    }

    public static boolean isLegal(String name) {
        char[] chars = name.toCharArray();
        for (char c : chars) {
            if(!Character.isLetter(c) && !Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }
}

