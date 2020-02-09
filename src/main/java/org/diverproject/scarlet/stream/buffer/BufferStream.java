package org.diverproject.scarlet.stream.buffer;

import org.diverproject.scarlet.stream.Stream;

public interface BufferStream extends Stream
{
	ByteBuffer getByteBuffer();
}
