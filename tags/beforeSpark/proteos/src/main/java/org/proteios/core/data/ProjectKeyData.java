/*
	$Id: ProjectKeyData.java 3207 2009-04-09 06:48:11Z gregory $

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
	This class holds information access privileges for projects.

	@author Nicklas
	@version 2.0
	@see org.proteios.core.ProjectKey
	@see <a href="../../../../../../../development/overview/data/authentication.html">Authentication overview</a>
	@base.modified $Date: 2009-04-08 23:48:11 -0700 (Wed, 08 Apr 2009) $
	@hibernate.subclass discriminator-value="2"
*/
public class ProjectKeyData
	extends KeyData
{

	public ProjectKeyData()
	{}

	private Map<ProjectData,Integer> projects;
	/**
		Get the map that manages which projects that have permissions 
		for this key.

		@hibernate.map table="`ProjectKeys`" lazy="true"
		@hibernate.collection-key column="`key_id`"
		@hibernate.index-many-to-many column="`project_id`" class="org.proteios.core.data.ProjectData"
		@hibernate.collection-element column="`permission`" type="int" not-null="true"
	*/
	public Map<ProjectData,Integer> getProjects()
	{
		if (projects == null) projects = new HashMap<ProjectData,Integer>();
		return projects;
	}
	void setProjects(Map<ProjectData,Integer> projects)
	{
		this.projects = projects;
	}
}
