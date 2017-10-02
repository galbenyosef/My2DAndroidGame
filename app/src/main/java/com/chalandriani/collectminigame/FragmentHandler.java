package com.chalandriani.collectminigame;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

/**
 * Created by Gal on 02/10/2017.
 */

public class FragmentHandler {

    public static void switchFragment(int xamlresid, Fragment fragment){
        FragmentManager fm = Main.fragmentizer;
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(xamlresid,fragment);
        transaction.commit();
    }

}
