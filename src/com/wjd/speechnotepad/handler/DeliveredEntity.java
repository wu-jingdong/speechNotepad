package com.wjd.speechnotepad.handler;

public class DeliveredEntity
{

	private byte innerKey;

	private Object obj;

	public DeliveredEntity(byte innerKey, Object obj)
	{
		this.innerKey = innerKey;
		this.obj = obj;
	}

	public byte getInnerKey()
	{
		return innerKey;
	}

	public Object getObj()
	{
		return obj;
	}
}
