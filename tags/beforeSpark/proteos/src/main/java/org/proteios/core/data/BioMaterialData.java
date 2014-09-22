/*
	$Id: BioMaterialData.java 3207 2009-04-09 06:48:11Z gregory $

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

import java.util.HashSet;
import java.util.Set;

/**
	This class is the root class for biomaterials.

	@author Nicklas
	@version 2.0
	@see org.proteios.core.BioMaterial
	@see <a href="../../../../../../../development/overview/data/biomaterial.html">Biomaterials overview</a>
	@base.modified $Date: 2009-04-08 23:48:11 -0700 (Wed, 08 Apr 2009) $
	@hibernate.class table="`BioMaterials`" lazy="false" discriminator-value="-1"
	@hibernate.discriminator column="`discriminator`" type="int"
*/
public abstract class BioMaterialData
	extends AnnotatedData
{

	public BioMaterialData()
	{}
	
	/**
		The maximum length of the external id that can be stored in the database.
		@see #setExternalId(String)
	*/
	public static final int MAX_EXTERNAL_ID_LENGTH = 255;
	private String externalId;
	/**
		Get the external id for the biomaterial
		@hibernate.property column="`external_id`" type="string" length="255" not-null="false"
	*/
	public String getExternalId()
	{
		return externalId;
	}
	public void setExternalId(String externalId)
	{
		this.externalId = externalId;
	}

	private Set<FileData> files;

	/**
	 * @hibernate.set table="`BioMaterialFiles`" lazy="true" inverse="true"
	 * @hibernate.collection-key column="`biomaterial_id`"
	 * @hibernate.collection-many-to-many class="org.proteios.core.data.FileData"
	 *                                    column="`file_id`" 
	 * @return Returns the files. Empty Set if no files
	 */
	public Set<FileData> getFiles()
	{
		if (files==null)
		{
			files=new HashSet<FileData>();
		}
		return files;
	}
	
	public void setFiles(Set<FileData> files)
	{
		this.files=files;
	}

	
	/**
	 *	The maximum length of the storage location info that can be
	 *	stored in the database.
	 *	@see #setStorageLocation(String)
	 */
	public static final int MAX_STORAGE_LOCATION_LENGTH = 255;
	private String storageLocation;
	/**
	 *	Get the storage location for the biomaterial
	 *	@hibernate.property column="`storage_location`" type="string" length="255" not-null="false"
	 */
	public String getStorageLocation()
	{
		return storageLocation;
	}
	public void setStorageLocation(String storageLocation)
	{
		this.storageLocation = storageLocation;
	}
}