package com.wjd.speechnotepad.home.clock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.wjd.speechnotepad.MainApp;
import com.wjd.speechnotepad.R;
import com.wjd.speechnotepad.base.BaseActivity;
import com.wjd.speechnotepad.database.NotepadDbWrapper;
import com.wjd.speechnotepad.entity.NotepadEntity;
import com.wjd.speechnotepad.handler.DeliveredEntity;
import com.wjd.speechnotepad.handler.MainHandler;
import com.wjd.speechnotepad.handler.PostListener;
import com.wjd.speechnotepad.util.AudioMsgUtil;
import com.wjd.speechnotepad.util.FileUtil;

public class AlarmActivity extends BaseActivity implements PostListener
{

	public static final String NOTE_ID = "NOTE_ID";

	public static void actionLuanch(Context context, String id)
	{
		Intent intent = new Intent(context, AlarmActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("id", id);
		intent.putExtras(bundle);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	private Button btnAudio;

	private NotepadEntity entity;

	private MediaPlayer player;

	private PhoneStateReceiver receiver;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		MainHandler.instance().registListener(hashCode(), this,
				getClass().getName());
		entity = NotepadDbWrapper.getNoticeById(getIntent().getExtras()
				.getString("id"), ((MainApp) getApplication()).db());
		if (null == entity)
		{
			finish();
			return;
		}
		setContentView(R.layout.speechnotepad_alarm_layout);
		btnAudio = (Button) findViewById(R.id.btn_audio);
		btnAudio.setText(entity.getDuration() + "\'");
		btnAudio.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				stopAlarm();
				AudioMsgUtil.getInstance().startPlay(
						FileUtil.newInstance().getFile(entity.getAudioRoute(),
								false));
			}
		});
		findViewById(R.id.btn_stop).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				finish();
			}
		});
		regist();
		playAlarm();
	}

	private void playAlarm()
	{
		stopAlarm();
		player = MediaPlayer.create(getBaseContext(), R.raw.alarm);
		player.start();
		Integer key = MainHandler.getIntKey(getClass().getName());
		if (null != key)
		{
			MainHandler.instance().sendEmptyMessageDelayed(key, 61000);
		}
	}

	private void stopAlarm()
	{
		if (null != player)
		{
			player.stop();
			player = null;
		}
	}

	@Override
	public void onPostListener(DeliveredEntity postEntity)
	{
		finish();
	}

	@Override
	public void finish()
	{
		unRegist();
		MainHandler.instance().unRegistListener(hashCode());
		stopAlarm();
		AudioMsgUtil.getInstance().stopPlay();
		AlarmReceiver.addAlarm((MainApp) getApplication(), this);
		super.finish();
	}

	private void regist()
	{
		receiver = new PhoneStateReceiver();
		registerReceiver(receiver, new IntentFilter(
				TelephonyManager.ACTION_PHONE_STATE_CHANGED));
	}

	private void unRegist()
	{
		if (null != receiver)
		{
			unregisterReceiver(receiver);
			receiver = null;
		}
	}

	class PhoneStateReceiver extends BroadcastReceiver
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			if (intent.getAction().equals(
					TelephonyManager.ACTION_PHONE_STATE_CHANGED))
			{
				String state = intent
						.getStringExtra(TelephonyManager.EXTRA_STATE);
				if (!TelephonyManager.EXTRA_STATE_IDLE.equals(state))
				{
					finish();
				}
			}
		}
	}
}
