package ru.n_develop.escape_from_lesson;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity
{

	TextView textView;

	private ImageView mImageView;
	private Animation mFadeInAnimation, mFadeOutAnimation;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		textView = (TextView) findViewById(R.id.textView);

		mImageView = (ImageView) findViewById(R.id.imageView);

		// подключаем файл анимации
		mFadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.alphain);
		mFadeOutAnimation = AnimationUtils.loadAnimation(this, R.anim.alphaout);
		mFadeInAnimation.setAnimationListener(animationFadeInListener);
		mFadeOutAnimation.setAnimationListener(animationFadeOutListener);

	}

	public void onClick (View view)
	{


		// при запуске начинаем с анимации исчезновения
		mImageView.startAnimation(mFadeInAnimation);

		textView.setText(escape());


	}


	Animation.AnimationListener animationFadeOutListener = new Animation.AnimationListener() {

		@Override
		public void onAnimationEnd(Animation animation) {
//			mImageView.startAnimation(mFadeInAnimation);
//			mImageView.setVisibility(View.GONE);
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onAnimationStart(Animation animation) {
			// TODO Auto-generated method stub
		}
	};


	Animation.AnimationListener animationFadeInListener = new Animation.AnimationListener() {
		@Override
		public void onAnimationEnd(Animation animation) {
//			mImageView.startAnimation(mFadeOutAnimation);
//			mImageView.setVisibility(View.VISIBLE);
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onAnimationStart(Animation animation) {
			// TODO Auto-generated method stub
			mImageView.setVisibility(View.VISIBLE);

		}
	};


	private String escape ()
	{
		int escape = 0;
		int notEscape = 0;
		for (int i = 0; i < 3; i++)
		{
			Random rand = new Random();
			int h = rand.nextInt(100)+1;
			if (h % 2 == 0) escape++;
			else notEscape++;
		}

		String result;
		if (escape > notEscape)
		{
			result = "ребята удачи вам валите из этой дыры";
			result = "погода норм пойдем гулять";
			result = "го за шурмой";
			result = "го в кино";
			result = "го бахнем";
			result = "валим валим валим";
		}
		else
		{
			result = "к сожалению остаемся на паре";
			result = "сегодня не наш день. оставемся";
			result = "сиди";
			result = "неа";
			result = "неудачный момент чтобы слиться";
		}

		return result;
	}



}
