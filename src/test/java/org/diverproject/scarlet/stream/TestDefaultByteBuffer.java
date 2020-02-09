package org.diverproject.scarlet.stream;

import org.diverproject.scarlet.stream.buffer.DefaultByteBuffer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DisplayName("Default Byte Buffer")
public class TestDefaultByteBuffer
{
	private static final byte[] BYTES = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };

	@Test
	@DisplayName("Close buffer")
	public void testClose()
	{
		DefaultByteBuffer defaultByteBuffer = new DefaultByteBuffer(BYTES);
		assertEquals(BYTES, defaultByteBuffer.getBytes());
		defaultByteBuffer.close();
		assertNull(defaultByteBuffer.getBytes());
	}

	@Test
	@DisplayName("Clear buffer")
	public void testClear()
	{
		DefaultByteBuffer defaultByteBuffer = new DefaultByteBuffer(BYTES);
		assertEquals(BYTES, defaultByteBuffer.getBytes());
		defaultByteBuffer.clear();
		assertNotEquals(BYTES, defaultByteBuffer.getBytes());
		assertEquals(BYTES.length, defaultByteBuffer.getBytes().length);
	}

	@Test
	@DisplayName("Reset Buffer")
	public void testReset()
	{
		int newLength = BYTES.length * 2;
		DefaultByteBuffer defaultByteBuffer = new DefaultByteBuffer(BYTES);
		assertEquals(BYTES, defaultByteBuffer.getBytes());
		defaultByteBuffer.reset(newLength);
		assertNotEquals(BYTES, defaultByteBuffer.getBytes());
		assertEquals(newLength, defaultByteBuffer.getBytes().length);
	}

	@Test
	@DisplayName("Length of Buffer")
	public void testLength()
	{
		int length = BYTES.length;
		DefaultByteBuffer defaultByteBuffer = new DefaultByteBuffer(BYTES);
		assertEquals(BYTES, defaultByteBuffer.getBytes());
		assertEquals(length, defaultByteBuffer.length());

		defaultByteBuffer.clear();
		assertEquals(length, defaultByteBuffer.length());

		defaultByteBuffer.reset((length += 10));
		assertEquals(length, defaultByteBuffer.length());

		defaultByteBuffer.close();
		assertEquals(DefaultByteBuffer.EMPTY, defaultByteBuffer.length());
	}
}
