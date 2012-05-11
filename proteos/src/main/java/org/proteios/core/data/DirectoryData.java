/*
  $Id: DirectoryData.java 3207 2009-04-09 06:48:11Z gregory $

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

import java.util.Date;
import java.util.Set;


/**
	This class holds information about directories.
	
	@author enell
	@version 2.0
	@see org.proteios.core.Directory
	@see <a href="../../../../../../../development/overview/data/file.html">File and directory overview</a>
	@base.modified $Date: 2009-04-08 23:48:11 -0700 (Wed, 08 Apr 2009) $
	@hibernate.class table="`Directories`" lazy="false"
 */
public class DirectoryData
	extends CommonData
	implements SystemData, CreationInterface
{

	private Date created;
	/* (non-Javadoc)
	   @see org.proteios.core.data.CreationInterface#getCreated()
	 */
	public Date getCreated()
	{
		return created;
	}
	/* (non-Javadoc)
	   @see org.proteios.core.data.CreationInterface#setCreated(java.util.Date)
	 */
	public void setCreated(Date created)
	{
		this.created = created;
	}
	/*
		From the NameableData interface
		-------------------------------------------
	*/
	/**
		Get the name of the item.
		@return A <code>String</code> with the name of the item
		@hibernate.property type="string"
		@hibernate.column name="`name`" length="255" not-null="true" index="name_idx" unique-key="uniquedirectory"
	*/
	@Override
	public String getName()
	{
		return super.getName();
	}
	// -------------------------------------------
	/*
		From the SystemData interface
		-------------------------------------------
	*/
	private String systemId;
	public String getSystemId()
	{
		return systemId;
	}
	public void setSystemId(String systemId)
	{
		this.systemId = systemId;
	}
	// -------------------------------------------

	private DirectoryData parent;
	/**
		Get the parent directory of this directory.
		@hibernate.many-to-one outer-join="false"
		@hibernate.column name="`parent_id`" unique-key="uniquedirectory" not-null="false"
	*/
	public DirectoryData getParent()
	{
		return parent;
	}
	public void setParent(DirectoryData parent)
	{
		this.parent = parent;
	}
	
	private Set<FileData> files;
	/**
		This is the inverse end.
		@see FileData#getDirectory()
		@hibernate.set lazy="true" inverse="true" 
		@hibernate.collection-key column="`directory_id`"
		@hibernate.collection-one-to-many class="org.proteios.core.data.FileData"
	*/
	Set<FileData> getFiles()
	{
		return files;
	}
	void setFiles(Set<FileData> files)
	{
		this.files = files;
	}

	private Set<DirectoryData> subdirectories;
	/**
		This is the inverse end.
		@see DirectoryData#getParent()
		@hibernate.set lazy="true" inverse="true" 
		@hibernate.collection-key column="`parent_id`"
		@hibernate.collection-one-to-many class="org.proteios.core.data.DirectoryData"
	*/
	Set<DirectoryData> getSubdirectories()
	{
		return subdirectories;
	}
	void setSubdirectories(Set<DirectoryData> subdirectories)
	{
		this.subdirectories = subdirectories;
	}

}