package com.chalandriani.collectminigame;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class GameScreen extends SurfaceView implements SurfaceHolder.Callback
{
    public static final int WIDTH = 856;
    public static final int HEIGHT = 480;
    private MainThread thread;
    private Background bg;
    private Player player,player2;
    private Animation walkup,walkleft,walkdown,walkright,current;

    FirebaseDatabase database = FirebaseDatabase.getInstance();

    public GameScreen(Context context)
    {
        super(context);
    }

    public GameScreen(Context context, AttributeSet attrs) {
        super (context, attrs);
        //add the callback to the surfaceholder to intercept events
        getHolder().addCallback(this);
        thread = new MainThread(getHolder(), this);
        //make gamePanel focusable so it can handle events
        setFocusable(true);

    }

    public GameScreen(Context context, AttributeSet attrs,
                        int defStyle) {
        super (context, attrs, defStyle);
    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){}

    @Override
    public void surfaceDestroyed(SurfaceHolder holder){
        boolean retry = true;
        int counter = 0;
        while(retry && counter < 1000)
        {
            counter++;
            try{thread.setRunning(false);
                thread.join();
                retry = false;

            }catch(InterruptedException e){e.printStackTrace();}
        }

    }

    void setJoystickKeys(JoystickView joy){
        joy.setOnJoystickMoveListener(new JoystickView.OnJoystickMoveListener() {
            @Override
            public void onValueChanged(int angle, int power, int direction) {
                player.setMoving(true);
                player.setDirection(direction);
            }

            @Override
            public void onReleased() {
                player.setMoving(false);
            }
        });
    }
    void showJoystick(){

        JoystickView joyview = new JoystickView(getContext());
        joyview.setLayoutParams(new FrameLayout.LayoutParams(getHeight()/5,getWidth()/5, Gravity.BOTTOM));
        setJoystickKeys(joyview);
        View rootView = ((Activity)getContext()).getWindow().getDecorView().findViewById(android.R.id.content);
        ((FrameLayout)rootView).addView(joyview);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder){

        ValueEventListener playerReader = (new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // GenericTypeIndicator<HashMap<String,PlayerState>> data_base = new GenericTypeIndicator<HashMap<String,PlayerState>>(){};
                Player data_messages = dataSnapshot.getValue(Player.class);
                Log.d("bug", data_messages.toString());
                if (data_messages == null) return;
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inScaled = false;
                player2 = data_messages;
                Bitmap res =   BitmapFactory.decodeResource(getResources(), R.drawable.walk,options)   ;

                player2.setWalkup(Animation.getWalkAnimation(res,player.getWidth(),player.getHeight(),JoystickView.UP));
                player2.setWalkdown(Animation.getWalkAnimation(res,player.getWidth(),player.getHeight(),JoystickView.DOWN));
                player2.setWalkleft(Animation.getWalkAnimation(res,player.getWidth(),player.getHeight(),JoystickView.LEFT));
                player2.setWalkright(Animation.getWalkAnimation(res,player.getWidth(),player.getHeight(),JoystickView.RIGHT));


                player2.update();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });

        bg = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.grassbg1));
        showJoystick();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        player = new Player(BitmapFactory.decodeResource(getResources(), R.drawable.walk,options), 64, 64 , 100,100,3);

        DatabaseReference r = database.getReference("players").child("givon");
        r.addValueEventListener(playerReader);

        //we can safely start the game loop
        thread.setRunning(true);
        thread.start();

    }
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
        }
        return super.onTouchEvent(event);
    }

    public void update()
    {
            player.update();

            bg.update(player.getDirection(),player.getSpeed()/2);
            if (player.getX() <= 40) {
                player.setX(player.getX() + 200);
                bg.setX(bg.getX() + 200);
            }
            if (player.getX() >= WIDTH-30) {
                player.setX(player.getX() - 200);
                bg.setX(bg.getX() - 200);
            }
    }
    @Override
    public void draw(Canvas canvas)
    {
        super.draw(canvas);

        final float scaleFactorX = (float)getWidth()/(WIDTH*1.f);
        final float scaleFactorY = (float)getHeight()/(HEIGHT*1.f);
        if(canvas!=null) {
            final int savedState = canvas.save();
            canvas.scale(scaleFactorX, scaleFactorY);
            bg.draw(canvas);
            player.draw(canvas);
            player2.draw(canvas);
            canvas.restoreToCount(savedState);
        }
    }
}