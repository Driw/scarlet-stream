package org.diverproject.scarlet.stream.buffer;

public interface ByteBuffer
{
	boolean isClosed();

	void close();
	void clear();
	void reset(int length);

	int length();
	byte[] data();
}
