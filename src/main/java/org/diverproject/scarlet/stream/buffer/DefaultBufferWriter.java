package org.diverproject.scarlet.stream.buffer;

import static org.diverproject.scarlet.stream.language.BufferLanguage.WRITE_CLOSED_BUFFER;
import static org.diverproject.scarlet.stream.language.BufferLanguage.WRITE_END_OF_BUFFER;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class DefaultBufferWriter implements BufferWriter
{
	private ByteBuffer byteBuffer;
	private int offset;
	private boolean inverted;

	@Override
	public void close()
	{
		this.getByteBuffer().close();
	}

	@Override
	public void reset()
	{
		this.setOffset(0);
		this.getByteBuffer().clear();
	}

	@Override
	public boolean isClosed()
	{
		return this.getByteBuffer().isClosed();
	}

	@Override
	public boolean isFull()
	{
		return this.offset() == this.capacity();
	}

	@Override
	public boolean isEmpty()
	{
		return this.offset() == 0;
	}

	@Override
	public int capacity()
	{
		return this.getByteBuffer().length();
	}

	@Override
	public int offset()
	{
		return this.getOffset();
	}

	@Override
	public byte[] data()
	{
		return this.getByteBuffer().data();
	}

	@Override
	public void invert()
	{
		this.setInverted(!this.isInverted());
	}

	@Override
	public void write(byte value)
	{
		try {

			this.data()[this.offset()] = value;
			this.setOffset(this.getOffset() + 1);

		} catch (IndexOutOfBoundsException e) {
			throw new BufferRuntimeException(e, WRITE_END_OF_BUFFER, this.capacity(), this.offset());
		} catch (NullPointerException e) {
			throw new BufferRuntimeException(WRITE_CLOSED_BUFFER);
		}
	}

	@Override
	public void write(byte[] values)
	{
		if (this.isInverted())
		{
			for (int i = values.length - 1; i >= 0; i--)
				this.write(values[i]);
		}

		else
		{
			for (byte value : values)
				this.write(value);
		}
	}
}
