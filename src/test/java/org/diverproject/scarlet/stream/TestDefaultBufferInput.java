package org.diverproject.scarlet.stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.diverproject.scarlet.stream.buffer.BufferRuntimeException;
import org.diverproject.scarlet.stream.buffer.DefaultBufferInput;
import org.diverproject.scarlet.stream.buffer.DefaultBufferReader;
import org.diverproject.scarlet.stream.buffer.DefaultByteBuffer;
import org.diverproject.scarlet.util.ArrayUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Default Buffer Input")
public class TestDefaultBufferInput
{
	private static final byte[] BYTES = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
	private static final byte ZERO = 0x00;
	private static final byte ONE = 0x01;
	private static final String STRING_1 = "Some text1";
	private static final String STRING_2 = "Some text2";

	@Test
	@DisplayName("Get buffer bytes")
	public void testData()
	{
		DefaultBufferInput defaultBufferInput = this.getDefaultBufferInput(BYTES);
		assertEquals(BYTES, defaultBufferInput.getByteBuffer().data());
	}

	@Test
	@DisplayName("Is full")
	public void testIsFull()
	{
		DefaultBufferInput defaultBufferInput = this.getDefaultBufferInput(new byte[3]);
		assertTrue(defaultBufferInput.isFull());
		defaultBufferInput.getByte();
		assertFalse(defaultBufferInput.isFull());

		defaultBufferInput = this.getDefaultBufferInput(new byte[3]);
		assertTrue(defaultBufferInput.isFull());
		defaultBufferInput.getBytes(2);
		assertFalse(defaultBufferInput.isFull());
	}

	@Test
	@DisplayName("Is empty")
	public void testIsEmpty()
	{
		DefaultBufferInput defaultBufferInput = this.getDefaultBufferInput(new byte[3]);
		assertFalse(defaultBufferInput.isEmpty());

		defaultBufferInput.getByte();
		assertFalse(defaultBufferInput.isEmpty());

		defaultBufferInput.getBytes(2);
		assertTrue(defaultBufferInput.isEmpty());
	}

	@Test
	@DisplayName("Buffer capacity")
	public void testCapacity()
	{
		DefaultBufferInput defaultBufferInput = this.getDefaultBufferInput(new byte[3]);
		assertEquals(3, defaultBufferInput.capacity());

		defaultBufferInput.setBufferReader(new DefaultBufferReader().setByteBuffer(new DefaultByteBuffer(new byte[5])));
		assertEquals(5, defaultBufferInput.capacity());
	}

	@Test
	@DisplayName("Buffer offset")
	public void testOffset()
	{
		DefaultBufferInput defaultBufferInput = this.getDefaultBufferInput(new byte[36]);
		assertEquals(0, defaultBufferInput.offset());

		defaultBufferInput.getByte();
		assertEquals(1, defaultBufferInput.offset());

		defaultBufferInput.getShort();
		assertEquals(3, defaultBufferInput.offset());

		defaultBufferInput.getInt();
		assertEquals(7, defaultBufferInput.offset());

		defaultBufferInput.getLong();
		assertEquals(15, defaultBufferInput.offset());

		defaultBufferInput.getFloat();
		assertEquals(19, defaultBufferInput.offset());

		defaultBufferInput.getDouble();
		assertEquals(27, defaultBufferInput.offset());

		defaultBufferInput.getChar();
		assertEquals(29, defaultBufferInput.offset());

		defaultBufferInput.getBoolean();
		assertEquals(30, defaultBufferInput.offset());

		defaultBufferInput.getStrings(6);
		assertEquals(36, defaultBufferInput.offset());
	}

	@Test
	@DisplayName("Reset buffer")
	public void testReset()
	{
		DefaultBufferInput defaultBufferInput = this.getDefaultBufferInput(BYTES);
		assertArrayEquals(BYTES, defaultBufferInput.getBytes(BYTES.length));
		assertThrows(BufferRuntimeException.class, defaultBufferInput::getByte);
		defaultBufferInput.reset();
		assertEquals(BYTES[0], defaultBufferInput.getByte());
		assertArrayEquals(ArrayUtils.subArray(BYTES, 1, BYTES.length - 1), defaultBufferInput.getBytes(BYTES.length - 1));
	}

