package com.wall.fairytail;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.R.drawable;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

@SuppressLint("NewApi")
public class MainActivity extends Activity {
	
	private final R.raw rawResources = new R.raw();
	private final Class<R.raw> r = R.raw.class;
	private final Field[] fields = r.getDeclaredFields();
	private RelativeLayout content;
	private List<Integer> cards;
	private InterstitialAd interstitial;
	private int position = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		carregarADS_Banner();
		carregarADS_Interticial();
		getCards();
		content = (RelativeLayout) findViewById(R.id.layout_content);
		content.setBackground(getResources().getDrawable(cards.get(position)));
	}
	
	public List<Integer> getCards(){
		cards = new ArrayList<Integer>();

		for (int i = 0, max = fields.length; i < max; i++) {
		    try {
		    	if(!fields[i].getName().contains("fairy_tail")){
		    		continue;
		    	}else{
		    	cards.add(fields[i].getInt(rawResources));
		    	Log.d("DEBUG", fields[i].getName());
		    	}
		    } catch (Exception e) {
		    	Log.d("ERROR", e.getMessage());
		    }
		}
		return cards;
	}
	
	public void setWallpaper(View view){
		//Toast.makeText(this, "Set", Toast.LENGTH_SHORT).show();
		
		WallpaperManager manager = WallpaperManager.getInstance(getApplicationContext());
		try {
			manager.setResource(cards.get(position));
			Toast.makeText(this, "Applied!", Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			Log.d("ERROR", e.getMessage());
			Toast.makeText(this, "Fail!", Toast.LENGTH_LONG).show();
		}
	}
	
	public void next(View view){
		//Toast.makeText(this, "Next", Toast.LENGTH_SHORT).show();
		if(position == cards.size()-1){
			position = 0;
			content.setBackground(getResources().getDrawable(cards.get(position)));
		}
		else{
			position++;
			content.setBackground(getResources().getDrawable(cards.get(position)));
		}
	}
	
	public void previous(View view){
		//Toast.makeText(this, "Previous", Toast.LENGTH_SHORT).show();
		if(position == 0){
			position = cards.size()-1;
			content.setBackground(getResources().getDrawable(cards.get(position)));
		}
		else{
			position--;
			content.setBackground(getResources().getDrawable(cards.get(position)));
		}
	}
	
	public void carregarADS_Banner(){
		try {
			RelativeLayout banner = (RelativeLayout) findViewById(R.id.banner);
			
			AdView ads = new AdView(this);
			ads.setAdSize(AdSize.SMART_BANNER);
			
			// insira o código do banner aqui
			
			ads.setAdUnitId("ca-app-pub-4967173191053190/8475327465");
			AdRequest request = new AdRequest.Builder().build();
			
			//seta o device como despositivo de teste para impedir que você clique nos bannes por acidente
			request.isTestDevice(this);
			
			banner.addView(ads);			
			ads.loadAd(request);
			
		} catch (Exception e) {
			Log.e("carregarADS_Banner", e.getMessage());
		}
	}
	
	 public void carregarADS_Interticial(){
			try {
				interstitial = new InterstitialAd(MainActivity.this);
				interstitial.setAdUnitId("ca-app-pub-4967173191053190/9952060664");
				
				AdRequest request = new AdRequest.Builder().build();
				request.isTestDevice(this);
				
				interstitial.loadAd(request);
				
			} catch (Exception e) {
				Log.e("carregarADS_Interticial", e.getMessage());
			}
	}
	 public void displayInterstitial() {
		  if (interstitial.isLoaded()) {
			  interstitial.show();
		   }
	 }
	 
	 @Override
	protected void onDestroy() {
		// Quando a aplicação for fechada um ads intersticial será carregado
		 //displayInterstitial();
		super.onDestroy();
	}
}

