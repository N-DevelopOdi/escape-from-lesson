package ru.n_develop.escape_from_lesson;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import com.appodeal.ads.Appodeal;

import java.util.Random;

public class MainActivity extends AppCompatActivity
{

	TextView textView;

	private ImageView escapeImageView;
	private ImageView notEscapeImageView;

	private Animation mFadeInAnimation;
    private Animation mPreInAnimationLeft;
    private Animation mPreOutAnimationLeft;
    private Animation mPreInAnimationRight;
    private Animation mPreOutAnimationRight;



	private Boolean escapeBool;
	private Boolean preAnim = false;

	int countClick = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{

		Appodeal.disableNetwork(this, "cheetah");
		String appKey = "b813a660819ba4de319bc8cb913c6cac24411889cb75ef88";

		Appodeal.confirm(Appodeal.SKIPPABLE_VIDEO);
		Appodeal.initialize(this, appKey, Appodeal.INTERSTITIAL);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		textView = (TextView) findViewById(R.id.textView);

		escapeImageView = (ImageView) findViewById(R.id.escape);
		notEscapeImageView = (ImageView) findViewById(R.id.notescape);

		// подключаем файл анимации
		mFadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.alphain);
		mFadeInAnimation.setAnimationListener(animationFadeInListener);


        // подключаем файл анимации
        mPreInAnimationLeft = AnimationUtils.loadAnimation(this, R.anim.alphain);
        mPreInAnimationLeft.setAnimationListener(animationPreInListenerLeft);
		// подключаем файл анимации
		mPreOutAnimationLeft = AnimationUtils.loadAnimation(this, R.anim.alphaout);
		mPreOutAnimationLeft.setAnimationListener(animationPreOutListenerLeft);

        // подключаем файл анимации
        mPreInAnimationRight = AnimationUtils.loadAnimation(this, R.anim.alphain);
        mPreInAnimationRight.setAnimationListener(animationPreInListenerRight);
        // подключаем файл анимации
        mPreOutAnimationRight = AnimationUtils.loadAnimation(this, R.anim.alphaout);
        mPreOutAnimationRight.setAnimationListener(animationPreOutListenerRight);

	}

	public void onClick (View view)
	{
		escapeImageView.setVisibility(View.GONE);
		notEscapeImageView.setVisibility(View.GONE);

		escapeImageView.setImageResource(R.drawable.preescape);

//		 при запуске начинаем с анимации исчезновения
		escapeImageView.setVisibility(View.VISIBLE);
		escapeImageView.startAnimation(mPreInAnimationLeft);

        preAnim = true;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                notEscapeImageView.setImageResource(R.drawable.prenotescape);
                notEscapeImageView.setVisibility(View.VISIBLE);
                notEscapeImageView.startAnimation(mPreInAnimationRight);

            }
        }, 300);


		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
                preAnim = false;
				countClick++;
				escape();

				if (countClick == 2)
				{
					ImageButton bt = (ImageButton)findViewById(R.id.button);
					bt.setImageResource(R.drawable.button);
				}
			}
		}, 5000);


	}

	Animation.AnimationListener animationFadeInListener = new Animation.AnimationListener()
	{
		@Override
		public void onAnimationEnd(Animation animation)
		{

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

			// делаем паузу в пол секунды перед рекламой
			if (countClick % 5 == 0)
			{
				if(Appodeal.isLoaded(Appodeal.INTERSTITIAL))
				{
					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							Appodeal.show(MainActivity.this, Appodeal.INTERSTITIAL);
						}
					}, 500);

				}
			}
		}

		@Override
		public void onAnimationRepeat(Animation animation)
		{
			// TODO Auto-generated method stub
		}

		@Override
		public void onAnimationStart(Animation animation)
		{
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









    Animation.AnimationListener animationPreOutListenerLeft = new Animation.AnimationListener() {

        @Override
        public void onAnimationEnd(Animation animation) {
            if (preAnim)
            {
                escapeImageView.startAnimation(mPreInAnimationLeft);
//                notEscapeImageView.startAnimation(mPreInAnimationLeft);
            }
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

    Animation.AnimationListener animationPreInListenerLeft = new Animation.AnimationListener() {
        @Override
        public void onAnimationEnd(Animation animation) {
            if (preAnim)
            {
                escapeImageView.startAnimation(mPreOutAnimationLeft);
//                notEscapeImageView.startAnimation(mPreOutAnimationLeft);
            }

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

    Animation.AnimationListener animationPreOutListenerRight = new Animation.AnimationListener() {

        @Override
        public void onAnimationEnd(Animation animation) {
            if (preAnim)
            {
//                escapeImageView.startAnimation(mPreInAnimationLeft);
                notEscapeImageView.startAnimation(mPreInAnimationRight);
            }
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

    Animation.AnimationListener animationPreInListenerRight = new Animation.AnimationListener() {
        @Override
        public void onAnimationEnd(Animation animation) {
            if (preAnim)
            {
//                escapeImageView.startAnimation(mPreOutAnimationLeft);
                notEscapeImageView.startAnimation(mPreOutAnimationRight);
            }

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

