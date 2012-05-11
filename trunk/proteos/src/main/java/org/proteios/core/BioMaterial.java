/*
	$Id: BioMaterial.java 3207 2009-04-09 06:48:11Z gregory $

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

import org.proteios.core.data.BioMaterialData;

/**
	This is the base class for for the four types of biomaterials:
	{@link BioSource}, {@link Sample}, {@link Extract}
	and {@link LabeledExtract}.

	@author Nicklas
	@version 2.0
	@base.modified $Date: 2009-04-08 23:48:11 -0700 (Wed, 08 Apr 2009) $
*/
public abstract class BioMaterial<D extends BioMaterialData>
	extends AnnotatedItem<D>
{

	BioMaterial(D bioMaterialData)
	{
		super(bioMaterialData);
	}

	/**
		The maximum length of the external id that can be stored in the database.
		@see #setExternalId(String)
	*/
	public static final int MAX_EXTERNAL_ID_LENGTH = BioMaterialData.MAX_EXTERNAL_ID_LENGTH;

	/**
		Get the external id of the biomaterial. This value can be used to link
		with information in external databases. It is not used by the Proteios core
		and it doesn't have to be unique.
		@return The external id for the biomaterial
	*/
	public String getExternalId()
	{
		return getData().getExternalId();
	}
	/**
		Set the external id of the biomaterial. The value
		may be null but must not be longer than the value specified by
		the {@link #MAX_EXTERNAL_ID_LENGTH} constant.
		@param externalId The new value for the external id
		@throws PermissionDeniedException If the logged in user doesn't have
			write permission
		@throws InvalidDataException If the string is too long
	*/
	public void setExternalId(String externalId)
		throws PermissionDeniedException, InvalidDataException
	{
		checkPermission(Permission.WRITE);
		getData().setExternalId(StringUtil.setNullableString(externalId, "externalId", MAX_EXTERNAL_ID_LENGTH));
	}

	/**
	 * Add a File
	 * 
	 * @param file The File to add to the Set
	 */
	@SuppressWarnings("unchecked")
//	public void addFile(File file)
//	{
//		(file.getData()).getBioMaterials().add(this.getData());
//		getData().getFiles().add(file.getData());
//	}
//
//
//	/**
//	 * @return Returns the files.
//	 */
//	@SuppressWarnings("unchecked")
//	public Set<File> getFiles()
//			throws ItemNotFoundException, BaseException
//	{
//		Set<File> files = new HashSet<File>();
//		try
//		{
//			Iterator<FileData> it = getData().getFiles()
//				.iterator();
//			while (it.hasNext())
//			{
//				files.add(getDbControl().getItem(File.class,
//					it.next()));
//			}
//		}
//		catch (org.hibernate.ObjectNotFoundException e)
//		{
//			throw new ItemNotFoundException(HibernateUtil.getShortEntityName(e
//				.getEntityName()) + "[id=" + e.getIdentifier() + "]");
//		}
//		return files;
//	}
//
//
//	/**
//	 * Set the {@link File} files set.
//	 *
//	 * @param files The new <code>Set<File></code> item.
//	 */
//	@SuppressWarnings("unchecked")
//	public void setFiles(Set<File> files)
//	{
//		Set<FileData> fileDataSet = new HashSet<FileData>();
//		Iterator<File> it = files.iterator();
//		while (it.hasNext())
//		{
//			fileDataSet.add(it.next().getData());
//		}
//		getData().setFiles(fileDataSet);
//	}
//
	
	/**
	 *	The maximum length of the storage location info that can be
	 *	stored in the database.
	 *	@see #setStorageLocation(String)
	 */
	public static final int MAX_STORAGE_LOCATION_LENGTH = BioMaterialData.MAX_STORAGE_LOCATION_LENGTH;

	/**
	 *	Get the storage location of the biomaterial.
	 *	@return The storage location for the biomaterial
	 */
	public String getStorageLocation()
	{
		return getData().getStorageLocation();
	}
	/**
	 *	Set the storage location of the biomaterial. The value
	 *	may be null but must not be longer than the value specified by
	 *	the {@link #MAX_STORAGE_LOCATION_LENGTH} constant.
	 *	@param storageLocation The new value for the storage location.
	 *	@throws PermissionDeniedException If the logged in user doesn't have
	 *	write permission
	 *	@throws InvalidDataException If the string is too long
	 */
	public void setStorageLocation(String storageLocation)
		throws PermissionDeniedException, InvalidDataException
	{
		checkPermission(Permission.WRITE);
		getData().setStorageLocation(StringUtil.setNullableString(storageLocation, "storageLocation", MAX_STORAGE_LOCATION_LENGTH));
	}
}



