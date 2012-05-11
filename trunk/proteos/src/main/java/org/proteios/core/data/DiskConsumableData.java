/*
  $Id: DiskConsumableData.java 3207 2009-04-09 06:48:11Z gregory $

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
	A diskconsumable item is an item that occupies a lot of diskspace and 
	should be controlled by the quota system. The {@link DiskUsageData}
	object is ued to hold information about the size, location, owner, etc.
	of the item.
	<p>
	This interface defines Hibernate database mappings for the
	<code>diskUsage</code> property to the database column 
	<code>diskusage_id</code>. If a subclass wants
	to map the property to another column, it should override
	the {@link #getDiskUsage()} method and add a Hibernate tag in the comment.
	<p>
	Hibernate also requires a <code>setDiskUsage()</code> method, so this must
	also be implemented even though it is not required by this interface.
	<p>
	
	<b>Reference implementation</b><br>
	<pre class="code">
private DiskUsageData diskUsage;
public DiskUsageData getDiskUsage()
{
   if (diskUsage == null) diskUsage = new DiskUsageData();
   return diskUsage;
}
void setDiskUsage(DiskUsageData diskUsage)
{
   this.diskUsage = diskUsage;
}
</pre>
	Since there is a one-to-one relation between the item and the <code>DiskUsageData</code>
	object which should not be changed once it is created the recommended implementation
	has a package private set method (used only by Hibernate).

	@author enell
	@version 2.0
	@see DiskUsageData
	@see org.proteios.core.DiskConsumable
	@see <a href="../../../../../../../development/overview/data/authentication.html">Authentication overview</a>
*/
public interface DiskConsumableData
	extends OwnableData
{
	/**
		Get the {@link DiskUsageData} that this item use.
		@hibernate.many-to-one column="`diskusage_id`" not-null="true" outer-join="false" unique="true" cascade="all"
	*/
	public DiskUsageData getDiskUsage();
	
}
