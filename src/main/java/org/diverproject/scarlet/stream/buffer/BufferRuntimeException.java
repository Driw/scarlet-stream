package org.diverproject.scarlet.stream.buffer;

import org.diverproject.scarlet.language.Language;
import org.diverproject.scarlet.stream.StreamRuntimeException;

public class BufferRuntimeException extends StreamRuntimeException
{
	private static final long serialVersionUID = -2369136684542681375L;

	public BufferRuntimeException(Language language)
	{
		super(language);
	}

	public BufferRuntimeException(Language language, Object... args)
	{
		super(language, args);
	}

	public BufferRuntimeException(Exception e)
	{
		super(e);
	}

	public BufferRuntimeException(Exception e, Language language)
	{
		super(e, language);
	}

	public BufferRuntimeException(Exception e, Language language, Object... args)
	{
		super(e, language, args);
	}
}