	@Test
	@DisplayName("Close buffer")
	public void testClose()
	{
		DefaultBufferInput defaultBufferInput = this.getDefaultBufferInput(BYTES);
		assertEquals(BYTES[0], defaultBufferInput.getByte());
		defaultBufferInput.close();
		assertThrows(BufferRuntimeException.class, defaultBufferInput::getByte);
		assertEquals(0, defaultBufferInput.offset());
		assertTrue(defaultBufferInput.isClosed());
	}

	@Test
	@DisplayName("Get byte")
	public void testGetByte()
	{
		DefaultBufferInput defaultBufferInput = this.getDefaultBufferInput(bytes(Byte.MIN_VALUE, Byte.MAX_VALUE));
		assertEquals(Byte.MIN_VALUE, defaultBufferInput.getByte());
		assertEquals(Byte.MAX_VALUE, defaultBufferInput.getByte());
		assertTrue(defaultBufferInput.isEmpty());
		assertTrue(defaultBufferInput.isEmpty());
	}

	@Test
	@DisplayName("Get bytes")
	public void testGetBytes()
	{
		DefaultBufferInput defaultBufferInput = this.getDefaultBufferInput(BYTES);
		assertArrayEquals(BYTES, defaultBufferInput.getBytes(BYTES.length));

		byte[] bytes = new byte[BYTES.length];
		defaultBufferInput.reset();
		defaultBufferInput.getBytes(bytes);
		assertArrayEquals(BYTES, bytes);
		assertTrue(defaultBufferInput.isEmpty());
	}

	@Test
	@DisplayName("Get short")
	public void testGetShort()
	{
		byte zero = 0x00;
		byte one = 0x01;

		DefaultBufferInput defaultBufferInput = this.getDefaultBufferInput(zero, one, one, zero);
		assertEquals(0x0001, defaultBufferInput.getShort());
		assertEquals(0x0100, defaultBufferInput.getShort());

		defaultBufferInput.reset();
		defaultBufferInput.invert();
		assertEquals(0x0100, defaultBufferInput.getShort());
		assertEquals(0x0001, defaultBufferInput.getShort());
		assertTrue(defaultBufferInput.isEmpty());
	}

	@Test
	@DisplayName("Get shorts")
	public void testGetShorts()
	{
		byte zero = 0x00;
		byte one = 0x01;
		short[] shorts = new short[2];

		DefaultBufferInput defaultBufferInput = this.getDefaultBufferInput(zero, one, one, zero);
		assertArrayEquals(new short[] { 0x0001, 0x0100 }, defaultBufferInput.getShorts(2));
		assertTrue(defaultBufferInput.isEmpty());

		defaultBufferInput.reset();
		defaultBufferInput.getShorts(shorts);
		assertArrayEquals(new short[] { 0x0001, 0x0100 }, shorts);
		assertTrue(defaultBufferInput.isEmpty());

		defaultBufferInput.reset();
		defaultBufferInput.invert();
		assertArrayEquals(new short[] { 0x0100, 0x0001 }, defaultBufferInput.getShorts(2));
		assertTrue(defaultBufferInput.isEmpty());

		defaultBufferInput.reset();
		defaultBufferInput.getShorts(shorts);
		assertArrayEquals(new short[] { 0x0100, 0x0001 }, shorts);
		assertTrue(defaultBufferInput.isEmpty());
	}

	@Test
	@DisplayName("Get int")
	public void testGetInt()
	{
		byte zero = 0x00;
		byte one = 0x01;

		DefaultBufferInput defaultBufferInput = this.getDefaultBufferInput(zero, zero, zero, one, one, zero, zero, zero);
		assertEquals(0x00000001, defaultBufferInput.getInt());
		assertEquals(0x01000000, defaultBufferInput.getInt());

		defaultBufferInput.reset();
		defaultBufferInput.invert();
		assertEquals(0x01000000, defaultBufferInput.getInt());
		assertEquals(0x00000001, defaultBufferInput.getInt());
		assertTrue(defaultBufferInput.isEmpty());
	}

