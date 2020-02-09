package org.diverproject.scarlet.stream.buffer;

import org.diverproject.scarlet.stream.Output;

public interface BufferOutput extends BufferStream, Output
{
	byte[] flush();
}
