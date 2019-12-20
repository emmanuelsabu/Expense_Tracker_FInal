package com.example.expensetracker;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;

public class Splash extends Activity implements Animation.AnimationListener {
    SharedPreferences mSharedPreferences;
    TextView t1;
    ImageView im1;

    Animation mAnimation;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.splash);

        t1=findViewById(R.id.t1);
        im1=findViewById(R.id.im1);


        mAnimation = AnimationUtils.loadAnimation(this,R.anim.fade_in);
        mAnimation.setAnimationListener(this);
        t1.setAnimation(mAnimation);
        im1.setAnimation(mAnimation);


        new Handler().postDelayed(new Runnable() {
           @Override
           public void run() {
               setSharedPreferences();
           }
       },5000);
    }

   public void setSharedPreferences ()
   {
        mSharedPreferences = getSharedPreferences("Login Completed",MODE_PRIVATE);
        if (mSharedPreferences.getInt("Login",-1)==1)
        {
            Intent intent = new Intent(Splash.this,HomePage.class);

            String name = mSharedPreferences.getString("name","______");
            String mail = mSharedPreferences.getString("mail","______");
            String photo = mSharedPreferences.getString("photo","_____");
            intent.putExtra("Name",name);
            intent.putExtra("Mail",mail);
            intent.putExtra("Photo",photo);

            startActivity(intent);
            finish();
        }
        else
        {
            Intent intent = new Intent(Splash.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
   }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
