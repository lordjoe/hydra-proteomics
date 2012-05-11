/*
	$Id: ShareableData.java 3207 2009-04-09 06:48:11Z gregory $

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
	A shareable item is an item which can be shared to other users, 
	groups or projects. To be able to share an item, it must have an
	owner, thus this interface extends the {@link OwnableData}
	interface. 
	<p>
	Access permissions are held in a {@link ItemKeyData} object
	for users and groups and a {@link ProjectKeyData} object for
	projects.
	<p>
	The {@link SharedData} class provides an implementation
	for this interface and it is recommended that shareable classes inherit
	from that class.
	<p>
	This interface defines Hibernate database mappings for the
	<code>itemKey</code> and <code>projectKey</code> properties to default 
	database columns. If a subclass wants to map these properties to other columns,
	it should override the {@link #getItemKey()} and {@link #getProjectKey()} methods
	and add a Hibernate tag in the comment.
	<p>
	
	<b>Reference implementation</b><br>
	<pre class="code">
private ItemKeyData itemKey;
public ItemKeyData getItemKey()
{
   return itemKey;
}
public void setItemKey(ItemKeyData itemKey)
{
   this.itemKey = itemKey;
}
private ProjectKeyData projectKey;
public ProjectKeyData getProjectKey()
{
   return projectKey;
}
public void setProjectKey(ProjectKeyData projectKey)
{
   this.projectKey = projectKey;
}
</pre>

	@author Nicklas
	@version 2.0
	@see SharedData
	@see org.proteios.core.Shareable
	@see <a href="../../../../../../../development/overview/data/authentication.html">Authentication overview</a>
	@see <a href="../../../../../../../development/overview/data/basic.html">Basic classes and interfaces</a>
*/
public interface ShareableData
	extends OwnableData
{

	/**
		Get the {@link ItemKeyData} for the item. An item key is used
		to share an item to individual users and/or groups.
		@hibernate.many-to-one column="`itemkey_id`" not-null="false" outer-join="false"
	*/
	public ItemKeyData getItemKey();

	/**
		Set the {@link ItemKeyData} for the item. An item key is used
		to share an item to individual users and or groups. Use null to
		disable sharing.
	*/
	public void setItemKey(ItemKeyData key);


	/**
		Get the {@link ProjectKeyData} for the item. A project key is used
		to share an item to projects.
		@hibernate.many-to-one column="`projectkey_id`" not-null="false" outer-join="false"
	*/
	public ProjectKeyData getProjectKey();

	/**
		Set the {@link ProjectKeyData} for the item. A project key is used
		to share an item to projects. Use null to disable sharing.
	*/
	public void setProjectKey(ProjectKeyData key);

}
