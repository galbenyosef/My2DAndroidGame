package com.chalandriani.collectminigame;

import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;

public class AnimationManager {

    ArrayList<UniversalLPCSpritesheet> sprites;
    AssetManager assetMgr;

    public AnimationManager (AssetManager assetManager) {

        sprites = new ArrayList<>();
        assetMgr = assetManager;
    }

    public UniversalLPCSpritesheet getCharacter(int id) {
        try {
            UniversalLPCSpritesheet retval =  new UniversalLPCSpritesheet(BitmapFactory.decodeStream(assetMgr.open("characters/character_"+id+".png")));
            return retval;
        } catch (IOException e) {
            Log.d("H",e.getMessage());
            return null;
        }
    }
}