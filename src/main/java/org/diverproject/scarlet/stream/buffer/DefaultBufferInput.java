package org.diverproject.scarlet.stream.buffer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.diverproject.scarlet.stream.DefaultInput;
import org.diverproject.scarlet.stream.Reader;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class DefaultBufferInput extends DefaultInput implements BufferInput
{
	private BufferReader bufferReader;
	private boolean inverted;

	@Override
	protected Reader getReader()
	{
		return this.getBufferReader();
	}

	@Override
	public boolean isClosed()
	{
		return this.getBufferReader().isClosed();
	}

	@Override
	public boolean isFull()
	{
		return this.getBufferReader().isFull();
	}

	@Override
	public boolean isEmpty()
	{
		return this.getBufferReader().isEmpty();
	}

	@Override
	public int capacity()
	{
		return this.getBufferReader().capacity();
	}

	@Override
	public int offset()
	{
		return this.getBufferReader().offset();
	}

	@Override
	public void reset()
	{
		this.getBufferReader().reset();
	}

	@Override
	public void close()
	{
		this.getBufferReader().close();
	}

	@Override
	public ByteBuffer getByteBuffer()
	{
		return this.getBufferReader().getByteBuffer();
	}
}
