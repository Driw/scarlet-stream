package org.diverproject.scarlet.stream.buffer;

import org.diverproject.scarlet.stream.Writer;

public interface BufferWriter extends Writer
{
	void reset();
	void close();

	ByteBuffer getByteBuffer();
	byte[] data();
	int offset();
}
