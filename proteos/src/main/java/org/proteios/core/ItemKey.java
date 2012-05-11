/*
	$Id: ItemKey.java 3207 2009-04-09 06:48:11Z gregory $

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

import org.proteios.core.data.ItemKeyData;

import java.util.Set;


/**
	This class is used to get information about which users and groups
	a {@link Shareable} item has been shared to.
	<p>
	An item key is immutable and the user/group/permission 
	combinations cannot be changed once the key has been created.
	This allows the core to resuse item keys for all items with the same
	combination of user/group/permission values without fear that someone
	else may change the permissions.
	<p>
	To create a new item key use the {@link UserPermissions} and/or
	{@link GroupPermissions} objects and pass the information to
	the {@link #getNewOrExisting(DbControl, UserPermissions, GroupPermissions)}
	method.
	<p>
	Another option is to use the {@link MultiPermissions} object which allows
	you to modifiy the sharing information of a whole bunch of shareable items
	in one go. The <code>MultiPermissions</code> class takes care of all
	the cumbersome work of creating new keys and assigning those to the items
	for you.
	
	@author Nicklas
	@version 2.0
	@see Shareable
*/
public class ItemKey
	extends Key<ItemKeyData>
{
	/**
		The type of item represented by this class.
		@see Item#ITEMKEY
		@see #getType()
	*/
	public static final Item TYPE = Item.ITEMKEY;
	
	/**
		Create a new or load an existing <code>ItemKey</code> when you
		have a combination of user/group/permission values. If the database
		contains an item key with the same combination that key is
		loaded and returned, otherwise a new item key is created.
		If no existing key is found, the new key will immediately be saved
		and committed to the database using a different transaction. This ensures
		that no two keys contains identical permissions.

		@param dc The <code>DbControl</code> which will be used for
			permission checking and database access
		@param userPermissions A <code>UserPermissions</code> object
			holding the permissions for users
		@param groupPermissions A <code>GroupPermissions</code> object
			holding the permissions for groups
		@return The <code>ItemKey</code> item
		@throws PermissionDeniedException If the logged in user tried to share to the
			{@link Group#EVERYONE} group without having {@link Permission#SHARE_TO_EVERYONE}
			permission
		@throws InvalidDataException If not sharing to any user or group
		@throws BaseException If there is an error
	*/
//	public static ItemKey getNewOrExisting(DbControl dc, UserPermissions userPermissions, GroupPermissions groupPermissions)
//		throws PermissionDeniedException, InvalidDataException, BaseException
//	{
//		if ((userPermissions == null || userPermissions.size() == 0) && (groupPermissions == null || groupPermissions.size() == 0))
//		{
//			throw new InvalidDataException("Both userPermissions and groupPermissions are null or empty");
//		}
//		if (groupPermissions != null && !dc.getSessionControl().hasSystemPermission(Permission.SHARE_TO_EVERYONE))
//		{
//			GroupData everyone = HibernateUtil.loadData(dc.getHibernateSession(), GroupData.class, SystemItems.getId(Group.EVERYONE));
//			assert everyone != null : "everyone == null";
//			if (groupPermissions.getGroupPermissions().containsKey(everyone))
//			{
//				throw new PermissionDeniedException("Not allowed to share to the EVERYONE group.");
//			}
//		}
//		int id = getNewOrExistingKey(
//			null,
//			userPermissions == null ? null : userPermissions.getUserPermissions(),
//			groupPermissions == null ? null : groupPermissions.getGroupPermissions()
//		);
//		return getById(dc, id);
//	}

	/**
		Get an <code>ItemKey</code> item when you know the ID.

		@param dc The <code>DbControl</code> which will be used for
			permission checking and database access.
		@param id The ID of the item to load
		@return The <code>ItemKey</code> item
		@throws ItemNotFoundException If an item with the specified 
			ID is not found
		@throws PermissionDeniedException If the logged in user doesn't 
			have {@link Permission#READ} permission to the item
		@throws BaseException If there is another error
	*/
//	public static ItemKey getById(DbControl dc, int id)
//		throws ItemNotFoundException, PermissionDeniedException, BaseException
//	{
//		ItemKey ik = dc.loadItem(ItemKey.class, id);
//		if (ik == null) throw new ItemNotFoundException("ItemKey[id="+id+"]");
//		return ik;
//	}

	/**
		Delete all keys that are currently not used by any item. This method 
		is intended to be executed at regular intervals by a cleanup application.
	*/
//	public static synchronized void deleteUnusedKeys()
//		throws BaseException
//	{
//		org.hibernate.Session session = null;
//		org.hibernate.Transaction tx = null;
//		try
//		{
//			session = HibernateUtil.newSession();
//			tx = HibernateUtil.newTransaction(session);
//			org.hibernate.Query query = HibernateUtil.getPredefinedQuery(session, "FIND_USED_ITEMKEY_IDS");
//			/*
//				SELECT DISTINCT sd.itemKey.id
//				FROM org.proteios.core.data.ShareableData sd
//				WHERE NOT sd.itemKey IS NULL
//			*/
//			Set<Integer> used = new HashSet<Integer>(HibernateUtil.loadList(Integer.class, query));
//			if (used.size() == 0) used.add(0);
//			query = HibernateUtil.getPredefinedQuery(session, "SELECT_UNUSED_ITEMKEYS");
//			/*
//				SELECT ik FROM ItemKeyData ik
//				WHERE ik.id NOT IN (:used)
//			*/
//			query.setParameterList("used", used, org.hibernate.Hibernate.INTEGER);
//			List<ItemKeyData> unused = HibernateUtil.loadList(ItemKeyData.class, query);
//			for (ItemKeyData ik : unused)
//			{
//				HibernateUtil.deleteData(session, ik);
//			}
//			HibernateUtil.commit(tx);
//		}
//		catch (BaseException ex)
//		{
//			if (tx != null) HibernateUtil.rollback(tx);
//			throw ex;
//		}
//		finally
//		{
//			if (session != null) HibernateUtil.close(session);
//		}
//	}
	
	ItemKey(ItemKeyData itemKeyData)
	{
		super(itemKeyData);
	}

	/*
		From the Identifiable interface
		-------------------------------------------
	*/
	public Item getType()
	{
		return TYPE;
	}
	// -------------------------------------------
	/*
		From the BasicItem class
		-------------------------------------------
	*/
	/**
		Checks if:
		<ul>
		<li>A {@link Shareable} item is using this key.
		</ul>
	*/
	@Override
	public boolean isUsed()
		throws BaseException
	{
        throw new UnsupportedOperationException("Fix This"); // ToDo
//		org.hibernate.Session session = getDbControl().getHibernateSession();
//		org.hibernate.Query query = HibernateUtil.getPredefinedQuery(session, "GET_SHAREABLE_ITEMS_FOR_ITEMKEY");
//		/*
//			SELECT s
//			FROM org.proteios.core.data.ShareableData s
//			WHERE s.itemKey = :itemKey
//		*/
//		query.setEntity("itemKey", this.getData());
//		return HibernateUtil.loadData(ShareableData.class, query) != null;
	}
	/**
		WRITE permission is always denied. USE and CREATE
		permission is always granted.
	*/
	@Override
	void initPermissions(int granted, int denied)
		throws BaseException
	{
		denied |= Permission.deny(Permission.WRITE);
		granted |= Permission.grant(Permission.USE, Permission.CREATE);
		super.initPermissions(granted, denied);
	}
	// -------------------------------------------

	/**
		Get the permissions for a user.
		@param user The <code>User</code> for which we want to get the permission
		@return A <code>Set</code> containing the granted permissions, or an
			empty set if no permissions have been granted
		@throws InvalidDataException If the user is null
		@see Permission
	*/
	public Set<Permission> getPermissions(User user)
		throws InvalidDataException
	{
		if (user == null) throw new InvalidUseOfNullException("user");
		return Permission.fromInt(getData().getUsers().get(user.getData()));
	}

	/**
		Get the permissions for a group.
		@param group The <code>Group</code> for which we want to get the permission
		@return A <code>Set</code> containing the granted permissions, or an
			empty set if no permissions have been granted
		@throws InvalidDataException If the user is null
		@see Permission
	*/
//	public Set<Permission> getPermissions(Group group)
//		throws InvalidDataException
//	{
//		if (group == null) throw new InvalidUseOfNullException("group");
//		return Permission.fromInt(getData().getGroups().get(group.getData()));
//	}

	
	/**
		Find the ID of an existing key with exactly the same
		user/group/permission combination. If no existing key is found a new one is created.

		@param session The Hibernate session to use, or null if a new session should be created
		@param userPermissions A <code>Map</code> holding the user/permission combinations
		@param groupPermissions A <code>Map</code> holding the group/permission combinations
		@return The ID of the new or existing key
	*/
//	static synchronized int getNewOrExistingKey(org.hibernate.Session session, Map<UserData,Integer> userPermissions, Map<GroupData,Integer> groupPermissions)
//		throws BaseException
//	{
//
//		org.hibernate.Transaction tx = null;
//		if (session == null)
//		{
//			session = HibernateUtil.newSession();
//			tx = HibernateUtil.newTransaction(session);
//		}
//		try
//		{
//			// 1. Find all keys with the same number of entries as we have
//			//    user/permissions combinations
//			int numPermissions = userPermissions == null ? 0 : userPermissions.size();
//			org.hibernate.Query query = HibernateUtil.getPredefinedQuery(session, "GET_ITEMKEY_IDS_FOR_USERCOUNT");
//			/*
//				SELECT ik.id FROM ItemKeyData ik
//				LEFT JOIN ik.users u
//				GROUP BY ik.id
//				HAVING COUNT(u.index) = :numPermissions
//			*/
//			query.setInteger("numPermissions", numPermissions);
//			List<Integer> candidates = HibernateUtil.loadList(Integer.class, query);
//			if (candidates.size() > 0 && numPermissions > 0 && userPermissions != null)
//			{
//				// 2. Check that the candidates have the same user/permission combinations
//				StringBuilder sb = new StringBuilder();
//				int i = 0;
//				for (Map.Entry<UserData,Integer> entry : userPermissions.entrySet())
//				{
//					if (i > 0) sb.append(" OR ");
//					i++;
//					sb.append("(uk.userId=").append(entry.getKey().getId()).
//						append(" AND uk.permission=").append(entry.getValue()).append(") ");
//				}
//
//				query = HibernateUtil.createQuery(session,
//					"SELECT uk.keyId FROM UserKeys uk "+
//					"WHERE uk.keyId IN (:candidates) AND ("+sb.toString()+") "+
//					"GROUP BY uk.keyId "+
//					"HAVING COUNT(uk.userId) = :numPermissions"
//				);
//				query.setParameterList("candidates", candidates, org.hibernate.Hibernate.INTEGER);
//				query.setInteger("numPermissions", numPermissions);
//				candidates = HibernateUtil.loadList(Integer.class, query);
//			}
//
//			if (candidates.size() > 0)
//			{
//				// 3. Find all keys with the same number of entries as we have
//				//    group/permissions combinations
//				numPermissions = groupPermissions == null ? 0 : groupPermissions.size();
//				query = HibernateUtil.getPredefinedQuery(session, "GET_ITEMKEY_IDS_FOR_GROUPCOUNT");
//				/*
//					SELECT ik.id FROM ItemKeyData ik
//					LEFT JOIN ik.groups g
//					WHERE ik.id IN (:candidates)
//					GROUP BY ik.id
//					HAVING COUNT(g.index) = :numPermissions
//				*/
//				query.setInteger("numPermissions", numPermissions);
//				query.setParameterList("candidates", candidates, org.hibernate.Hibernate.INTEGER);
//				candidates = HibernateUtil.loadList(Integer.class, query);
//			}
//
//			if (candidates.size() > 0 && numPermissions > 0 && groupPermissions != null)
//			{
//				// 4. Check that the candidates have the same group/permission combinations
//				StringBuilder sb = new StringBuilder();
//
//				int i = 0;
//				for (Map.Entry<GroupData,Integer> entry : groupPermissions.entrySet())
//				{
//					if (i > 0) sb.append(" OR ");
//					i++;
//					sb.append("(gk.groupId=").append(entry.getKey().getId()).
//						append(" AND gk.permission=").append(entry.getValue()).append(") ");
//				}
//
//				query = HibernateUtil.createQuery(session,
//					"SELECT gk.keyId FROM GroupKeys gk "+
//					"WHERE gk.keyId IN (:candidates) AND ("+sb.toString()+") "+
//					"GROUP BY gk.keyId "+
//					"HAVING COUNT(gk.groupId) = :numPermissions"
//				);
//				query.setParameterList("candidates", candidates, org.hibernate.Hibernate.INTEGER);
//				query.setInteger("numPermissions", numPermissions);
//				candidates = HibernateUtil.loadList(Integer.class, query);
//			}
//
//			if (candidates.size() == 0)
//			{
//				// No more candidates, create a new item key
//				ItemKeyData data = new ItemKeyData();
//				if (userPermissions != null) data.getUsers().putAll(userPermissions);
//				if (groupPermissions != null) data.getGroups().putAll(groupPermissions);
//				HibernateUtil.saveData(session, data);
//				if (tx != null) HibernateUtil.commit(tx);
//				return data.getId();
//			}
//			if (tx != null)
//				HibernateUtil.commit(tx);
//			return candidates.get(0);
//		}
//		catch (BaseException ex)
//		{
//			if (tx != null) HibernateUtil.rollback(tx);
//			throw ex;
//		}
//		finally
//		{
//			if (tx != null && session != null) HibernateUtil.close(session);
//		}
//	}
}
