package com.chalandriani.collectminigame;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.RelativeLayout;


/**
 * Created by Gal on 27/09/2017.
 */

public class ButtonsView extends RelativeLayout {

    Button a,b,c;
    OnButtonPressedListener listener;

    public ButtonsView(Context context,int btn_size) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View buttonsView = inflater.inflate(R.layout.gamepads, this, true);

        a = findViewById(R.id.button_a);
        b = findViewById(R.id.button_b);
        c = findViewById(R.id.button_c);

        a.setTag("A");
        b.setTag("B");
        c.setTag("C");

        GridLayout.Spec aCol = GridLayout.spec(0, 2);
        GridLayout.Spec aRow = GridLayout.spec(1);
        GridLayout.Spec bCol = GridLayout.spec(2, 2);
        GridLayout.Spec bRow = GridLayout.spec(1);
        GridLayout.Spec cCol = GridLayout.spec(0, 4);
        GridLayout.Spec cRow = GridLayout.spec(0);
        GridLayout.LayoutParams Aparams = new GridLayout.LayoutParams(aRow,aCol);
        Aparams.setMargins(0,0,0,10);
        GridLayout.LayoutParams Bparams = new GridLayout.LayoutParams(bRow,bCol);
        GridLayout.LayoutParams Cparams = new GridLayout.LayoutParams(cRow,cCol);
        Cparams.setGravity(Gravity.CENTER_HORIZONTAL);

        Aparams.width = Bparams.width = Cparams.width = btn_size;
        Aparams.height = Bparams.height = Cparams.height = btn_size;

        a.setLayoutParams(Aparams);
        b.setLayoutParams(Bparams);
        c.setLayoutParams(Cparams);

        OnClickListener clicker = new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onButtonClick((Button)view);
                }
            }
        };

        a.setOnClickListener(clicker);
        b.setOnClickListener(clicker);
        c.setOnClickListener(clicker);

        a.setAlpha(0.2f);
        b.setAlpha(0.2f);
        c.setAlpha(0.2f);

    }


    public void setOnButtonPressedListener(OnButtonPressedListener listener) {
        this.listener = listener;
    }

    public interface OnButtonPressedListener {
        void onButtonClick(Button clcked);
    }
}
