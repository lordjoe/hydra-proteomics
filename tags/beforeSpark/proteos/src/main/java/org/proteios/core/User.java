/*
 $Id: User.java 3207 2009-04-09 06:48:11Z gregory $

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

import org.proteios.core.data.UserData;
//import serp.bytecode.*;
//import org.proteios.core.query.EntityQuery;
//import org.proteios.core.query.Hql;
//import org.proteios.core.query.Restrictions;
//import org.proteios.util.MD5;
import javax.management.relation.*;
import java.security.Principal;
import java.util.Date;

/**
 * This class is used to represent a user in Proteios.
 * 
 * @author Nicklas
 * @version 2.0
 * @base.modified $Date: 2009-04-08 23:48:11 -0700 (Wed, 08 Apr 2009) $
 */
public class User
		extends BasicItem<UserData>
		implements Nameable, Removable, SystemItem, Principal
{
	/**
	 * The type of item represented by this class.
	 * 
	 * @see Item#USER
	 * @see #getType()
	 */
	public static final Item TYPE = Item.USER;
	/**
	 * The system id for the <code>User</code> representing the root account.
	 * The root user has full access to everything in Proteios.
	 */
	public static final String ROOT = "org.proteios.core.User.ROOT";
	/**
	 * This filter will limit a query to only return users which are members of
	 * at least one of the groups where thelogged in user is a member unless the
	 * logged in user has generic read permission.
	 */
	//private static final QueryRuntimeFilter RUNTIME_FILTER = new QueryRuntimeFilterImpl();


	/**
	 * Get a <code>User</code> item when you know the ID.
	 * 
	 * @param dc The <code>DbControl</code> which will be used for permission
	 *        checking and database access.
	 * @param id The ID of the item to load
	 * @return The <code>User</code> item
	 * @throws ItemNotFoundException If an item with the specified ID is not
	 *         found
	 * @throws PermissionDeniedException If the logged in user doesn't have
	 *         {@link Permission#READ} permission to the item
	 * @throws BaseException If there is another error
	 */
//	public static User getById(DbControl dc, int id)
//			throws ItemNotFoundException, PermissionDeniedException,
//			BaseException
//	{
//		User u = dc.loadItem(User.class, id);
//		if (u == null)
//			throw new ItemNotFoundException("User[id=" + id + "]");
//		return u;
//	}
//

	/**
	 * Get a query configured to retrieve users. If the logged in user doesn't
	 * have generic permission to all users, only users that are members in at
	 * least one group where the logged in user is also a member are returned.
	 * The <code>Everyone</code> group is not considered since all users
	 * automatically are members to that group.
	 * 
	 * @return An {@link ItemQuery} object
	 */
	public static ItemQuery<User> getQuery()
	{
        throw new UnsupportedOperationException("Fix This"); // ToDo
	//	return new ItemQuery<User>(User.class, RUNTIME_FILTER);
	}


	User(UserData userData)
	{
		super(userData);
	}


	/*
	 * From the Identifiable interface
	 * -------------------------------------------
	 */
	public Item getType()
	{
		return TYPE;
	}


	// -------------------------------------------
	/*
	 * From the Nameable interface -------------------------------------------
	 */
	public String getName()
	{
		return getData().getName();
	}


	public void setName(String name)
			throws PermissionDeniedException, InvalidDataException
	{
		checkPermission(Permission.WRITE);
		NameableUtil.setName(getData(), name);
	}


	public String getDescription()
	{
		return getData().getDescription();
	}


	public void setDescription(String description)
			throws PermissionDeniedException, InvalidDataException
	{
		checkPermission(Permission.RESTRICTED_WRITE);
		NameableUtil.setDescription(getData(), description);
	}


	// -------------------------------------------
	/*
	 * From the Removable interface -------------------------------------------
	 */
	public boolean isRemoved()
	{
		return getData().isRemoved();
	}


	public void setRemoved(boolean removed)
			throws PermissionDeniedException
	{
		checkPermission(removed ? Permission.DELETE : Permission.WRITE);
		getData().setRemoved(removed);
	}


	// -------------------------------------------
	/*
	 * From the SystemItem interface -------------------------------------------
	 */
	public String getSystemId()
	{
		return getData().getSystemId();
	}


	public boolean isSystemItem()
	{
		return getSystemId() != null;
	}


	// -------------------------------------------
	/*
	 * From the BasicItem class -------------------------------------------
	 */
	/**
	 * Checks if:
	 * <ul>
	 * <li>any {@link Ownable} item is owned by this user
	 * </ul>
	 * A user can also be referenced from groups, roles, projects, item keys,
	 * sessions and settings, but those references are automatically deleted if
	 * the user is deleted and aren't inclued in this check.
	 */
	@Override
	public boolean isUsed()
			throws BaseException
	{
        throw new UnsupportedOperationException("Fix This"); // ToDo
//		org.hibernate.Session session = getDbControl().getHibernateSession();
//		org.hibernate.Query query = HibernateUtil.getPredefinedQuery(session,
//			"GET_OWNABLE_ITEMS_FOR_USER");
//		/*
//		 * SELECT o FROM org.proteios.core.data.OwnableData o WHERE o.owner =
//		 * :user
//		 */
//		query.setEntity("user", this.getData());
//		return HibernateUtil.loadData(OwnableData.class, query) != null;
	}


	/**
	 * If the logged in user is the same as this user RESTRICTED_WRITE
	 * permissions is granted, unless it is a multiuser account. If this is a
	 * system user, delete and create permissions are revoked. Finally READ
	 * permission is granted to if the user is a member of at least one group
	 * where the logged in user is also a member.
	 */
	@Override
	void initPermissions(int granted, int denied)
			throws BaseException
	{
		if (isSystemItem())
		{
			denied |= Permission.deny(Permission.DELETE, Permission.CREATE);
		}
//		if (getSessionControl().getLoggedInUserId() == getId())
//		{
//			granted |= Permission
//				.grant(isMultiuserAccount() ? Permission.READ : Permission.RESTRICTED_WRITE);
//		}
//		if (getSessionControl().isFriendOf(this))
//		{
//			granted |= Permission.grant(Permission.READ);
//		}
		super.initPermissions(granted, denied);
	}


	// -------------------------------------------
	/**
	 * Set the password. A null password is not allowed.
	 * 
	 * @throws PermissionDeniedException If the logged in user doesn't have
	 *         restricted_write permission
	 * @throws InvalidDataException If the new password is null
	 */
	public void setPassword(String password)
			throws PermissionDeniedException, InvalidDataException
	{
        throw new UnsupportedOperationException("Fix This"); // ToDo
//		checkPermission(Permission.RESTRICTED_WRITE);
//		if (password == null)
//			throw new InvalidUseOfNullException("password");
//		getData().getPassword().setMd5Password(MD5.getHashString(password));
	}


	/**
	 * Set the encrypted password from Proteios 1. This method is only intended
	 * to be used from the migration application, and will throw a
	 * {@link PermissionDeniedException} unless the logged in user is the root
	 * and the user account is a newly created account.
	 * 
	 * @param md5Password The MD5 password from a Proteios 1 installation
	 * @throws PermissionDeniedException If it is not a new user or root isn't
	 *         logged in
	 */
	public void setBase1Password(String md5Password)
			throws PermissionDeniedException, BaseException
	{
        throw new UnsupportedOperationException("Fix This"); // ToDo
//		if (isInDatabase() || (SystemItems.getId(User.ROOT) != getSessionControl()
//			.getLoggedInUserId()))
//		{
//			throw new PermissionDeniedException(Permission.WRITE,
//				"Password[login=" + getLogin() + "]");
//		}
//		getData().getPassword().setMd5Password(md5Password);
	}


	/**
	 * Get the expire date of the account. When the expiration date have been
	 * passed the user can't login. A null value indicates that the account will
	 * never expire.
	 */
	public Date getExpirationDate()
	{
        throw new UnsupportedOperationException("Fix This"); // ToDo
	//	return DateUtil.copy(getData().getExpirationDate());
	}


	/**
	 * Sets the expiration date of the account. A null value indicates that the
	 * account will never expire.
	 * 
	 * @throws PermissionDeniedException If the logged in user doesn't have
	 *         write permission
	 */
	public void setExpirationDate(Date expirationDate)
			throws PermissionDeniedException
	{
		checkPermission(Permission.WRITE);
		getData().setExpirationDate(
			DateUtil.setNullableDate(expirationDate, "expirationDate"));
	}


	/**
	 * Check if this account has been disabled. It is not possible to login if
	 * the account is disabled.
	 * 
	 * @return TRUE if the account is disabled, FALSE otherwise
	 */
	public boolean isDisabled()
	{
		return getData().isDisabled();
	}


	/**
	 * Disables or enables the account.
	 * 
	 * @param disabled TRUE to disabled the account, FALSE to enabled it
	 * @throws PermissionDeniedException If the logged in user doesn't have
	 *         write permission
	 * @see #isDisabled()
	 */
	public void setDisabled(boolean disabled)
			throws PermissionDeniedException
	{
		checkPermission(Permission.WRITE);
		getData().setDisabled(disabled);
	}


	/**
	 * Check if the user account is a multiuser account or not. Multiuser
	 * accounts don't have write permissions for contact information and
	 * settings.
	 * 
	 * @return TRUE if the user account is a multiuser account, FALSE otherwise
	 */
	public boolean isMultiuserAccount()
	{
		return getData().isMultiuserAccount();
	}


	/**
	 * Sets if the user account is multiuser account.
	 * 
	 * @throws PermissionDeniedException If the logged in user doesn't have
	 *         write permission
	 */
	public void setMultiuserAccount(boolean multiuserAccount)
			throws PermissionDeniedException
	{
		checkPermission(Permission.WRITE);
		getData().setMultiuserAccount(multiuserAccount);
	}

	/**
	 * The maximum length of the external ID that can be stored in the database.
	 * 
	 * @see #setExternalId(String)
	 */
	public static final int MAX_EXTERNAL_ID_LENGTH = UserData.MAX_EXTERNAL_ID_LENGTH;


	/**
	 * Get the external id for the user account. The external id is intended to
	 * be used by external applications which need to synchronize data between
	 * the Proteios database and some external database. It is not used by the
	 * core.
	 */
	public String getExternalId()
	{
		return getData().getExternalId();
	}


	/**
	 * Set the external id for the user account.
	 * 
	 * @throws PermissionDeniedException If the logged in user doesn't have
	 *         write permission
	 * @throws InvalidDataException If the new value is longer than
	 *         {@link #MAX_EXTERNAL_ID_LENGTH}
	 * @throws BaseException If there is another error
	 */
	public void setExternalId(String externalId)
			throws PermissionDeniedException, InvalidDataException,
			BaseException
	{
		checkPermission(Permission.WRITE);
		getData().setExternalId(
			StringUtil.setNullableString(externalId, "externalId",
				MAX_EXTERNAL_ID_LENGTH));
	}

	/**
	 * The maximum length of the login that can be stored in the database.
	 * 
	 * @see #setLogin(String)
	 */
	public static final int MAX_LOGIN_LENGTH = UserData.MAX_LOGIN_LENGTH;


	/**
	 * Get the login for the user account.
	 */
	public String getLogin()
	{
		return getData().getLogin();
	}


	/**
	 * Set the login for the user account.
	 * 
	 * @throws PermissionDeniedException If the logged in user doesn't have
	 *         write permission
	 * @throws InvalidDataException If the new value is null or longer than
	 *         {@link #MAX_LOGIN_LENGTH}
	 */
	public void setLogin(String login)
			throws PermissionDeniedException, InvalidDataException
	{
		checkPermission(Permission.WRITE);
		getData().setLogin(
			StringUtil.setNotNullString(login, "login", MAX_LOGIN_LENGTH));
	}

	/**
	 * The maximum length of the organisation that can be stored in the
	 * database.
	 * 
	 * @see #setOrganisation(String)
	 */
	public static final int MAX_ORGANISATION_LENGTH = UserData.MAX_ORGANISATION_LENGTH;


	/**
	 * Get the organisation this user works for, or null if unknown.
	 */
	public String getOrganisation()
	{
		return getData().getOrganisation();
	}


	/**
	 * Set the organisation this user works for, or null if unknown.
	 * 
	 * @throws PermissionDeniedException If the logged in user doesn't have
	 *         restricted write permission
	 * @throws InvalidDataException If the new value is longer than
	 *         {@link #MAX_ORGANISATION_LENGTH}
	 */
	public void setOrganisation(String organisation)
			throws PermissionDeniedException, InvalidDataException
	{
		checkPermission(Permission.RESTRICTED_WRITE);
		getData().setOrganisation(
			StringUtil.setNullableString(organisation, "organisation",
				MAX_ORGANISATION_LENGTH));
	}

	/**
	 * The maximum length of the address that can be stored in the database.
	 * 
	 * @see #setAddress(String)
	 */
	public static final int MAX_ADDRESS_LENGTH = UserData.MAX_ADDRESS_LENGTH;


	/**
	 * Get the address for the user, or null if unknown.
	 */
	public String getAddress()
	{
		return getData().getAddress();
	}


	/**
	 * Set the address for the user, or null if unknown.
	 * 
	 * @throws PermissionDeniedException If the logged in user doesn't have
	 *         write permission
	 * @throws InvalidDataException If the new value is longer than
	 *         {@link #MAX_ADDRESS_LENGTH}
	 */
	public void setAddress(String address)
			throws PermissionDeniedException, InvalidDataException
	{
		checkPermission(Permission.RESTRICTED_WRITE);
		getData().setAddress(
			StringUtil
				.setNullableString(address, "address", MAX_ADDRESS_LENGTH));
	}

	/**
	 * The maximum length of the phone that can be stored in the database.
	 * 
	 * @see #setPhone(String)
	 */
	public static final int MAX_PHONE_LENGTH = UserData.MAX_PHONE_LENGTH;


	/**
	 * Get the phone number to the user, or null if unknown.
	 */
	public String getPhone()
	{
		return getData().getPhone();
	}


	/**
	 * Set the phone number to the user, or null if unknown.
	 * 
	 * @throws PermissionDeniedException If the logged in user doesn't have
	 *         write permission
	 * @throws InvalidDataException If the new value is longer than
	 *         {@link #MAX_PHONE_LENGTH}
	 */
	public void setPhone(String phone)
			throws PermissionDeniedException, InvalidDataException
	{
		checkPermission(Permission.RESTRICTED_WRITE);
		getData().setPhone(
			StringUtil.setNullableString(phone, "phone", MAX_PHONE_LENGTH));
	}

	/**
	 * The maximum length of the fax that can be stored in the database.
	 * 
	 * @see #setFax(String)
	 */
	public static final int MAX_FAX_LENGTH = UserData.MAX_FAX_LENGTH;


	/**
	 * Get the fax number to the user, or null if unknown.
	 */
	public String getFax()
	{
		return getData().getFax();
	}


	/**
	 * Set the fax number to the user, or null if unknown.
	 * 
	 * @throws PermissionDeniedException If the logged in user doesn't have
	 *         write permission
	 * @throws InvalidDataException If the new value is longer than
	 *         {@link #MAX_FAX_LENGTH}
	 */
	public void setFax(String fax)
			throws PermissionDeniedException, InvalidDataException
	{
		checkPermission(Permission.RESTRICTED_WRITE);
		getData().setFax(
			StringUtil.setNullableString(fax, "fax", MAX_FAX_LENGTH));
	}

	/**
	 * The maximum length of the email address that can be stored in the
	 * database.
	 * 
	 * @see #setEmail(String)
	 */
	public static final int MAX_EMAIL_LENGTH = UserData.MAX_EMAIL_LENGTH;


	/**
	 * Get the email address to the user, or null if unknown.
	 */
	public String getEmail()
	{
		return getData().getEmail();
	}


	/**
	 * Set the email address to the user, or null if unknown.
	 * 
	 * @throws PermissionDeniedException If the logged in user doesn't have
	 *         write permission
	 * @throws InvalidDataException If the new value is longer than
	 *         {@link #MAX_EMAIL_LENGTH}
	 */
	public void setEmail(String email)
			throws PermissionDeniedException, InvalidDataException
	{
		checkPermission(Permission.RESTRICTED_WRITE);
		getData().setEmail(
			StringUtil.setNullableString(email, "email", MAX_EMAIL_LENGTH));
	}

	/**
	 * The maximum length of the url that can be stored in the database.
	 * 
	 * @see #setUrl(String)
	 */
	public static final int MAX_URL_LENGTH = UserData.MAX_URL_LENGTH;


	/**
	 * Get the URL to the user's homepage, or null if unknown.
	 */
	public String getUrl()
	{
		return getData().getUrl();
	}


	/**
	 * Set the URL to the user's homepage, or null if unknown.
	 * 
	 * @throws PermissionDeniedException If the logged in user doesn't have
	 *         write permission
	 * @throws InvalidDataException If the new value is longer than
	 *         {@link #MAX_URL_LENGTH}
	 */
	public void setUrl(String url)
			throws PermissionDeniedException, InvalidDataException
	{
		checkPermission(Permission.RESTRICTED_WRITE);
		getData().setUrl(
			StringUtil.setNullableString(url, "url", MAX_URL_LENGTH));
	}


	/**
	 * Get the {@link Quota} that applies to the user.
	 * 
	 * @return A <code>Quota</code> item
	 * @throws PermissionDeniedException If the logged in user doesn't have
	 *         {@link Permission#READ} permission to the item
	 */
//	public Quota getQuota()
//			throws PermissionDeniedException, BaseException
//	{
//		return getDbControl().getItem(Quota.class, getData().getQuota());
//	}


	/**
	 * Set the quota for the user.
	 * 
	 * @param quota The new <code>Quota</code>
	 * @throws InvalidDataException If the quota is null
	 * @throws PermissionDeniedException If the logged in user doesn't have
	 *         {@link Permission#WRITE} permission for the user or
	 *         {@link Permission#USE} permission for the quota
	 */
//	public void setQuota(Quota quota)
//			throws PermissionDeniedException, InvalidDataException
//	{
//		checkPermission(Permission.WRITE);
//		if (quota == null)
//			throw new InvalidUseOfNullException("quota");
//		quota.checkPermission(Permission.USE);
//		getData().setQuota(quota.getData());
//	}


	/**
	 * Get the {@link Group} whose {@link Quota} also applies to the user.
	 * 
	 * @return A <code>Group</code> item, or null if no group has been
	 *         specified
	 * @throws PermissionDeniedException If the logged in user doesn't have
	 *         {@link Permission#READ} permission to the item
	 */
//	public Group getQuotaGroup()
//			throws PermissionDeniedException, BaseException
//	{
//		return getDbControl().getItem(Group.class, getData().getQuotaGroup());
//	}


	/**
	 * Set the group whose quota should be checked for disk consuming items.
	 * 
	 * @param quotaGroup The new <code>Group</code>
	 * @throws PermissionDeniedException If the logged in user doesn't have
	 *         {@link Permission#WRITE} permission for the user or
	 *         {@link Permission#USE} permission for the group
	 */
//	public void setQuotaGroup(Group quotaGroup)
//			throws PermissionDeniedException
//	{
//		checkPermission(Permission.WRITE);
//		if (quotaGroup != null)
//			quotaGroup.checkPermission(Permission.USE);
//		getData().setQuotaGroup(
//			quotaGroup == null ? null : quotaGroup.getData());
//	}


	/**
	 * Get the used number of bytes for the specified quota type and location.
	 * 
	 * @param quotaType The {@link QuotaType}
	 * @param location The location
	 * @return The number of bytes that have been used
	 */
//	public long getDiskUsage(QuotaType quotaType, Location location)
//			throws BaseException
//	{
//		if (quotaType == null)
//			throw new InvalidUseOfNullException("quotaType");
//		if (location == null)
//			throw new InvalidUseOfNullException("location");
//		return getDbControl().getDiskUsage(getData(), quotaType.getData(),
//			location);
//	}


	/**
	 * Get the home {@link Directory} for this user.
	 * 
	 * @return A <code>Directory</code> item, or null if no home directory has
	 *         been specified
	 * @throws PermissionDeniedException If the logged in user doesn't have
	 *         {@link Permission#READ} permission to the directory
	 */
//	public Directory getHomeDirectory()
//			throws PermissionDeniedException, BaseException
//	{
//		return getDbControl().getItem(Directory.class,
//			getData().getHomeDirectory());
//	}


	/**
	 * Set the home directory for the user.
	 * 
	 * @param homeDirectory The new home <code>Directory</code>
	 * @throws PermissionDeniedException If the logged in user doesn't have
	 *         {@link Permission#WRITE} permission for the user or
	 *         {@link Permission#USE} permission for the directory
	 * @throws InvalidDataException If the home directory is not a subdirectory
	 *         to /root/home
	 */
//	public void setHomeDirectory(Directory homeDirectory)
//			throws PermissionDeniedException, InvalidDataException
//	{
//		checkPermission(Permission.WRITE);
//		if (homeDirectory != null)
//		{
//			homeDirectory.checkPermission(Permission.USE);
//			DirectoryData parent = homeDirectory.getData().getParent();
//			if (parent == null || parent.getId() != SystemItems
//				.getId(Directory.HOME))
//			{
//				throw new InvalidDataException(
//					"homeDirectory is not a subdirectory to /root/home");
//			}
//		}
//		getData().setHomeDirectory(
//			homeDirectory == null ? null : homeDirectory.getData());
//	}


	/**
	 * Get a query that returns the roles where this user is a member. The query
	 * excludes roles that the logged in user doesn't have permission to read.
	 * 
	 * @see Role#getQuery()
	 */
//	public ItemQuery<Role> getRoles()
//	{
//		ItemQuery<Role> query = Role.getQuery();
//		query.joinPermanent(Hql.innerJoin("users", Item.USER.getAlias()));
//		query.restrictPermanent(Restrictions.eq(
//			Hql.alias(Item.USER.getAlias()), Hql.entity(this)));
//		return query;
//	}


	/**
	 * Get a query that returns the groups where this user is a member. The
	 * query excludes groups that the logged in user doesn't have permission to
	 * read.
	 * 
	 * @see Group#getQuery()
	 */
//	public ItemQuery<Group> getGroups()
//	{
//		ItemQuery<Group> query = Group.getQuery();
//		query.joinPermanent(Hql.innerJoin("users", Item.USER.getAlias()));
//		query.restrictPermanent(Restrictions.eq(
//			Hql.alias(Item.USER.getAlias()), Hql.entity(this)));
//		return query;
//	}


	/**
	 * Get a query that returns the projects where this user is a member. The
	 * query excludes projects that the logged in user doesn't have permission
	 * to read. The query doesn't include projects where this user is the owner.
	 * 
	 * @see Project#getQuery()
	 */
//	public ItemQuery<Project> getProjects()
//	{
//		ItemQuery<Project> query = Project.getQuery();
//		query.joinPermanent(Hql.innerJoin("users", Item.USER.getAlias()));
//		query.restrictPermanent(Restrictions.eq(Hql.index(Item.USER.getAlias(),
//			null), Hql.entity(this)));
//		return query;
//	}
//

	/**
	 * Get a query that returns all messages for the user.
	 * 
	 * @return An {@link ItemQuery} object
	 * @see Message#getQuery(User)
	 */
//	public ItemQuery<Message> getMessages()
//	{
//		return Message.getQuery(this);
//	}

	/**
	 * A runtime filter implementation that limits a query to only return users
	 * which are members of at least one group where the logged in user is also
	 * a member unless the logged in user has generic read permission.
	 */
//	private static class QueryRuntimeFilterImpl
//			implements QueryRuntimeFilter
//	{
//		public void enableFilters(QueryRuntimeFilterManager manager,
//				EntityQuery query, DbControl dc)
//		{
//			SessionControl sc = dc.getSessionControl();
//			if (!sc.hasPermission(Permission.READ, Item.USER))
//			{
//				if (sc.isLoggedIn())
//				{
//					// Load users that are friends of the logged in user
//					org.hibernate.Filter filter = manager
//						.enableFilter("memberOf");
//					if (filter != null)
//					{
//						Set<Integer> friends = sc.getFriends();
//						if (friends == null || friends.size() == 0)
//							friends = Collections.singleton(0);
//						filter.setParameterList("items", friends);
//						filter.setParameter("owner", sc.getLoggedInUserId());
//					}
//				}
//				else
//				{
//					// Do not load any users if not logged in
//					manager.enableFilter("denyAll");
//				}
//			}
//		}
//	}


//	static User getByLogin(DbControl dc, String login)
//			throws ItemNotFoundException, BaseException
//	{
//		org.hibernate.Query query = HibernateUtil.getPredefinedQuery(dc
//			.getHibernateSession(), "GET_USER_FOR_LOGIN");
//		query.setString("login", login);
//		User u = dc.getItem(User.class, HibernateUtil.loadData(UserData.class,
//			query));
//		if (u == null)
//			throw new ItemNotFoundException("User[login=" + login + "]");
//		return u;
//	}
}
