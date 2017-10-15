package com.chalandriani.collectminigame;

/**
 * Created by Gal on 08/10/2017.
 */

public class Rectangle {

    protected int left;
    protected int top;
    protected int right;
    protected int bottom;

    public Rectangle() {
    }
    public Rectangle(int left, int top, int right, int bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(32);
        sb.append("Rect("); sb.append(left); sb.append(", ");
        sb.append(top); sb.append(" - "); sb.append(right);
        sb.append(", "); sb.append(bottom); sb.append(")");
        return sb.toString();
    }

    public boolean intersects(int left, int top, int right, int bottom) {
        if (this.left < right && left < this.right && this.top < bottom && top < this.bottom) {
            return true;
        }
        return false;
    }

    public boolean intersects(Rectangle rectangle) {
        if (this.left < rectangle.getRight() && rectangle.getLeft() < this.right && this.top < rectangle.getBottom() && rectangle.getTop() < this.bottom) {
            return true;
        }
        return false;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public void setRight(int right) {
        this.right = right;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public void setBottom(int bottom) {
        this.bottom = bottom;
    }

    public int getLeft() {
        return left;
    }

    public int getRight() {
        return right;
    }

    public int getTop() {
        return top;
    }

    public int getBottom() {
        return bottom;
    }


}
