package com.wjd.speechnotepad.handler;

import java.util.HashMap;
import java.util.Map;

import android.os.Handler;
import android.os.Message;
import android.util.SparseArray;

public class MainHandler extends Handler
{

	private static MainHandler handler = null;

	/**
	 * This method must call on main thread at first.
	 * 
	 * @return
	 */
	public static synchronized MainHandler instance()
	{
		if (null == handler)
		{
			handler = new MainHandler();
		}
		return handler;
	}

	private SparseArray<PostListener> registedListener = new SparseArray<PostListener>();

	private Map<String, Integer> keyParser = new HashMap<String, Integer>();

	private MainHandler()
	{

	}

	/**
	 * @param key
	 * @param listener
	 * @param className
	 *            The class name of listener
	 */
	public void registListener(Integer key, PostListener listener,
			String className)
	{
		registedListener.append(key, listener);
		keyParser.put(className, key);
	}

	public void unRegistListener(Integer key)
	{
		registedListener.remove(key);
	}

	/**
	 * Get the Integer key via the class name.
	 * 
	 * @param className
	 * @return
	 */
	public static Integer getIntKey(String className)
	{
		if (null == handler)
		{
			return null;
		}
		return handler.keyParser.get(className);
	}

	/**
	 * The 'what' field of Message is corresponding to the key of
	 * registedListener.
	 * 
	 * The 'arg1' field of Message is corresponding to the 'innerKey' field of
	 * DeliveredEntity.
	 * 
	 * The 'obj' field of Message is corresponding to the 'obj' field of
	 * DeliveredEntity.
	 * 
	 * @see android.os.Handler#handleMessage(android.os.Message)
	 */
	@Override
	public void handleMessage(Message msg)
	{
		PostListener listener = registedListener.get(msg.what);
		if (null != listener)
		{
			DeliveredEntity entity = new DeliveredEntity((byte) msg.arg1,
					msg.obj);
			listener.onPostListener(entity);
		}
	}
}
