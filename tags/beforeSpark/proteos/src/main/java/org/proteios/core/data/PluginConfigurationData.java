/*
	$Id: PluginConfigurationData.java 3207 2009-04-09 06:48:11Z gregory $

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

import java.util.HashMap;
import java.util.Map;

/**
	This data class is used to configure a plugin. A plugin can have many
	configurations.

	@author Nicklas, Samuel
	@version 2.0
	@see org.proteios.core.PluginConfiguration
	@see <a href="../../../../../../../development/overview/data/plugins.html">Plugins</a>
	@hibernate.class table="`PluginConfigurations`" lazy="false"
*/
public class PluginConfigurationData
	extends CommonData
{
	public PluginConfigurationData()
	{}
	
	private PluginDefinitionData pluginDefinition;
	/**
		Get the plugin definition for this configuration.
		@return A <code>PluginDefinition</code> object.
		@hibernate.many-to-one column="`plugindefinition_id`" not-null="true" update="false"
	*/
	public PluginDefinitionData getPluginDefinition()
	{
		return pluginDefinition;
	}
	public void setPluginDefinition(PluginDefinitionData pd)
	{
		this.pluginDefinition = pd;
	}

	private Map<String, ParameterValueData<?>> configurationValues;
	/**
		Used by Hibernate to link with configuration values.
		@hibernate.map table="`PluginConfigurationValues`" lazy="true" cascade="all"
		@hibernate.collection-key column="`pluginconfiguration_id`"
		@hibernate.collection-index column="`name`" type="string" length="255"
		@hibernate.collection-many-to-many column="`value_id`" class="org.proteios.core.data.ParameterValueData"
	*/
	public Map<String, ParameterValueData<?>> getConfigurationValues()
	{
		if (configurationValues == null) 
		{
			configurationValues = new HashMap<String, ParameterValueData<?>>();
		}
		return configurationValues;
	}
	void setConfigurationValues(Map<String, ParameterValueData<?>> configurationValues)
	{
		this.configurationValues = configurationValues;
	}
}
