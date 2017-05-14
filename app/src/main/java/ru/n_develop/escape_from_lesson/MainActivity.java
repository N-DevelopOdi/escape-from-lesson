package ru.n_develop.escape_from_lesson;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity
{

	TextView textView;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		textView = (TextView) findViewById(R.id.textView);

	}

	public void onClick (View view)
	{




		textView.setText(escape());


	}

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
