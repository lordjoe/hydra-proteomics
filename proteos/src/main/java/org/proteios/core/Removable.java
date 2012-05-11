/*
	$Id: Removable.java 3207 2009-04-09 06:48:11Z gregory $

	Copyright (C) 2006 Gregory Vincic, Jari Hakkinen, Olle Mansson
	Copyright (C) 2007 Gregory Vincic

	This file is part of Proteios.
	Available at http://www.proteios.org/

	Proteios is free software; you can redistribute it and/or modify it
	under the terms of the GNU General Public License as published by
	the Free Software Foundation;	BioArray Software Environment (Proteios) - http://base.thep.lu.se/
	Copyright (C) 2002-2004 Lao Saal, Carl Troein,
	Johan Vallon-Christersson, Jari H�kkinen, Nicklas Nordborg

	This file is part of Proteios.

	Proteios is free software; you can redistribute it and/or
	modify it under the terms of the GNU General Public License
	as published by the Free Software Foundation; either version 2
	of the License, or (at your option) any later version.

	Proteios is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.

	You should have received a copy of the GNU General Public License
	along with this program; if not, write to the Free Software
	Foundation, Inc., 59 Temple Place - Suite 330,
	Boston, MA  02111-1307, USA.
 either version 2 of the License, or
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
	A <code>Removable</code> item is an item that can be flagged
	as removed. This doesn't generally remove the information about the
	item from the database, but can be used by client applications
	to hide items that the user is not interested in.
	<p>
	For example, all methods generating lists of items, will by default
	not include those that are flagged as removed.
	<p>
	At regular intervals, a cleanup application is checking the database for 
	items that are flagged to be removed and deletes them permanently.
	<p>
	
	<b>Reference implementation</b><br>
	<pre class="code">
public boolean isRemoved()
{
   return getData().isRemoved();
}
public void setRemoved(boolean removed)
   throws PermissionDeniedException
{
   checkPermission(removed ? Permission.DELETE : Permission.WRITE);
   getData().setRemoved(removed);
}
</pre>
	@author Nicklas
	@version 2.0
	@base.modified $Date: 2009-04-08 23:48:11 -0700 (Wed, 08 Apr 2009) $
*/
public interface Removable
	extends Identifiable
{

	/**
		Check if the removed flag is set for this item.
		@return TRUE if the item is flagged as removed, FALSE otherwise
	*/
	public boolean isRemoved();

	/**
		Set the removed flag for this item.
		@param removed TRUE if the item should be flagged as removed,
			FALSE otherwise
		@throws PermissionDeniedException If the logged in user doesn't 
			have {@link Permission#DELETE} permission for setting the flag 
			to TRUE or {@link Permission#WRITE} permission for setting the 
			flag to FALSE
	*/
	public void setRemoved(boolean removed)
		throws PermissionDeniedException;

}
