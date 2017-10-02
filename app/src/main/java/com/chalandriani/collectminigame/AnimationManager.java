package com.chalandriani.collectminigame;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.util.ArrayList;

public class AnimationManager {

    ArrayList<UniversalLPCSpritesheet> sprites;
    AssetManager assetMgr;

    public AnimationManager (AssetManager assetManager){

        sprites = new ArrayList<>();
        assetMgr = assetManager;

        try {
            for (int i = 0; i < 50; i++) {
                Bitmap character = BitmapFactory.decodeStream(assetManager.open("characters/character_"+i+".png"));
                sprites.add(new UniversalLPCSpritesheet(character));
            }
        } catch (IOException e) {
            e.getMessage();
            return;
        }

    }

    public UniversalLPCSpritesheet getCharacter(int id){
        return sprites.get(id);
    }

}
