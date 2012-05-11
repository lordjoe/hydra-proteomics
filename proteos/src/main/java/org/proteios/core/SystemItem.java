/*
	$Id: SystemItem.java 3207 2009-04-09 06:48:11Z gregory $

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
package org.proteios.core;

/**
	A <code>SystemItem</code> is an item which has an
	additional systemwide unique id in the form of string. 
	A system id is required when we need to make sure that we can 
	get a specific item without knowing the numeric id. Example of 
	such items are the root user and the everyone group.
	<p>
	Items with a system id can only be created during installation
	and cannot be removed.
	<p>
	
	<b>Reference implementation</b><br>
	<pre class="code">
public String getSystemId()
{
   return getData().getSystemId();
}
public boolean isSystemItem()
{
   return getSystemId() != null;
}
</pre>

	@author Nicklas
	@version 2.0
	@base.modified $Date: 2009-04-08 23:48:11 -0700 (Wed, 08 Apr 2009) $
*/
public interface SystemItem
	extends Identifiable
{
	/**
		Get the system id for the item.
		@return The id of the item or null if it is not a system item
	*/
	public String getSystemId();
	
	/**
		Check if the item is a system item or not. A system item have a non-null
		value for the system id.
		@return TRUE if this item is a system item, FALSE otherwise
	*/
	public boolean isSystemItem();

}