	@Test
	@DisplayName("Get ints")
	public void testGetInts()
	{
		byte zero = 0x00;
		byte one = 0x01;
		int[] ints = new int[2];

		DefaultBufferInput defaultBufferInput = this.getDefaultBufferInput(zero, zero, zero, one, one, zero, zero, zero);
		assertArrayEquals(new int[] { 0x00000001, 0x01000000 }, defaultBufferInput.getInts(2));
		assertTrue(defaultBufferInput.isEmpty());

		defaultBufferInput.reset();
		defaultBufferInput.getInts(ints);
		assertArrayEquals(new int[] { 0x00000001, 0x01000000 }, ints);
		assertTrue(defaultBufferInput.isEmpty());

		defaultBufferInput.reset();
		defaultBufferInput.invert();
		assertArrayEquals(new int[] { 0x01000000, 0x00000001 }, defaultBufferInput.getInts(2));
		assertTrue(defaultBufferInput.isEmpty());

		defaultBufferInput.reset();
		defaultBufferInput.getInts(ints);
		assertArrayEquals(new int[] { 0x01000000, 0x00000001 }, ints);
		assertTrue(defaultBufferInput.isEmpty());
	}

	@Test
	@DisplayName("Get long")
	public void testGetLong()
	{
		byte zero = 0x00;
		byte one = 0x01;

		DefaultBufferInput defaultBufferInput = this.getDefaultBufferInput(zero, zero, zero, zero, zero, zero, zero, one, one, zero, zero, zero, zero, zero, zero, zero);
		assertEquals(0x0000000000000001L, defaultBufferInput.getLong());
		assertEquals(0x0100000000000000L, defaultBufferInput.getLong());

		defaultBufferInput.reset();
		defaultBufferInput.invert();
		assertEquals(0x0100000000000000L, defaultBufferInput.getLong());
		assertEquals(0x0000000000000001L, defaultBufferInput.getLong());
		assertTrue(defaultBufferInput.isEmpty());
	}

	@Test
	@DisplayName("Get longs")
	public void testGetLongs()
	{
		byte zero = 0x00;
		byte one = 0x01;
		long[] longs = new long[2];

		DefaultBufferInput defaultBufferInput = this.getDefaultBufferInput(zero, zero, zero, zero, zero, zero, zero, one, one, zero, zero, zero, zero, zero, zero, zero);
		assertArrayEquals(new long[] { 0x0000000000000001L, 0x0100000000000000L }, defaultBufferInput.getLongs(2));
		assertTrue(defaultBufferInput.isEmpty());

		defaultBufferInput.reset();
		defaultBufferInput.getLongs(longs);
		assertArrayEquals(new long[] { 0x0000000000000001L, 0x0100000000000000L }, longs);
		assertTrue(defaultBufferInput.isEmpty());

		defaultBufferInput.reset();
		defaultBufferInput.invert();
		assertArrayEquals(new long[] { 0x0100000000000000L, 0x0000000000000001L }, defaultBufferInput.getLongs(2));
		assertTrue(defaultBufferInput.isEmpty());

		defaultBufferInput.reset();
		defaultBufferInput.getLongs(longs);
		assertArrayEquals(new long[] { 0x0100000000000000L, 0x0000000000000001L }, longs);
		assertTrue(defaultBufferInput.isEmpty());
	}

	@Test
	@DisplayName("Get float")
	public void testGetFloat()
	{
		byte zero = 0x00;
		byte one = 0x01;

		DefaultBufferInput defaultBufferInput = this.getDefaultBufferInput(zero, zero, zero, one, one, zero, zero, zero);
		assertEquals(Float.intBitsToFloat(0x00000001), defaultBufferInput.getFloat());
		assertEquals(Float.intBitsToFloat(0x01000000), defaultBufferInput.getFloat());

		defaultBufferInput.reset();
		defaultBufferInput.invert();
		assertEquals(Float.intBitsToFloat(0x01000000), defaultBufferInput.getFloat());
		assertEquals(Float.intBitsToFloat(0x00000001), defaultBufferInput.getFloat());

		assertTrue(defaultBufferInput.isEmpty());
	}

