package org.diverproject.scarlet.stream.buffer;

import org.diverproject.scarlet.stream.Reader;

public interface BufferReader extends Reader
{
	void reset();
	void close();

	ByteBuffer getByteBuffer();
	byte[] data();
	int offset();
}
