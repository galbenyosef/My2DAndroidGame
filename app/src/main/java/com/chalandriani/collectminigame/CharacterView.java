package com.chalandriani.collectminigame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.View;

import java.util.Random;

/**
 * Created by Gal on 30/09/2017.
 */


public class CharacterView extends View {

    int characterId;
    final int CHARACTERS_AMOUNT=50;

    public CharacterView(Context context, AttributeSet attrs) {
            super(context, attrs);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.TRANSPARENT);
        canvas.scale(5,5);
        canvas.drawBitmap(Main.animator.getCharacter().getResource(),0,-75,null);
    }

    void nextCharacter(){
        ++characterId;
        if (characterId == CHARACTERS_AMOUNT)
            characterId=0;
        invalidate();
    }

    void prevCharacter(){
        --characterId;
        if (characterId == -1)
            characterId=49;
        invalidate();
    }

    public int getCharacterId() {
        return characterId;
    }
}