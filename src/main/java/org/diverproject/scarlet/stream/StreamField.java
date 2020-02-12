package org.diverproject.scarlet.stream;

import lombok.Data;
import lombok.experimental.Accessors;

import java.lang.reflect.Field;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Data
@Accessors(chain = true)
public class StreamField
{
	private boolean made;
	private boolean equals;
	private Field field;
	private Object object;

	public StreamField is(Class<?>... classTypes)
	{
		return this.isMade() ? this : this.isAssignableFrom(classTypes);
	}

	public StreamField isArray(Class<?>... classTypes)
	{
		return this.isMade() ? this : this.isArrayAssignableFrom(classTypes);
	}

	private StreamField isAssignableFrom(Class<?>[] classTypes)
	{
		for (Class<?> classType : classTypes)
			if (classType.isAssignableFrom(this.getField().getType()))
			{
				this.setEquals(true);
				break;
			}

		return this;
	}

	private StreamField isArrayAssignableFrom(Class<?>[] classTypes)
	{
		for (Class<?> classType : classTypes)
			if (classType.isAssignableFrom(this.getField().getType().getComponentType()))
			{
				this.setEquals(true);
				break;
			}

		return this;
	}

	public StreamField of(Object instance)
	{
		return this.setObject(instance);
	}

	public <D> StreamField thenDo(Supplier<D> supplier) throws IllegalAccessException
	{
		if (this.isMade())
			return this;

		if (!this.isEquals())
			return this;

		this.setMade(true);
		this.getField().set(this.getObject(), supplier.get());
		this.setMade(true);

		return this;
	}

	public <T> StreamField thenDo(Consumer<T> consumer) throws IllegalAccessException
	{
		if (this.isMade())
			return this;

		if (!this.isEquals())
			return this;

		this.setMade(true);

		@SuppressWarnings("unchecked")
		T currentValue = (T) field.get(this.getObject());
		consumer.accept(currentValue);
		return this;
	}

	public StreamField reset()
	{
		return this.setMade(false).setEquals(false);
	}
}
