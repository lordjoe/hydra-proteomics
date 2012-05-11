/*
	$Id: OwnedItem.java 3207 2009-04-09 06:48:11Z gregory $

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

import org.proteios.core.data.OwnedData;
 

/**
	This class inherits from the {@link BasicItem}
	class and implements the {@link Ownable} interface.
	An <code>Ownable</code> item is an item that has a
	{@link User} as its owner.
	<p>

	@author Nicklas
	@version 2.0
	@see Ownable
	@base.modified $Date: 2009-04-08 23:48:11 -0700 (Wed, 08 Apr 2009) $
*/
public abstract class OwnedItem<D extends OwnedData>
	extends BasicItem<D>
	implements Ownable
{
	OwnedItem(D ownedData)
	{
		super(ownedData);
	}

	/*
		From the Ownable interface
		-------------------------------------------
	*/
	public User getOwner()
		throws PermissionDeniedException, BaseException
	{
        throw new UnsupportedOperationException("Fix This"); // ToDo
//		return getDbControl().getItem(User.class, getData().getOwner());
	}
	public void setOwner(User owner)
		throws PermissionDeniedException, InvalidDataException
	{
		checkPermission(Permission.SET_OWNER);
		OwnableUtil.setOwner(getData(), owner);
	}
	// -------------------------------------------

	/*
		From the BasicItem class
		-------------------------------------------
	*/
	/**
		Overrides the {@link BasicItem#onBeforeCommit(Transactional.Action)}
		method. It sets the owner of the new item to the logged in user, unless
		it has already been specified.
		@throws NotLoggedInException If no user is logged in
		@throws BaseException If there is another error
	*/
//	@Override
//	void onBeforeCommit(Transactional.Action action)
//		throws NotLoggedInException, BaseException
//	{
//		super.onBeforeCommit(action);
//		if (action == Transactional.Action.CREATE && getData().getOwner() == null)
//		{
//			org.hibernate.Session session = getDbControl().getHibernateSession();
//			int loggedInuserId = getSessionControl().getLoggedInUserId();
//			UserData owner = HibernateUtil.loadData(session, UserData.class, loggedInuserId);
//			if (owner == null) throw new NotLoggedInException();
//			getData().setOwner(owner);
//		}
//	}
	/**
		If the logged in user is the owner of this item, DELETE,
		SET_OWNER and SET_PERMISSION permission is granted.
	*/
//	@Override
//	void initPermissions(int granted, int denied)
//		throws BaseException
//	{
//		UserData owner = getData().getOwner();
//		// owner may be null for new items
//		if (owner != null && owner.getId() == getSessionControl().getLoggedInUserId())
//		{
//			granted |= Permission.grant(Permission.DELETE, Permission.SET_OWNER, Permission.SET_PERMISSION);
//		}
//		super.initPermissions(granted, denied);
//	}
}