	@Test
	@DisplayName("Get floats")
	public void testGetFloats()
	{
		byte zero = 0x00;
		byte one = 0x01;
		float[] ints = new float[2];

		DefaultBufferInput defaultBufferInput = this.getDefaultBufferInput(zero, zero, zero, one, one, zero, zero, zero);
		assertArrayEquals(new float[] { Float.intBitsToFloat(0x00000001), Float.intBitsToFloat(0x01000000) }, defaultBufferInput.getFloats(2));
		assertTrue(defaultBufferInput.isEmpty());

		defaultBufferInput.reset();
		defaultBufferInput.getFloats(ints);
		assertArrayEquals(new float[] { Float.intBitsToFloat(0x00000001), Float.intBitsToFloat(0x01000000) }, ints);
		assertTrue(defaultBufferInput.isEmpty());

		defaultBufferInput.reset();
		defaultBufferInput.invert();
		assertArrayEquals(new float[] { Float.intBitsToFloat(0x01000000), Float.intBitsToFloat(0x00000001) }, defaultBufferInput.getFloats(2));
		assertTrue(defaultBufferInput.isEmpty());

		defaultBufferInput.reset();
		defaultBufferInput.getFloats(ints);
		assertArrayEquals(new float[] { Float.intBitsToFloat(0x01000000), Float.intBitsToFloat(0x00000001) }, ints);
		assertTrue(defaultBufferInput.isEmpty());
	}

	@Test
	@DisplayName("Get double")
	public void testGetDouble()
	{
		byte zero = 0x00;
		byte one = 0x01;

		DefaultBufferInput defaultBufferInput = this.getDefaultBufferInput(zero, zero, zero, zero, zero, zero, zero, one, one, zero, zero, zero, zero, zero, zero, zero);
		assertEquals(Double.longBitsToDouble(0x0000000000000001L), defaultBufferInput.getDouble());
		assertEquals(Double.longBitsToDouble(0x0100000000000000L), defaultBufferInput.getDouble());

		defaultBufferInput.reset();
		defaultBufferInput.invert();
		assertEquals(Double.longBitsToDouble(0x0100000000000000L), defaultBufferInput.getDouble());
		assertEquals(Double.longBitsToDouble(0x0000000000000001L), defaultBufferInput.getDouble());
		assertTrue(defaultBufferInput.isEmpty());
	}

	@Test
	@DisplayName("Get doubles")
	public void testGetDoubles()
	{
		byte zero = 0x00;
		byte one = 0x01;
		double[] doubles = new double[2];

		DefaultBufferInput defaultBufferInput = this.getDefaultBufferInput(zero, zero, zero, zero, zero, zero, zero, one, one, zero, zero, zero, zero, zero, zero, zero);
		assertArrayEquals(new double[] { Double.longBitsToDouble(0x0000000000000001L), Double.longBitsToDouble(0x0100000000000000L) }, defaultBufferInput.getDoubles(2));
		assertTrue(defaultBufferInput.isEmpty());

		defaultBufferInput.reset();
		defaultBufferInput.getDoubles(doubles);
		assertArrayEquals(new double[] { Double.longBitsToDouble(0x0000000000000001L), Double.longBitsToDouble(0x0100000000000000L) }, doubles);
		assertTrue(defaultBufferInput.isEmpty());

		defaultBufferInput.reset();
		defaultBufferInput.invert();
		assertArrayEquals(new double[] { Double.longBitsToDouble(0x0100000000000000L), Double.longBitsToDouble(0x0000000000000001L) }, defaultBufferInput.getDoubles(2));
		assertTrue(defaultBufferInput.isEmpty());

		defaultBufferInput.reset();
		defaultBufferInput.getDoubles(doubles);
		assertArrayEquals(new double[] { Double.longBitsToDouble(0x0100000000000000L), Double.longBitsToDouble(0x0000000000000001L) }, doubles);
		assertTrue(defaultBufferInput.isEmpty());
	}

