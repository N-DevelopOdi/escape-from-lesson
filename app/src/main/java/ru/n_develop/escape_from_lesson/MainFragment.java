package ru.n_develop.escape_from_lesson;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.appodeal.ads.Appodeal;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Random;

import ru.n_develop.escape_from_lesson.Helper.Connection;
import ru.n_develop.escape_from_lesson.Helper.DBHelper;
import ru.n_develop.escape_from_lesson.Helper.ZaprosPhp;

public class MainFragment extends Fragment implements View.OnClickListener
{
	private ImageView escapeImageView;
	private ImageView notEscapeImageView;

	private Animation mFadeInAnimation;
	private Animation mPreInAnimationLeft;
	private Animation mPreOutAnimationLeft;
	private Animation mPreInAnimationRight;
	private Animation mPreOutAnimationRight;
	private ImageButton bShare;

	private Boolean escapeBool;
	private Boolean preAnim = false;
	private Boolean startAnim = true;

	int countClick = 1;
	View viewMain;

	public RelativeLayout view1;

	Intent shareIntent;
	String shareText;
	private ZaprosPhp Zapros1;


	DBHelper dbHelper;
	SQLiteDatabase database;
	String idUser;
	Integer isSend;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{

		viewMain = inflater.inflate(R.layout.fragment_main, container, false);
		viewMain.findViewById(R.id.button).setOnClickListener(this);
		viewMain.findViewById(R.id.buttonShare).setOnClickListener(this);

		view1 = (RelativeLayout)viewMain.findViewById(R.id.fragment_main);

		bShare= (ImageButton)viewMain.findViewById(R.id.buttonShare);
		bShare.setVisibility(View.GONE);

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

		dbHelper = new DBHelper(viewMain.getContext());
		database = dbHelper.getWritableDatabase();

		return viewMain;
	}

	public void onClick (final View view)
	{
		this.__isStart();
		if (Connection.hasConnection(MainFragment.this.getContext()) && isSend != 1)
		{
			this.__sendRegistration();
		}

			switch (view.getId())
		{
			case R.id.button: buttonEscape(); break;
			case R.id.buttonShare: buttonShare(); break;
		}
	}

	public void buttonEscape ()
	{
		if (startAnim)
		{
			startAnim = false;

			escapeImageView.setVisibility(View.GONE);
			notEscapeImageView.setVisibility(View.GONE);
			bShare.setVisibility(View.GONE);

			escapeImageView.setImageResource(R.drawable.preescape);

//		 при запуске начинаем с анимации исчезновения
			escapeImageView.setVisibility(View.VISIBLE);
			escapeImageView.startAnimation(mPreInAnimationLeft);

			preAnim = true;

			new Handler().postDelayed(new Runnable()
			{
				@Override
				public void run()
				{
					notEscapeImageView.setImageResource(R.drawable.prenotescape);
					notEscapeImageView.setVisibility(View.VISIBLE);
					notEscapeImageView.startAnimation(mPreInAnimationRight);

				}
			}, 300);

			new Handler().postDelayed(new Runnable()
			{
				@Override
				public void run()
				{
					preAnim = false;
					startAnim = true;
					countClick++;
					escape();

					if (countClick == 2)
					{
						ImageButton bt = (ImageButton) viewMain.findViewById(R.id.button);
						bt.setImageResource(R.drawable.button);
					}
					bShare.setVisibility(View.VISIBLE);
				}
			}, 3000);

		}
	}

