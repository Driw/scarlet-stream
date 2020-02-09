package org.diverproject.scarlet.stream;

public interface Output extends Stream
{
	void invert();
	void put(byte value);
	void put(byte[] values);
	void put(short value);
	void put(short[] values);
	void put(int value);
	void put(int[] values);
	void put(long value);
	void put(long[] values);
	void put(float value);
	void put(float[] values);
	void put(double value);
	void put(double[] values);
	void put(boolean value);
	void put(boolean[] values);
	void put(char value);
	void put(char[] values);
	void put(String value);
	void put(String[] values);
	void put(Object value);
	void put(Object[] values);

	boolean isInverted();
}
