package com.chalandriani.collectminigame;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import static com.chalandriani.collectminigame.GameScreen.HEIGHT;
import static com.chalandriani.collectminigame.GameScreen.WIDTH;

public class JoystickView extends View {

    // =========================================
    // Private Members
    // =========================================
    private final double RAD = 57.2957795;
    private Paint mainCircle;
    private Paint secondaryCircle;
    private Paint button;
    private Paint horizontalLine;
    private Paint verticalLine;
    private int xPosition = 0; // Touch x position
    private int yPosition = 0; // Touch y position
    private double centerX = 0; // Center view x position
    private double centerY = 0; // Center view y position
    private int lastAngle = 0;
    private int buttonRadius;
    private int joystickRadius;
    private OnJoystickMoveListener listener;


    public final static int UP = 8;
    public final static int UP_RIGHT = 9;
    public final static int RIGHT = 6;
    public final static int DOWN_RIGHT = 3;
    public final static int DOWN = 2;
    public final static int DOWN_LEFT = 1;
    public final static int LEFT = 4;
    public final static int UP_LEFT = 7;
    public final static int CENTER = 5;

    // =========================================
    // Constructors
    // =========================================

    public JoystickView(Context context) {
        super (context);
        initJoystickView();
    }

    public JoystickView(Context context, AttributeSet attrs) {
        super (context, attrs);
        initJoystickView();
    }

    public JoystickView(Context context, AttributeSet attrs,
                        int defStyle) {
        super (context, attrs, defStyle);
        initJoystickView();
    }

    // =========================================
    // Initialization
    // =========================================

