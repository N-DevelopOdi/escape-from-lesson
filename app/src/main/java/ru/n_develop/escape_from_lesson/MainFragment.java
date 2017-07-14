package ru.n_develop.escape_from_lesson;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.appodeal.ads.Appodeal;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.model.VKApiPhoto;
import com.vk.sdk.api.model.VKPhotoArray;
import com.vk.sdk.api.photo.VKImageParameters;
import com.vk.sdk.api.photo.VKUploadImage;
import com.vk.sdk.dialogs.VKShareDialog;
import com.vk.sdk.dialogs.VKShareDialogBuilder;

import java.io.IOException;
import java.util.Random;

public class MainFragment extends Fragment implements View.OnClickListener
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
	View viewMain;


	private static final String[] sMyScope = new String[]{
			VKScope.FRIENDS,
			VKScope.WALL,
			VKScope.PHOTOS,
			VKScope.NOHTTPS,
			VKScope.MESSAGES,
			VKScope.DOCS
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		viewMain = inflater.inflate(R.layout.fragment_main, container, false);
		viewMain.findViewById(R.id.button).setOnClickListener(this);

		textView = (TextView) viewMain.findViewById(R.id.textView);

		escapeImageView = (ImageView) viewMain.findViewById(R.id.escape);
		notEscapeImageView = (ImageView) viewMain.findViewById(R.id.notescape);

		// подключаем файл анимации
		mFadeInAnimation = AnimationUtils.loadAnimation(this.getContext(), R.anim.alphain);
		mFadeInAnimation.setAnimationListener(animationFadeInListener);


        // подключаем файл анимации прелоадера
        mPreInAnimationLeft = AnimationUtils.loadAnimation(this.getContext(), R.anim.alphainpre);
		mPreOutAnimationLeft = AnimationUtils.loadAnimation(this.getContext(), R.anim.alphaoutpre);
		mPreInAnimationLeft.setAnimationListener(animationPreInListenerLeft);
		mPreOutAnimationLeft.setAnimationListener(animationPreOutListenerLeft);

        mPreInAnimationRight = AnimationUtils.loadAnimation(this.getContext(), R.anim.alphainpre);
		mPreOutAnimationRight = AnimationUtils.loadAnimation(this.getContext(), R.anim.alphaoutpre);
		mPreInAnimationRight.setAnimationListener(animationPreInListenerRight);
        mPreOutAnimationRight.setAnimationListener(animationPreOutListenerRight);


		return viewMain;
	}

	public void onClick (View view)
	{

		if (!VKSdk.isLoggedIn())
		{
			VKSdk.login(MainFragment.this.getActivity(), sMyScope);
		}

		final Bitmap b = getPhoto();
		VKPhotoArray photos = new VKPhotoArray();
		photos.add(new VKApiPhoto("photo-47200925_314622346"));
		new VKShareDialogBuilder()
				.setText("I created this post with VK Android SDK\nSee additional information below\n#vksdk")
				.setUploadedPhotos(photos)
				.setAttachmentImages(new VKUploadImage[]{
						new VKUploadImage(b, VKImageParameters.pngImage())
				})
				.setAttachmentLink("VK Android SDK information", "https://vk.com/dima_nofficial")
				.setShareDialogListener(new VKShareDialog.VKShareDialogListener() {
					@Override
					public void onVkShareComplete(int postId) {
						recycleBitmap(b);
					}

					@Override
					public void onVkShareCancel() {
						recycleBitmap(b);
					}

					@Override
					public void onVkShareError(VKError error) {
						recycleBitmap(b);
					}
				})
				.show(getFragmentManager(), "VK_SHARE_DIALOG");



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
					ImageButton bt = (ImageButton) viewMain.findViewById(R.id.button);
					bt.setImageResource(R.drawable.button);
				}
			}
		}, 3000);


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
							Appodeal.show(MainFragment.this.getActivity(), Appodeal.INTERSTITIAL);
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
			if (r <= 20)
			{
				escapeImageView.setImageResource(R.drawable.escape1);
			}
			else if (r > 20 && r <= 40)
			{
				escapeImageView.setImageResource(R.drawable.escape2);

			}
			else if (r > 40 && r <= 60)
			{
				escapeImageView.setImageResource(R.drawable.escape3);

			}
			else if (r > 60 && r <= 80)
			{
				escapeImageView.setImageResource(R.drawable.escape4);

			}
			else if (r > 80 && r <= 100)
			{
				escapeImageView.setImageResource(R.drawable.escape5);

			}
			else if (r > 100)
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
			if (r <= 20)
			{
				notEscapeImageView.setImageResource(R.drawable.notescape1);
			}
			else if (r > 20 && r <= 40)
			{
				notEscapeImageView.setImageResource(R.drawable.notescape2);

			}
			else if (r > 40 && r <= 60)
			{
				notEscapeImageView.setImageResource(R.drawable.notescape3);

			}
			else if (r > 60 && r <= 80)
			{
				notEscapeImageView.setImageResource(R.drawable.notescape4);

			}else if (r > 80)
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

	private static void recycleBitmap(@Nullable final Bitmap bitmap) {
		if (bitmap != null) {
			bitmap.recycle();
		}
	}

	private Bitmap getPhoto() {
		try {
			return BitmapFactory.decodeStream(getActivity().getAssets().open("android.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}

