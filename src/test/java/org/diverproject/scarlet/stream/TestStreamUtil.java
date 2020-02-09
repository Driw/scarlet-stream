package org.diverproject.scarlet.stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Stream Util")
public class TestStreamUtil
{
	private static final byte ZERO = 0;
	private static final byte ONE = 1;

	@Test
	@DisplayName("Char of")
	public void testCharOf()
	{
		assertEquals((char) 0x0000, StreamUtil.charOf(ZERO));
		assertEquals((char) 0x0001, StreamUtil.charOf(ONE));
		assertEquals((char) 0x0001, StreamUtil.charOf(ZERO, ONE));
		assertEquals((char) 0x0100, StreamUtil.charOf(ONE, ZERO));
		assertEquals((char) 0x0001, StreamUtil.charOf(ZERO, ZERO, ONE));
		assertEquals((char) 0x0100, StreamUtil.charOf(ONE, ONE, ZERO));
	}

	@Test
	@DisplayName("Short of")
	public void testShortOf()
	{
		assertEquals(0x0000, StreamUtil.shortOf(ZERO));
		assertEquals(0x0001, StreamUtil.shortOf(ONE));
		assertEquals(0x0001, StreamUtil.shortOf(ZERO, ONE));
		assertEquals(0x0100, StreamUtil.shortOf(ONE, ZERO));
		assertEquals(0x0001, StreamUtil.shortOf(ZERO, ZERO, ONE));
		assertEquals(0x0100, StreamUtil.shortOf(ONE, ONE, ZERO));
	}

	@Test
	@DisplayName("Int of")
	public void testIntOf()
	{
		assertEquals(0x00000001, StreamUtil.intOf(ZERO, ZERO, ONE));
		assertEquals(0x00010000, StreamUtil.intOf(ONE, ZERO, ZERO));
		assertEquals(0x00000001, StreamUtil.intOf(ZERO, ZERO, ZERO, ONE));
		assertEquals(0x01000000, StreamUtil.intOf(ONE, ZERO, ZERO, ZERO));
		assertEquals(0x00000001, StreamUtil.intOf(ZERO, ZERO, ZERO, ZERO, ONE));
		assertEquals(0x01000000, StreamUtil.intOf(ONE, ONE, ZERO, ZERO, ZERO));
	}

	@Test
	@DisplayName("Long of")
	public void testLongOf()
	{
		assertEquals(0x0000000000000001L, StreamUtil.longOf(ZERO, ZERO, ZERO, ZERO, ZERO, ZERO, ONE));
		assertEquals(0x0001000000000000L, StreamUtil.longOf(ONE, ZERO, ZERO, ZERO, ZERO, ZERO, ZERO));
		assertEquals(0x0000000000000001L, StreamUtil.longOf(ZERO, ZERO, ZERO, ZERO, ZERO, ZERO, ZERO, ONE));
		assertEquals(0x0100000000000000L, StreamUtil.longOf(ONE, ZERO, ZERO, ZERO, ZERO, ZERO, ZERO, ZERO));
		assertEquals(0x0000000000000001L, StreamUtil.longOf(ZERO, ZERO, ZERO, ZERO, ZERO, ZERO, ZERO, ZERO, ONE));
		assertEquals(0x0100000000000000L, StreamUtil.longOf(ONE, ONE, ZERO, ZERO, ZERO, ZERO, ZERO, ZERO, ZERO));
	}

	@Test
	@DisplayName("Float Of")
	public void testFloatOf()
	{
		assertEquals(Float.intBitsToFloat(0x00000001), StreamUtil.floatOf(ZERO, ZERO, ONE));
		assertEquals(Float.intBitsToFloat(0x00010000), StreamUtil.floatOf(ONE, ZERO, ZERO));
		assertEquals(Float.intBitsToFloat(0x00000001), StreamUtil.floatOf(ZERO, ZERO, ZERO, ONE));
		assertEquals(Float.intBitsToFloat(0x01000000), StreamUtil.floatOf(ONE, ZERO, ZERO, ZERO));
		assertEquals(Float.intBitsToFloat(0x00000001), StreamUtil.floatOf(ZERO, ZERO, ZERO, ZERO, ONE));
		assertEquals(Float.intBitsToFloat(0x01000000), StreamUtil.floatOf(ONE, ONE, ZERO, ZERO, ZERO));
	}

