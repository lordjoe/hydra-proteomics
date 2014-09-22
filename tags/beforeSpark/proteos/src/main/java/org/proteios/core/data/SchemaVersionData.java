/*
 $Id: SchemaVersionData.java 3207 2009-04-09 06:48:11Z gregory $

 Copyright (C) 2007 Gregory Vincic, Olle Mansson

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

/**
 * This class holds information about the current schema version of the
 * database. It contains only one record. All modifications to the database
 * which requires some update script action on the data bumps the version number
 * with one.
 * 
 * @author Nicklas
 * @version 2.0
 * @base.modified $Date$
 * @hibernate.class table="`SchemaVersion`" lazy="false"
 */
public class SchemaVersionData
		extends BasicData
{
	public SchemaVersionData()
	{}

	private int schemaVersion;


	/**
	 * Get the schema version.
	 * 
	 * @hibernate.property column="`schema_version`" type="int" not-null="true"
	 * @return int current schema version
	 */
	public int getSchemaVersion()
	{
		return this.schemaVersion;
	}


	/**
	 * Set the schema version.
	 * 
	 * @param schemaVersion int the schema version value to set.
	 */
	public void setSchemaVersion(int schemaVersion)
	{
		this.schemaVersion = schemaVersion;
	}

	private int build;


	/**
	 * Get the build number.
	 * 
	 * @hibernate.property column="`build`" type="int" not-null="true"
	 * @return int the current build number.
	 */
	public int getBuild()
	{
		return this.build;
	}


	/**
	 * Set the build number.
	 * 
	 * @param build int the build number to set.
	 */
	public void setBuild(int build)
	{
		this.build = build;
	}
}
