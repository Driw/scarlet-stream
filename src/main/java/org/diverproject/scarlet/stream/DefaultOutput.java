package org.diverproject.scarlet.stream;

import static org.diverproject.scarlet.stream.language.StreamLanguage.GET_OBJECT_FIELD_SET;
import static org.diverproject.scarlet.stream.language.StreamLanguage.PUT_OBJECT_NULL;
import static org.diverproject.scarlet.stream.language.StreamLanguage.PUT_STRING_LENGTH;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.function.Consumer;

@Data
@NoArgsConstructor
public abstract class DefaultOutput implements Output
{
	protected  abstract Writer getWriter();

	@Override
	public void invert()
	{
		this.getWriter().invert();
	}

	@Override
	public void put(byte value)
	{
		this.getWriter().write(value);
	}

	@Override
	public void put(byte[] values)
	{
		this.getWriter().write(values);
	}

	@Override
	public void put(short value)
	{
		this.getWriter().write(StreamUtil.bytesOf(value));
	}

	@Override
	public void put(short[] values)
	{
		for (short value : values)
			this.put(value);
	}

	@Override
	public void put(int value)
	{
		this.getWriter().write(StreamUtil.bytesOf(value));
	}

	@Override
	public void put(int[] values)
	{
		for (int value : values)
			this.put(value);
	}

	@Override
	public void put(long value)
	{
		this.getWriter().write(StreamUtil.bytesOf(value));
	}

	@Override
	public void put(long[] values)
	{
		for (long value : values)
			this.put(value);
	}

	@Override
	public void put(float value)
	{
		this.getWriter().write(StreamUtil.bytesOf(value));
	}

	@Override
	public void put(float[] values)
	{
		for (float value : values)
			this.put(value);
	}

	@Override
	public void put(double value)
	{
		this.getWriter().write(StreamUtil.bytesOf(value));
	}

	@Override
	public void put(double[] values)
	{
		for (double value : values)
			this.put(value);
	}

	@Override
	public void put(boolean value)
	{
		this.getWriter().write((byte) (value ? 1 : 0));
	}

	@Override
	public void put(boolean[] values)
	{
		for (boolean value : values)
			this.put(value);

	}

	@Override
	public void put(char value)
	{
		this.getWriter().write(StreamUtil.bytesOf(value));
	}

	@Override
	public void put(char[] values)
	{
		for (char value : values)
			this.put(value);
	}

	@Override
	public void put(String value)
	{
		value = Optional.ofNullable(value).orElse("");

		if (value.length() > 255)
			throw new StreamRuntimeException(PUT_STRING_LENGTH, value.length());

		this.getWriter().write((byte) value.length());
		this.getWriter().write(value.getBytes());
	}

	@Override
	public void put(String[] values)
	{
		for (String value : values)
			this.put(value);
	}

	@Override
	public void put(Object value)
	{
		if (value == null)
			throw new StreamRuntimeException(PUT_OBJECT_NULL);

		StreamField streamField = new StreamField();

		for (Field field : value.getClass().getDeclaredFields())
		{
			try {

				field.setAccessible(true);
				{
					streamField.setField(field).of(value);

					if (field.getType().isArray())
					{
						streamField
							.is(Byte[].class).thenDo((v) -> this.put((byte[]) v))
							.is(Short[].class).thenDo((v) -> this.put((short[]) v))
							.is(Integer[].class).thenDo((v) -> this.put((int[]) v))
							.is(Long[].class).thenDo((v) -> this.put((long[]) v))
							.is(Float[].class).thenDo((v) -> this.put((float[]) v))
							.is(Double[].class).thenDo((v) -> this.put((double[]) v))
							.is(Boolean[].class).thenDo((v) -> this.put((boolean[]) v))
							.is(Character[].class).thenDo((v) -> this.put((char[]) v))
							.is(String[].class).thenDo((v) -> this.put((String[]) v))
						;
					}

					else
					{
						streamField
							.is(Byte.class).thenDo((v) -> this.put((byte) v))
							.is(Short.class).thenDo((v) -> this.put((short) v))
							.is(Integer.class).thenDo((v) -> this.put((int) v))
							.is(Long.class).thenDo((v) -> this.put((long) v))
							.is(Float.class).thenDo((v) -> this.put((float) v))
							.is(Double.class).thenDo((v) -> this.put((double) v))
							.is(Boolean.class).thenDo((v) -> this.put((boolean) v))
							.is(Character.class).thenDo((v) -> this.put((char) v))
							.is(String.class).thenDo((v) -> this.put((String) v))
							.is(Object.class).thenDo((Consumer<Object>) this::put)
						;
					}
				}
				field.setAccessible(false);

			} catch (IllegalAccessException e) {
				throw new StreamRuntimeException(e, GET_OBJECT_FIELD_SET, value.getClass().getName(), field.getName());
			}
		}
	}

	@Override
	public void put(Object[] values)
	{
		for (Object value : values)
			this.put(value);
	}

	@Override
	public boolean isInverted()
	{
		return this.getWriter().isInverted();
	}

	@Override
	public boolean isClosed()
	{
		return this.getWriter().isClosed();
	}

	@Override
	public boolean isFull()
	{
		return this.getWriter().isFull();
	}

	@Override
	public boolean isEmpty()
	{
		return this.getWriter().isEmpty();
	}

	@Override
	public int capacity()
	{
		return this.getWriter().capacity();
	}

	@Override
	public int offset()
	{
		return this.getWriter().offset();
	}

	@Override
	public void reset()
	{
		this.getWriter().reset();
	}

	@Override
	public void close()
	{
		this.getWriter().close();
	}
}
