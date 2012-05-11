/*
	$Id: PluginTypeData.java 3207 2009-04-09 06:48:11Z gregory $

	Copyright (C) 2006 Gregory Vincic, Jari Hakkinen, Olle Mansson
	Copyright (C) 2007 Gregory Vincic

	This file is part of Proteios.
	Available at http://www.proteios.org/

	Proteios is free software; you can redistribute it and/or
	modify it under the terms of the GNU General Public License
	as published by the Free Software Foundation; either version 2
	of the License, or (at your option) any later version.

	Proteios is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.

	You should have received a copy of the GNU General Public License
	along with this program; if not, write to the Free Software
	Foundation, Inc., 59 Temple Place - Suite 330,
	Boston, MA  02111-1307, USA.
*/
package org.proteios.core.data;

import java.util.Set;

/**
	This class represent an interface that a plugin can implement.

	@author Nicklas
	@version 2.0
	@see org.proteios.core.PluginType
	@see <a href="../../../../../../../development/overview/data/plugins.html">Plugins</a>
	@hibernate.class table="`PluginTypes`" lazy="false"
	@base.modified $Date: 2009-04-08 23:48:11 -0700 (Wed, 08 Apr 2009) $
*/
public class PluginTypeData
	extends BasicData
	implements NameableData, RemovableData
{

	public PluginTypeData()
	{}

	/*
		From the NameableData interface
		-------------------------------------------
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
		From the RemovableData interface
		-------------------------------------------
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
		The maximum length of the interface name that can be stored in the
		database.
	*/
	public static final int MAX_INTERFACENAME_LENGTH = 255;
	private String interfaceName;
	/**
		Get the interface name a plugin must implement to be of this type.
		The specified interface must extend the {@link org.proteios.core.plugin.Plugin}
		interface.
		@hibernate.property column="`interface_name`" type="string" length="255" not-null="true" unique="true"
	*/
	public String getInterfaceName()
	{
		return interfaceName;
	}
	public void setInterfaceName(String interfaceName)
	{
		this.interfaceName = interfaceName;
	}

	public static final int MAX_JARPATH_LENGTH = 65535;
	private String jarPath;
	/**
		Get the path to the JAR file where the interface class is located. If the
		value is null the interface must be in the classpath.
		@hibernate.property column="`jar_path`" type="text" not-null="false"
	*/
	public String getJarPath()
	{
		return jarPath;
	}
	public void setJarPath(String jarPath)
	{
		this.jarPath = jarPath;
	}
	
	private Set<PluginDefinitionData> plugins;
	/**
		The plugin implementing this interface. This is the inverse end.
		@see PluginDefinitionData#getPluginTypes()
		@hibernate.set table="`PluginDefinitionTypes`" lazy="true"
		@hibernate.collection-key column="`plugintype_id`"
		@hibernate.collection-many-to-many column="`plugindefinition_id`" class="org.proteios.core.data.PluginDefinitionData"
	*/
	Set<PluginDefinitionData> getPlugins()
	{
		return plugins;
	}
	void setPlugins(Set<PluginDefinitionData> plugins)
	{
		this.plugins = plugins;
	}
}
