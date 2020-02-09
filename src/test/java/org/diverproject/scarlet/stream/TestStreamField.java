package org.diverproject.scarlet.stream;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Stream Field")
public class TestStreamField
{
	private static final AllFields ALL_FIELDS = new AllFields();

	@Test
	public void testThenDoWithSupplier() throws IllegalAccessException
	{
		AllFields allFields = new AllFields();
		assertEquals(0, allFields.getByteValue());
		assertEquals(0, allFields.getShortValue());
		assertEquals(0, allFields.getIntValue());
		assertEquals(0L, allFields.getLongValue());
		assertEquals(0F, allFields.getFloatValue());
		assertEquals(0D, allFields.getDoubleValue());
		assertFalse(allFields.isBooleanValue());
		assertEquals(0, allFields.getCharValue());
		assertNull(allFields.getStringValue());
		assertNull(allFields.getAllFields());

		StreamField streamField = new StreamField();

		for (Field field : allFields.getClass().getDeclaredFields())
		{
			field.setAccessible(true);
			{
				streamField.reset().setField(field).of(allFields)
					.is(Byte.class, Byte.TYPE).thenDo(this::getByte)
					.is(Short.class, Short.TYPE).thenDo(this::getShort)
					.is(Integer.class, Integer.TYPE).thenDo(this::getInt)
					.is(Long.class, Long.TYPE).thenDo(this::getLong)
					.is(Float.class, Float.TYPE).thenDo(this::getFloat)
					.is(Double.class, Double.TYPE).thenDo(this::getDouble)
					.is(Boolean.class, Boolean.TYPE).thenDo(this::getBoolean)
					.is(Character.class, Character.TYPE).thenDo(this::getChar)
					.is(String.class).thenDo(this::getString)
					.is(Object.class).thenDo(this::getObject);
			}
			field.setAccessible(false);
		}

		assertEquals(Byte.MAX_VALUE, allFields.getByteValue());
		assertEquals(Short.MAX_VALUE, allFields.getShortValue());
		assertEquals(Integer.MAX_VALUE, allFields.getIntValue());
		assertEquals(Long.MAX_VALUE, allFields.getLongValue());
		assertEquals(Float.MAX_VALUE, allFields.getFloatValue());
		assertEquals(Double.MAX_VALUE, allFields.getDoubleValue());
		assertTrue(allFields.isBooleanValue());
		assertEquals(Character.MAX_VALUE, allFields.getCharValue());
		assertEquals(TestStreamField.class.getName(), allFields.getStringValue());
		assertEquals(ALL_FIELDS, allFields.getAllFields());
	}

	@Test
	public void testThenDoWithConsumer() throws IllegalAccessException
	{
		AllFieldsArray allFieldsArray = new AllFieldsArray();
		assertNull(allFieldsArray.getByteValue());
		assertNull(allFieldsArray.getShortValue());
		assertNull(allFieldsArray.getIntValue());
		assertNull(allFieldsArray.getLongValue());
		assertNull(allFieldsArray.getFloatValue());
		assertNull(allFieldsArray.getDoubleValue());
		assertNull(allFieldsArray.getBooleanValue());
		assertNull(allFieldsArray.getCharValue());
		assertNull(allFieldsArray.getStringValue());
		assertNull(allFieldsArray.getAllFields());

		StreamField streamField = new StreamField();

		for (Field field : allFieldsArray.getClass().getDeclaredFields())
		{
			field.setAccessible(true);
			{
				streamField.reset().setField(field).of(allFieldsArray)
					.is(Byte[].class, byte[].class).thenDo(this::getBytes)
					.is(Short[].class, short[].class).thenDo(this::getShorts)
					.is(Integer[].class, int[].class).thenDo(this::getInts)
					.is(Long[].class, long[].class).thenDo(this::getLongs)
					.is(Float[].class, float[].class).thenDo(this::getFloats)
					.is(Double[].class, double[].class).thenDo(this::getDoubles)
					.is(Boolean[].class, boolean[].class).thenDo(this::getBooleans)
					.is(Character[].class, char[].class).thenDo(this::getChars)
					.is(String[].class).thenDo(this::getStrings)
					.is(Object[].class).thenDo(this::getObjects);
			}
			field.setAccessible(false);
		}

		assertArrayEquals(this.getBytes(), allFieldsArray.getByteValue());
		assertArrayEquals(this.getShorts(), allFieldsArray.getShortValue());
		assertArrayEquals(this.getInts(), allFieldsArray.getIntValue());
		assertArrayEquals(this.getLongs(), allFieldsArray.getLongValue());
		assertArrayEquals(this.getFloats(), allFieldsArray.getFloatValue());
		assertArrayEquals(this.getDoubles(), allFieldsArray.getDoubleValue());
		assertArrayEquals(this.getBooleans(), allFieldsArray.getBooleanValue());
		assertArrayEquals(this.getChars(), allFieldsArray.getCharValue());
		assertArrayEquals(this.getStrings(), allFieldsArray.getStringValue());
		assertArrayEquals(this.getObjects(), allFieldsArray.getAllFields());
	}

	private byte getByte() { return Byte.MAX_VALUE; }
	private short getShort() { return Short.MAX_VALUE; }
	private int getInt() { return Integer.MAX_VALUE; }
	private long getLong() { return Long.MAX_VALUE; }
	private float getFloat() { return Float.MAX_VALUE; }
	private double getDouble() { return Double.MAX_VALUE; }
	private boolean getBoolean() { return true; }
	private char getChar() { return Character.MAX_VALUE; }
	private String getString() { return TestStreamField.class.getName(); }
	private AllFields getObject() { return ALL_FIELDS; }

	private byte[] getBytes() { return new byte[] { Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE }; }
	private short[] getShorts() { return new short[] { Short.MAX_VALUE, Short.MAX_VALUE, Short.MAX_VALUE }; }
	private int[] getInts() { return new int[] { Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE }; }
	private long[] getLongs() { return new long[] { Long.MAX_VALUE, Long.MAX_VALUE, Long.MAX_VALUE }; }
	private float[] getFloats() { return new float[] { Float.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE }; }
	private double[] getDoubles() { return new double[] { Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE }; }
	private boolean[] getBooleans() { return new boolean[] { true, true, true }; }
	private char[] getChars() { return new char[] { Character.MAX_VALUE, Character.MAX_VALUE, Character.MAX_VALUE }; }
	private String[] getStrings() { return new String[] { TestStreamField.class.getName(), TestStreamField.class.getName(), TestStreamField.class.getName() }; }
	private AllFields[] getObjects() { return new AllFields[] { ALL_FIELDS, ALL_FIELDS, ALL_FIELDS }; }

	@Data
	@Accessors(chain = true)
	@NoArgsConstructor
	@AllArgsConstructor
	private static class AllFields
	{
		private byte byteValue;
		private short shortValue;
		private int intValue;
		private long longValue;
		private float floatValue;
		private double doubleValue;
		private boolean booleanValue;
		private char charValue;
		private String stringValue;
		private AllFields allFields;
	}

	@Data
	@Accessors(chain = true)
	@NoArgsConstructor
	@AllArgsConstructor
	private static class AllFieldsArray
	{
		private byte[] byteValue;
		private short[] shortValue;
		private int[] intValue;
		private long[] longValue;
		private float[] floatValue;
		private double[] doubleValue;
		private boolean[] booleanValue;
		private char[] charValue;
		private String[] stringValue;
		private AllFields[] allFields;
	}
}
