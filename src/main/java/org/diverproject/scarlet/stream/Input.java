package org.diverproject.scarlet.stream;

public interface Input extends Stream
{
	void invert();

	byte getByte();
	byte[] getBytes(int length);
	void getBytes(byte[] bytes);
	short getShort();
	short[] getShorts(int length);
	void getShorts(short[] shorts);
	int getInt();
	int[] getInts(int length);
	void getInts(int[] ints);
	long getLong();
	long[] getLongs(int length);
	void getLongs(long[] longs);
	float getFloat();
	float[] getFloats(int length);
	void getFloats(float[] floats);
	double getDouble();
	double[] getDoubles(int length);
	void getDoubles(double[] doubles);
	boolean getBoolean();
	boolean[] getBooleans(int length);
	void getBooleans(boolean[] booleans);
	char getChar();
	char[] getChars(int length);
	void getChars(char[] chars);
	String getString();
	String[] getStrings(int length);
	void getStrings(String[] strings);
	String getString(int size);
	String[] getStrings(int length, int size);
	void getStrings(String[] strings, int size);
	<D> D getObject(Class<D> objectClass);
	<D> D[] getObjects(Class<D> objectClass, int length);
	<D> void getObject(D object);
	<D> void getObjects(D[] objects);

	boolean isInverted();
}