	@Test
	@DisplayName("Get char")
	public void testGetChar()
	{
		byte zero = 0x00;
		byte one = 0x01;

		DefaultBufferInput defaultBufferInput = this.getDefaultBufferInput(zero, one, one, zero);
		assertEquals(0x0001, defaultBufferInput.getChar());
		assertEquals(0x0100, defaultBufferInput.getChar());

		defaultBufferInput.reset();
		defaultBufferInput.invert();
		assertEquals(0x0100, defaultBufferInput.getChar());
		assertEquals(0x0001, defaultBufferInput.getChar());
		assertTrue(defaultBufferInput.isEmpty());
	}

	@Test
	@DisplayName("Get chars")
	public void testGetChars()
	{
		byte zero = 0x00;
		byte one = 0x01;
		char[] chars = new char[2];

		DefaultBufferInput defaultBufferInput = this.getDefaultBufferInput(zero, one, one, zero);
		assertArrayEquals(new char[] { 0x0001, 0x0100 }, defaultBufferInput.getChars(2));
		assertTrue(defaultBufferInput.isEmpty());

		defaultBufferInput.reset();
		defaultBufferInput.getChars(chars);
		assertArrayEquals(new char[] { 0x0001, 0x0100 }, chars);
		assertTrue(defaultBufferInput.isEmpty());

		defaultBufferInput.reset();
		defaultBufferInput.invert();
		assertArrayEquals(new char[] { 0x0100, 0x0001 }, defaultBufferInput.getChars(2));
		assertTrue(defaultBufferInput.isEmpty());

		defaultBufferInput.reset();
		defaultBufferInput.getChars(chars);
		assertArrayEquals(new char[] { 0x0100, 0x0001 }, chars);
		assertTrue(defaultBufferInput.isEmpty());
	}

	@Test
	@DisplayName("Get boolean")
	public void testGetBoolean()
	{
		byte zero = 0x00;
		byte one = 0x01;

		DefaultBufferInput defaultBufferInput = this.getDefaultBufferInput(zero, one);
		assertFalse(defaultBufferInput.getBoolean());
		assertTrue(defaultBufferInput.getBoolean());
		assertTrue(defaultBufferInput.isEmpty());
	}

	@Test
	@DisplayName("Get booleans")
	public void testGetBooleans()
	{
		byte zero = 0x00;
		byte one = 0x01;
		boolean[] booleans = new boolean[2];

		DefaultBufferInput defaultBufferInput = this.getDefaultBufferInput(zero, one);
		assertArrayEquals(new boolean[] { false, true }, defaultBufferInput.getBooleans(2));
		assertTrue(defaultBufferInput.isEmpty());

		defaultBufferInput.reset();
		defaultBufferInput.getBooleans(booleans);
		assertArrayEquals(new boolean[] { false, true }, booleans);
		assertTrue(defaultBufferInput.isEmpty());
	}

	@Test
	@DisplayName("Get string")
	public void testGetString()
	{
		String string = "Test get String";
		String[] strings = null;
		byte[] data = null;
		{
			data = new byte[string.length() + 1];
			data[0] = (byte) string.length();
			System.arraycopy(string.getBytes(), 0, data, 1, string.length());
			DefaultBufferInput defaultBufferInput = this.getDefaultBufferInput(data);
			assertEquals(string, defaultBufferInput.getString());

			defaultBufferInput = this.getDefaultBufferInput(string.getBytes());
			assertEquals(string, defaultBufferInput.getString(string.length()));

			data = new byte[string.length() * 3];
			System.arraycopy(string.getBytes(), 0, data, 0, string.length());
			System.arraycopy(string.getBytes(), 0, data, string.length(), string.length());
			System.arraycopy(string.getBytes(), 0, data, string.length() * 2, string.length());
			defaultBufferInput = this.getDefaultBufferInput(data);
			assertArrayEquals(new String[] { string, string, string }, defaultBufferInput.getStrings(3, string.length()));

			defaultBufferInput.reset();
			defaultBufferInput.getStrings((strings = new String[3]), string.length());
			assertArrayEquals(new String[] { string, string, string }, strings);
		}
	}

