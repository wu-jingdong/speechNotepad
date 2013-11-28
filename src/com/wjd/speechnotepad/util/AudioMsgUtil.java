package com.wjd.speechnotepad.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.wjd.speex.Speex;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder.AudioSource;

public class AudioMsgUtil
{

	private static final int REC_SIZE = 8000;

	private static AudioMsgUtil instance = null;

	private AudioRecord recorder;

	private int recSize = 0;

	private int frameSize = 0;

	private int readSize = 0;

	private Speex speex;

	public synchronized static AudioMsgUtil getInstance()
	{
		if (null == instance)
		{
			instance = new AudioMsgUtil();
		}
		return instance;
	}

	private AudioMsgUtil()
	{
		speex = new Speex();
		speex.init();
		frameSize = speex.getFrameSize();
	}

	public void release()
	{
		stopRecord();
		stopPlay();
		if (null != speex)
		{
			speex.close();
			speex = null;
		}
	}

	private EncodeThread recordThread;

	// record and encode --------------------------------
	private void initRecorder()
	{
		recSize = AudioRecord.getMinBufferSize(REC_SIZE,
				AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
		recorder = new AudioRecord(AudioSource.MIC, REC_SIZE,
				AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT,
				recSize);
		int temp = frameSize * 6;
		readSize = temp > recSize / 2 ? recSize / 2 : temp;
	}

	public boolean startRecord(File file)
	{
		stopRecord();
		initRecorder();
		recorder.startRecording();
		return startRecThread(file);
	}

	public void stopRecord()
	{
		if (null != recorder)
		{
			recorder.release();
			recorder = null;
		}
		if (null != recordThread)
		{
			recordThread.running = false;
			recordThread = null;
		}
	}

	private boolean startRecThread(File file)
	{
		recordThread = new EncodeThread();
		try
		{
			recordThread.fos = new FileOutputStream(file);
			recordThread.running = true;
		} catch (FileNotFoundException e)
		{
			recordThread.fos = null;
			return false;
		}
		recordThread.start();
		return true;
	}

	private class EncodeThread extends Thread
	{

		FileOutputStream fos = null;

		boolean running = false;

		@Override
		public void run()
		{
			while (isRecording() && running)
			{
				doRecord();
			}
			closeFile();
		}

		private void doRecord()
		{
			short[] buffer = new short[readSize];
			int len = 0;
			try
			{
				len = recorder.read(buffer, 0, buffer.length);
			} catch (NullPointerException e)
			{
				e.printStackTrace();
				return;
			}
			if (len == buffer.length)
			{
				short[] toEncode = new short[frameSize];
				for (int i = 0; i < readSize / frameSize; ++i)
				{
					System.arraycopy(buffer, i * frameSize, toEncode, 0,
							frameSize);
					byte[] encoded = new byte[38];
					int eLen = speex.encode(toEncode, encoded);
					writeFile(encoded, eLen);
				}
			}
		}

		private void writeFile(byte[] encoded, int eLen)
		{
			if (eLen > 0 && null != fos)
			{
				try
				{
					fos.write(eLen);
					fos.write(encoded, 0, eLen);
				} catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}

		private void closeFile()
		{
			if (null != fos)
			{
				try
				{
					fos.flush();
					fos.close();
				} catch (IOException e)
				{
					e.printStackTrace();
				}
				fos = null;
			}
		}
	}

	private boolean isRecording()
	{
		return null != recorder
				&& recorder.getRecordingState() == AudioRecord.RECORDSTATE_RECORDING;
	}

	// decode and play -------------------------------------------

	private AudioTrack track;

	private DecodeThread playThread;

	private void initAudioTrack()
	{
		int minBufferSize = AudioTrack.getMinBufferSize(REC_SIZE,
				AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);
		track = new AudioTrack(AudioManager.STREAM_MUSIC, REC_SIZE,
				AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT,
				minBufferSize, AudioTrack.MODE_STREAM);
	}

	public void startPlay(File file)
	{
		stopPlay();
		initAudioTrack();
		track.play();
		startPlayThread(file);
	}

	private void startPlayThread(File file)
	{
		playThread = new DecodeThread();
		try
		{
			playThread.fis = new FileInputStream(file);
			playThread.running = true;
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
			return;
		}
		playThread.start();
	}

	public void stopPlay()
	{
		if (null != track)
		{
			track.release();
			track = null;
		}

		if (null != playThread)
		{
			playThread.running = false;
			playThread = null;
		}
	}

	private class DecodeThread extends Thread
	{

		FileInputStream fis = null;

		boolean running = false;

		@Override
		public void run()
		{
			doPlay();
			closeFile();
		}

		private void doPlay()
		{
			try
			{
				while (isPlaying() && running)
				{
					int encLen = fis.read();
					if (-1 == encLen)
					{
						break;
					}
					byte[] encData = new byte[encLen];
					int readLen = fis.read(encData);
					if (encLen != readLen)
					{
						break;
					}
					short[] lin = new short[frameSize];
					int len = speex.decode(encData, lin, encData.length);
					if (len == frameSize)
					{
						try
						{
							track.write(lin, 0, len);
						} catch (Exception e)
						{
							e.printStackTrace();
							break;
						}
					}
				}
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}

		private void closeFile()
		{
			if (null != fis)
			{
				try
				{
					fis.close();
				} catch (IOException e)
				{
					e.printStackTrace();
				}
				fis = null;
			}
		}
	}

	private boolean isPlaying()
	{
		return null != track
				&& track.getPlayState() == AudioTrack.PLAYSTATE_PLAYING;
	}
}