    private void initJoystickView() {
        mainCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
        mainCircle.setColor(Color.TRANSPARENT);
        mainCircle.setStyle(Paint.Style.FILL_AND_STROKE);

        secondaryCircle = new Paint();
        secondaryCircle.setColor(Color.GREEN);
        secondaryCircle.setStyle(Paint.Style.STROKE);

        verticalLine = new Paint();
        verticalLine.setStrokeWidth(5);
        verticalLine.setColor(Color.BLACK);
        verticalLine.setAlpha(40);

        horizontalLine = new Paint();
        horizontalLine.setStrokeWidth(2);
        horizontalLine.setColor(Color.BLACK);
        horizontalLine.setAlpha(40);

        button = new Paint(Paint.ANTI_ALIAS_FLAG);
        button.setColor(Color.RED);
        button.setStyle(Paint.Style.FILL);
        button.setAlpha(40);
    }
    @Override
    protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld) {
        super.onSizeChanged(xNew, yNew, xOld, yOld);
        // before measure, get the center of view
        xPosition =  getWidth()/2;
        yPosition =  getHeight()/2;
        int d = Math.min(xNew, yNew);
        buttonRadius = (int) (d / 2 * 0.25);
        joystickRadius = (int) (d / 2 * 0.75);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Here we make sure that we have a perfect circle
        int measuredWidth = measure(widthMeasureSpec);
        int measuredHeight = measure(heightMeasureSpec);
        int d = Math.min(measuredWidth, measuredHeight);

        setMeasuredDimension(d, d);
    }

    private int measure(int measureSpec) {
        int result;
        // Decode the measurement specifications.
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.UNSPECIFIED) {
            // Return a default size of 200 if no bounds are specified.
            result = 200;
        } else {
            // As you want to fill the available space
            // always return the full available bounds.
            result = specSize;
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        centerX = getWidth()/2;
        centerY = getHeight()/2;

        // painting the main circle
        canvas.drawCircle((int) centerX, (int) centerY, joystickRadius,
                mainCircle);
        // painting the secondary circle
        canvas.drawCircle((int) centerX, (int) centerY, joystickRadius / 2,
                secondaryCircle);
        // paint lines
        canvas.drawLine((float) centerX, (float) centerY, (float) centerX,
                (float) (centerY - joystickRadius), verticalLine);
        canvas.drawLine((float) (centerX - joystickRadius), (float) centerY,
                (float) (centerX + joystickRadius), (float) centerY,
                horizontalLine);
        canvas.drawLine((float) centerX, (float) (centerY + joystickRadius),
                (float) centerX, (float) centerY, horizontalLine);

        // painting the move button
        canvas.drawCircle(xPosition, yPosition, buttonRadius, button);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int actionType = event.getAction();
        if (actionType == MotionEvent.ACTION_MOVE) {
            xPosition = (int) event.getX();
            yPosition = (int) event.getY();
            double abs = Math.sqrt((xPosition - centerX) * (xPosition - centerX)
                    + (yPosition - centerY) * (yPosition - centerY));
            if (abs > joystickRadius) {
                xPosition = (int) ((xPosition - centerX) * joystickRadius / abs + centerX);
                yPosition = (int) ((yPosition - centerY) * joystickRadius / abs + centerY);
            }

            //Log.d(TAG, "Angle:" + getAngle() + "Power:" + getPower());

            // Pressure
            if (listener != null) {
                listener.onValueChanged(getAngle(),getPower(),getDirection());
            }

            invalidate();
        } else if (actionType == MotionEvent.ACTION_UP) {
            returnHandleToCenter();
        }
        return true;
    }

    private void returnHandleToCenter() {

        Handler handler = new Handler();
        int numberOfFrames = 5;

        for (int i = 0; i < numberOfFrames; i++) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    xPosition = (int)centerX;
                    yPosition = (int)centerY;
                    invalidate();
                }
            }, i * 40);
        }

        if (listener != null) {
            listener.onReleased();
        }
    }

    private int getAngle() {
        if (xPosition > centerX) {
            if (yPosition < centerY) {
                return lastAngle = (int) (Math.atan((yPosition - centerY)
                        / (xPosition - centerX))
                        * RAD + 90);
            } else if (yPosition > centerY) {
                return lastAngle = (int) (Math.atan((yPosition - centerY)
                        / (xPosition - centerX)) * RAD) + 90;
            } else {
                return lastAngle = 90;
            }
        } else if (xPosition < centerX) {
            if (yPosition < centerY) {
                return lastAngle = (int) (Math.atan((yPosition - centerY)
                        / (xPosition - centerX))
                        * RAD - 90);
            } else if (yPosition > centerY) {
                return lastAngle = (int) (Math.atan((yPosition - centerY)
                        / (xPosition - centerX)) * RAD) - 90;
            } else {
                return lastAngle = -90;
            }
        } else {
            if (yPosition <= centerY) {
                return lastAngle = 0;
            } else {
                if (lastAngle < 0) {
                    return lastAngle = -180;
                } else {
                    return lastAngle = 180;
                }
            }
        }
    }

    private int getPower() {
        return (int) (100 * Math.sqrt((xPosition - centerX)
                * (xPosition - centerX) + (yPosition - centerY)
                * (yPosition - centerY)) / joystickRadius);
    }

    private int getDirection() {

        if ((lastAngle >= 0 && lastAngle < 22.5) || (lastAngle < 0 && lastAngle > -22.5)) {
            return UP;
        } else if (lastAngle >= 22.5 && lastAngle < 67.5) {
            return UP_RIGHT;
        } else if (lastAngle >= 67.5 && lastAngle < 112.5) {
            return RIGHT;
        } else if (lastAngle >= 112.5 && lastAngle < 157.5) {
            return DOWN_RIGHT;
        } else if ((lastAngle >= 157.5 && lastAngle <= 180) || (lastAngle >= -180 && lastAngle < -157.5)) {
            return DOWN;
        } else if (lastAngle >= -157.5 && lastAngle < -112.5) {
            return DOWN_LEFT;
        } else if (lastAngle >= -112.5 && lastAngle < -67.5) {
            return LEFT;
        } else if (lastAngle >= -67.5 && lastAngle < -22.5) {
            return UP_LEFT;
        } else {
            return CENTER;
        }
    }

    public void setOnJoystickMoveListener(OnJoystickMoveListener listener) {
        this.listener = listener;
    }

    public interface OnJoystickMoveListener {
        public void onValueChanged(int angle, int power, int direction);
        public void onReleased();
    }

}