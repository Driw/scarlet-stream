package org.diverproject.scarlet.stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.diverproject.scarlet.stream.buffer.DefaultBufferWriter;
import org.diverproject.scarlet.stream.buffer.DefaultByteBuffer;
import org.diverproject.scarlet.util.ArrayUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Default Buffer Writer")
public class TestDefaultBufferWriter
{
	private static final byte[] BYTES = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };

	@Test
	@DisplayName("Close buffer")
	public void testClose()
	{
		DefaultBufferWriter defaultBufferWriter = getDefaultBufferWriter();
		assertNotNull(defaultBufferWriter.getByteBuffer().data());
		defaultBufferWriter.close();
		assertNull(defaultBufferWriter.getByteBuffer().data());
	}

	@Test
	@DisplayName("Buffer reset")
	public void testReset()
	{
		DefaultBufferWriter defaultBufferWriter = getDefaultBufferWriter();
		ArrayUtils.copy(BYTES, defaultBufferWriter.getByteBuffer().data());
		assertArrayEquals(BYTES, defaultBufferWriter.getByteBuffer().data());
		defaultBufferWriter.reset();
		assertArrayEquals(new byte[10], defaultBufferWriter.getByteBuffer().data());
	}

	@Test
	@DisplayName("Buffer offset")
	public  void testOffset()
	{
		DefaultBufferWriter defaultBufferWriter = getDefaultBufferWriter();
		assertEquals(0, defaultBufferWriter.offset());
		defaultBufferWriter.write((byte) 1);
		assertEquals(1, defaultBufferWriter.offset());
		defaultBufferWriter.write(new byte[] { 2, 3, 4 });
		assertEquals(4, defaultBufferWriter.offset());
		defaultBufferWriter.write((byte) 5);
		assertEquals(5, defaultBufferWriter.offset());
		defaultBufferWriter.write(new byte[] { 6, 7, 8 });
		assertEquals(8, defaultBufferWriter.offset());

		assertArrayEquals(new byte[] { 1, 2, 3, 4, 5, 6, 7, 8, 0, 0 }, defaultBufferWriter.getByteBuffer().data());
		defaultBufferWriter.setOffset(2);

		defaultBufferWriter.write(new byte[] { 6, 7, 8 });
		assertEquals(5, defaultBufferWriter.offset());
		assertArrayEquals(new byte[] { 1, 2, 6, 7, 8, 6, 7, 8, 0, 0 }, defaultBufferWriter.getByteBuffer().data());
	}

	@Test
	@DisplayName("Buffer capacity")
	public  void testSize()
	{
		DefaultBufferWriter defaultBufferWriter = getDefaultBufferWriter();
		assertEquals(10, defaultBufferWriter.capacity());

		defaultBufferWriter.setOffset(5);
		assertEquals(10, defaultBufferWriter.capacity());

		defaultBufferWriter.write((byte) 1);
		assertEquals(10, defaultBufferWriter.capacity());

		defaultBufferWriter.write(new byte[4]);
		assertEquals(10, defaultBufferWriter.capacity());
	}

	@Test
	@DisplayName("Buffer write")
	public  void testWrite()
	{
		DefaultBufferWriter defaultBufferWriter = getDefaultBufferWriter();
		assertArrayEquals(new byte[10], defaultBufferWriter.getByteBuffer().data());

		defaultBufferWriter.write((byte) 1);
		assertArrayEquals(new byte[] { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, defaultBufferWriter.getByteBuffer().data());

		defaultBufferWriter.write((byte) 2);
		assertArrayEquals(new byte[] { 1, 2, 0, 0, 0, 0, 0, 0, 0, 0 }, defaultBufferWriter.getByteBuffer().data());

		defaultBufferWriter.write(new byte[] { 3, 4 });
		assertArrayEquals(new byte[] { 1, 2, 3, 4, 0, 0, 0, 0, 0, 0 }, defaultBufferWriter.getByteBuffer().data());

		defaultBufferWriter.write(new byte[] { 5, 6, 7 });
		assertArrayEquals(new byte[] { 1, 2, 3, 4, 5, 6, 7, 0, 0, 0 }, defaultBufferWriter.getByteBuffer().data());

		defaultBufferWriter.setOffset(5);
		defaultBufferWriter.write(new byte[] { 9, 9, 9 });
		assertArrayEquals(new byte[] { 1, 2, 3, 4, 5, 9, 9, 9, 0, 0 }, defaultBufferWriter.getByteBuffer().data());
	}

	private DefaultBufferWriter getDefaultBufferWriter()
	{
		return new DefaultBufferWriter().setByteBuffer(new DefaultByteBuffer().setBytes(new byte[10]));
	}
}
