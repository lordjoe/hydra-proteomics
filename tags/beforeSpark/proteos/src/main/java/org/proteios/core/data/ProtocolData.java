/*
  $Id: ProtocolData.java 3207 2009-04-09 06:48:11Z gregory $

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

/**
	@author enell
	@version 2.0
	@see org.proteios.core.Protocol
	@see <a href="../../../../../../../development/overview/data/protocols.html">Protocol overview</a>
	@hibernate.class table="`Protocols`" lazy="true"
*/
public class ProtocolData
	extends CommonData
	implements FileAttachableData
{

	/*
		From the FileAttachableData interface
		-------------------------------------------
	*/
	private FileData file;
	public FileData getFile()
	{
		return file;
	}

	public void setFile(FileData file)
	{
		this.file = file;
	}
	// -------------------------------------------

	private ProtocolTypeData protocolType;
	/**
		Get the associated {@link ProtocolTypeData} item.
		@hibernate.many-to-one column="`protocoltype_id`" not-null="true" outer-join="false"
	*/
	public ProtocolTypeData getProtocolType()
	{
		return protocolType;
	}
	public void setProtocolType(ProtocolTypeData protocolType)
	{
		this.protocolType = protocolType;
	}
	
}
