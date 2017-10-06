package com.chalandriani.collectminigame;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorSpace;
import android.graphics.Rect;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class AnimationManager {

    ArrayList<UniversalLPCSpritesheetConcrete> sprites;
    AssetManager assetMgr;

    public AnimationManager (AssetManager assetManager) {

        sprites = new ArrayList<>();
        assetMgr = assetManager;
    }

    public UniversalLPCSpritesheetConcrete getCharacter(int id) {
        try {
            UniversalLPCSpritesheetConcrete retval =  new UniversalLPCSpritesheetConcrete(BitmapFactory.decodeStream(assetMgr.open("characters/character_"+id+".png")));
            return retval;
        } catch (IOException e) {
            Log.d("H",e.getMessage());
            return null;
        }
    }
}
