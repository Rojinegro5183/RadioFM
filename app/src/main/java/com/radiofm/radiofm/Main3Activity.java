package com.radiofm.radiofm;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

public class Main3Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout mDrawerlayout ;
    private ActionBarDrawerToggle mToogle ;
    private InterstitialAd interstitial;
    private AdView adView;
    private static boolean isExitMenuClicked;
    private Intent bindIntent;
    private RadioService radioService;
    InterstitialAd mInterstitialAd;
    AdView mAdView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        isExitMenuClicked = false;

        bindIntent = new Intent(this, RadioService.class);
        bindService(bindIntent, radioConnection, Context.BIND_AUTO_CREATE);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        mAdView = (AdView) findViewById(R.id.adVista);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mDrawerlayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToogle = new ActionBarDrawerToggle(this,mDrawerlayout,R.string.open,R.string.close);
        mDrawerlayout.addDrawerListener(mToogle);
        mToogle.syncState();
        interstitial = new InterstitialAd(Main3Activity.this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isExitMenuClicked == true)
            finish();
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        final String thisClassName = this.getClass().getName();
        final String thisPackageName = this.getPackageName();

        if (mToogle.onOptionsItemSelected(item)) {
            return true ;
        }
        int id = item.getItemId();


        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.radio) {

            interstitial = new InterstitialAd(Main3Activity.this);
            interstitial.setAdUnitId(getString(R.string.admob_interstitial_id));
            AdRequest adRequest = new AdRequest.Builder().build();

            interstitial.loadAd(adRequest);
            interstitial.setAdListener(new AdListener() {


                public void onAdLoaded() {
                    if (interstitial.isLoaded()) {
                        interstitial.show();

                    }


                }
                @Override
                public void onAdClosed() {

                    Intent intent = new Intent(Main3Activity.this, StationList.class);
                    startActivity(intent);

                }


            });


        }else if(id == R.id.stations){

            interstitial = new InterstitialAd(Main3Activity.this);
            interstitial.setAdUnitId(getString(R.string.admob_interstitial_id));
            AdRequest adRequest = new AdRequest.Builder().build();

            interstitial.loadAd(adRequest);
            interstitial.setAdListener(new AdListener() {


                public void onAdLoaded() {
                    if (interstitial.isLoaded()) {
                        interstitial.show();

                    }


                }
                @Override
                public void onAdClosed() {

                    Intent intent = new Intent(Main3Activity.this, MainActivity.class);
                    startActivity(intent);

                }






            });



        }else if(id == R.id.facebook){
            Intent searchIntent = new Intent(Main3Activity.this, Webview.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

        }else if(id == R.id.exit){

            String title = "Salir de la Radio";
            String message = "Estas seguro que quieres salir de la App?";
            String buttonYesString = "Sí";
            String buttonNoString = "No";

            isExitMenuClicked = true;

            AlertDialog.Builder ad = new AlertDialog.Builder(this);
            ad.setTitle(title);
            ad.setMessage(message);
            ad.setCancelable(true);
            ad.setPositiveButton(buttonYesString,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            if (radioService != null) {
                                radioService.exitNotification();
                                radioService.stop();
                                radioService.stopService(bindIntent);
                                isExitMenuClicked = true;
                                finish();
                            }
                        }
                    }).setNegativeButton(buttonNoString, null);

            ad.show();

            return true;

        }







        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private final ServiceConnection radioConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            radioService = ((RadioService.RadioBinder) service).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName className) {
            radioService = null;
        }
    };

}