	@Test
	@DisplayName("Get object")
	public void testGetObject()
	{
		SomeObject someObject1 = getSomeObject1();
		SomeObject someObject2 = getSomeObject2().setSomeObject(someObject1);
		byte[] data = getSomeObjectData(someObject2, someObject1);

		DefaultBufferInput defaultBufferInput = this.getDefaultBufferInput(data);
		SomeObject someObject = defaultBufferInput.getObject(SomeObject.class);
		assertEquals(someObject2.getByteValue(), someObject.getByteValue());
		assertEquals(someObject2.getShortValue(), someObject.getShortValue());
		assertEquals(someObject2.getIntValue(), someObject.getIntValue());
		assertEquals(someObject2.getLongValue(), someObject.getLongValue());
		assertEquals(someObject2.getFloatValue(), someObject.getFloatValue());
		assertEquals(someObject2.getDoubleValue(), someObject.getDoubleValue());
		assertEquals(someObject2.isBooleanValue(), someObject.isBooleanValue());
		assertEquals(someObject2.getCharValue(), someObject.getCharValue());
		assertEquals(someObject2.getStringValue(), someObject.getStringValue());
		assertEquals(someObject1.getByteValue(), someObject.getSomeObject().getByteValue());
		assertEquals(someObject1.getShortValue(), someObject.getSomeObject().getShortValue());
		assertEquals(someObject1.getIntValue(), someObject.getSomeObject().getIntValue());
		assertEquals(someObject1.getLongValue(), someObject.getSomeObject().getLongValue());
		assertEquals(someObject1.getFloatValue(), someObject.getSomeObject().getFloatValue());
		assertEquals(someObject1.getDoubleValue(), someObject.getSomeObject().getDoubleValue());
		assertEquals(someObject1.isBooleanValue(), someObject.getSomeObject().isBooleanValue());
		assertEquals(someObject1.getCharValue(), someObject.getSomeObject().getCharValue());
		assertEquals(someObject1.getStringValue(), someObject.getSomeObject().getStringValue());
	}

