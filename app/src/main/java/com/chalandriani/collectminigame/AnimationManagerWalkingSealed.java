package com.chalandriani.collectminigame;

import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.util.Pair;
import android.util.SparseArray;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AnimationManagerWalkingSealed {

   // ArrayList<Pair<String,UniversalLPCSpritesheetWalkingLoaded>> sprites;
  //  HashMap<String,UniversalLPCSpritesheetWalkingLoaded> sprites;
    UniversalLPCSpritesheetWalkingLoaded sprite;
    AssetManager assetMgr;

    public AnimationManagerWalkingSealed(AssetManager assetManager) {

      //  sprites = new ArrayList<>();
        assetMgr = assetManager;
        try {
            sprite =
                    new UniversalLPCSpritesheetWalkingLoaded(BitmapFactory.decodeStream(assetMgr.open("characters/character_0" + ".png")));
        }
        catch (IOException ex){

        }
    }

//
//    public void load (String name,int id){
//        try {
//            if (!sprites.isEmpty()) {
//                for (Pair<String, UniversalLPCSpritesheetWalkingLoaded> pair : sprites) {
//                    if (pair.first.equalsIgnoreCase(name))
//                        return;
//                }
//            }
//            UniversalLPCSpritesheetWalkingLoaded spriteToLoad =
//                    new UniversalLPCSpritesheetWalkingLoaded(BitmapFactory.decodeStream(assetMgr.open("characters/character_" + id + ".png")));
//            sprites.add(new Pair<>(name,spriteToLoad));
//            Log.d("H","Loaded "+name+" "+spriteToLoad.toString());
//        }
//        catch (IOException e) {
//            Log.d("H",e.getMessage());
//        }
//    }
//
//    public UniversalLPCSpritesheetWalkingLoaded getCharacter(String name) {
//
//        if (!sprites.isEmpty()) {
//            for (Pair<String, UniversalLPCSpritesheetWalkingLoaded> pair : sprites) {
//                if (pair.first.equalsIgnoreCase(name)) {
//                    Log.d("H", "Returned " + name + " " + pair.second.toString());
//                    Log.d("H", "All " + sprites.toString());
//                    return pair.second;
//                }
//            }
//        }
//        return null;
//    }
//
//    public void remove(String name){
//        if (!sprites.isEmpty()) {
//            for (int i = 0; i < sprites.size() ; i++) {
//                if (sprites.get(i).first.equalsIgnoreCase(name))
//                    sprites.remove(i);
//            }
//        }
//    }

//    public UniversalLPCSpritesheet getCharacterForSelection(int id) {
//        try {
//            UniversalLPCSpritesheet retval =  new UniversalLPCSpritesheet(BitmapFactory.decodeStream(assetMgr.open("characters/character_"+id+".png")));
//            return retval;
//        } catch (IOException e) {
//            Log.d("H",e.getMessage());
//            return null;
//        }
//    }

    public UniversalLPCSpritesheetWalkingLoaded getCharacter() {

        return sprite;
    }
}




//public class AnimationManagerWalkingSealed {
//
//    ArrayList<UniversalLPCSpritesheet> sprites;
//    AssetManager assetMgr;
//
//    public AnimationManagerWalkingSealed (AssetManager assetManager) {
//
//        sprites = new ArrayList<>();
//        assetMgr = assetManager;
//    }
//
//    public UniversalLPCSpritesheet getCharacter(int id) {
//        try {
//            UniversalLPCSpritesheet retval =  new UniversalLPCSpritesheet(BitmapFactory.decodeStream(assetMgr.open("characters/character_"+id+".png")));
//            return retval;
//        } catch (IOException e) {
//            Log.d("H",e.getMessage());
//            return null;
//        }
//    }
//}
