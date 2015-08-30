package com.mytasks;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.mytasks.constants.MyTaskConstants;

/**
 * Created by nimilpeethambaran on 8/28/15.
 */
public class Splash extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.splash);
        final ImageView im = (ImageView) findViewById(R.id.imageView);
        final Animation animation = AnimationUtils.loadAnimation(getBaseContext(),R.anim.rotate);
        final Animation fadeOut = AnimationUtils.loadAnimation(getBaseContext(), android.support.v7.appcompat.R.anim.abc_fade_out);
        im.startAnimation(animation);
        startMyBroadCast();
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                im.startAnimation(fadeOut);
                finish();
                Intent intent = new Intent(getBaseContext(),MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    /**
     * Method that invokes the broadcast
     */
    private void startMyBroadCast() {
        long now = System.currentTimeMillis();
        long time = 1000;
        Intent myIntent = new Intent(MyTaskConstants.PENDING_INTENT_NAME);
        myIntent.setClass(this, MyTasksReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, myIntent, 0);
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME, AlarmManager.INTERVAL_FIFTEEN_MINUTES, AlarmManager.INTERVAL_HALF_DAY, pendingIntent);
    }
}
