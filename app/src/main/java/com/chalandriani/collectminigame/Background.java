package com.chalandriani.collectminigame;
import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Background {

    private Bitmap image;
    private int x, y;

    public Background(Bitmap res)
    {
        image = res;
    }
    public void update(int direction,int velocity)
    {
//        int diagonalMovementFactor = (int)(velocity / Math.sqrt(2));
//        if (diagonalMovementFactor == 0)
//            --diagonalMovementFactor;
//        velocity *= -1;
//
//        switch (direction) {
//            case JoystickView.UP:
//                break;
//            case JoystickView.UP_LEFT:
//                x -= diagonalMovementFactor;
//                break;
//            case JoystickView.UP_RIGHT:
//                x += diagonalMovementFactor;
//                break;
//            case JoystickView.DOWN:
//                break;
//            case JoystickView.DOWN_LEFT:
//                x -= diagonalMovementFactor;
//                break;
//            case JoystickView.DOWN_RIGHT:
//                x += diagonalMovementFactor;
//                break;
//            case JoystickView.LEFT:
//                x -= velocity;
//                break;
//            case JoystickView.RIGHT:
//                x += velocity;
//                break;
//            default:
//                break;
//        }
//        //cancel y background movement
//        y = 0;

    }
    public void draw(Canvas canvas)
    {
        canvas.drawBitmap(image, x, y,null);
        int tempx = 0;
        if(x>0)
            tempx = x - GameScreen.WIDTH;
        if(x<0)
            tempx = x + GameScreen.WIDTH;
        canvas.drawBitmap(image, tempx , y,  null);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }
}