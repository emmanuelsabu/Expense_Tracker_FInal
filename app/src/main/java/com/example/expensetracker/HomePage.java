package com.example.expensetracker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.viewpager.widget.ViewPager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import static com.example.expensetracker.MainActivity.googleSignInOptions;

public class HomePage extends AppCompatActivity
{
    NavigationView mNavigationView;
    Toolbar mToolbar;
    DrawerLayout mDrawerLayout;
    TextView nametextview;
    CircularImageView mImageView;


    String name,mail;

    SharedPreferences mSharedPreferences;
    SharedPreferences.Editor mEditor;
    FirebaseAuth mAuth;

    TabLayout tablayout;
    ViewPager viewpager;


    private GoogleSignInClient googleSignInClient;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);

        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        mToolbar=findViewById(R.id.toolbar);
        mNavigationView=findViewById(R.id.navigation);
        mDrawerLayout=findViewById(R.id.drawerlayout);
        viewpager = findViewById(R.id.viewpager);
        viewpager.setOffscreenPageLimit(3);

        mSharedPreferences = getSharedPreferences("Login Completed",MODE_PRIVATE);
         name = mSharedPreferences.getString("name","______");
         mail = mSharedPreferences.getString("mail","______");


        View headerview = mNavigationView.getHeaderView(0);
        nametextview = headerview.findViewById(R.id.nametext1);
        nametextview.setText(name);

        mSharedPreferences=getSharedPreferences("Login Completed", MODE_PRIVATE);
        String url = mSharedPreferences.getString("DP","");
        if(url.equals(""))
        {

        }
        else
            {
            Uri image = Uri.parse(url);
            mImageView = headerview.findViewById(R.id.image1);
            Picasso.get().load(image).into(mImageView);
            }


        mAuth=FirebaseAuth.getInstance();

        tablayout = findViewById(R.id.tablayout);
        viewpager = findViewById(R.id.viewpager);

        ViewPagerAdapter_homepage adapter = new ViewPagerAdapter_homepage (getSupportFragmentManager());
        viewpager.setAdapter(adapter);
        addTabs(viewpager);

        tablayout.setupWithViewPager(viewpager);
        tablayout.setTabTextColors(Color.GRAY,Color.BLACK);


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(HomePage.this,
                mDrawerLayout,mToolbar,R.string.opendrawer,R.string.closedrawer);
        mDrawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull final MenuItem menuItem)
            {
                final int id = menuItem.getItemId();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        switch (id)
                        {
                            case R.id.profile:
                                Intent intent = new Intent(HomePage.this,Profiles.class);
                                intent.putExtra("names", name);
                                intent.putExtra("mail", mail);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slidi_in_left, R.anim.slide_out_left);

                                finish();
                                break;

                            case R.id.trans1:
                                Intent intentDaily = new Intent(HomePage.this,Transactions.class);
                                intentDaily.putExtra("Fragments","dailytransaction");
                                startActivity(intentDaily);
                                finish();
                                break;
                            case R.id.trans2:
                                Intent intentMonthlty = new Intent(HomePage.this,Transactions.class);
                                intentMonthlty.putExtra("Fragments","monthlytransaction");
                                startActivity(intentMonthlty);
                                finish();
                                break;

                            case R.id.trans3:
                                Intent intentYearly = new Intent(HomePage.this,Transactions.class);
                                intentYearly.putExtra("Fragments","yearlytransaction");
                                startActivity(intentYearly);
                                finish();
                                break;


                            case R.id.categ1:
                                Intent monthcat = new Intent(HomePage.this,Categorys.class);
                                monthcat.putExtra("Fragments","monthlycategory");
                                startActivity(monthcat);
                                finish();
                                break;
                            case R.id.categ2:
                                Intent yearcat = new Intent(HomePage.this,Categorys.class);
                                yearcat.putExtra("Fragments","yearlycategory");
                                startActivity(yearcat);
                                finish();
                                break;

                            case R.id.logout:
                                logout();
                                break;
                        }
                    }
                }, 200);

                mDrawerLayout.closeDrawer(GravityCompat.START);

                return true;
            }

        });
    }

    public void logout()
    {
        mAuth.signOut();
        googleSignInClient.signOut()
                .addOnCompleteListener(HomePage.this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        mSharedPreferences = getSharedPreferences("Login Completed", MODE_PRIVATE);
                        mEditor = mSharedPreferences.edit();
                        mEditor.putInt("Login", -1);
                        mEditor.apply();
                        Intent intent = new Intent(HomePage.this, MainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slidi_in_left, R.anim.slide_out_left);
                        finish();

                    }
                });
    }
    private void addTabs(ViewPager viewPager) {
        ViewPagerAdapter_homepage adapter = new ViewPagerAdapter_homepage(getSupportFragmentManager());
        adapter.addFrag(new DailyHomepage(),"Daily");
        adapter.addFrag(new MonthlyHomepage(), "Monthly");
        adapter.addFrag(new YearlyHomePage(), "Yearly");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onBackPressed()
    {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START))
        {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
        else {
            AlertDialog.Builder mBuilder;
            mBuilder = new AlertDialog.Builder(HomePage.this);

            mBuilder.setMessage("Do you want to Exit ?")
                    .setTitle("Exit")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            finishAffinity();
                            finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(HomePage.this, "Cancelled", Toast.LENGTH_SHORT).show();
                        }
                    });

            AlertDialog alertDialog = mBuilder.create();
            alertDialog.setTitle("Exit");
            alertDialog.show();
        }

    }
}
