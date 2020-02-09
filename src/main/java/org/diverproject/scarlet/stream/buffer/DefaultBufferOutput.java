package org.diverproject.scarlet.stream.buffer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.diverproject.scarlet.stream.DefaultOutput;
import org.diverproject.scarlet.stream.Writer;
import org.diverproject.scarlet.util.ArrayUtils;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class DefaultBufferOutput extends DefaultOutput implements BufferOutput
{
	private BufferWriter bufferWriter;
	private boolean inverted;
	private boolean dynamic;

	@Override
	public byte[] flush()
	{
		if (this.offset() == 0)
			return new byte[0];

		byte[] data = this.getByteBuffer().data();
		byte[] flushed = ArrayUtils.subArray(data, 0, this.offset());
		int length = this.isDynamic() ? this.capacity() : this.capacity() - this.offset();
		byte[] newData = new byte[length];

		System.arraycopy(data, this.offset(), newData, 0, length);
		this.setBufferWriter(new DefaultBufferWriter().setByteBuffer(new DefaultByteBuffer(newData)));

		return flushed;
	}

	@Override
	protected Writer getWriter()
	{
		return this.getBufferWriter();
	}

	@Override
	public boolean isClosed()
	{
		return this.getBufferWriter().isClosed();
	}

	@Override
	public boolean isFull()
	{
		return this.getBufferWriter().isFull();
	}

	@Override
	public boolean isEmpty()
	{
		return this.getBufferWriter().isEmpty();
	}

	@Override
	public int capacity()
	{
		return this.isDynamic() ? Integer.MAX_VALUE : this.getBufferWriter().capacity();
	}

	@Override
	public int offset()
	{
		return this.getBufferWriter().offset();
	}

	@Override
	public void reset()
	{
		this.getBufferWriter().reset();
	}

	@Override
	public void close()
	{
		this.getBufferWriter().close();
	}

	@Override
	public ByteBuffer getByteBuffer()
	{
		return this.getBufferWriter().getByteBuffer();
	}
}
