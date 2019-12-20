package com.example.expensetracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

public class Profiles extends AppCompatActivity
{
    TextView t4,t5;
    String name,mail;
    CircularImageView mImageView;
    SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profiles);

        t4=findViewById(R.id.t4);
        t5=findViewById(R.id.t5);
        mImageView=findViewById(R.id.im1);

        mSharedPreferences = getSharedPreferences("Login Completed",MODE_PRIVATE);


        name = getIntent().getExtras().getString("names");
        mail = getIntent().getExtras().getString("mail");
        mSharedPreferences=getSharedPreferences("Login Completed", MODE_PRIVATE);
        String url = mSharedPreferences.getString("DP","");
        if(url.equals(""))
        {

        }
        else
        {
            Uri image = Uri.parse(url);
            Picasso.get().load(image).into(mImageView);
        }

        t4.setText(name);
        t5.setText(mail);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Profiles.this,HomePage.class);
        startActivity(intent);
    }
}
