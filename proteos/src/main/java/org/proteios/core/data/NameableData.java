/*
	$Id: NameableData.java 3207 2009-04-09 06:48:11Z gregory $

	Copyright (C) 2006 Gregory Vincic, Olle Mansson
	Copyright (C) 2007 Gregory Vincic

	This file is part of Proteios.
	Available at http://www.proteios.org/

	Proteios is free software; you can redistribute it and/or modify it
	under the terms of the GNU General Public License as published by
	the Free Software Foundation; either version 2 of the License, or
	(at your option) any later version.

	Proteios is distributed in the hope that it will be useful, but
	WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
	General Public License for more details.

	You should have received a copy of the GNU General Public License
	along with this program; if not, write to the Free Software
	Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
	02111-1307, USA.
*/
package org.proteios.core.data;

/**
	A nameable item is an item that has a <code>name</code> 
	and, optionally, a <code>description</code>.
	<p>
	This interface defines Hibernate database mappings for the
	<code>name</code> and <code>description</code> properties
	to database columns with the same name. If a subclass wants
	to map these properties to other columns, it should override
	the {@link #getName()} and/or {@link #getDescription()} methods 
	and add a Hibernate tag in the comment.
	<p>
	
	<b>Reference implementation</b><br>
	<pre class="code">
private String name;
public String getName()
{
   return name;
}
public void setName(String name)
{
   this.name = name;
}
private String description;
public String getDescription()
{
   return description;
}
public void setDescription(String description)
{
   this.description = description;
}
</pre>

	@author Nicklas
	@version 2.0
	@see CommonData
	@see <a href="../../../../../../../development/overview/data/basic.html">Basic classes and interfaces</a>
*/
public interface NameableData
	extends IdentifiableData
{

	/**
		The maximum length of the name of the item that can be
		stored in the database.
		@see #setName(String)
	*/
	public static int MAX_NAME_LENGTH = 255;

	/**
		Get the name of the item.
		@return A <code>String</code> with the name of the item
		@hibernate.property type="string"
		@hibernate.column name="`name`" length="255" not-null="true" index="name_idx"
	*/
	public String getName();

	/**
		Set the name of the item. The name cannot be null and mustn't
		be longer than the value specified by the
		{@link #MAX_NAME_LENGTH MAX_NAME_LENGTH} constant.
		@param name The new name for the item
	*/
	public void setName(String name);

	/**
		The maximum length of the description of the item that can be
		stored in the database.
		@see #setDescription(String)
	*/
	public static int MAX_DESCRIPTION_LENGTH = 65535;

	/**
		Get the description for the item.
		@return A <code>String</code> with a description of the item
		@hibernate.property column="`description`" type="text" not-null="false"
	*/
	public String getDescription();

	/**
		Set the description for the item. The description can be null but
		mustn't be longer than the value specified by the
		{@link #MAX_DESCRIPTION_LENGTH MAX_DESCRIPTION_LENGTH} constant.
		@param description The new description for the item
	*/
	public void setDescription(String description);

}
