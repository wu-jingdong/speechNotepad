package com.wjd.speechnotepad.home.fragment;

import java.io.File;

import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wjd.speechnotepad.R;
import com.wjd.speechnotepad.database.NotepadDbWrapper;
import com.wjd.speechnotepad.entity.NotepadEntity;
import com.wjd.speechnotepad.handler.DeliveredEntity;
import com.wjd.speechnotepad.handler.MainHandler;
import com.wjd.speechnotepad.handler.PostListener;
import com.wjd.speechnotepad.home.clock.ClockActivity;
import com.wjd.speechnotepad.util.AudioMsgUtil;
import com.wjd.speechnotepad.util.DateUtil;
import com.wjd.speechnotepad.util.FileUtil;
import com.wjd.speechnotepad.widget.TimerView;

public class CreateFragment extends BaseFragment implements OnClickListener,
		OnTouchListener, PostListener
{

	public static final int INNER_UPDATE_TIME = 1;

	public static final int INNER_PICK_TIME = 2;

	public static CreateFragment newInstance()
	{
		return new CreateFragment();
	}

	private Button btnAddClock;

	private LinearLayout lineTime;

	private TimerView timerView;

	private TextView tvTime;

	private Button btnPress2Speack;

	private Button btnAudio;

	private Button btnSave;

	private TextView tvClock;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View view = LayoutInflater.from(getActivity().getBaseContext())
				.inflate(R.layout.speechnotepad_create_fragment_layout, null);
		btnAddClock = (Button) view.findViewById(R.id.btn_add_clock);
		btnAddClock.setOnClickListener(this);
		lineTime = (LinearLayout) view.findViewById(R.id.line_time);
		lineTime.setVisibility(View.GONE);
		timerView = (TimerView) view.findViewById(R.id.timer_view);
		tvTime = (TextView) view.findViewById(R.id.tv_time);
		btnPress2Speack = (Button) view.findViewById(R.id.btn_press_2_speak);
		btnPress2Speack.setOnTouchListener(this);
		btnSave = (Button) view.findViewById(R.id.btn_save);
		btnSave.setOnClickListener(this);
		btnAudio = (Button) view.findViewById(R.id.btn_audio);
		btnAudio.setVisibility(View.INVISIBLE);
		btnAudio.setOnClickListener(this);
		tvClock = (TextView) view.findViewById(R.id.tv_clock);
		MainHandler.instance().registListener(hashCode(), this,
				getClass().getName());
		return view;
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.btn_add_clock:
				doAddClock();
				break;
			case R.id.btn_save:
				doSave();
				break;
			case R.id.btn_audio:
				doPlay();
				break;
			default:
				break;
		}
	}

	private void doAddClock()
	{
		ClockActivity.actionLuanch(getActivity());
	}

	private void doSave()
	{
		if (TextUtils.isEmpty(confirmPath))
		{
			return;
		}
		NotepadEntity entity = new NotepadEntity();
		entity.setId(String.valueOf(System.currentTimeMillis()));
		entity.setAudioRoute(confirmPath);
		entity.setDuration(confirmTime);
		if (clock > System.currentTimeMillis() + 60000)
		{
			entity.setNoticeTime(String.valueOf(clock));
		}
		NotepadDbWrapper.insertNote(entity, getApp().db());
		getParent().setCurPage(0);
	}

	private void doPlay()
	{
		if (TextUtils.isEmpty(tempAudioRecPath))
		{
			return;
		}
		if (null == recPlay)
		{
			recPlay = new AudioMsgUtil();
		}
		if (recPlay.isPlaying())
		{
			return;
		}
		File file = FileUtil.newInstance().getFile(tempAudioRecPath, false);
		recPlay.initAudioTrack();
		recPlay.startPlay(file);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event)
	{
		if (event.getAction() == MotionEvent.ACTION_DOWN)
		{
			audioRecDown();
			sendUpdateTimeMessage();
		} else if (event.getAction() == MotionEvent.ACTION_UP)
		{
			audioRecUp();
		}
		return false;
	}

	protected void audioRecUp()
	{
		endRecTime = System.currentTimeMillis();
		if (null != recPlay)
		{
			recPlay.release();
			recPlay = null;
		}
		MainHandler.instance().removeMessages(
				MainHandler.getIntKey(getClass().getName()));
		audioRecupView();
		if (endRecTime - beginRecTime < 1000)
		{
			Toast.makeText(getActivity().getBaseContext(),
					R.string.record_short, Toast.LENGTH_SHORT).show();
			return;
		}
		handleRecEnd();
	}

	protected void handleRecEnd()
	{
		confirmPath = tempAudioRecPath;
		confirmTime = getRecDuration();
		btnAudio.setVisibility(View.VISIBLE);
		btnAudio.setText(getRecDuration() + "\'");
	}

	protected AudioMsgUtil recPlay;
	private File tempAudioRecFile = null;
	private long beginRecTime = 0;
	private long endRecTime = 0;

	protected void audioRecDown()
	{
		if (null != recPlay)
		{
			recPlay.release();
			recPlay = null;
		}
		recPlay = new AudioMsgUtil();
		recPlay.initRecorder();
		recPlay.startRecord(initFile());
		beginRecTime = System.currentTimeMillis();
		audioRecdownView();
	}

	protected String tempAudioRecPath = null;

	private String confirmPath = null;

	private int confirmTime = 0;

	private File initFile()
	{
		tempAudioRecPath = FileUtil.append(System.currentTimeMillis()
				+ "jumploo.a");
		tempAudioRecFile = FileUtil.newInstance().getFile(tempAudioRecPath,
				true);
		return tempAudioRecFile;
	}

	private void updateTime()
	{
		int time = (int) getRecTime();
		tvTime.setText(String.format("%02d:%02d:%02d", time / 3600, time / 60,
				time % 60));
		timerView.invalidate(time);
		sendUpdateTimeMessage();
	}

	private void sendUpdateTimeMessage()
	{
		Message msg = MainHandler.instance().obtainMessage(
				MainHandler.getIntKey(getClass().getName()), INNER_UPDATE_TIME,
				0);
		MainHandler.instance().sendMessageDelayed(msg, 1000);
	}

	protected int getRecDuration()
	{
		return (int) ((endRecTime - beginRecTime) / 1000);
	}

	protected long getRecTime()
	{
		return (System.currentTimeMillis() - beginRecTime) / 1000;
	}

	protected void audioRecupView()
	{
		lineTime.setVisibility(View.GONE);
	};

	protected void audioRecdownView()
	{
		tvTime.setText("00:00:00");
		timerView.invalidate(0);
		lineTime.setVisibility(View.VISIBLE);
	};

	private long clock;

	@Override
	public void onPostListener(DeliveredEntity postEntity)
	{
		if (postEntity.getInnerKey() == INNER_UPDATE_TIME)
		{
			updateTime();
		} else if (postEntity.getInnerKey() == INNER_PICK_TIME)
		{
			clock = (Long) postEntity.getObj();
			tvClock.setText(DateUtil.format(clock, DateUtil.FMT_YMDHM));
		}
	}

	@Override
	public void updateView()
	{

	}
}
