package org.proteios.core.data;

import java.util.Date;

public interface CreationInterface
{
	/**
	 Get the creation date and time
	 @hibernate.property column="`created`" type="timestamp" not-null="true" update="false"
	 */
	public abstract Date getCreated();


	public abstract void setCreated(Date created);
}