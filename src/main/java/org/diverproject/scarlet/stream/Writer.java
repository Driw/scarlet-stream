package org.diverproject.scarlet.stream;

public interface Writer extends Stream
{
	void invert();
	void write(byte value);
	void write(byte[] values);

	boolean isInverted();
}