	@Test
	@DisplayName("Get objects")
	public void testGetObjects()
	{
		SomeObject someObject1 = getSomeObject1();
		SomeObject someObject2 = getSomeObject2();
		SomeObject someObject3 = getSomeObject1().setSomeObject(getSomeObject2());
		SomeObject someObject4 = getSomeObject2().setSomeObject(getSomeObject1());
		SomeObject[] someObjects = new SomeObject[] {
			someObject1,
			someObject2,
			someObject3,
			someObject3.getSomeObject(),
			someObject4,
			someObject4.getSomeObject(),
		};
		byte[] data = getSomeObjectData(someObjects);

		DefaultBufferInput defaultBufferInput = this.getDefaultBufferInput(data);
		someObjects = defaultBufferInput.getObjects(SomeObject.class, 4);

		assertEquals(someObjects[0].getByteValue(), someObject1.getByteValue());
		assertEquals(someObjects[0].getShortValue(), someObject1.getShortValue());
		assertEquals(someObjects[0].getIntValue(), someObject1.getIntValue());
		assertEquals(someObjects[0].getLongValue(), someObject1.getLongValue());
		assertEquals(someObjects[0].getFloatValue(), someObject1.getFloatValue());
		assertEquals(someObjects[0].getDoubleValue(), someObject1.getDoubleValue());
		assertEquals(someObjects[0].isBooleanValue(), someObject1.isBooleanValue());
		assertEquals(someObjects[0].getCharValue(), someObject1.getCharValue());
		assertEquals(someObjects[0].getStringValue(), someObject1.getStringValue());

		assertEquals(someObjects[1].getByteValue(), someObject2.getByteValue());
		assertEquals(someObjects[1].getShortValue(), someObject2.getShortValue());
		assertEquals(someObjects[1].getIntValue(), someObject2.getIntValue());
		assertEquals(someObjects[1].getLongValue(), someObject2.getLongValue());
		assertEquals(someObjects[1].getFloatValue(), someObject2.getFloatValue());
		assertEquals(someObjects[1].getDoubleValue(), someObject2.getDoubleValue());
		assertEquals(someObjects[1].isBooleanValue(), someObject2.isBooleanValue());
		assertEquals(someObjects[1].getCharValue(), someObject2.getCharValue());
		assertEquals(someObjects[1].getStringValue(), someObject2.getStringValue());

		assertEquals(someObjects[2].getByteValue(), someObject3.getByteValue());
		assertEquals(someObjects[2].getShortValue(), someObject3.getShortValue());
		assertEquals(someObjects[2].getIntValue(), someObject3.getIntValue());
		assertEquals(someObjects[2].getLongValue(), someObject3.getLongValue());
		assertEquals(someObjects[2].getFloatValue(), someObject3.getFloatValue());
		assertEquals(someObjects[2].getDoubleValue(), someObject3.getDoubleValue());
		assertEquals(someObjects[2].isBooleanValue(), someObject3.isBooleanValue());
		assertEquals(someObjects[2].getCharValue(), someObject3.getCharValue());
		assertEquals(someObjects[2].getStringValue(), someObject3.getStringValue());

		assertEquals(someObjects[2].getSomeObject().getByteValue(), someObject3.getSomeObject().getByteValue());
		assertEquals(someObjects[2].getSomeObject().getShortValue(), someObject3.getSomeObject().getShortValue());
		assertEquals(someObjects[2].getSomeObject().getIntValue(), someObject3.getSomeObject().getIntValue());
		assertEquals(someObjects[2].getSomeObject().getLongValue(), someObject3.getSomeObject().getLongValue());
		assertEquals(someObjects[2].getSomeObject().getFloatValue(), someObject3.getSomeObject().getFloatValue());
		assertEquals(someObjects[2].getSomeObject().getDoubleValue(), someObject3.getSomeObject().getDoubleValue());
		assertEquals(someObjects[2].getSomeObject().isBooleanValue(), someObject3.getSomeObject().isBooleanValue());
		assertEquals(someObjects[2].getSomeObject().getCharValue(), someObject3.getSomeObject().getCharValue());
		assertEquals(someObjects[2].getSomeObject().getStringValue(), someObject3.getSomeObject().getStringValue());

		assertEquals(someObjects[3].getByteValue(), someObject4.getByteValue());
		assertEquals(someObjects[3].getShortValue(), someObject4.getShortValue());
		assertEquals(someObjects[3].getIntValue(), someObject4.getIntValue());
		assertEquals(someObjects[3].getLongValue(), someObject4.getLongValue());
		assertEquals(someObjects[3].getFloatValue(), someObject4.getFloatValue());
		assertEquals(someObjects[3].getDoubleValue(), someObject4.getDoubleValue());
		assertEquals(someObjects[3].isBooleanValue(), someObject4.isBooleanValue());
		assertEquals(someObjects[3].getCharValue(), someObject4.getCharValue());
		assertEquals(someObjects[3].getStringValue(), someObject4.getStringValue());

		assertEquals(someObjects[3].getSomeObject().getByteValue(), someObject4.getSomeObject().getByteValue());
		assertEquals(someObjects[3].getSomeObject().getShortValue(), someObject4.getSomeObject().getShortValue());
		assertEquals(someObjects[3].getSomeObject().getIntValue(), someObject4.getSomeObject().getIntValue());
		assertEquals(someObjects[3].getSomeObject().getLongValue(), someObject4.getSomeObject().getLongValue());
		assertEquals(someObjects[3].getSomeObject().getFloatValue(), someObject4.getSomeObject().getFloatValue());
		assertEquals(someObjects[3].getSomeObject().getDoubleValue(), someObject4.getSomeObject().getDoubleValue());
		assertEquals(someObjects[3].getSomeObject().isBooleanValue(), someObject4.getSomeObject().isBooleanValue());
		assertEquals(someObjects[3].getSomeObject().getCharValue(), someObject4.getSomeObject().getCharValue());
		assertEquals(someObjects[3].getSomeObject().getStringValue(), someObject4.getSomeObject().getStringValue());
	}

