/*
	$Id: BasicItem.java 3207 2009-04-09 06:48:11Z gregory $

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

import org.proteios.core.data.BasicData;
 
import java.util.Set;

/**
 * This is the root superclass of all item classes. All items must inherit from
 * this class. This class provides access to the id, type and version of the
 * item as well as permission checking methods.
 * 
 * @author Nicklas
 * @version 2.0
 * @base.modified $Date: 2009-04-08 23:48:11 -0700 (Wed, 08 Apr 2009) $
 */
public abstract class BasicItem<D extends BasicData>
		implements Identifiable
{
	/**
	 * The data class from the org.proteios.core.data layer that holds the data
	 * for this item.
	 */
	private final D data;
	/**
	 * A reference to the current DbControl object. We use a WeakReference since
	 * we it is always the client applications responsibility to keep control
	 * over the DbControl object.
	 */
//	private WeakReference<DbControl> dc;
	/**
	 * The logged in user's permission to this item. Calculated by the
	 * {@link #initPermissions(int, int)} method.
	 */
	private int permissions;


	/**
	 * Create a new instance. Do not call this directly. Use the
	 * {@link DbControl#newItem(Class, Object[])} or
	 * {@link DbControl#getItem(Class, BasicData, Object[])} methods instead.
	 */
	BasicItem(D data)
	{
		this.data = data;
		this.permissions = 127; // All item permissions
	}


	/*
	 * From the AccessControlled interface
	 * -------------------------------------------
	 */
	/**
	 * Checks if the logged in user has the specified permission on this item.
	 * The default implementation only checks the role keys for permissions. A
	 * subclass may not override this method since it would make it possible to
	 * bypass security checks.
	 * <p>
	 * Subclasses that needs to check other keys, such as the {@link SharedItem}
	 * should override the {@link #initPermissions(int,int)} method instead.
	 * 
	 * @param permission A value from the {@link Permission} class
	 * @return <code>TRUE</code> if the user has the permission,
	 *         <code>FALSE</code> otherwise
	 */
	public final boolean hasPermission(Permission permission)
	{
		return Permission.hasPermission(permissions, permission);
	}


	/**
	 * Checks if the logged in user has the specified permission on this item.
	 * If not, a {@link PermissionDeniedException} is thrown.
	 * 
	 * @param permission A value from the {@link Permission} class
	 * @throws PermissionDeniedException If the logged in user doesn't have the
	 *         requested permission
	 */
	public final void checkPermission(Permission permission)
			throws PermissionDeniedException
	{
		if (!hasPermission(permission))
		{
			throw new PermissionDeniedException(permission, this.toString());
		}
	}


	public final Set<Permission> getPermissions()
	{
		return Permission.fromInt(permissions);
	}


	// -------------------------------------------
	/*
	 * From the Identifiable interface
	 * -------------------------------------------
	 */
	public final int getId()
	{
		return data.getId();
	}


	public final int getVersion()
	{
		return data.getVersion();
	}


	// The getType() method must be implemented by each subclass
	// -------------------------------------------
	/*
	 * From the Object class -------------------------------------------
	 */
	@Override
	public String toString()
	{
		String id = getId() == 0 ? "new" : "id=" + getId();
		if (this instanceof Nameable)
		{
			Nameable n = (Nameable) this;
			id += "; name=" + n.getName();
		}
		return getType().toString() + "[" + id + "]";
	}


	/**
	 * Check if this item is equal to another item. They are considered to be
	 * equal if their BasicData objects are equal.
	 * 
	 * @see BasicData#equals(Object)
	 */
	@Override
	public final boolean equals(Object o)
	{
		if (this == o)
			return true;
		if (!(o instanceof BasicItem))
			return false;
		BasicItem item = (BasicItem) o;
		return data.equals(item.data);
	}


	/**
	 * The hash code is calculated at object construction and remains the same
	 * during the objects lifetime. For new items the hash code is randomly
	 * generated. Items loaded from the database calculates the hash code from
	 * the from the id.
	 * 
	 * @see BasicData#hashCode()
	 */
	@Override
	public final int hashCode()
	{
		return data.hashCode();
	}


	// -------------------------------------------
	/**
	 * Checks if the item has been saved to the database or not.
	 * 
	 * @return TRUE if the item is saved to the database, FALSE otherwise
	 */
	public final boolean isInDatabase()
	{
		return getId() != 0;
	}


	/**
	 * Check if this item is attached to a {@link DbControl} object or not. An
	 * item is detached if there is no <code>DbControl</code>, if it is
	 * closed, or if is not managed by the <code>DbControl</code>.
	 * 
	 * @return TRUE if the item is detached, FALSE otherwise
	 * @see DbControl#detachItem(BasicItem)
	 * @see DbControl#reattachItem(BasicItem)
	 * @see DbControl#saveItem(BasicItem)
	 */
	public final boolean isDetached()
	{
        return true; // modified SL
//		return dc == null || dc.get() == null || dc.get().isClosed() || !dc
//			.get().isAttached(this);
	}


	/**
	 * Check if this item is used by some other item. With used we mean that
	 * another item is linking to this item in way that prevents this item from
	 * beeing deleted. Ie. if we tried to delete an item that is used, we would
	 * get a foreign key violation error from the database.
	 * 
	 * @return TRUE if this item is used, FALSE otherwise
	 */
	public abstract boolean isUsed()
			throws BaseException;


	/**
	 * Get the {@link BasicData} object that holds all data for this item.
	 */
	final D getData()
	{
		return data;
	}


//	/**
//	 * Get the {@link DbControl} object that currently manages this item.
//	 *
//	 * @throws ConnectionClosedException If the item is connected to a closed
//	 *         <code>DbControl</code> object, or not connected at all
//	 */
//	public final DbControl getDbControl()
//			throws ConnectionClosedException
//	{
//		DbControl dbControl = dc != null ? dc.get() : null;
//		if (dbControl == null)
//			throw new ConnectionClosedException();
//		return dbControl;
//	}
//
//
//	/**
//	 * Set the {@link DbControl} object that should manage this item. A null
//	 * value detaches the item
//	 */
//	final void setDbControl(DbControl dbControl)
//	{
//		dc = dbControl == null ? null : new WeakReference<DbControl>(dbControl);
//	}
//
//
//	/**
//	 * Get the {@link SessionControl} object that manages this item.
//	 *
//	 * @throws ConnectionClosedException If the item is connected to a closed
//	 *         <code>DbControl</code> object, or not connected at all
//	 */
//	public final SessionControl getSessionControl()
//			throws ConnectionClosedException
//	{
//		return getDbControl().getSessionControl();
//	}
//

	/**
	 * Initialise the logged in user's permissions for this item. For items
	 * loaded from the database, the default implementation checks the role
	 * keys. For new items, write permission is added.
	 * <p>
	 * Subclasses that needs to check other keys or properties, such as the
	 * {@link OwnedItem} and {@link SharedItem} should override this method. The
	 * subclass should calculate additional permissions to be granted or denied,
	 * and combine those with whatever was passed as parameters. Use the binary
	 * OR operator ( | ) to combine the permissions. Finally the subclass must
	 * call <code>super.initPermissions(granted, denied)</code>.
	 * 
	 * @param granted Permissions that have been granted by the subclass
	 * @param denied Permissions that have been denied by the subclass
	 * @throws BaseException If the permissions couldn't be initialised
	 */
	void initPermissions(int granted, int denied)
			throws BaseException
	{
        throw new UnsupportedOperationException("Fix This"); // ToDo
//		int rolePermissions = getSessionControl().getRolePermissions(getType());
//		granted |= rolePermissions;
//		if (!isInDatabase())
//		{
//			granted |= Permission.grant(Permission.WRITE);
//		}
//		this.permissions = granted & ~denied; // granted AND NOT denied
	}


	/**
	 * This method is called on each {@link Validatable} item before is saved to
	 * the database. If the subclass overrides this method it should also
	 * propagate the call to the superclass, ie. <code>super.validate()</code>.
	 * 
	 * @throws BaseException If there is an error
	 * @see Validatable
	 * @see <a
	 *      href="../../../../../../development/overview/core/datavalidation.html">Core
	 *      API overview - Data validation</a>
	 * @see <a
	 *      href="../../../../../../development/coding/item/index.html#validation">Coding
	 *      rules and guidelines for item classes</a>
	 */
	void validate()
			throws InvalidDataException, BaseException
	{}


	/**
	 * This method is called on each {@link Transactional} item and on all items
	 * if the action is {@link Transactional.Action#CREATE} or
	 * {@link Transactional.Action#DELETE} before a commit is issued to the
	 * database. If the subclass overrides this method it should also propagate
	 * the call to the superclass, ie. <code>super.onBeforeCommit(action)</code>.
	 * 
	 * @throws BaseException If there is an error
	 * @see Transactional
	 * @see <a
	 *      href="../../../../../../development/overview/core/transactions.html">Core
	 *      API overview - Transaction handling</a>
	 * @see <a
	 *      href="../../../../../../development/coding/item/index.html#transactions">Coding
	 *      rules and guidelines for item classes</a>
	 */
//	void onBeforeCommit(Transactional.Action action)
//			throws BaseException
//	{}


	/**
	 * This method is called on all items directly after Hibernate has inserted
	 * it into the database. This method can be used in place of the
	 * {@link #onBeforeCommit(Transactional.Action)} in case the id is needed.
	 * The id has not been generated when the <code>onBeforeCommit</code> is
	 * called.
	 * 
	 * @throws BaseException If there is an error
	 * @see Transactional
	 * @see <a
	 *      href="../../../../../../development/overview/core/transactions.html">Core
	 *      API overview - Transaction handling</a>
	 * @see <a
	 *      href="../../../../../../development/coding/item/index.html#transactions">Coding
	 *      rules and guidelines for item classes</a>
	 */
	void onAfterInsert()
			throws BaseException
	{}


	/**
	 * This method is called on each {@link Transactional} object after a
	 * successful commit to the database. If the subclass overrides this method
	 * it should also propagate the call to the superclass, ie.
	 * <code>super.onAfterCommit(action)</code>. It is forbidden to access
	 * the <code>DbControl</code> object from this method and it must not
	 * throw any exceptions. All exceptions should be logged using the
	 * {@link Application#getLogger()} object.
	 * 
	 * @see Transactional
	 * @see <a
	 *      href="../../../../../../development/overview/core/transactions.html">Core
	 *      API overview - Transaction handling</a>
	 * @see <a
	 *      href="../../../../../../development/coding/item/index.html#transactions">Coding
	 *      rules and guidelines for item classes</a>
	 */
//	void onAfterCommit(Transactional.Action action)
//	{}


	/**
	 * This method is called on each {@link Transactional} object after an
	 * unsuccessful commit to the database. If the subclass overrides this
	 * method it should also propagate the call to the superclass, ie.
	 * <code>super.onRollback(action)</code>. It is forbidden to access the
	 * <code>DbControl</code> object from this method and it must not throw
	 * any exceptions. All exceptions should be logged using the
	 * {@link Application#getLogger()} object.
	 * 
	 * @see Transactional
	 * @see <a
	 *      href="../../../../../../development/overview/core/transactions.html">Core
	 *      API overview - Transaction handling</a>
	 * @see <a
	 *      href="../../../../../../development/coding/item/index.html#transactions">Coding
	 *      rules and guidelines for item classes</a>
	 */
//	void onRollback(Transactional.Action action)
//	{}
}
