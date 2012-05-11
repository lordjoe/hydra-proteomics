/*
 $Id: UserData.java 3677 2010-04-20 12:02:33Z gregory $

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
 * This class holds information about a user.
 * 
 * @author Nicklas
 * @version 2.0
 * @see org.proteios.core.User
 * @base.modified $Date: 2010-04-20 05:02:33 -0700 (Tue, 20 Apr 2010) $
 * @hibernate.class table="`Users`" lazy="false"
 */
public class UserData
		extends BasicData
		implements NameableData, RemovableData, SystemData
{
	public UserData()
	{}


	/*
	 * From the Identifiable interface
	 * -------------------------------------------
	 */
	/**
	 * We must override this method because the id must be the same as the id
	 * for the password.
	 * 
	 * @hibernate.id column="`id`" generator-class="foreign" unsaved-value="0"
	 * @hibernate.generator-param name="property" value="password"
	 */
	@Override
	public int getId()
	{
		return super.getId();
	}

	// -------------------------------------------
	/*
	 * From the NameableData interface
	 * -------------------------------------------
	 */
	private String name;


	public String getName()
	{
		return name;
	}


	public void setName(String name)
	{
		this.name = name;
	}

	private String description;


	public String getDescription()
	{
		return description;
	}


	public void setDescription(String description)
	{
		this.description = description;
	}

	// -------------------------------------------
	/*
	 * From the RemovableData interface
	 * -------------------------------------------
	 */
	private boolean removed = false;


	public boolean isRemoved()
	{
		return removed;
	}


	public void setRemoved(boolean removed)
	{
		this.removed = removed;
	}

	// -------------------------------------------
	/*
	 * From the SystemData interface -------------------------------------------
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
	private PasswordData password;


	/**
	 * Get the {@link PasswordData} object which allows you to change the
	 * password for the user. This is never null and is automatically deleted
	 * when the user a user is deleted.
	 * 
	 * @hibernate.one-to-one class="org.proteios.core.data.PasswordData"
	 *                       cascade="all" outer-join="false" constrained="true"
	 */
	public PasswordData getPassword()
	{
		if (password == null)
		{
			password = new PasswordData();
			password.setUser(this);
		}
		return password;
	}


	void setPassword(PasswordData password)
	{
		this.password = password;
	}

	private boolean multiuserAccount = false;


	/**
	 * Check if this account has enabled the multiuser feature.
	 * 
	 * @hibernate.property column="`multiuser_account`" type="boolean"
	 *                     not-null="true"
	 */
	public boolean isMultiuserAccount()
	{
		return multiuserAccount;
	}


	public void setMultiuserAccount(boolean multiuserAccount)
	{
		this.multiuserAccount = multiuserAccount;
	}

	private Date expirationDate;


	/**
	 * Get the expiration date for this user account, or null if no expiration
	 * date is set. Since the date object is mutable it must be copied if it is
	 * returned to client applications:
	 * <code>return new Date(userdata.getExpirationDate());</code>
	 * 
	 * @hibernate.property column="`expiration_date`" type="date"
	 *                     not-null="false"
	 */
	public Date getExpirationDate()
	{
		return expirationDate;
	}


	public void setExpirationDate(Date expirationDate)
	{
		this.expirationDate = expirationDate;
	}

	private boolean disabled = false;


	/**
	 * Check if this account is disabled or enabled.
	 * 
	 * @hibernate.property column="`disabled`" type="boolean" not-null="true"
	 */
	public boolean isDisabled()
	{
		return disabled;
	}


	public void setDisabled(boolean disabled)
	{
		this.disabled = disabled;
	}

	/**
	 * The maximum length of the external ID that can be stored in the database.
	 * 
	 * @see #setExternalId(String)
	 */
	public static final int MAX_EXTERNAL_ID_LENGTH = 255;
	private String externalId;


	/**
	 * Get the external ID for the user account.
	 * 
	 * @hibernate.property column="`external_id`" type="string" length="255"
	 *                     not-null="false" unique="true"
	 */
	public String getExternalId()
	{
		return externalId;
	}


	public void setExternalId(String externalId)
	{
		this.externalId = externalId;
	}

	/**
	 * The maximum length of the login that can be stored in the database.
	 * 
	 * @see #setExternalId(String)
	 */
	public static final int MAX_LOGIN_LENGTH = 255;
	private String login;


	/**
	 * Get the login for the user account.
	 * 
	 * @hibernate.property column="`login`" type="string" length="255"
	 *                     not-null="true" unique="true"
	 */
	public String getLogin()
	{
		return login;
	}


	public void setLogin(String login)
	{
		this.login = login;
	}

	/**
	 * The maximum length of the organisation that can be stored in the
	 * database.
	 * 
	 * @see #setOrganisation(String)
	 */
	public static final int MAX_ORGANISATION_LENGTH = 255;
	private String organisation;


	/**
	 * Get the organisation this user works for, or null if unknown.
	 * 
	 * @hibernate.property column="`organisation`" type="string" length="255"
	 */
	public String getOrganisation()
	{
		return organisation;
	}


	public void setOrganisation(String organisation)
	{
		this.organisation = organisation;
	}

	/**
	 * The maximum length of the address that can be stored in the database.
	 * 
	 * @see #setAddress(String)
	 */
	public static final int MAX_ADDRESS_LENGTH = 255;
	private String address;


	/**
	 * Get the address for the user, or null if unknown.
	 * 
	 * @hibernate.property column="`address`" type="string" length="255"
	 */
	public String getAddress()
	{
		return address;
	}


	public void setAddress(String address)
	{
		this.address = address;
	}

	/**
	 * The maximum length of the phone that can be stored in the database.
	 * 
	 * @see #setPhone(String)
	 */
	public static final int MAX_PHONE_LENGTH = 255;
	private String phone;


	/**
	 * Get the phone number to the user, or null if unknown.
	 * 
	 * @hibernate.property column="`phone`" type="string" length="255"
	 */
	public String getPhone()
	{
		return phone;
	}


	public void setPhone(String phone)
	{
		this.phone = phone;
	}

	/**
	 * The maximum length of the fax that can be stored in the database.
	 * 
	 * @see #setFax(String)
	 */
	public static final int MAX_FAX_LENGTH = 255;
	private String fax;


	/**
	 * Get the fax number to the user, or null if unknown.
	 * 
	 * @hibernate.property column="`fax`" type="string" length="255"
	 */
	public String getFax()
	{
		return fax;
	}


	public void setFax(String fax)
	{
		this.fax = fax;
	}

	/**
	 * The maximum length of the email address that can be stored in the
	 * database.
	 * 
	 * @see #setEmail(String)
	 */
	public static final int MAX_EMAIL_LENGTH = 255;
	private String email;


	/**
	 * Get the email address to the user, or null if unknown.
	 * 
	 * @hibernate.property column="`email`" type="string" length="255"
	 */
	public String getEmail()
	{
		return email;
	}


	public void setEmail(String email)
	{
		this.email = email;
	}

	/**
	 * The maximum length of the url that can be stored in the database.
	 * 
	 * @see #setUrl(String)
	 */
	public static final int MAX_URL_LENGTH = 255;
	private String url;


	/**
	 * Get the URL to the user's homepage, or null if unknown.
	 * 
	 * @hibernate.property column="`url`" type="string" length="255"
	 */
	public String getUrl()
	{
		return url;
	}


	public void setUrl(String url)
	{
		this.url = url;
	}

	private GroupData quotaGroup;


	/**
	 * Get the group this user is sharing quota with, or null if this user only
	 * has it's own quota.
	 * 
	 * @see #getQuota()
	 * @hibernate.many-to-one column="`quotagroup_id`" not-null="false"
	 *                        outer-join="false"
	 */
	public GroupData getQuotaGroup()
	{
		return quotaGroup;
	}


	public void setQuotaGroup(GroupData quotaGroup)
	{
		this.quotaGroup = quotaGroup;
	}

	private QuotaData quota;


	/**
	 * Get the quota object which holds quota information for this user.
	 * 
	 * @see #getQuotaGroup()
	 * @hibernate.many-to-one column="`quota_id`" not-null="true"
	 *                        outer-join="false"
	 */
	public QuotaData getQuota()
	{
		return quota;
	}


	public void setQuota(QuotaData quota)
	{
		this.quota = quota;
	}

	private DirectoryData homeDirectory;


	/**
	 * Get the home directory for this user.
	 * 
	 * @see #getQuota()
	 * @hibernate.many-to-one column="`homedirectory_id`" not-null="false"
	 *                        outer-join="false"
	 */
	public DirectoryData getHomeDirectory()
	{
		return homeDirectory;
	}


	public void setHomeDirectory(DirectoryData homeDirectory)
	{
		this.homeDirectory = homeDirectory;
	}

	private Set<GroupData> groups;


	/**
	 * This is the inverse end.
	 * 
	 * @see GroupData#getUsers()
	 * @hibernate.set table="`UserGroups`" lazy="true"
	 * @hibernate.collection-key column="`user_id`"
	 * @hibernate.collection-many-to-many column="`group_id`"
	 *                                    class="org.proteios.core.data.GroupData"
	 */
	Set<GroupData> getGroups()
	{
		return groups;
	}


	void setGroups(Set<GroupData> groups)
	{
		this.groups = groups;
	}

	private Set<RoleData> roles;


	/**
	 * This is the inverse end.
	 * 
	 * @see RoleData#getUsers()
	 * @hibernate.set table="`UserRoles`" lazy="true"
	 * @hibernate.collection-key column="`user_id`"
	 * @hibernate.collection-many-to-many column="`role_id`"
	 *                                    class="org.proteios.core.data.RoleData"
	 */
	Set<RoleData> getRoles()
	{
		return roles;
	}


	void setRoles(Set<RoleData> roles)
	{
		this.roles = roles;
	}

	private Set<ProjectData> projects;


	/**
	 * This is the inverse end.
	 * 
	 * @see ProjectData#getUsers()
	 * @hibernate.set table="`UserProjects`" lazy="true"
	 * @hibernate.collection-key column="`user_id`"
	 * @hibernate.collection-many-to-many column="`project_id`"
	 *                                    class="org.proteios.core.data.ProjectData"
	 */
	Set<ProjectData> getProjects()
	{
		return projects;
	}


	void setProjects(Set<ProjectData> projects)
	{
		this.projects = projects;
	}

	private Set<ItemKeyData> itemKeys;


	/**
	 * This is the inverse end.
	 * 
	 * @see ItemKeyData#getUsers()
	 * @hibernate.set table="`UserKeys`" lazy="true"
	 * @hibernate.collection-key column="`user_id`"
	 * @hibernate.collection-many-to-many column="`key_id`"
	 *                                    class="org.proteios.core.data.ItemKeyData"
	 */
	Set<ItemKeyData> getItemKeys()
	{
		return itemKeys;
	}


	void setItemKeys(Set<ItemKeyData> itemKeys)
	{
		this.itemKeys = itemKeys;
	}

	private Set<SessionData> sessions;


	/**
	 * This is the inverse end.
	 * 
	 * @see SessionData#getUser()
	 * @hibernate.set lazy="true" inverse="true" cascade="delete"
	 * @hibernate.collection-key column="`user_id`"
	 * @hibernate.collection-one-to-many class="org.proteios.core.data.SessionData"
	 */
	Set<SessionData> getSessions()
	{
		return sessions;
	}


	void setSessions(Set<SessionData> sessions)
	{
		this.sessions = sessions;
	}

	private Set<UserClientSettingData> clientSettings;


	/**
	 * This is the inverse end.
	 * 
	 * @see UserClientSettingData#getUser()
	 * @hibernate.set lazy="true" inverse="true" cascade="delete"
	 * @hibernate.collection-key column="`user_id`"
	 * @hibernate.collection-one-to-many class="org.proteios.core.data.UserClientSettingData"
	 */
	Set<UserClientSettingData> getClientSettings()
	{
		return clientSettings;
	}


	void setClientSettings(Set<UserClientSettingData> clientSettings)
	{
		this.clientSettings = clientSettings;
	}

	private Set<UserDefaultSettingData> defaultSettings;


	/**
	 * This is the inverse end.
	 * 
	 * @see UserDefaultSettingData#getUser()
	 * @hibernate.set lazy="true" inverse="true" cascade="delete"
	 * @hibernate.collection-key column="`user_id`"
	 * @hibernate.collection-one-to-many class="org.proteios.core.data.UserDefaultSettingData"
	 */
	Set<UserDefaultSettingData> getDefaultSettings()
	{
		return defaultSettings;
	}


	void setDefaultSettings(Set<UserDefaultSettingData> defaultSettings)
	{
		this.defaultSettings = defaultSettings;
	}

	private Set<MessageData> messages;


	/**
	 * This is the inverse end.
	 * 
	 * @see MessageData#getTo()
	 * @hibernate.set lazy="true" inverse="true" cascade="delete"
	 * @hibernate.collection-key column="`to_user_id`"
	 * @hibernate.collection-one-to-many class="org.proteios.core.data.MessageData"
	 */
	Set<MessageData> getMessages()
	{
		return messages;
	}


	void setMessages(Set<MessageData> messages)
	{
		this.messages = messages;
	}

}