	private byte[] getSomeObjectData(SomeObject... someObjects)
	{
		byte[][] someObjectsData = new byte[someObjects.length][];

		for (int i = 0; i < someObjects.length; i++)
			someObjectsData[i] = getSomeObjectData(someObjects[i]);

		return joinData(someObjectsData);
	}

	private SomeObject getSomeObject1()
	{
		return new SomeObject()
			.setByteValue(Byte.MAX_VALUE)
			.setShortValue(Short.MAX_VALUE)
			.setIntValue(Integer.MAX_VALUE)
			.setLongValue(Long.MAX_VALUE)
			.setFloatValue(Float.MAX_VALUE)
			.setDoubleValue(Double.MAX_VALUE)
			.setCharValue(Character.MAX_VALUE)
			.setBooleanValue(true)
			.setStringValue(STRING_1);
	}

	private SomeObject getSomeObject2()
	{
		return new SomeObject()
			.setByteValue(Byte.MIN_VALUE)
			.setShortValue(Short.MIN_VALUE)
			.setIntValue(Integer.MIN_VALUE)
			.setLongValue(Long.MIN_VALUE)
			.setFloatValue(Float.MIN_VALUE)
			.setDoubleValue(Double.MIN_VALUE)
			.setCharValue(Character.MIN_VALUE)
			.setBooleanValue(false)
			.setStringValue(STRING_2);
	}

	private byte[] getSomeObjectData(SomeObject someObject)
	{
		byte[] data = new byte[Byte.BYTES + Short.BYTES + Integer.BYTES + Long.BYTES + Float.BYTES + Double.BYTES + Character.BYTES + 1 + someObject.getStringValue().length() + 1 + 1];
		System.arraycopy(bytes(someObject.getByteValue()), 0, data, 0, Byte.BYTES);
		System.arraycopy(StreamUtil.bytesOf(someObject.getShortValue()), 0, data, 1, Short.BYTES);
		System.arraycopy(StreamUtil.bytesOf(someObject.getIntValue()), 0, data, 3, Integer.BYTES);
		System.arraycopy(StreamUtil.bytesOf(someObject.getLongValue()), 0, data, 7, Long.BYTES);
		System.arraycopy(StreamUtil.bytesOf(someObject.getFloatValue()), 0, data, 15, Float.BYTES);
		System.arraycopy(StreamUtil.bytesOf(someObject.getDoubleValue()), 0, data, 19, Double.BYTES);
		System.arraycopy(new byte[] {someObject.isBooleanValue() ? ONE : ZERO }, 0, data, 27, 1);
		System.arraycopy(StreamUtil.bytesOf(someObject.getCharValue()), 0, data, 28, Character.BYTES);
		System.arraycopy(new byte[] {(byte) someObject.getStringValue().length()}, 0, data, 30, 1);
		System.arraycopy(someObject.getStringValue().getBytes(), 0, data, 31, someObject.getStringValue().length());
		System.arraycopy(new byte[] { someObject.getSomeObject() == null ? ZERO : ONE }, 0, data, 41, 1);

		return data;
	}

	private byte[] joinData(byte[]... arrays)
	{
		int length = 0;

		for (byte[] array : arrays)
			length += array.length;

		byte[] data = new byte[length];

		for (int i = 0, offset = 0; i < arrays.length; i++)
		{
			System.arraycopy(arrays[i], 0, data, offset, arrays[i].length);
			offset += arrays[i].length;
		}

		return data;
	}

	private DefaultBufferInput getDefaultBufferInput(byte... bytes)
	{
		return new DefaultBufferInput().setBufferReader(new DefaultBufferReader().setByteBuffer(new DefaultByteBuffer(bytes)));
	}

	private byte[] bytes(byte... bytes)
	{
		return bytes;
	}

	@Data
	@Accessors(chain = true)
	@NoArgsConstructor
	@AllArgsConstructor
	private static class SomeObject
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
		private SomeObject someObject;
	}
}
