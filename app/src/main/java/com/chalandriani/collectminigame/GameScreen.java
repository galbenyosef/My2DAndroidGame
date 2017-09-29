package com.chalandriani.collectminigame;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;


public class GameScreen extends SurfaceView implements SurfaceHolder.Callback
{
    public static final int WIDTH = 2400  ;
    public static final int HEIGHT = 480;
    MainThread thread;
    Background bg;
    Player player;
    int playerCount = 0;
    OnlinePlayer player2;
    Rect mapTopLimit,mapBottomLimit;
    float scaleFactorX;
    float scaleFactorY;


    FirebaseDatabase database ;

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


    @Override
    public void surfaceCreated(SurfaceHolder holder){

        FirebaseApp.initializeApp(getContext());
        database  = FirebaseDatabase.getInstance();

        bg = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.map));
        mapTopLimit = new Rect(0,0,2400,8*24);
        mapBottomLimit = new Rect(0,18*24,2400,20*24);

        scaleFactorX = (float)getWidth()/(WIDTH/3*1.f);
        scaleFactorY = (float)getHeight()/(HEIGHT*1.f);
        player = new Player();
        player.setReference("gal");
        player2 = new OnlinePlayer();
        player2.setReference("ziviva");

        DatabaseReference r = database.getReference("players");
        r.addListenerForSingleValueEvent(playerReader);
        r.addValueEventListener(playerUpdater);

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

    void showUI(){

        final View rootView = ((Activity)getContext()).getWindow().getDecorView().findViewById(android.R.id.content);
        final RelativeLayout buttonsView = (RelativeLayout)rootView.findViewById(R.id.buttonview_layout);
        final RelativeLayout joystickView = (RelativeLayout)rootView.findViewById(R.id.joystickview_layout);

        FrameLayout.LayoutParams resizeJoystick = new FrameLayout.LayoutParams(getWidth()/4,getHeight()/4,Gravity.BOTTOM | Gravity.START);
        resizeJoystick.setMargins((int)(24*scaleFactorX),0,0,(int)(24*scaleFactorY));
        joystickView.setLayoutParams(resizeJoystick);
        JoystickView joyview = new JoystickView(getContext());
        joyview.setOnJoystickMoveListener(new JoystickView.OnJoystickMoveListener() {
            @Override
            public void onValueChanged(int angle, int power, int direction) {
                player.setWalking(true);
                player.setDirection(direction);
            }

            @Override
            public void onReleased() {

                player.setWalking(false);
            }
        });
        joystickView.addView(joyview);

        FrameLayout.LayoutParams resizeButtons = new FrameLayout.LayoutParams(getWidth()/4,getHeight()/4,Gravity.BOTTOM | Gravity.END);
        resizeButtons.setMargins(0,0,0,(int)(24*scaleFactorY));
        ButtonsView bn = new ButtonsView(getContext(),getWidth()/16);
        bn.setLayoutParams(resizeButtons);
        bn.setOnButtonPressedListener(new ButtonsView.OnButtonPressedListener() {
            @Override
            public void onButtonClick(Button clcked) {
                if (clcked.getTag() == "A"){
                    player.setSlashing(true);
                }
                else if (clcked.getTag() == "B"){

                }
                else if (clcked.getTag() == "C"){


                }
                else
                    return;
            }
        });
        buttonsView.addView(bn);
    }

    ValueEventListener playerUpdater = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            GenericTypeIndicator<Map<String,Player>> player_db = new GenericTypeIndicator<Map<String,Player>>(){};
            if (dataSnapshot.getValue(player_db) != null) {
                ArrayList<Player> players_updates = new ArrayList<>(dataSnapshot.getValue(player_db).values());
                playerCount = players_updates.size();
                if (playerCount == 0) return;
                for (int i = 0; i < players_updates.size(); i++) {
                    Player currentPlayer = players_updates.get(i);
                    if (!currentPlayer.getPlayerName().equals(player.getPlayerName())) {
                        player2.setX(currentPlayer.getX());
                        player2.setY(currentPlayer.getY());
                        player2.setDirection(currentPlayer.getDirection());
                        player2.setWidth(currentPlayer.getWidth());
                        player2.setHeight(currentPlayer.getHeight());
                        player2.setWalking(currentPlayer.isWalking());
                        player2.setSlashing(currentPlayer.isSlashing());
                        player2.setSpeed(currentPlayer.getSpeed());
                    }
                }
            }
        }

        @Override
        public void onCancelled(DatabaseError error) {
            // Failed to read value
        }
    };

    ValueEventListener playerReader = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            GenericTypeIndicator<Map<String,Player>> player_db = new GenericTypeIndicator<Map<String,Player>>(){};
            if (dataSnapshot.getValue(player_db) != null) {
                ArrayList<Player> players_updates = new ArrayList<>(dataSnapshot.getValue(player_db).values());
                playerCount = players_updates.size();
                if (playerCount == 0) return;
                for (int i = 0; i < players_updates.size(); i++) {
                    Player currentPlayer = players_updates.get(i);
                    if (!currentPlayer.getPlayerName().equals(player.getPlayerName())) {
                        player2.setX(currentPlayer.getX());
                        player2.setY(currentPlayer.getY());
                        player2.setDirection(currentPlayer.getDirection());
                        player2.setWidth(currentPlayer.getWidth());
                        player2.setHeight(currentPlayer.getHeight());
                        player2.setWalking(currentPlayer.isWalking());
                        player2.setSlashing(currentPlayer.isSlashing());
                        player2.setSpeed(currentPlayer.getSpeed());
                    } else {
                        player.setX(currentPlayer.getX());
                        player.setY(currentPlayer.getY());
                        player.setDirection(currentPlayer.getDirection());
                        player.setWidth(currentPlayer.getWidth());
                        player.setHeight(currentPlayer.getHeight());
                        player.setWalking(currentPlayer.isWalking());
                        player.setSlashing(currentPlayer.isSlashing());
                        player.setSpeed(currentPlayer.getSpeed());
                    }
                }
                showUI();
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    public boolean collision(){

        RectF playerOval = new RectF(player.getX() + player.getDx() + 25,player.getY() + player.getDy() + 55
                ,player.getX() + player.getDx() + player.getWidth() - 25,player.getY() + player.getDy() + player.getHeight());
        RectF player2Oval = new RectF(player2.getX()+25,player2.getY()+55,player2.getX()+player2.getWidth()-25,player2.getY()+player2.getHeight());
        if (playerOval.intersects(mapTopLimit.left,mapTopLimit.top,mapTopLimit.right,mapTopLimit.bottom)
                || playerOval.intersects(mapBottomLimit.left,mapBottomLimit.top,mapBottomLimit.right,mapBottomLimit.bottom)) {
            return true;
        }
        if (RectF.intersects(playerOval,player2Oval)) {
            return true;
        }
//            for (GameObject obj: list) {
//                if (Rect.intersects(new Rect(obj.getX(),obj.getY(),obj.getX()+obj.getWidth(),obj.getY()+obj.getHeight()),new Rect(getX(),getY(),getX()+getWidth(),getY()+getHeight()))){
//                    return(true);
//                }
//            }
        return false;
    }

    public void update()
    {
        if (playerCount > 0) {

            ArrayList<GameObject> others = new ArrayList<>();
            others.add(player2);
            if (collision()) {
                player.setDx(0);
                player.setDy(0);
            }

            player.update();
            player2.update();

            bg.update(player.getDirection(), player.getSpeed() / 2);
            if (player.getX() <= 40) {
                player.setX(player.getX() + 200);
                bg.setX(bg.getX() + 200);
            }
            if (player.getX() >= WIDTH - 30) {
                player.setX(player.getX() - 200);
                bg.setX(bg.getX() - 200);
            }
        }
    }
    @Override
    public void draw(Canvas canvas)
    {
        super.draw(canvas);

        if(canvas!=null) {
            final int savedState = canvas.save();
            canvas.scale(scaleFactorX, scaleFactorY);
            bg.draw(canvas);

            //draw shadows by collision shapes +-
            Paint mPaint = new Paint();
            mPaint.setColor(Color.BLACK);
            mPaint.setAlpha(40);
            canvas.drawOval(new RectF(player.getX() + player.getDx() + 15,player.getY() + player.getDy() + 50
                    ,player.getX() + player.getDx() + player.getWidth() - 15,player.getY() + player.getDy() + player.getHeight()),mPaint);
            canvas.drawOval(new RectF(player2.getX()+15,player2.getY()+50,player2.getX()+player2.getWidth()-15,player2.getY()+player2.getHeight()),mPaint);


            player.draw(canvas);
            player2.draw(canvas);
            canvas.restoreToCount(savedState);
        }
    }
}