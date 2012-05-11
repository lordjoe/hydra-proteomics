/*
	$Id: ProjectKey.java 3207 2009-04-09 06:48:11Z gregory $

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

import org.proteios.core.data.ProjectKeyData;

/**
	This class is used to get information about which projects
	a {@link Shareable} item has been shared to.
	<p>
	A project key is immutable and the project/permission combinations 
	cannot be changed once the key has been created.
	This allows the core to resuse project keys for all items with the same
	combination of project/permission values without fear that someone
	else may change the permissions.
	<p>
	Project keys can be used with {@link Shareable} items
	to share them with projects. If the logged in user has
	set an active project (see {@link SessionControl#setActiveProject(Project)})
	new items will automatically be shared to that project.
	<p>
	To create a new project key use the {@link ProjectPermissions} object and pass 
	the information to  the {@link #getNewOrExisting(DbControl, ProjectPermissions)}
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
	@see SessionControl#setActiveProject(Project)
	@base.modified $Date: 2009-04-08 23:48:11 -0700 (Wed, 08 Apr 2009) $
*/
public class ProjectKey
	extends Key<ProjectKeyData>
{
	/**
		The type of item represented by this class.
		@see Item#PROJECTKEY
		@see #getType()
	*/
	public static final Item TYPE = Item.PROJECTKEY;
	
	/**
		Create a new or load an existing <code>ProjectKey</code> when you
		have a combination of project/permission values. If the database
		contains a project key with the same combination that project key is
		loaded and returned, otherwise a new project key is created.
		If no existing key is found, the new key will immediately be saved
		and committed to the database using another transaction. This ensures
		that no two keys contains identical permissions. 

		@param dc The <code>DbControl</code> which will be used for
			permission checking and database access
		@param projectPermissions A <code>ProjectPermissions</code> object
			holding the permissions
		@return The <code>ProjectKey</code> item
		@throws BaseException If there is an error
	*/
//	public static ProjectKey getNewOrExisting(DbControl dc, ProjectPermissions projectPermissions)
//		throws BaseException
//	{
//		if (projectPermissions == null || projectPermissions.size() == 0)
//		{
//			throw new InvalidDataException("projectPermissions is null or empty");
//		}
//		int id = getNewOrExistingId(null, projectPermissions.getProjectPermissions());
//		return getById(dc, id);
//	}
//
	/**
		Get a <code>ProjectKey</code> item when you know the ID.

		@param dc The <code>DbControl</code> which will be used for
			permission checking and database access.
		@param id The ID of the item to load
		@return The <code>ProjectKey</code> item
		@throws ItemNotFoundException If an item with the specified 
			ID is not found
		@throws PermissionDeniedException If the logged in user doesn't 
			have {@link Permission#READ} permission to the item
		@throws BaseException If there is another error
	*/
//	public static ProjectKey getById(DbControl dc, int id)
//		throws ItemNotFoundException, PermissionDeniedException, BaseException
//	{
//		ProjectKey pk = dc.loadItem(ProjectKey.class, id);
//		if (pk == null) throw new ItemNotFoundException("ProjectKey[id="+id+"]");
//		return pk;
//	}
//
	/**
		Delete all keys that are currently not used by any item. This method 
		is intended to be executed at regular intervals by a cleanup application.
	*/
	public static synchronized void deleteUnusedKeys()
		throws BaseException
	{
//		org.hibernate.Session session = null;
//		org.hibernate.Transaction tx = null;
//		try
//		{
//			session = HibernateUtil.newSession();
//			tx = HibernateUtil.newTransaction(session);
//			org.hibernate.Query query = HibernateUtil.getPredefinedQuery(session, "FIND_USED_PROJECTKEY_IDS");
//			/*
//				SELECT DISTINCT sd.projectKey.id
//				FROM org.proteios.core.data.ShareableData sd
//				WHERE NOT sd.projectKey IS NULL
//			*/
//			Set<Integer> used = new HashSet<Integer>(HibernateUtil.loadList(Integer.class, query));
//			if (used.size() == 0) used.add(0);
//			query = HibernateUtil.getPredefinedQuery(session, "SELECT_UNUSED_PROJECTKEYS");
//			/*
//				SELECT pk FROM ProjectKeyData pk
//				WHERE pk.id NOT IN (:used)
//			*/
//			query.setParameterList("used", used, org.hibernate.Hibernate.INTEGER);
//			List<ProjectKeyData> unused = HibernateUtil.loadList(ProjectKeyData.class, query);
//			for (ProjectKeyData pk : unused)
//			{
//				HibernateUtil.deleteData(session, pk);
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
	}
	
	ProjectKey(ProjectKeyData projectKeyData)
	{
		super(projectKeyData);
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
//		org.hibernate.Query query = HibernateUtil.getPredefinedQuery(session, "GET_SHAREABLE_ITEMS_FOR_PROJECTKEY");
//		/*
//			SELECT s
//			FROM org.proteios.core.data.ShareableData s
//			WHERE s.projectKey = :projectKey
//		*/
//		query.setEntity("projectKey", this.getData());
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
		Get the permissions for a project.
		@param project The <code>Project</code> for which we want to get the permission
		@return A <code>Set</code> containing the granted permissions, or an
			empty set if no permissions have been granted
		@throws InvalidDataException If the project is null
		@see Permission
	*/
//	public Set<Permission> getPermissions(Project project)
//		throws InvalidDataException
//	{
//		if (project == null) throw new InvalidUseOfNullException("project");
//		return Permission.fromInt(getData().getProjects().get(project.getData()));
//	}
	
	/**
		Find the ID of an existing key with exactly the same
		project/permission combination. If no existing key is found a new one is created.
		@param projectPermissions A <code>Map</code> holding the project/permission combinations
		@return The ID of the new or existing key
	*/
//	static synchronized int getNewOrExistingId(org.hibernate.Session session, Map<ProjectData,Integer> projectPermissions)
//		throws BaseException
//	{
//		org.hibernate.Transaction tx = null;
//		if (session == null)
//		{
//			session = HibernateUtil.newSession();
//			tx = HibernateUtil.newTransaction(session);
//		}
//		try
//		{
//			// 1. Find all keys with the same number of entries as we
//			// have project/permission combinations
//			int numPermissions = projectPermissions.size();
//			org.hibernate.Query query = HibernateUtil.getPredefinedQuery(session, "GET_PROJECTKEY_IDS_FOR_COUNT");
//			/*
//				SELECT pk.keyId FROM ProjectKeys pk
//				GROUP BY pk.keyId
//				HAVING count(pk.projectId) = :numPermissions
//			*/
//			query.setInteger("numPermissions", numPermissions);
//			List<Integer> candidates = HibernateUtil.loadList(Integer.class, query);
//			if (candidates.size() > 0)
//			{
//
//				// 2. Check that the candidates have the same project/permission combinations
//				StringBuilder sb = new StringBuilder();
//				int i = 0;
//				for (Map.Entry<ProjectData,Integer> entry : projectPermissions.entrySet())
//				{
//					if (i > 0) sb.append(" OR ");
//					i++;
//					sb.append("(pk.projectId=").append(entry.getKey().getId()).
//						append(" AND pk.permission=").append(entry.getValue()).append(") ");
//				}
//
//				query = HibernateUtil.createQuery(session,
//					"SELECT pk.keyId FROM ProjectKeys pk "+
//					"WHERE pk.keyId IN (:candidates) AND ("+sb.toString()+") "+
//					"GROUP BY pk.keyId "+
//					"HAVING COUNT(pk.projectId) = :numPermissions"
//				);
//				query.setParameterList("candidates", candidates, org.hibernate.Hibernate.INTEGER);
//				query.setInteger("numPermissions", numPermissions);
//				candidates = HibernateUtil.loadList(Integer.class, query);
//			}
//			if (candidates.size() == 0)
//			{
//				// No more candidates, create a new project key
//				ProjectKeyData data = new ProjectKeyData();
//				data.getProjects().putAll(projectPermissions);
//				HibernateUtil.saveData(session, data);
//				if (tx != null) HibernateUtil.commit(tx);
//				return data.getId();
//			}
//			if (tx != null) HibernateUtil.commit(tx);
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




