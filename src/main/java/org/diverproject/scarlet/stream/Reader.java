package org.diverproject.scarlet.stream;

public interface Reader extends Stream
{
	void invert();
	byte read();
	byte[] read(int count);

	boolean isInverted();
}
