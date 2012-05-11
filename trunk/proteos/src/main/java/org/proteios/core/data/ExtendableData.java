/*
	$Id: ExtendableData.java 3207 2009-04-09 06:48:11Z gregory $

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
	This class is inherited by data classes that supports
	per-server additions of columns to the underlying database table.
	<p>
	The added columns must be described in the <code>extended-properties.xml</code>
	configuration file. The format of the XML file is forced by a DTD. In summary:

	<pre class="code">
&lt;?xml version="1.0" ?&gt;
&lt;!DOCTYPE extended-properties SYSTEM "extended-properties.dtd" &gt;
&lt;extended-properties&gt;
&lt;/extended-properties&gt;
</pre>

	<p>
	The <code>name</code> attribute of the <code>&lt;class&gt;</code> tag is the
	name of the data class that the extra property should be added to. That
	class must of course implement this interface. The <code>&lt;class&gt;</code>
	tag may contain one or more <code>&lt;property&gt;</code> tags. Here is a
	short description of the attributes for the <code>&lt;property&gt;</code> tag.

	<table border=1>
	<tr>
		<th>Attribute</th>
		<th>Description</th>
	</tr>
	<tr valign="top">
		<td>name (required)</td>
		<td>
			The property name of the extended property.
			See {@link org.proteios.core.ExtendedProperty#getName()}.
		</td>
	</tr>
	<tr valign="top">
		<td>title</td>
		<td>
			The display title for the extended property.
			See {@link org.proteios.core.ExtendedProperty#getTitle()}.
		</td>
	</tr>
	<tr valign="top">
		<td>description</td>
		<td>
			A short description of the extended property.
			See {@link org.proteios.core.ExtendedProperty#getDescription()}.
		</td>
	</tr>
	<tr valign="top">
		<td>column (required)</td>
		<td>
			The database column name of the extended property. This value
			must of course be unique for each class.
			See {@link org.proteios.core.ExtendedProperty#getColumn()}.
		</td>
	</tr>
	<tr valign="top">
		<td>type (required)</td>
		<td>
			The type of the column. Allowed values are:
			<ul>
				<li>int
				<li>long
				<li>float
				<li>double
				<li>boolean
				<li>string
				<li>text
				<li>date
			</ul>
			See {@link org.proteios.core.ExtendedProperty#getType()}.
		</td>
	</tr>
	<tr valign="top">
		<td>length</td>
		<td>
			The maximum allowed length of the value if it is a string property.
			See {@link org.proteios.core.ExtendedProperty#getLength()}.
			The default value is 255.
		</td>
	</tr>
	<tr valign="top">
		<td>null</td>
		<td>
			If the column supports null values or not. Allowed
			values are:
			<ul>
			<li>true (default)
			<li>false
			</ul>
			See {@link org.proteios.core.ExtendedProperty#isNullable()}.
		</td>
	</tr>
	<tr valign="top">
		<td>insert</td>
		<td>
			If the value for this property should be inserted into the database
			or not.
			<ul>
			<li>true (default)
			<li>false
			</ul>
			See {@link org.proteios.core.ExtendedProperty#isInsertable()}.
		</td>
	</tr>
	<tr valign="top">
		<td>update</td>
		<td>
			If the value for this property should be updated in the database
			or not.
			<ul>
			<li>true (default)
			<li>false
			</ul>
			See {@link org.proteios.core.ExtendedProperty#isUpdateable()}.
		</td>
	</tr>
	</table>
	<p>
	
	<b>Reference implementation</b><br>
	<pre class="code">
private Map&lt;String, Object&gt; extendedProperties;
public Object getExtended(String name)
{
   return extendedProperties == null ? null : extendedProperties.get(name);
}
public void setExtended(String name, Object value)
{
   if (extendedProperties == null) extendedProperties = new HashMap&lt;String, Object&gt;();
   extendedProperties.put(name, value);
}
</pre>

	@see org.proteios.core.ExtendedProperties
	@see org.proteios.core.ExtendedProperty
	@author Nicklas, Samuel
	@version 2.0
*/
public interface ExtendableData
	extends IdentifiableData
{
	/**
		Get the value of an extended property.
		@param name The name of the property
		@return The value of the property or null if not found
	*/
	public Object getExtended(String name);
	
	/**
		Set the value of an extended property.
		@param name The name of the property
		@param value The new value for the property
	*/
	public void setExtended(String name, Object value);

}
