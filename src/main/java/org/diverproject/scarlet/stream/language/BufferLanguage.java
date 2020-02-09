package org.diverproject.scarlet.stream.language;

import lombok.Getter;
import lombok.Setter;
import org.diverproject.scarlet.language.Language;

public enum BufferLanguage implements Language
{
	READ_SINGLE_BYTE					("end of buffer reached to read a single byte (offset: %d, capacity: %d)"),
	READ_BYTE_ARRAY                     ("end of buffer reached to read a byte array (offset: %d, count: %d, capacity: %d)"),
	READ_CLOSED                         ("cannot read a closed buffer"),

	WRITE_END_OF_BUFFER                 ("end of buffer reached to write a byte (offset: %d, capacity: %d)"),
	WRITE_CLOSED_BUFFER					("cannot write a closed buffer")

	;

	private @Getter @Setter	String format;

	private BufferLanguage(String format)
	{
		this.setFormat(format);
	}

	@Override
	public int getCode()
	{
		return this.ordinal();
	}
}
