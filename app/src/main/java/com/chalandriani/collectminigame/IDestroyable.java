package com.chalandriani.collectminigame;

/**
 * Created by Gal on 29/09/2017.
 */

interface IDestroyable  {
    void decreaseHp(int amount);
    void increaseHp(int amount);
    void setHp(int hp);
    void setHpMax(int max_hp);
}
