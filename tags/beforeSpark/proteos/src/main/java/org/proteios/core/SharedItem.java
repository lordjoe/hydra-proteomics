/*
	$Id: SharedItem.java 3207 2009-04-09 06:48:11Z gregory $

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

import org.proteios.core.data.SharedData;

/**
	This class inherits from the {@link OwnedItem}
	class and implements the {@link Shareable} interface.
	An <code>Shareable</code> item is an item that can be
	shared to {@link User}:s, {@link Group}:s and {@link Project}:s

	@author Nicklas
	@version 2.0
	@see Shareable
	@base.modified $Date: 2009-04-08 23:48:11 -0700 (Wed, 08 Apr 2009) $
*/
public abstract class SharedItem<D extends SharedData>
	extends OwnedItem<D>
	implements Shareable
{
	
	private boolean projectKeySet = false;
	
	SharedItem(D sharedData)
	{
		super(sharedData);
	}

	/*
		From the Sharable interface
		-------------------------------------------
	*/
	public ItemKey getItemKey()
		throws PermissionDeniedException, BaseException
	{
        throw new UnsupportedOperationException("Fix This"); // ToDo
//		return getDbControl().getItem(ItemKey.class, getData().getItemKey());
	}
	public void setItemKey(ItemKey itemKey)
		throws PermissionDeniedException
	{
		checkPermission(Permission.SET_PERMISSION);
		ShareableUtil.setItemKey(getData(), itemKey);
	}
	public ProjectKey getProjectKey()
		throws PermissionDeniedException, BaseException
	{
        throw new UnsupportedOperationException("Fix This"); // ToDo
	//	return getDbControl().getItem(ProjectKey.class, getData().getProjectKey());
	}
	public void setProjectKey(ProjectKey projectKey)
		throws PermissionDeniedException
	{
		checkPermission(Permission.SET_PERMISSION);
		ShareableUtil.setProjectKey(getData(), projectKey);
		projectKeySet = true;
	}
	public boolean isShared()
	{
		return getData().getItemKey() != null || getData().getProjectKey() != null;
	}
	// -------------------------------------------

	/*
		From the BasicItem class
		-------------------------------------------
	*/
	/**
		If a project is active, automatically share the new item to that project
		unless a project key has been explicitely set (including null).
		@throws BaseException If there is another error
	*/
//	@Override
//	void onBeforeCommit(Transactional.Action action)
//		throws NotLoggedInException, BaseException
//	{
//		super.onBeforeCommit(action);
//		if (action == Transactional.Action.CREATE && !projectKeySet)
//		{
//			int activeProjectKeyId = getSessionControl().getProjectKeyId();
//			if (activeProjectKeyId != 0)
//			{
//				org.hibernate.Session session = getDbControl().getHibernateSession();
//				getData().setProjectKey(HibernateUtil.loadData(session, ProjectKeyData.class, activeProjectKeyId));
//			}
//		}
//	}

	/**
		Grant permissions according to the item and project keys.
	*/
	@Override
	void initPermissions(int granted, int denied)
		throws BaseException
	{
//		granted |= getSessionControl().getSharedPermissions(getData());
		super.initPermissions(granted, denied);
	}
}
