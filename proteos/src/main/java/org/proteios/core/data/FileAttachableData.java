/*
  $Id: FileAttachableData.java 3207 2009-04-09 06:48:11Z gregory $

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
	A fileattachable item is an item which can have a <code>File</code> 
	attached to it.
	<p>
	<p>
	This interface defines Hibernate database mapping for the
	<code>file</code> property to the database column <code>file_id</code>.
	If a subclass wants to map the property to another column, 
	it should override the {@link #getFile()} method and add a 
	Hibernate tag in the comment.
	<p>
	
	<b>Reference implementation</b><br>
	<pre class="code">
private FileData file;
public FileData getFile()
{
   return file;
}
public void setFile(FileData file)
{
   this.file = file;
}
</pre>

	@author enell
	@version 2.0
	@see BasicData
	@see <a href="../../../../../../../development/overview/data/files.html">File and directory overview</a>
	@see <a href="../../../../../../../development/overview/data/basic.html">Basic classes and interfaces</a>
*/
public interface FileAttachableData
	extends IdentifiableData
{
	/**
		Get the file that is attached to the item.
		@return A {@link FileData} object or null if no file is attached
		@hibernate.many-to-one column="`file_id`" not-null="false" outer-join="false"
	*/
	public FileData getFile();

	/**
		Attach a file to the item. Null is allowed.
	*/
	public void setFile(FileData file);
}
