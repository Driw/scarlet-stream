package org.diverproject.scarlet.stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.diverproject.scarlet.stream.buffer.BufferRuntimeException;
import org.diverproject.scarlet.stream.buffer.ByteBuffer;
import org.diverproject.scarlet.stream.buffer.DefaultBufferReader;
import org.diverproject.scarlet.stream.buffer.DefaultByteBuffer;
import org.diverproject.scarlet.util.ArrayUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Default Buffer Reader")
public class TestDefaultBufferReader
{
	private static final byte[] BYTES = new byte[] { 0, 1, 2,3, 4, 5, 6, 7, 8, 9 };

	@Test
	@DisplayName("Reading byte a byte")
	public void testReadSingleByte()
	{
		DefaultBufferReader defaultBufferReader = getDefaultBufferReader();

		for (int i = 0; i < BYTES.length; i++)
			assertEquals(BYTES[i], defaultBufferReader.read());

		assertThrows(BufferRuntimeException.class, () -> {
			defaultBufferReader.read();
		});
	}

	@Test
	@DisplayName("Reading a byte array")
	public void testReadByteArray()
	{
		DefaultBufferReader defaultBufferReader = getDefaultBufferReader();
		assertArrayEquals(ArrayUtils.subArray(BYTES, 0, 5), defaultBufferReader.read(5));
		assertArrayEquals(ArrayUtils.subArray(BYTES, 5, 5), defaultBufferReader.read(5));

		defaultBufferReader = getDefaultBufferReader();
		assertArrayEquals(ArrayUtils.subArray(BYTES, 0, 6), defaultBufferReader.read(6));
		DefaultBufferReader finalDefaultBufferReader = defaultBufferReader;
		assertThrows(BufferRuntimeException.class, () -> {
			finalDefaultBufferReader.read(5);
		});
	}

	@Test
	@DisplayName("Reset buffer")
	public void testReset()
	{
		DefaultBufferReader defaultBufferReader = getDefaultBufferReader();
		assertArrayEquals(BYTES, defaultBufferReader.read(10));
		assertThrows(BufferRuntimeException.class, defaultBufferReader::read);
		defaultBufferReader.reset();
		assertEquals(BYTES[0], defaultBufferReader.read());
		assertArrayEquals(ArrayUtils.subArray(BYTES, 1, 9), defaultBufferReader.read(9));
	}

	@Test
	@DisplayName("Close buffer")
	public void testClose()
	{
		DefaultBufferReader defaultBufferReader = getDefaultBufferReader();
		defaultBufferReader.close();
		assertThrows(BufferRuntimeException.class, defaultBufferReader::read);
		assertThrows(BufferRuntimeException.class, () -> {
			defaultBufferReader.read(2);
		});
	}

	@Test
	@DisplayName("Offset of buffer")
	public void testOffset()
	{
		DefaultBufferReader defaultBufferReader = getDefaultBufferReader();
		assertEquals(0, defaultBufferReader.offset());

		defaultBufferReader.read();
		assertEquals(1, defaultBufferReader.offset());

		defaultBufferReader.read(3);
		assertEquals(4, defaultBufferReader.offset());

		defaultBufferReader.read();
		assertEquals(5, defaultBufferReader.offset());

		defaultBufferReader.read(3);
		assertEquals(8, defaultBufferReader.offset());
	}

	@Test
	@DisplayName("Buffer capacity")
	public void testSize()
	{
		assertEquals(10,new DefaultBufferReader().setByteBuffer(new DefaultByteBuffer(new byte[10])).capacity());
		assertEquals(15,new DefaultBufferReader().setByteBuffer(new DefaultByteBuffer(new byte[15])).capacity());
	}

	@Test
	@DisplayName("Bytes of buffer")
	public void testBytes()
	{
		byte[] temp = new byte[100];
		assertEquals(BYTES, getDefaultBufferReader().data());
		assertEquals(temp, new DefaultBufferReader().setByteBuffer(new DefaultByteBuffer(temp)).data());
	}

	private DefaultBufferReader getDefaultBufferReader()
	{
		ByteBuffer byteBuffer = new DefaultByteBuffer(BYTES);
		return new DefaultBufferReader().setByteBuffer(byteBuffer);
	}
}
