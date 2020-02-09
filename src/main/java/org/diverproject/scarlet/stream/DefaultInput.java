package org.diverproject.scarlet.stream;

import static org.diverproject.scarlet.stream.language.StreamLanguage.GET_OBJECT_CONSTRUCTOR;
import static org.diverproject.scarlet.stream.language.StreamLanguage.GET_OBJECT_FIELD_SET;
import static org.diverproject.scarlet.stream.language.StreamLanguage.GET_OBJECT_INSTANCE;

import lombok.NoArgsConstructor;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Supplier;

@NoArgsConstructor
public abstract class DefaultInput implements Input
{
	protected abstract Reader getReader();

	@Override
	public void invert()
	{
		this.getReader().invert();
	}

	@Override
	public byte getByte()
	{
		return this.getReader().read();
	}

	@Override
	public byte[] getBytes(int length)
	{
		byte[] bytes = new byte[length];
		this.getBytes(bytes);

		return bytes;
	}

	@Override
	public void getBytes(byte[] bytes)
	{
		for (int i = 0; i < bytes.length; i++)
			bytes[i] = this.getByte();
	}

	@Override
	public short getShort()
	{
		return StreamUtil.shortOf(this.getReader().read(Short.BYTES));
	}

	@Override
	public short[] getShorts(int length)
	{
		short[] shorts = new short[length];
		this.getShorts(shorts);

		return shorts;
	}

	@Override
	public void getShorts(short[] shorts)
	{
		for (int i = 0; i < shorts.length; i++)
			shorts[i] = this.getShort();
	}

	@Override
	public int getInt()
	{
		return StreamUtil.intOf(this.getReader().read(Integer.BYTES));
	}

	@Override
	public int[] getInts(int length)
	{
		int[] ints = new int[length];
		this.getInts(ints);

		return ints;
	}

	@Override
	public void getInts(int[] ints)
	{
		for (int i = 0; i < ints.length; i++)
			ints[i] = this.getInt();
	}

	@Override
	public long getLong()
	{
		return StreamUtil.longOf(this.getReader().read(Long.BYTES));
	}

	@Override
	public long[] getLongs(int length)
	{
		long[] longs = new long[length];
		this.getLongs(longs);

		return longs;
	}

	@Override
	public void getLongs(long[] longs)
	{
		for (int i = 0; i < longs.length; i++)
			longs[i] = this.getLong();
	}

	@Override
	public float getFloat()
	{
		return StreamUtil.floatOf(this.getReader().read(Float.BYTES));
	}

	@Override
	public float[] getFloats(int length)
	{
		float[] floats = new float[length];
		this.getFloats(floats);

		return floats;
	}

	@Override
	public void getFloats(float[] floats)
	{
		for (int i = 0; i < floats.length; i++)
			floats[i] = this.getFloat();
	}

	@Override
	public double getDouble()
	{
		return StreamUtil.doubleOf(this.getReader().read(Double.BYTES));
	}

	@Override
	public double[] getDoubles(int length)
	{
		double[] doubles = new double[length];
		this.getDoubles(doubles);

		return doubles;
	}

	@Override
	public void getDoubles(double[] doubles)
	{
		for (int i = 0; i < doubles.length; i++)
			doubles[i] = this.getDouble();
	}

	@Override
	public boolean getBoolean()
	{
		return this.getByte() == 1;
	}

	@Override
	public boolean[] getBooleans(int length)
	{
		boolean[] booleans = new boolean[length];
		this.getBooleans(booleans);

		return booleans;
	}

	@Override
	public void getBooleans(boolean[] booleans)
	{
		for (int i = 0; i < booleans.length; i++)
			booleans[i] = this.getBoolean();
	}

	@Override
	public char getChar()
	{
		return (char) this.getShort();
	}

	@Override
	public char[] getChars(int length)
	{
		char[] chars = new char[length];
		this.getChars(chars);

		return chars;
	}

	@Override
	public void getChars(char[] chars)
	{
		for (int i = 0; i < chars.length; i++)
			chars[i] = this.getChar();
	}

	@Override
	public String getString()
	{
		return this.getString(StreamUtil.intOf(this.getByte()));
	}

	@Override
	public String[] getStrings(int length)
	{
		String[] strings = new String[length];
		this.getStrings(strings);

		return strings;
	}

