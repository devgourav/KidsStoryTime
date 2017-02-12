package com.beeblebroxlabs.kidsstorytime;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.plus.PlusShare;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    //Declare the ArrayLists where the value from each column of Database is stored.
    public static ArrayList<String> titles;
    public static ArrayList<String> contents;
    public static ArrayList<String> genre;
    public static ArrayList<String> storyImage;
    public static ArrayList<Integer> favoriteFlag;

    private ListView titleListView;
    private DatabaseAccess databaseAccess;

    /**
     * Method name:populateData
     * returns void
     * Fetches the data from the DB and populates the arrayLists.
     * */
    public void populateData(){
        databaseAccess.open();
        Cursor c = databaseAccess.getStory();

        System.out.println("populateData::getColumnCount::"+c.getColumnCount());
        System.out.println("populateData::getCount::"+c.getCount());


        int titleIndex = c.getColumnIndex("title");
        int contentIndex = c.getColumnIndex("content");
        int genreIndex = c.getColumnIndex("genre");
        int imageIdIndex = c.getColumnIndex("imageId");
        int favoriteFlagIndex = c.getColumnIndex("favoriteFlag");

        if(c.moveToFirst()){
            titles.clear();
            contents.clear();
            genre.clear();
            storyImage.clear();
            favoriteFlag.clear();
            while(c!=null && c.getPosition()<c.getCount()){
                titles.add(c.getString(titleIndex));
                contents.add(c.getString(contentIndex));
                genre.add(c.getString(genreIndex));
                storyImage.add(c.getString(imageIdIndex));
                favoriteFlag.add(c.getInt(favoriteFlagIndex));
                c.moveToNext();
            }
            System.out.println("storyImage::"+storyImage);
            System.out.println("favoriteFlag::"+favoriteFlag);
        }
    }

    /**
     * Method name:onCreate
     * returns void
     * Most important method.All initialization done here.
     * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //System.out.println("printKeyHash:"+printKeyHash(MainActivity.this));
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //Code for Google Admobs
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-7701527550430940~9917175713");
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //Initialize all ArrayList
        titles = new ArrayList<>();
        contents = new ArrayList<>();
        storyImage = new ArrayList<>();
        genre = new ArrayList<>();
        favoriteFlag = new ArrayList<>();

        databaseAccess = DatabaseAccess.getInstance(this);

        titleListView = (ListView) findViewById(R.id.titleListView);
        CustomListAdapter customListAdapter = new CustomListAdapter(this,titles,storyImage);
        titleListView.setAdapter(customListAdapter);

        populateData();

        titleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),StoryActivity.class);
                intent.putExtra("listPosition",position);
                startActivity(intent);
                System.out.println("position"+position);
            }
        });



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.share_to_fb) {
            ShareDialog shareDialog = new ShareDialog(this);
            ShareLinkContent shareLinkContent = new ShareLinkContent.Builder()
                    .setContentTitle("Download the \"Kids Story Time\" girl app")
                    .setContentUrl(Uri.parse("https://play.google.com/store/apps/details?id=com.beeblebroxlabs.kidsstorytime"))
                    .build();
            shareDialog.show(shareLinkContent);
        }else if(id == R.id.share_to_google){
            Intent shareIntent = new PlusShare.Builder(this)
                    .setType("text/plain")
                    .setText("Welcome to the Google+ platform.")
                    .setContentUrl(Uri.parse("https://developers.google.com/+/"))
                    .getIntent();

            startActivityForResult(shareIntent, 0);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

//    public static String printKeyHash(Activity context) {
//        PackageInfo packageInfo;
//        String key = null;
//        try {
//            //getting application package name, as defined in manifest
//            String packageName = context.getApplicationContext().getPackageName();
//
//            //Retriving package info
//            packageInfo = context.getPackageManager().getPackageInfo(packageName,
//                    PackageManager.GET_SIGNATURES);
//
//            Log.e("Package Name=", context.getApplicationContext().getPackageName());
//
//            for (Signature signature : packageInfo.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//                key = new String(Base64.encode(md.digest(), 0));
//
//                // String key = new String(Base64.encodeBytes(md.digest()));
//                Log.e("Key Hash=", key);
//            }
//        } catch (PackageManager.NameNotFoundException e1) {
//            Log.e("Name not found", e1.toString());
//        }
//        catch (NoSuchAlgorithmException e) {
//            Log.e("No such an algorithm", e.toString());
//        } catch (Exception e) {
//            Log.e("Exception", e.toString());
//        }
//
//        return key;
//    }

}

