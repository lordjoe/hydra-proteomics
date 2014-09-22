/*
 $Id: FileData.java 3473 2009-11-06 11:30:35Z olle $

 Copyright (C) 2006, 2007 Gregory Vincic, Olle Mansson

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
import java.util.HashSet;
import java.util.Set;

/**
 * This class holds information about a file.
 * 
 * @author enell
 * @version 2.0
 * @see org.proteios.core.File
 * @base.modified $Date: 2009-11-06 03:30:35 -0800 (Fri, 06 Nov 2009) $
 * @hibernate.class table="`Files`" lazy="true"
 */
public class FileData
		extends AnnotatedData
		implements DiskConsumableData, CreationInterface
{
	private Date created;


	public Date getCreated()
	{
		return created;
	}


	public void setCreated(Date created)
	{
		this.created = created;
	}


	/*
	 * From the NameableData interface
	 * -------------------------------------------
	 */
	/**
	 * Get the name of the item.
	 * 
	 * @return A <code>String</code> with the name of the item
	 * @hibernate.property type="string"
	 * @hibernate.column name="`name`" length="255" not-null="true"
	 *                   index="name_idx" unique-key="uniquefile"
	 */
	@Override
	public String getName()
	{
		return super.getName();
	}

	// -------------------------------------------
	/*
	 * From the DiskConsumableData interface
	 * -------------------------------------------
	 */
	private DiskUsageData diskUsage;


	public DiskUsageData getDiskUsage()
	{
		if (diskUsage == null)
			diskUsage = new DiskUsageData();
		return diskUsage;
	}


	void setDiskUsage(DiskUsageData diskUsage)
	{
		this.diskUsage = diskUsage;
	}

	// -------------------------------------------
	private long size;


	/**
	 * Get the size in bytes for this <code>FileData</code> object.
	 * 
	 * @hibernate.property column="`size`" type="long" not-null="true"
	 */
	public long getSize()
	{
		return size;
	}


	public void setSize(long size)
	{
		this.size = size;
	}

	private String md5;


	/**
	 * Get the MD5 hash of the file contents. It is always returned as a string
	 * with 32 hexadecimal characters.
	 * 
	 * @hibernate.property column="`md5`" type="string" length="32"
	 *                     not-null="false"
	 */
	public String getMd5()
	{
		return md5;
	}


	public void setMd5(String md5)
	{
		this.md5 = md5;
	}

	private int action;


	/**
	 * Get the value of the action variable for this <code>FileData</code>
	 * object.
	 * 
	 * @hibernate.property column="`action`" type="int" not-null="true"
	 */
	public int getAction()
	{
		return action;
	}


	public void setAction(int action)
	{
		this.action = action;
	}

	private int location;


	/**
	 * Get the {@link org.proteios.core.Location} of this <code>FileData</code>
	 * 
	 * @hibernate.property column="`location`" type="int" not-null="true"
	 */
	public int getLocation()
	{
		return location;
	}


	public void setLocation(int location)
	{
		this.location = location;
	}

	private String mimeType;
	public static final int MAX_MIMETYPE_LENGTH = 255;


	/**
	 * Get the mime type of this <code>FileData</code> item.
	 * 
	 * @hibernate.property column="`mimetype`" type="string" length="255"
	 */
	public String getMimeType()
	{
		return mimeType;
	}


	public void setMimeType(String mimeType)
	{
		this.mimeType = mimeType;
	}

	private DirectoryData directory;


	/**
	 * Get the associated {@link DirectoryData} item.
	 * 
	 * @hibernate.many-to-one outer-join="false"
	 * @hibernate.column name="`directory_id`" not-null="true"
	 *                   unique-key="uniquefile"
	 */
	public DirectoryData getDirectory()
	{
		return directory;
	}


	public void setDirectory(DirectoryData directory)
	{
		this.directory = directory;
	}

	private FileTypeData fileType;


	/**
	 * Get the associated {@link FileTypeData} item.
	 * 
	 * @hibernate.many-to-one column="`filetype_id`" not-null="false"
	 */
	public FileTypeData getFileType()
	{
		return fileType;
	}


	public void setFileType(FileTypeData fileType)
	{
		this.fileType = fileType;
	}

	private boolean compressed;
	
	
	/**
	 * If the file is stored in a compressed format or not.
	 * 
	 * @return boolean Returns true if the file is stored in compressed format.
	 * @hibernate.property column="`compressed`" type="boolean" not-null="true"
	 */
	public boolean isCompressed()
	{
		return compressed;
	}


	/**
	 * Set the flag indicating that the file is stored in a compressed format.
	 * 
	 * @param compressed boolean The the file is stored in a compressed format.
	 */
	public void setCompressed(boolean compressed)
	{
		this.compressed = compressed;
	}

	private long compressedSize;


	/**
	 * Get the size in bytes that this <code>FileData</code> object 
 	 * uses after compression. If the file is not compressed, this value 
 	 * is the same as {@link #getSize()}
 	 * 
 	 * @return long The size in bytes of the compressed file.
 	 * @hibernate.property column="`compressed_size`" type="long" not-null="true"
	 */
	public long getCompressedSize()
	{
		return compressedSize;
	}


	/**
	 * Set the size in bytes that the file object uses after compression.
	 * 
	 * @param compressedSize long The size in bytes that the file object uses after compression.
	 */
	public void setCompressedSize(long compressedSize)
	{
		this.compressedSize = compressedSize;
	}

	private String internalName;


	/**
	 * Get the internal name associated with this file.
	 * 
	 * @hibernate.property column="`internalname`" type="string" length="255"
	 *                     not-null="false"
	 */
	public String getInternalName()
	{
		return internalName;
	}


	public void setInternalName(String internalName)
	{
		this.internalName = internalName;
	}

	private String uniformResourceIdentifier;
	public static final int MAX_URI_LENGTH = 255;


	/**
	 * Get the URI of this file. This is only set in the database if the file is
	 * not found in the Proteios primary storage. That is internalName = null.
	 * 
	 * @hibernate.property column="`uniformResourceIdentifier`" type="string"
	 *                     length="255" not-null="false"
	 */
	public String getUniformResourceIdentifier()
	{
		return uniformResourceIdentifier;
	}


	/**
	 * Set the URI of this file. This must only be set in the database if the
	 * file is not found in the Proteios primary storage. That is internalName =
	 * null.
	 */
	public void setUniformResourceIdentifier(String uniformResourceIdentifier)
	{
		this.uniformResourceIdentifier = uniformResourceIdentifier;
	}

	private Set<BioMaterialData> bioMaterials;


	/**
	 * @hibernate.set table="`BioMaterialFiles`" lazy="true"
	 * @hibernate.collection-key column="`file_id`"
	 * @hibernate.collection-many-to-many class="org.proteios.core.data.BioMaterialData"
	 *                                    column="`biomaterial_id`"
	 * @return Returns the bioMaterials. Empty Set if no bioMaterials
	 */
	public Set<BioMaterialData> getBioMaterials()
	{
		if (bioMaterials == null)
		{
			bioMaterials = new HashSet<BioMaterialData>();
		}
		return bioMaterials;
	}


	public void setBioMaterials(Set<BioMaterialData> bioMaterials)
	{
		this.bioMaterials = bioMaterials;
	}
}