	@Test
	@DisplayName("Double Of")
	public void testDoubleOf()
	{
		assertEquals(Double.longBitsToDouble(0x0000000000000001L), StreamUtil.doubleOf(ZERO, ZERO, ZERO, ZERO, ZERO, ZERO, ONE));
		assertEquals(Double.longBitsToDouble(0x0001000000000000L), StreamUtil.doubleOf(ONE, ZERO, ZERO, ZERO, ZERO, ZERO, ZERO));
		assertEquals(Double.longBitsToDouble(0x0000000000000001L), StreamUtil.doubleOf(ZERO, ZERO, ZERO, ZERO, ZERO, ZERO, ZERO, ONE));
		assertEquals(Double.longBitsToDouble(0x0100000000000000L), StreamUtil.doubleOf(ONE, ZERO, ZERO, ZERO, ZERO, ZERO, ZERO, ZERO));
		assertEquals(Double.longBitsToDouble(0x0000000000000001L), StreamUtil.doubleOf(ZERO, ZERO, ZERO, ZERO, ZERO, ZERO, ZERO, ZERO, ONE));
		assertEquals(Double.longBitsToDouble(0x0100000000000000L), StreamUtil.doubleOf(ONE, ONE, ZERO, ZERO, ZERO, ZERO, ZERO, ZERO, ZERO));
	}

	@Test
	@DisplayName("Bytes of")
	public void testBytesOf()
	{
		assertArrayEquals(bytes(ZERO, ZERO), StreamUtil.bytesOf((char) 0x0000));
		assertArrayEquals(bytes(ZERO, ONE), StreamUtil.bytesOf((char) 0x0001));
		assertArrayEquals(bytes(ONE, ZERO), StreamUtil.bytesOf((char) 0x0100));
		assertArrayEquals(bytes(ONE, ONE), StreamUtil.bytesOf((char) 0x0101));

		assertArrayEquals(bytes(ZERO, ZERO), StreamUtil.bytesOf((short) 0x0000));
		assertArrayEquals(bytes(ZERO, ONE), StreamUtil.bytesOf((short) 0x0001));
		assertArrayEquals(bytes(ONE, ZERO), StreamUtil.bytesOf((short) 0x0100));
		assertArrayEquals(bytes(ONE, ONE), StreamUtil.bytesOf((short) 0x0101));

		assertArrayEquals(bytes(ZERO, ZERO, ZERO, ZERO), StreamUtil.bytesOf(0x00000000));
		assertArrayEquals(bytes(ZERO, ZERO, ZERO, ONE), StreamUtil.bytesOf(0x00000001));
		assertArrayEquals(bytes(ONE, ZERO, ZERO, ZERO), StreamUtil.bytesOf(0x01000000));
		assertArrayEquals(bytes(ONE, ZERO, ZERO, ONE), StreamUtil.bytesOf(0x01000001));

		assertArrayEquals(bytes(ZERO, ZERO, ZERO, ZERO, ZERO, ZERO, ZERO, ZERO), StreamUtil.bytesOf(0x0000000000000000L));
		assertArrayEquals(bytes(ZERO, ZERO, ZERO, ZERO, ZERO, ZERO, ZERO, ONE), StreamUtil.bytesOf(0x0000000000000001L));
		assertArrayEquals(bytes(ONE, ZERO, ZERO, ZERO, ZERO, ZERO, ZERO, ZERO), StreamUtil.bytesOf(0x0100000000000000L));
		assertArrayEquals(bytes(ONE, ZERO, ZERO, ZERO, ZERO, ZERO, ZERO, ONE), StreamUtil.bytesOf(0x0100000000000001L));

		assertArrayEquals(bytes(ZERO, ZERO, ZERO, ZERO), StreamUtil.bytesOf(Float.intBitsToFloat(0x00000000)));
		assertArrayEquals(bytes(ZERO, ZERO, ZERO, ONE), StreamUtil.bytesOf(Float.intBitsToFloat(0x00000001)));
		assertArrayEquals(bytes(ONE, ZERO, ZERO, ZERO), StreamUtil.bytesOf(Float.intBitsToFloat(0x01000000)));
		assertArrayEquals(bytes(ONE, ZERO, ZERO, ONE), StreamUtil.bytesOf(Float.intBitsToFloat(0x01000001)));

		assertArrayEquals(bytes(ZERO, ZERO, ZERO, ZERO, ZERO, ZERO, ZERO, ZERO), StreamUtil.bytesOf(Double.longBitsToDouble(0x0000000000000000L)));
		assertArrayEquals(bytes(ZERO, ZERO, ZERO, ZERO, ZERO, ZERO, ZERO, ONE), StreamUtil.bytesOf(Double.longBitsToDouble(0x0000000000000001L)));
		assertArrayEquals(bytes(ONE, ZERO, ZERO, ZERO, ZERO, ZERO, ZERO, ZERO), StreamUtil.bytesOf(Double.longBitsToDouble(0x0100000000000000L)));
		assertArrayEquals(bytes(ONE, ZERO, ZERO, ZERO, ZERO, ZERO, ZERO, ONE), StreamUtil.bytesOf(Double.longBitsToDouble(0x0100000000000001L)));
	}

	private byte[] bytes(byte... bytes)
	{
		return bytes;
	}
}
