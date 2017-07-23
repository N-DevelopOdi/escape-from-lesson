package ru.n_develop.escape_from_lesson;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.appodeal.ads.Appodeal;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

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

	/**
	 * Проверка ответа от авторизации вк, если все ок то в onResult приходит, если отказ то в onError
	 * @param requestCode
	 * @param resultCode
	 * @param data
     */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		VKCallback<VKAccessToken> callback = new VKCallback<VKAccessToken>() {
			@Override
			public void onResult(VKAccessToken res) {
				Log.e("onResult","onResult");
				// User passed Authorization
//				startTestActivity();
			}

			@Override
			public void onError(VKError error) {
				Log.e("onError","onError");

				// User didn't pass Authorization
			}
		};

		if (!VKSdk.onActivityResult(requestCode, resultCode, data, callback)) {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}

}