	public void buttonShare ()
	{
		if (Connection.hasConnection(MainFragment.this.getContext()))
		{
			View v1 = view1.getRootView();
			v1.setDrawingCacheEnabled(false);
			v1.setDrawingCacheEnabled(true);

			final Bitmap screenshot = v1.getDrawingCache();
			Uri uri = Uri.fromFile(this.SavePicture(screenshot));

			database.execSQL("UPDATE " + DBHelper.TABLE_SHARE +
					" SET " + DBHelper.KEY_COUNT_SHARE + " = " + DBHelper.KEY_COUNT_SHARE + " + 1 " +
					" WHERE 1" );

			shareIntent = new Intent(android.content.Intent.ACTION_SEND);
			shareIntent.setType("*/*");
//			shareIntent.putExtra(Intent.EXTRA_SUBJECT, "MY APP");
//			shareIntent.putExtra(Intent.EXTRA_TEXT, shareText + "https://play.google.com/store/apps/details?id=ru.n_develop.escape_from_lesson" );
			shareIntent.putExtra(Intent.EXTRA_TEXT, shareText + " #cвалитьлиспары " + "https://goo.gl/CmRAb2" );
			shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
			shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
			shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			startActivity(Intent.createChooser(shareIntent,"Поделиться"));
		}
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

			shareText = "Мы валим c пары.\n" + "А что выпадет тебе? ";

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
			shareText = "Остаемся на паре(\n" + "Мб тебе повезет? ";

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

    private File SavePicture(Bitmap bmp)
    {
        OutputStream fOut = null;
		File dest = new File(this.getContext().getExternalCacheDir(), "efl");
		boolean result = dest.mkdirs();
		File file;
		try {
            file = new File(dest, System.currentTimeMillis()/1000 +".jpg"); // создать уникальное имя для файла основываясь на дате сохранения
            fOut = new FileOutputStream(file);

            bmp.compress(Bitmap.CompressFormat.JPEG, 85, fOut); // сохранять картинку в jpeg-формате с 85% сжатия.
            fOut.flush();
            fOut.close();
			return file;
        }
        catch (Exception e) // здесь необходим блок отслеживания реальных ошибок и исключений, общий Exception приведен в качестве примера
        {
			return null;
        }
    }

	private void __isStart ()
	{
		Cursor cursor = database.query(DBHelper.TABLE_START,
				new String[]{DBHelper.KEY_ID, DBHelper.KEY_USER, DBHelper.KEY_IS_START, DBHelper.KEY_IS_SEND},
				null,
				null,
				null, null, null);

		if (cursor.moveToFirst())
		{
			int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
			int userIndex = cursor.getColumnIndex(DBHelper.KEY_USER);
			int startIndex = cursor.getColumnIndex(DBHelper.KEY_IS_START);
			int sendIndex = cursor.getColumnIndex(DBHelper.KEY_IS_SEND);

			idUser = cursor.getString(userIndex);
			isSend = cursor.getInt(sendIndex);
			Log.e("sqlite idIndex = ", cursor.getString(idIndex));
			Log.e("sqlite userIndex = ", cursor.getString(userIndex));
			Log.e("sqlite startIndex = ", cursor.getString(startIndex));
			Log.e("sqlite sendIndex = ", cursor.getString(sendIndex));

			if (cursor.getInt(startIndex) == 0)
			{
				database.execSQL("UPDATE " + DBHelper.TABLE_START +
						" SET " + DBHelper.KEY_IS_START + " = 1 " +
						" WHERE " + DBHelper.KEY_USER  + " = " + idUser);
			}
		}
	}

	private void __sendRegistration()
	{
		Zapros1 = new ZaprosPhp();
		Zapros1.start(idUser);


		try
		{
			Zapros1.join();// ждем зовершения потока
			Log.e("zapros = ", Integer.toString(Zapros1.getSuccess()));
		}catch(InterruptedException ie)
		{
			Log.e("pass 0", ie.getMessage());
		}

		if (Zapros1.getSuccess() == 1)
		{
			database.execSQL("UPDATE " + DBHelper.TABLE_START +
					" SET " + DBHelper.KEY_IS_SEND + " = 1 " +
					" WHERE " + DBHelper.KEY_USER  + " = " + idUser);
		}
	}
}


/**
 *
 * Поделиться появляется когда нет инета
 * При каждом нажатии на кнопку уходит сообщение в БД
 */