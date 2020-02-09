package org.diverproject.scarlet.stream.buffer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class DefaultByteBuffer implements ByteBuffer
{
	public static final int EMPTY = -1;

	private byte[] bytes;

	@Override
	public boolean isClosed()
	{
		return bytes == null;
	}

	@Override
	public void close()
	{
		this.setBytes(null);
	}

	@Override
	public void clear()
	{
		this.reset(this.getBytes().length);
	}

	@Override
	public void reset(int length)
	{
		this.setBytes(new byte[length]);
	}

	@Override
	public int length()
	{
		return this.getBytes() != null ? this.getBytes().length : -1;
	}

	@Override
	public byte[] data()
	{
		return this.getBytes();
	}
}
