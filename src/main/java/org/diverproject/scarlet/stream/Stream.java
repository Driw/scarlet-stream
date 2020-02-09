package org.diverproject.scarlet.stream;

public interface Stream
{
	boolean isClosed();
	boolean isFull();
	boolean isEmpty();
	int capacity();
	int offset();

	void reset();
	void close();
}
