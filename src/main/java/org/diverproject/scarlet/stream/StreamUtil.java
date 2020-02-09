package org.diverproject.scarlet.stream;

import org.diverproject.scarlet.util.IntegerUtils;

public class StreamUtil
{
	public static char charOf(byte... bytes)
	{
		char value = 0;
		int maxIndex = IntegerUtils.capMax(bytes.length, Character.BYTES);

		for (int i = 0, j = 0; i < maxIndex; i++, j += 8)
			value |= (bytes[bytes.length - i - 1] & 255) << j;

		return value;
	}

	public static short shortOf(byte... bytes)
	{
		short value = 0;
		int maxIndex = IntegerUtils.capMax(bytes.length, Short.BYTES);

		for (int i = 0, j = 0; i < maxIndex; i++, j += 8)
			value |= (bytes[bytes.length - i - 1] & 255) << j;

		return value;
	}

	public static int intOf(byte... bytes)
	{
		int value = 0;
		int maxIndex = IntegerUtils.capMax(bytes.length, Integer.BYTES);

		for (int i = 0, j = 0; i < maxIndex; i++, j += 8)
			value |= (bytes[bytes.length - i - 1] & 255) << j;

		return value;
	}

	public static long longOf(byte... bytes)
	{
		long value = 0;
		int maxIndex = IntegerUtils.capMax(bytes.length, Long.BYTES);

		for (int i = 0, j = 0; i < maxIndex; i++, j += 8)
			value |= (bytes[bytes.length - i - 1] & 255L) << j;

		return value;
	}

	public static float floatOf(byte... bytes)
	{
		return Float.intBitsToFloat(intOf(bytes));
	}

	public static double doubleOf(byte... bytes)
	{
		return Double.longBitsToDouble(longOf(bytes));
	}

	public static byte[] bytesOf(char value)
	{
		return new byte[] {
			(byte) ((value >> 8) & 255),
			(byte) ((value) & 255),
		};
	}

	public static byte[] bytesOf(short value)
	{
		return new byte[] {
			(byte) ((value >> 8) & 255),
			(byte) ((value) & 255),
		};
	}

	public static byte[] bytesOf(int value)
	{
		return new byte[] {
			(byte) ((value >> 24) & 255),
			(byte) ((value >> 16) & 255),
			(byte) ((value >> 8) & 255),
			(byte) ((value) & 255),
		};
	}

	public static byte[] bytesOf(long value)
	{
		return new byte[] {
			(byte) ((value >> 56) & 255L),
			(byte) ((value >> 48) & 255L),
			(byte) ((value >> 40) & 255L),
			(byte) ((value >> 32) & 255L),
			(byte) ((value >> 24) & 255L),
			(byte) ((value >> 16) & 255L),
			(byte) ((value >> 8) & 255L),
			(byte) ((value) & 255),
		};
	}

	public static byte[] bytesOf(float value)
	{
		return bytesOf(Float.floatToIntBits(value));
	}

	public static byte[] bytesOf(double value)
	{
		return bytesOf(Double.doubleToLongBits(value));
	}
}
