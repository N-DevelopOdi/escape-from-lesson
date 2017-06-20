package ru.n_develop.escape_from_lesson;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.Random;

public class MainActivity extends AppCompatActivity
{

	TextView textView;

	private ImageView escapeImageView;
	private ImageView notEscapeImageView;
	private Animation mFadeInAnimation;

	private Boolean escapeBool;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{


		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		textView = (TextView) findViewById(R.id.textView);

		escapeImageView = (ImageView) findViewById(R.id.escape);
		notEscapeImageView = (ImageView) findViewById(R.id.notescape);

		// подключаем файл анимации
		mFadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.alphain);
		mFadeInAnimation.setAnimationListener(animationFadeInListener);

	}

	public void onClick (View view)
	{

		escapeImageView.setVisibility(View.GONE);
		notEscapeImageView.setVisibility(View.GONE);
		escape();
	}

	Animation.AnimationListener animationFadeInListener = new Animation.AnimationListener() {
		@Override
		public void onAnimationEnd(Animation animation) {
			if (escapeBool)
			{
				notEscapeImageView.setVisibility(View.GONE);
				escapeImageView.setVisibility(View.VISIBLE);
			}
			else
			{
				escapeImageView.setVisibility(View.GONE);
				notEscapeImageView.setVisibility(View.VISIBLE);
			}
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onAnimationStart(Animation animation) {
			// TODO Auto-generated method stub
			if (escapeBool)
			{
				notEscapeImageView.setVisibility(View.GONE);
				escapeImageView.setVisibility(View.VISIBLE);
			}
			else
			{
				escapeImageView.setVisibility(View.GONE);
				notEscapeImageView.setVisibility(View.VISIBLE);
			}

		}
	};


	private void escape ()
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

		Random rand = new Random();

		if (escape > notEscape)
		{
			notEscapeImageView.setVisibility(View.GONE);
			notEscapeImageView.clearAnimation();
			escapeBool = true;
			int r = rand.nextInt(120)+1;
			if (r < 20)
			{
				escapeImageView.setImageResource(R.drawable.escape1);
			}
			else if (r > 20 && r < 40)
			{
				escapeImageView.setImageResource(R.drawable.escape2);

			}
			else if (r > 40 && r < 60)
			{
				escapeImageView.setImageResource(R.drawable.escape3);

			}
			else if (r > 60 && r < 80)
			{
				escapeImageView.setImageResource(R.drawable.escape4);

			}
			else if (r > 80 && r < 100)
			{
				escapeImageView.setImageResource(R.drawable.escape5);

			}
			else if (r > 100 && r < 120)
			{
				escapeImageView.setImageResource(R.drawable.escape6);

			}

			// при запуске начинаем с анимации исчезновения
			escapeImageView.setVisibility(View.VISIBLE);
			escapeImageView.startAnimation(mFadeInAnimation);


//			result = "ребята, удачи вам, валите из этой дыры";
//			result = "погода норм, пойдем гулять";
//			result = "го за шаурмой";
//			result = "го в кино";
//			result = "го бахнем";
//			result = "валим-валим-валим";
		}
		else
		{
			escapeImageView.setVisibility(View.GONE);
			escapeImageView.clearAnimation();

			escapeBool = false;

			int r = rand.nextInt(100)+1;
			if (r < 20)
			{
				notEscapeImageView.setImageResource(R.drawable.notescape1);
			}
			else if (r > 20 && r < 40)
			{
				notEscapeImageView.setImageResource(R.drawable.notescape2);

			}
			else if (r > 40 && r < 60)
			{
				notEscapeImageView.setImageResource(R.drawable.notescape3);

			}
			else if (r > 60 && r < 80)
			{
				notEscapeImageView.setImageResource(R.drawable.notescape4);

			}else if (r > 80 && r < 100)
			{
				notEscapeImageView.setImageResource(R.drawable.notescape5);

			}
			notEscapeImageView.setVisibility(View.VISIBLE);
			notEscapeImageView.startAnimation(mFadeInAnimation);
//			result = "к сожалению, остаемся на паре";
//			result = "сегодня не наш день. остаемся";
//			result = "сиди";
//			result = "неа";
//			result = "неудачный момент, чтобы слиться";
		}
	}



}
