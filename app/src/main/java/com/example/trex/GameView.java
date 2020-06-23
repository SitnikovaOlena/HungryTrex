package com.example.trex;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.view.MotionEvent;
import android.view.SurfaceView;

import java.util.Random;

public class GameView extends SurfaceView implements Runnable {

    private Thread thread;
    private boolean isPlaying, isGameOver = false;
    private Background background1, background2;
    private int screenX, screenY, score = 0;
    private Paint paint;
    static public float screenRationX, screenRationY;
    private TrexRun trexRun;
    private Food[] foods;
    private Tumbleweed[] tumbleweeds;
    private Random random;
    private SharedPreferences sharedPreferences;
    private GameActivity activity;
    static public SoundPool soundPool;
    private int eatSound;
    private int gameOverSound;


    public GameView(GameActivity activity, int screenX, int screenY) {
        super(activity);


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .build();

        soundPool = new SoundPool.Builder().setAudioAttributes(audioAttributes).build();
        }else
            soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);


        eatSound = soundPool.load(activity, R.raw.eat_sound, 1);
        gameOverSound = soundPool.load(activity, R.raw.game_over, 1);


        this.activity = activity;
        sharedPreferences = activity.getSharedPreferences("game", Context.MODE_PRIVATE);
        this.screenX = screenX;
        this.screenY = screenY;
        screenRationX = 1920f / screenX;
        screenRationY = 1080f / screenY;
        background1 = new Background(screenX, screenY, getResources());
        background2 = new Background(screenX, screenY, getResources());

        trexRun = new TrexRun(this,screenY,getResources());


        background2.x = screenX;
        paint = new Paint();
        paint.setTextSize(128);
        paint.setColor(Color.BLACK);


        foods = new Food[2];
        for (int i = 0;i < 2;i++) {

            Food food = new Food(getResources());
            foods[i] = food;

        }

        tumbleweeds = new Tumbleweed[1];
        for (int i = 0;i < 1;i++) {

            Tumbleweed tumbleweed = new Tumbleweed(getResources());
            tumbleweeds[i] = tumbleweed;

        }

        random = new Random();
    }

    @Override
    public void run() {
        while (isPlaying){
            update();
            draw();
            sleep();
}
    }

    private void update() {
        background1.x -=7 * screenRationX;
        background2.x -=7 * screenRationX;
        if (background1.x +background1.background.getWidth() < 0 ){
            background1.x = screenX;
        }
        if (background2.x +background2.background.getWidth() < 0 ){
            background2.x = screenX;
        }
        if(trexRun.isJumping) {
            trexRun.y -= 100 * screenRationY;

        }else
            trexRun.y += 10 * screenRationY;

        if(trexRun.x < 200 * screenRationX)
            trexRun.x = (int) (200 * screenRationX);

        if(trexRun.y < 150 * screenRationY)
            trexRun.y = (int) (150 * screenRationY);

        if(trexRun.y > 565 * screenRationY)
            trexRun.y = (int) (565 * screenRationY);

        if(trexRun.y > screenY - trexRun.height)
            trexRun.y = screenY - trexRun.height;

        for(Food food: foods){
            food.x -=food.speed;
            if(food.x + food.width < 0){
                int bound = (int) (30 * screenRationX);
                food.speed = random.nextInt(bound);

                if(food.speed < 10 * screenRationX){
                    food.speed = (int) (10 * screenRationX);
                }
                food.x = screenX;
                food.y = (int) (screenY / 2);
            }
            if (trexRun.isEating){

                if (Rect.intersects(food.getCollisionShape(), trexRun.getCollisionShape())){
                    food.x = -500;
                    food.wasEaten = true;
                    score++;
                    if (!sharedPreferences.getBoolean("isMute",false)){
                        soundPool.play(eatSound, 1,1,0,0,1);
                    }}
                food.wasEaten = false;
            }

        }

        for(Tumbleweed tumbleweed: tumbleweeds){
            tumbleweed.x -=tumbleweed.speedTumbleweed;
            if(tumbleweed.x + tumbleweed.width < 0){
                int bound = (int) (30 * screenRationX);
                tumbleweed.speedTumbleweed = random.nextInt(bound);

                if(tumbleweed.speedTumbleweed < 10 * screenRationX){
                    tumbleweed.speedTumbleweed = (int) (10 * screenRationX);
                }
                tumbleweed.x = screenX;
                tumbleweed.y = screenY - tumbleweed.height;

                if(tumbleweed.y > 700 * screenRationY)
                    tumbleweed.y = (int) (700 * screenRationY);
            }
            if (Rect.intersects(tumbleweed.getCollisionShape(), trexRun.getCollisionShape())){
                isGameOver = true;
                return;
            }

        }

    }

    private void draw() {
        if (getHolder().getSurface().isValid()){
            Canvas canvas = getHolder().lockCanvas();
            canvas.drawBitmap(background1.background, background1.x, background1.y, paint);
            canvas.drawBitmap(background2.background, background2.x, background2.y, paint);
            for(Food food: foods){
                canvas.drawBitmap(food.getBurger(),food.x, food.y, paint);
            }

            for(Tumbleweed tumbleweed: tumbleweeds){
                canvas.drawBitmap(tumbleweed.getTumbleweed(),tumbleweed.x, tumbleweed.y, paint);
            }
            canvas.drawText(score + "", screenX / 2f, 164, paint);
            if (trexRun.isJumping){
                canvas.drawBitmap(trexRun.getJump(), trexRun.x, trexRun.y, paint);
            }else{
                canvas.drawBitmap(trexRun.getRun(), trexRun.x, trexRun.y, paint);}

            if(isGameOver){
                isPlaying = false;
                canvas.drawBitmap(trexRun.getDead(), trexRun.x, trexRun.y, paint);
                if (sharedPreferences.getInt("highScore", 0) < score ){
                canvas.drawText("WOW! You set new high score!", 20, 285,paint);
                }else{
                canvas.drawText("Game over", canvas.getWidth()/2, canvas.getHeight()/2,paint);}
                if (!sharedPreferences.getBoolean("isMute",false)){
                    soundPool.play(gameOverSound, 1,1,0,0,1);
                }
                getHolder().unlockCanvasAndPost(canvas);
                saveIfHighScore();
                waitBeforeExit();
                return;
            }


            getHolder().unlockCanvasAndPost(canvas);
        }
    }

    private void waitBeforeExit() {
        try {
            Thread.sleep(3000);
            activity.startActivity(new Intent(activity, MainActivity.class));
            activity.finish();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void saveIfHighScore() {
        if (sharedPreferences.getInt("highScore", 0) < score ){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("highScore", score);
            editor.apply();

        }
    }

    private void sleep() {
        try {
            Thread.sleep(17);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume(){
        isPlaying = true;
        thread = new Thread(this);
        thread.start();
    }

    public void pause(){
        try {
            isPlaying = false;
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if (event.getX() < screenX / 2){
                    trexRun.isJumping = true;
                }
                if (event.getX() > screenX / 2){
                trexRun.toEat++;
                trexRun.isEating = true;
            }
                break;

            case MotionEvent.ACTION_UP:
                    trexRun.isJumping = false;


                    trexRun.isEating=false;
                break;
        }
        return true;


    }

    public void newEat() {
    }
}
