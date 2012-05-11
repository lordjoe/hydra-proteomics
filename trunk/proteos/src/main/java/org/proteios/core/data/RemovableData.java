/*
	$Id: RemovableData.java 3207 2009-04-09 06:48:11Z gregory $

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
	A removable item is an item that can be flagged
	as removed. This doesn't remove the information about the
	item from the database, but can be used by client applications
	to hide items that the user is not interested in.
	<p>
	For example, all methods generating lists of items, will by default
	not include items that are flagged as removed.
	<p>
	A cleaning application is usually run at regular intervals
	to remove items that have been flagged as removed.
	
	<p>
	This interface defines Hibernate database mapping for the
	<code>removed</code> property to a database column with the same 
	name. If a subclass wants to map these properties to other columns, 
	it should override the {@link #isRemoved()} method and add a 
	Hibernate tag in the comment.
	<p>
	
	<b>Reference implementation</b><br>
	<pre class="code">
private boolean removed;
public boolean isRemoved()
{
   return removed;
}
public void setRemoved(boolean removed)
{
   this.removed = removed;
}
</pre>

	@author Nicklas
	@version 2.0
	@see <a href="../../../../../../../development/overview/data/basic.html">Basic classes and interfaces</a>
*/
public interface RemovableData
	extends IdentifiableData
{

	/**
		Check if the removed flag is set for this item.
		@return TRUE if the item is flagged as removed, FALSE otherwise
		@hibernate.property column="`removed`" type="boolean" not-null="true"
	*/
	public boolean isRemoved();

	/**
		Set the removed flag for this item.
		@param removed TRUE if the item should be flagged as removed,
			FALSE otherwise
	*/
	public void setRemoved(boolean removed);

}
