package com.radiofm.radiofm;

//Importamos librerias

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

//Creamos la clase publica

public class StationList extends Activity implements OnItemClickListener {

	//Definimos variables

	private Handler handler;
	private ListView listView;
	private TextView empty;
	private ProgressBar progressBar;
	private DrawableListAdapter mAdapter;
	private FrameLayout layout;
	private InterstitialAd interstitial;
	private static boolean displayAd;
	private AdView adView;

	//Comienza la vida de nuestra activity con el metodo onCreate

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//creamos un hilo secundario (handler= interacción entre el hilo secundario y el hilo principal,
		// Thread=crea un nuevo hilo  Runnable= Hace que se ejecute el hilo del Thread  )
		handler = new Handler();

		new Thread(new Runnable() {
			@Override
			public void run() {
				handler.post(new Runnable() {
					@Override
					public void run() {
						try {
							setContentView(R.layout.activity_station_list);
							loadList();
						} catch (Exception e) {
							finish();
							e.printStackTrace();
						}
					}
				});
			}
		}).start();


	}


	public void loadList() {
		listView = (ListView) findViewById(R.id.list);
		listView.setOnItemClickListener(this);

		progressBar = (ProgressBar) findViewById(R.id.load);
		progressBar.setVisibility(View.VISIBLE);

		empty = (TextView) findViewById(R.id.empty);

		layout = (FrameLayout) findViewById(R.id.list_show);
		layout.setVisibility(View.GONE);

		new Thread(new Runnable() {
			@Override
			public void run() {
				mAdapter = new DrawableListAdapter(StationList.this);

				handler.post(new Runnable() {
					@Override
					public void run() {
						listView.setAdapter(mAdapter);
						progressBar.setVisibility(View.GONE);
						layout.setVisibility(View.VISIBLE);
						if (mAdapter.getCount() == 0) {
							listView.setVisibility(View.GONE);
							empty.setVisibility(View.VISIBLE);
						}
					}
				});
			}
		}).start();
	}

	@Override
	public void onItemClick(AdapterView<?> a, View v, int position, long l) {
		MainActivity.stationID = position;
		MainActivity.isStationChanged = true;

		interstitial = new InterstitialAd(StationList.this);
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

				Intent intent = new Intent(StationList.this, MainActivity.class);
				startActivity(intent);

			}


		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		unbindDrawables(findViewById(R.id.RootView));
		Runtime.getRuntime().gc();
	}

	private void unbindDrawables(View view) {
		try {
			if (view.getBackground() != null) {
				view.getBackground().setCallback(null);
			}
			if (view instanceof ViewGroup) {
				for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
					unbindDrawables(((ViewGroup) view).getChildAt(i));
				}
				((ViewGroup) view).removeAllViews();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}




}
