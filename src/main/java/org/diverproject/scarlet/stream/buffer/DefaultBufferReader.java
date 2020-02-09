package org.diverproject.scarlet.stream.buffer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import static org.diverproject.scarlet.stream.language.BufferLanguage.READ_BYTE_ARRAY;
import static org.diverproject.scarlet.stream.language.BufferLanguage.READ_CLOSED;
import static org.diverproject.scarlet.stream.language.BufferLanguage.READ_SINGLE_BYTE;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class DefaultBufferReader implements BufferReader
{
	private ByteBuffer byteBuffer;
	private int offset;
	private boolean inverted;

	@Override
	public void invert()
	{
		this.setInverted(!this.isInverted());
	}

	@Override
	public byte read()
	{
		try {

			byte read = this.data()[this.offset()];
			this.setOffset(this.getOffset() + 1);

			return read;

		} catch (IndexOutOfBoundsException e) {
			throw new BufferRuntimeException(READ_SINGLE_BYTE, this.offset(), this.capacity());
		} catch (NullPointerException e) {
			throw new BufferRuntimeException(READ_CLOSED);
		}
	}

	@Override
	public byte[] read(int count)
	{
		if (!this.hasBytes(count))
			throw new BufferRuntimeException(READ_BYTE_ARRAY, this.offset(), count, this.capacity());

		try {

			byte[] data = new byte[count];

			if (this.isInverted())
				for (int i = 0; i < count; i++)
					data[count - i - 1] = this.read();
			else
				for (int i = 0; i < count; i++)
					data[i] = this.read();

			return data;

		} catch (IndexOutOfBoundsException e) {
			throw new BufferRuntimeException(READ_SINGLE_BYTE, this.offset(), this.capacity());
		} catch (NullPointerException e) {
			throw new BufferRuntimeException(READ_CLOSED);
		}
	}

	@Override
	public void close()
	{
		this.setOffset(0);
		this.getByteBuffer().close();
	}

	@Override
	public void reset()
	{
		this.setOffset(0);
	}

	@Override
	public boolean isClosed()
	{
		return this.getByteBuffer().isClosed();
	}

	@Override
	public boolean isFull()
	{
		return this.offset() == 0;
	}

	@Override
	public boolean isEmpty()
	{
		return this.offset() == this.capacity();
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

	private boolean hasBytes(int bytes)
	{
		return this.offset() + bytes <= this.capacity();
	}
}
