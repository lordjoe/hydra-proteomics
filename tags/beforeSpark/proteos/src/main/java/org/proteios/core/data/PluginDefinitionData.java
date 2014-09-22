/*
 $Id: PluginDefinitionData.java 3207 2009-04-09 06:48:11Z gregory $

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
 * This class represent an installed plugin.
 * 
 * @author Nicklas, Samuel
 * @version 2.0
 * @see org.proteios.core.PluginDefinition
 * @see <a
 *      href="../../../../../../../development/overview/data/plugins.html">Plugins</a>
 * @hibernate.class table="`PluginDefinitions`" lazy="false"
 */
public class PluginDefinitionData
		extends SharedData
		implements RemovableData
{
	public PluginDefinitionData()
	{}

	/*
	 * From the RemovableData interface
	 * -------------------------------------------
	 */
	private boolean removed;


	public boolean isRemoved()
	{
		return removed;
	}


	public void setRemoved(boolean removed)
	{
		this.removed = removed;
	}

	// -------------------------------------------
	/**
	 * The maximum length of the class name that can be stored in the database.
	 */
	public static final int MAX_CLASSNAME_LENGTH = 255;
	private String className;


	/**
	 * Get the class name of the Java class that implements this plugin. The
	 * specified class must implement the
	 * {@link org.proteios.core.plugin.Plugin} interface.
	 * 
	 * @return The class name for this plugin
	 * @hibernate.property column="`class_name`" type="string" length="255"
	 *                     not-null="true" unique="true"
	 */
	public String getClassName()
	{
		return className;
	}


	public void setClassName(String className)
	{
		this.className = className;
	}

	public static final int MAX_JARPATH_LENGTH = 65535;
	private String jarPath;


	/**
	 * Get the path to the JAR file where the plugin class is located. If the
	 * value is null the plugin must be in the classpath.
	 * 
	 * @return The class name for this plugin
	 * @hibernate.property column="`jar_path`" type="text" not-null="false"
	 */
	public String getJarPath()
	{
		return jarPath;
	}


	public void setJarPath(String jarPath)
	{
		this.jarPath = jarPath;
	}

	/**
	 * The maximum length of the name that can be stored in the database.
	 */
	public static final int MAX_NAME_LENGTH = 255;
	private String name;


	/**
	 * Get the name of the plugin.
	 * 
	 * @return A string with the name of the plugin
	 * @hibernate.property column="`name`" type="string" length="255"
	 *                     not-null="true"
	 */
	public String getName()
	{
		return name;
	}


	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * The maximum length of the plugin version that can be stored in the
	 * database.
	 */
	public static final int MAX_PLUGIN_VERSION_LENGTH = 255;
	private String pluginVersion;


	/**
	 * Get the plugin version.
	 * 
	 * @return String The plugin version.
	 * @hibernate.property column="`plugin_version`" type="string" length="255"
	 *                     not-null="false"
	 */
	public String getPluginVersion()
	{
		return pluginVersion;
	}


	public void setPluginVersion(String pluginVersion)
	{
		this.pluginVersion = pluginVersion;
	}


	/**
	 * The maximum length of the url that can be stored in the database.
	 */
	public static final int MAX_DESCRIPTION_LENGTH = 65535;
	private String description;


	/**
	 * Get a description of the plugin.
	 * 
	 * @return A string with a description, or null if not known
	 * @hibernate.property column="`description`" type="text" not-null="false"
	 */
	public String getDescription()
	{
		return description;
	}


	public void setDescription(String description)
	{
		this.description = description;
	}

	/**
	 * The maximum length of the copyright notice that can be stored in the
	 * database.
	 */
	public static final int MAX_COPYRIGHT_LENGTH = 255;
	private String copyright;


	/**
	 * Get a copyright notice for the plugin.
	 * 
	 * @return A string with a copyright notice, or null if not known
	 * @hibernate.property column="`copyright`" type="string" length="255"
	 *                     not-null="false"
	 */
	public String getCopyright()
	{
		return copyright;
	}


	public void setCopyright(String copyright)
	{
		this.copyright = copyright;
	}

	/**
	 * The maximum length of the contact information that can be stored in the
	 * database.
	 */
	public static final int MAX_CONTACT_LENGTH = 255;
	private String contact;


	/**
	 * Get contact information for the plugin.
	 * 
	 * @return A string with contact information, or null if not known
	 * @hibernate.property column="`contact`" type="string" length="255"
	 *                     not-null="false"
	 */
	public String getContact()
	{
		return contact;
	}


	public void setContact(String contact)
	{
		this.contact = contact;
	}

	/**
	 * The maximum length of the email address that can be stored in the
	 * database.
	 */
	public static final int MAX_EMAIL_LENGTH = 255;
	private String email;


	/**
	 * Get an email address that can be used to get more information about the
	 * plugin.
	 * 
	 * @return A string with the email address, or null if not known
	 * @hibernate.property column="`email`" type="string" length="255"
	 *                     not-null="false"
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
	 */
	public static final int MAX_URL_LENGTH = 255;
	private String url;


	/**
	 * Get a URL with more information about the plugin.
	 * 
	 * @return A string containing the URL, or null if not known
	 * @hibernate.property column="`url`" type="string" length="255"
	 *                     not-null="false"
	 */
	public String getUrl()
	{
		return url;
	}


	public void setUrl(String url)
	{
		this.url = url;
	}

	private int mainType;


	/**
	 * Get the main type of plugin.
	 * 
	 * @return A type code that is defined in the enum
	 *         {@link org.proteios.core.plugin.Plugin.MainType}.
	 * @hibernate.property column="`type`" type="int" not-null="true"
	 */
	public int getMainType()
	{
		return mainType;
	}


	public void setMainType(int mainType)
	{
		this.mainType = mainType;
	}

	private boolean interactive;


	/**
	 * If the plugin is interactive or not.
	 * 
	 * @hibernate.property column="`interactive`" type="boolean" not-null="true"
	 */
	public boolean isInteractive()
	{
		return interactive;
	}


	public void setInteractive(boolean interactive)
	{
		this.interactive = interactive;
	}

	private Set<GuiContextData> guiContexts;


	/**
	 * The item code for all items where it makes sense to use the plugin in a
	 * client application. Ie. a plugin that imports reporters should return a
	 * set containing the code for Item.REPORTER.
	 * 
	 * @hibernate.set table="`PluginDefinitionGuiContexts`" lazy="true"
	 * @hibernate.collection-key column="`plugindefinition_id`"
	 * @hibernate.collection-composite-element class="org.proteios.core.data.GuiContextData"
	 */
	public Set<GuiContextData> getGuiContexts()
	{
		if (guiContexts == null)
			guiContexts = new HashSet<GuiContextData>();
		return guiContexts;
	}


	void setGuiContexts(Set<GuiContextData> guiContexts)
	{
		this.guiContexts = guiContexts;
	}

	private Set<PluginTypeData> pluginTypes;


	/**
	 * The plugin types of this plugin, ie. all interfaces that it implements.
	 * 
	 * @hibernate.set table="`PluginDefinitionTypes`" lazy="true"
	 * @hibernate.collection-key column="`plugindefinition_id`"
	 * @hibernate.collection-many-to-many column="`plugintype_id`"
	 *                                    class="org.proteios.core.data.PluginTypeData"
	 */
	public Set<PluginTypeData> getPluginTypes()
	{
		if (pluginTypes == null)
			pluginTypes = new HashSet<PluginTypeData>();
		return pluginTypes;
	}


	void setPluginTypes(Set<PluginTypeData> pluginTypes)
	{
		this.pluginTypes = pluginTypes;
	}
}
