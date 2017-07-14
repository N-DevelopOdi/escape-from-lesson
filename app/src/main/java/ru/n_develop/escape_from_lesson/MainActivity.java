package ru.n_develop.escape_from_lesson;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.appodeal.ads.Appodeal;

public class MainActivity extends AppCompatActivity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		Appodeal.disableNetwork(this, "cheetah");
		String appKey = "b813a660819ba4de319bc8cb913c6cac24411889cb75ef88";

		Appodeal.initialize(this, appKey, Appodeal.INTERSTITIAL);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (savedInstanceState == null)
		{
			getSupportFragmentManager().beginTransaction().add(R.id.container, new MainFragment()).commit();
		}
	}
}

