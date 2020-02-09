package org.diverproject.scarlet.stream;

import org.diverproject.scarlet.ScarletRuntimeException;
import org.diverproject.scarlet.language.Language;

public class StreamRuntimeException extends ScarletRuntimeException
{
	private static final long serialVersionUID = -5342099091739607431L;

	public StreamRuntimeException(Language language)
	{
		super(language);
	}

	public StreamRuntimeException(Language language, Object... args)
	{
		super(language, args);
	}

	public StreamRuntimeException(Exception e)
	{
		super(e);
	}

	public StreamRuntimeException(Exception e, Language language)
	{
		super(e, language);
	}

	public StreamRuntimeException(Exception e, Language language, Object... args)
	{
		super(e, language, args);
	}
}
