package com.beeblebroxlabs.kidsstorytime;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

/**
 * Class StoryActivity
 * methods:onCreate()
 * It is used to display the content after the user clicks on a story
 * */
public class StoryActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(Color.parseColor("#1565C0"));
        }


        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        Toolbar storyToolbar = (Toolbar) findViewById(R.id.story_toolbar);

        setSupportActionBar(storyToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);




        MobileAds.initialize(getApplicationContext(), "ca-app-pub-7701527550430940~9917175713");
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        ImageView contentImageView = (ImageView)findViewById(R.id.contentImageView);
        TextView contentTextView = (TextView)findViewById(R.id.ContentTextView);
        Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/BandaRegular.otf");
        contentTextView.setTypeface(typeface);


        Intent intent = getIntent();
        String title = MainActivity.titles.get(intent.getIntExtra("listPosition",0));
        String content = MainActivity.contents.get(intent.getIntExtra("listPosition",0));
        String imgId = MainActivity.storyImage.get(intent.getIntExtra("listPosition",0));
        collapsingToolbar.setTitle(title);
        System.out.println("imgId"+imgId);

        int resId = getResources().getIdentifier(imgId,"raw",getPackageName());


        contentTextView.setText(content);
        contentImageView.setImageResource(resId);

    }

}