	@Override
	public void getStrings(String[] strings)
	{
		for (int i = 0; i < strings.length; i++)
			strings[i] = this.getString();
	}

	@Override
	public String getString(int size)
	{
		return new String(this.getBytes(size));
	}

	@Override
	public String[] getStrings(int length, int size)
	{
		String[] strings = new String[length];
		this.getStrings(strings, size);

		return strings;
	}

	@Override
	public void getStrings(String[] strings, int size)
	{
		for (int i = 0; i < strings.length; i++)
			strings[i] = this.getString(size);
	}

	@Override
	public <D> D getObject(Class<D> objectClass)
	{
		try {

			Constructor<D> constructor = objectClass.getConstructor();

			try {

				D object = constructor.newInstance();
				this.getObject(object);

				return object;

			} catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
				throw new StreamRuntimeException(e, GET_OBJECT_CONSTRUCTOR, objectClass.getName());
			}

		} catch (NoSuchMethodException e) {
			throw new StreamRuntimeException(e, GET_OBJECT_INSTANCE, objectClass.getName());
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public <D> D[] getObjects(Class<D> objectClass, int length)
	{
		D[] objects = (D[]) Array.newInstance(objectClass, length);

		for (int i = 0; i < objects.length; i++)
			objects[i] = this.getObject(objectClass);

		return objects;
	}

	@Override
	public <D> void getObject(D object)
	{
		StreamField streamField = new StreamField().of(object);

		for (Field field : object.getClass().getDeclaredFields())
		{
			try {
				field.setAccessible(true);
				{
					streamField.setField(field).reset();

					if (field.getType().isArray())
					{
						streamField
							.is(Byte[].class, Byte.TYPE).thenDo((c) -> this.getBytes((byte[]) c))
							.is(Short[].class, Short.TYPE).thenDo((c) -> this.getShorts((short[]) c))
							.is(Integer[].class, Integer.TYPE).thenDo((c) -> this.getInts((int[]) c))
							.is(Long[].class, Long.TYPE).thenDo((c) -> this.getLongs((long[]) c))
							.is(Float[].class, Float.TYPE).thenDo((c) -> this.getFloats((float[]) c))
							.is(Double[].class, Double.TYPE).thenDo((c) -> this.getDoubles((double[]) c))
							.is(Boolean[].class, Boolean.TYPE).thenDo((c) -> this.getBooleans((boolean[]) c))
							.is(Character[].class).thenDo((c) -> this.getChars((char[]) c))
							.is(String[].class).thenDo((c) -> this.getStrings((String[]) c));
					} else {
						streamField
							.is(Byte.class, Byte.TYPE).thenDo(this::getByte)
							.is(Short.class, Short.TYPE).thenDo(this::getShort)
							.is(Integer.class, Integer.TYPE).thenDo(this::getInt)
							.is(Long.class, Long.TYPE).thenDo(this::getLong)
							.is(Float.class, Float.TYPE).thenDo(this::getFloat)
							.is(Double.class, Double.TYPE).thenDo(this::getDouble)
							.is(Boolean.class, Boolean.TYPE).thenDo(this::getBoolean)
							.is(Character.class, Character.TYPE).thenDo(this::getChar)
							.is(String.class).thenDo((Supplier<String>) this::getString)
							.is(Object.class).thenDo(() -> {
								return this.getBoolean() ? this.getObject(field.getType()) : null;
							});
					}
				}
				field.setAccessible(false);
			} catch (IllegalAccessException e) {
				throw new StreamRuntimeException(e, GET_OBJECT_FIELD_SET, object.getClass().getName(), field.getName());
			}
		}
	}

	@Override
	public <D> void getObjects(D[] objects)
	{
		for (D object : objects)
			this.getObject(object);
	}

	@Override
	public boolean isInverted()
	{
		return this.getReader().isInverted();
	}

	@Override
	public boolean isClosed()
	{
		return this.getReader().isClosed();
	}

	@Override
	public boolean isFull()
	{
		return this.getReader().isFull();
	}

	@Override
	public boolean isEmpty()
	{
		return this.getReader().isEmpty();
	}

	@Override
	public int capacity()
	{
		return this.getReader().capacity();
	}

	@Override
	public int offset()
	{
		return this.getReader().offset();
	}

	@Override
	public void reset()
	{
		this.getReader().reset();
	}

	@Override
	public void close()
	{
		this.getReader().close();
	}
}
