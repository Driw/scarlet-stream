package org.diverproject.scarlet.stream.language;

import lombok.Getter;
import lombok.Setter;
import org.diverproject.scarlet.language.Language;

public enum StreamLanguage implements Language
{
	GET_OBJECT_CONSTRUCTOR			("there is no empty constructor to be used (class: %s)"),
	GET_OBJECT_INSTANCE				("failure on create a new instance (class: %s)"),
	GET_OBJECT_FIELD_SET			("failure on set a field of object (class: %s, field: %s)"),
	PUT_STRING_LENGTH				("cannot put a string with more then 255 bytes (length: %d)"),
	PUT_OBJECT_NULL					("cannot put a null object"),

	;

	private @Getter @Setter	String format;

	private StreamLanguage(String format)
	{
		this.setFormat(format);
	}

	@Override
	public int getCode()
	{
		return this.ordinal();
	}
}
