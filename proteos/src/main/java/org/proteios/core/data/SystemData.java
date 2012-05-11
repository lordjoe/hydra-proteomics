/*
	$Id: SystemData.java 3207 2009-04-09 06:48:11Z gregory $

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
	A system item is an item which has an
	additional id in the form of string. A system id is required
	when we need to make sure that we can get a specific item
	without knowing the numeric id. Example of such items are the
	root user and the everyone group. The system id:s are defined by
	the core layer, since the actual values are of no interest to
	the data layer. It is recommended that the id:s are constructed
	as: <code>org.proteios.core.User.ROOT</code>.
	<p>
	This interface defines Hibernate database mappings for the
	<code>systemId</code> property
	to the database column <code>system_id</code>. If a subclass wants
	to map the property to another column, it should override
	the {@link #getSystemId()} method and add a
	Hibernate tag in the comment.
	<p>
	Hibernate also requires a <code>setSystemId()</code> method, so this must
	also be implemented even though it is not required by this interface.
	<p>
	
	<b>Reference implementation</b><br>
	<pre class="code">
private String systemId;
public String getSystemId()
{
   return systemId;
}
public void setSystemId(String systemId)
{
   this.systemId = systemId;
}
</pre>

	@author Nicklas
	@version 2.0
	@see <a href="../../../../../../../development/overview/data/basic.html">Basic classes and interfaces</a>
*/
public interface SystemData
	extends IdentifiableData
{
	/**
		Get the system id for the item.
		@return The id of the item or null
		@hibernate.property column="`system_id`" type="string" length="255" not-null="false" update="false"
	*/
	public String getSystemId();


}
