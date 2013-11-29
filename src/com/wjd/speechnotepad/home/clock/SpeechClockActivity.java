package com.wjd.speechnotepad.home.clock;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.baidu.voicerecognition.android.ui.BaiduASRDigitalDialog;
import com.baidu.voicerecognition.android.ui.DialogRecognitionListener;
import com.wjd.speechnotepad.R;
import com.wjd.speechnotepad.base.BaseActivity;
import com.wjd.speechnotepad.handler.MainHandler;
import com.wjd.speechnotepad.home.fragment.CreateFragment;
import com.wjd.speechnotepad.util.DateUtil;
import com.wjd.speechnotepad.util.Loger;

public class SpeechClockActivity extends BaseActivity implements
		OnClickListener, DialogRecognitionListener
{

	private static final String API_KEY = "gK0f2d7dGCHuKeLxnzMU0S0T";

	private static final String SECRET_KEY = "xEIZVBVTHDZx5eSE7isEbbzzMlILGWmf";

	public static void actionLuanch(Activity activity)
	{
		activity.startActivity(new Intent(activity.getBaseContext(),
				SpeechClockActivity.class));
	}

	private BaiduASRDigitalDialog mDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.speechnotepad_home_speech_clock_layout);
		setSecondTitleBar(getString(R.string.speak_clock));
		findViewById(R.id.btn_record).setOnClickListener(this);
	}

	private void showRecordDialog()
	{
		Bundle params = new Bundle();
		params.putString(BaiduASRDigitalDialog.PARAM_API_KEY, API_KEY);
		params.putString(BaiduASRDigitalDialog.PARAM_SECRET_KEY, SECRET_KEY);
		params.putInt(BaiduASRDigitalDialog.PARAM_SPEECH_MODE,
				BaiduASRDigitalDialog.SPEECH_MODE_SEARCH);
		params.putBoolean(BaiduASRDigitalDialog.PARAM_NLU_ENABLE, true);
		params.putInt(BaiduASRDigitalDialog.PARAM_DIALOG_THEME,
				BaiduASRDigitalDialog.THEME_BLUE_LIGHTBG);
		mDialog = new BaiduASRDigitalDialog(this, params);
		mDialog.setDialogRecognitionListener(this);
		mDialog.show();
	}

	@Override
	public void onClick(View v)
	{
		if (R.id.btn_record == v.getId())
		{
			showRecordDialog();
		}
	}

	@Override
	protected void onDestroy()
	{
		if (null != mDialog)
		{
			mDialog.dismiss();
			mDialog = null;
		}
		super.onDestroy();
	}

	@Override
	public void onResults(Bundle arg0)
	{
		ArrayList<String> rs = arg0 != null ? arg0
				.getStringArrayList(RESULTS_RECOGNITION) : null;
		if (null != rs && !rs.isEmpty())
		{
			try
			{
				JSONObject jobj = new JSONObject(rs.get(0));
				String str = jobj.getString("json_res");
				JSONObject jres = new JSONObject(str);
				JSONArray jresult = jres.getJSONArray("results");
				handleSelectedTime(handleJresult(jresult));
			} catch (JSONException e)
			{
				Loger.print(this.getClass().getSimpleName(), e.getMessage(),
						Loger.ERROR);
			}
		}
	}

	private void handleSelectedTime(long timestamp)
	{
		if (timestamp == -1)
		{
			Toast.makeText(getBaseContext(), R.string.not_understand,
					Toast.LENGTH_LONG).show();
		} else if (timestamp <= System.currentTimeMillis())
		{
			Toast.makeText(getBaseContext(), R.string.select_time_lager,
					Toast.LENGTH_LONG).show();
		} else if (timestamp > System.currentTimeMillis())
		{
			MainHandler
					.instance()
					.obtainMessage(
							MainHandler.getIntKey(CreateFragment.class
									.getName()),
							CreateFragment.INNER_PICK_TIME, 0, timestamp)
					.sendToTarget();
			finish();
		}
	}

	private long handleJresult(JSONArray jresult) throws JSONException
	{
		if (null == jresult || jresult.length() == 0)
		{
			return -1;
		}
		for (int i = 0; i < jresult.length(); ++i)
		{
			JSONObject jobj = jresult.getJSONObject(i);
			if (jobj.getString("domain").equals("alarm"))
			{
				return handleAlarm(jobj.getJSONObject("object"));
			}
		}
		return -1;
	}

	private long handleAlarm(JSONObject jobj) throws JSONException
	{
		if (null == jobj)
		{
			return -1;
		}
		String date = jobj.getString("date");
		String time = jobj.getString("time");
		Loger.print(this.getClass().getSimpleName(), "clock: " + date + " "
				+ time, Loger.INFO);
		if (null != date && null != time)
		{
			long timestamp = DateUtil.parse(DateUtil.FMT_YMDHMS, date.trim()
					+ " " + time.trim());
			return timestamp;
		}
		return -1;
	}

}
