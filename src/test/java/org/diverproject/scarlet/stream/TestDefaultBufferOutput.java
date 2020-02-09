package org.diverproject.scarlet.stream;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.diverproject.scarlet.stream.buffer.BufferRuntimeException;
import org.diverproject.scarlet.stream.buffer.DefaultBufferOutput;
import org.diverproject.scarlet.stream.buffer.DefaultBufferWriter;
import org.diverproject.scarlet.stream.buffer.DefaultByteBuffer;
import org.diverproject.scarlet.util.ArrayUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Default Buffer Output")
public class TestDefaultBufferOutput
{
	private static final byte[] BYTES = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };

	@Test
	@DisplayName("Flush buffer")
	public void testFlush()
	{
		DefaultBufferOutput defaultBufferOutput = getDefaultBufferOutput(new byte[BYTES.length]);
		assertArrayEquals(new byte[0], defaultBufferOutput.flush());
		assertEquals(0, defaultBufferOutput.offset());
		assertTrue(defaultBufferOutput.isEmpty());

		defaultBufferOutput.put(BYTES);
		assertArrayEquals(BYTES, defaultBufferOutput.flush());
		assertEquals(0, defaultBufferOutput.offset());
		assertTrue(defaultBufferOutput.isEmpty());

		byte[] bytes = ArrayUtils.subArray(BYTES, 0, 5);
		defaultBufferOutput = getDefaultBufferOutput(new byte[BYTES.length]);
		defaultBufferOutput.put(bytes);
		assertArrayEquals(bytes, defaultBufferOutput.flush());
		assertEquals(0, defaultBufferOutput.offset());
		assertTrue(defaultBufferOutput.isEmpty());
	}

	@Test
	@DisplayName("Buffer is closed")
	public void testIsClosed()
	{
		DefaultBufferOutput defaultBufferOutput = getDefaultBufferOutput(new byte[BYTES.length]);
		assertFalse(defaultBufferOutput.isClosed());
		defaultBufferOutput.put(0x01);

		defaultBufferOutput.close();
		assertTrue(defaultBufferOutput.isClosed());
		assertThrows(BufferRuntimeException.class, () -> { defaultBufferOutput.put(0x00); });
	}

	@Test
	@DisplayName("Buffer is full")
	public void testIsFull()
	{
		DefaultBufferOutput defaultBufferOutput = getDefaultBufferOutput(new byte[BYTES.length]);
		assertFalse(defaultBufferOutput.isFull());
		defaultBufferOutput.put(new byte[defaultBufferOutput.capacity() - 1]);
		assertFalse(defaultBufferOutput.isFull());
		defaultBufferOutput.put((byte) 0x01);
		assertTrue(defaultBufferOutput.isFull());
	}

	@Test
	@DisplayName("Buffer is empty")
	public void testIsEmpty()
	{
		DefaultBufferOutput defaultBufferOutput = getDefaultBufferOutput(new byte[BYTES.length]);
		assertTrue(defaultBufferOutput.isEmpty());
		defaultBufferOutput.put((byte) 0x01);
		assertFalse(defaultBufferOutput.isEmpty());
	}

	@Test
	@DisplayName("Buffer capacity")
	public void testCapacity()
	{
		DefaultBufferOutput defaultBufferOutput = getDefaultBufferOutput(new byte[10]);
		assertEquals(10, defaultBufferOutput.capacity());

		defaultBufferOutput.put(new byte[5]);
		assertArrayEquals(new byte[5], defaultBufferOutput.flush());
		assertEquals(5, defaultBufferOutput.capacity());
	}

	@Test
	@DisplayName("Buffer offset")
	public void testOffset()
	{
		DefaultBufferOutput defaultBufferOutput = getDefaultBufferOutput(new byte[40]);
		assertEquals(0, defaultBufferOutput.offset());

		defaultBufferOutput.put((byte) 0x01);
		assertEquals(1, defaultBufferOutput.offset());

		defaultBufferOutput.put((short) 0x01);
		assertEquals(3, defaultBufferOutput.offset());

		defaultBufferOutput.put(1);
		assertEquals(7, defaultBufferOutput.offset());

		defaultBufferOutput.put(1L);
		assertEquals(15, defaultBufferOutput.offset());

		defaultBufferOutput.put(1F);
		assertEquals(19, defaultBufferOutput.offset());

		defaultBufferOutput.put(1D);
		assertEquals(27, defaultBufferOutput.offset());

		defaultBufferOutput.put(' ');
		assertEquals(29, defaultBufferOutput.offset());

		defaultBufferOutput.put(false);
		assertEquals(30, defaultBufferOutput.offset());

		defaultBufferOutput.put("Test");
		assertEquals(35, defaultBufferOutput.offset());
	}

	@Test
	@DisplayName("Buffer reset")
	public void testReset()
	{
		DefaultBufferOutput defaultBufferOutput = getDefaultBufferOutput(new byte[BYTES.length]);
		assertEquals(0, defaultBufferOutput.offset());
		assertEquals(BYTES.length, defaultBufferOutput.capacity());
		assertArrayEquals(new byte[BYTES.length], defaultBufferOutput.getByteBuffer().data());
		defaultBufferOutput.put(BYTES);

		assertEquals(BYTES.length, defaultBufferOutput.offset());
		assertArrayEquals(BYTES, defaultBufferOutput.getByteBuffer().data());

		defaultBufferOutput.reset();
		assertEquals(0, defaultBufferOutput.offset());
		assertEquals(BYTES.length, defaultBufferOutput.capacity());
		assertArrayEquals(new byte[BYTES.length], defaultBufferOutput.getByteBuffer().data());
	}

	@Test
	@DisplayName("Buffer close")
	public void testClose()
	{
		DefaultBufferOutput defaultBufferOutput = getDefaultBufferOutput(new byte[BYTES.length]);
		assertFalse(defaultBufferOutput.isClosed());
		assertNotNull(defaultBufferOutput.getByteBuffer().data());

		defaultBufferOutput.put(0x01);
		defaultBufferOutput.close();
		assertNull(defaultBufferOutput.getByteBuffer().data());
		assertTrue(defaultBufferOutput.isClosed());
		assertThrows(BufferRuntimeException.class, () -> { defaultBufferOutput.put(0x01); });
	}

	@Test
	@DisplayName("Put data")
	public void testPut()
	{
		byte one = 0x01;
		byte zero = 0x00;

		DefaultBufferOutput defaultBufferOutput = getDefaultBufferOutput(new byte[2]);
		defaultBufferOutput.put(one);
		assertArrayEquals(bytes(one, zero), defaultBufferOutput.getByteBuffer().data());

		defaultBufferOutput.reset();
		defaultBufferOutput.put(bytes(zero, one));
		assertArrayEquals(bytes(zero, one), defaultBufferOutput.getByteBuffer().data());

		defaultBufferOutput = getDefaultBufferOutput(new byte[4]);
		defaultBufferOutput.put((short) 0x0001);
		assertArrayEquals(bytes(zero, one, zero, zero), defaultBufferOutput.getByteBuffer().data());

		defaultBufferOutput.reset();
		defaultBufferOutput.put(new short[] { 0x0001, 0x0100 });
		assertArrayEquals(bytes(zero, one, one, zero), defaultBufferOutput.getByteBuffer().data());

		defaultBufferOutput = getDefaultBufferOutput(new byte[8]);
		defaultBufferOutput.put(0x00000001);
		assertArrayEquals(bytes(zero, zero, zero, one, zero, zero, zero, zero), defaultBufferOutput.getByteBuffer().data());

		defaultBufferOutput.reset();
		defaultBufferOutput.put(new int[] { 0x00000001, 0x01000000 });
		assertArrayEquals(bytes(zero, zero, zero, one, one, zero, zero, zero), defaultBufferOutput.getByteBuffer().data());

		defaultBufferOutput = getDefaultBufferOutput(new byte[16]);
		defaultBufferOutput.put(0x0000000000000001L);
		assertArrayEquals(bytes(zero, zero, zero, zero, zero, zero, zero, one, zero, zero, zero, zero, zero, zero, zero, zero), defaultBufferOutput.getByteBuffer().data());

		defaultBufferOutput.reset();
		defaultBufferOutput.put(new long[] { 0x0000000000000001L, 0x0100000000000000L });
		assertArrayEquals(bytes(zero, zero, zero, zero, zero, zero, zero, one, one, zero, zero, zero, zero, zero, zero, zero), defaultBufferOutput.getByteBuffer().data());

		defaultBufferOutput = getDefaultBufferOutput(new byte[8]);
		defaultBufferOutput.put(Float.intBitsToFloat(0x00000001));
		assertArrayEquals(bytes(zero, zero, zero, one, zero, zero, zero, zero), defaultBufferOutput.getByteBuffer().data());

		defaultBufferOutput.reset();
		defaultBufferOutput.put(new float[] { Float.intBitsToFloat(0x00000001), Float.intBitsToFloat(0x01000000) });
		assertArrayEquals(bytes(zero, zero, zero, one, one, zero, zero, zero), defaultBufferOutput.getByteBuffer().data());

		defaultBufferOutput = getDefaultBufferOutput(new byte[16]);
		defaultBufferOutput.put(Double.longBitsToDouble(0x0000000000000001L));
		assertArrayEquals(bytes(zero, zero, zero, zero, zero, zero, zero, one, zero, zero, zero, zero, zero, zero, zero, zero), defaultBufferOutput.getByteBuffer().data());

		defaultBufferOutput.reset();
		defaultBufferOutput.put(new double[] { Double.longBitsToDouble(0x0000000000000001L), Double.longBitsToDouble(0x0100000000000000L) });
		assertArrayEquals(bytes(zero, zero, zero, zero, zero, zero, zero, one, one, zero, zero, zero, zero, zero, zero, zero), defaultBufferOutput.getByteBuffer().data());

		defaultBufferOutput = getDefaultBufferOutput(new byte[2]);
		defaultBufferOutput.put(true);
		assertArrayEquals(bytes(one, zero), defaultBufferOutput.getByteBuffer().data());

		defaultBufferOutput.reset();
		defaultBufferOutput.put(new boolean[] { false, true });
		assertArrayEquals(bytes(zero, one), defaultBufferOutput.getByteBuffer().data());
	}

	private byte[] bytes(byte... bytes)
	{
		return bytes;
	}

	private DefaultBufferOutput getDefaultBufferOutput(byte[] data)
	{
		return new DefaultBufferOutput().setBufferWriter(new DefaultBufferWriter().setByteBuffer(new DefaultByteBuffer(data)));
	}
}
