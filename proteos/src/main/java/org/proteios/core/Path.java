/*
	$Id: Path.java 3207 2009-04-09 06:48:11Z gregory $

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

import java.util.regex.Pattern;

/**
	This class is used represent the path to a {@link Directory Directory}
	or {@link File File} item. Some examples:

	<ul>
	<li>/filename
	<li>/directory/filename
	<li>/directory/subdirectory/filename
	<li>~userlogin/directory/filename
	<li>/directory
	<li>/directory/subdirectory
	<li>~userlogin
	<li>~userlogin/directory
	</ul>

	@author enell
	@version 2.0
	@base.modified $Date: 2009-04-08 23:48:11 -0700 (Wed, 08 Apr 2009) $

*/
public class Path
{
	/**
		These characters are not valid within a file or directory name:
		{@value}
	*/
	public static final String INVALID_CHARACTERS = "~\\/:;*?<>|";

	/**
		A regexp checking for invalid characters.
	*/
	private static final Pattern invalid = Pattern.compile("[\\~\\\\\\/\\:\\;\\*\\?\\<\\>\\|]");

	/**
		Check if a name is valid as a partial path name, ie. it must not contain any
		of the characters in {@link #INVALID_CHARACTERS}.
		
		@param name The string to be tested.
		@return TRUE if this name is valid.
	*/
	public static boolean isValidName(String name)
	{
		return !invalid.matcher(name).find();
	}

	/**
		The type of the path.
	*/
	private Path.Type type;

	/**
		The userlogin part of the path.
	*/
	private String userlogin;

	/**
		The directory part of the path.
	*/
	private String[] directories;

	/**
		The filename part of the path.
	*/
	private String filename;

	/**
		Create a new <code>Path</code> object by parsing the given string.
		@param path The string representation of a path
		@param type If the path represents a {@link Type#FILE} or
			{@link Type#DIRECTORY}
		@throws InvalidPathException If the path is not valid
	*/
	public Path(String path, Path.Type type)
		throws InvalidPathException
	{
		this.type = type;

		if (!path.startsWith("/") && !path.startsWith("~"))
		{
			throw new InvalidPathException("Path must begin with a / or ~: "+path);
		}
		String[] parts = path.split("/");
		if (path.startsWith("~"))
		{
			userlogin = parts[0].substring(1);
			if (userlogin == null || "".equals(userlogin))
			{
				throw new InvalidPathException("Path must contain a user login: "+path);
			}
			parts[0] = "";
		}
		if (type == Type.FILE)
		{
			if (parts.length < 2)
			{
				throw new InvalidPathException("Path must contain a filename: "+path);
			}
			filename = parts[parts.length-1];
			parts[parts.length-1] = "";
		}
		
		int i = 0;
		for (String part : parts)
		{
			if (!"".equals(part))
			{
				if (!Path.isValidName(part)) 
				{
					throw new InvalidPathException("Path contains invalid characters ("+Path.INVALID_CHARACTERS+"): "+path);
				}
				parts[i] = part;
				i++;
			}
		}
		directories = new String[i];
		System.arraycopy(parts, 0, directories, 0, i);
	}

	/**
		Create a new <code>Path</code> object.
		@param userlogin The userlogin part of the path
		@param directories The directories in the path
		@param filename The filename part of the path
	*/
	Path(String userlogin, String[] directories, String filename)
	{
		this.userlogin = userlogin;
		this.directories = directories;
		this.filename = filename;
		this.type = (filename == null) ? Path.Type.DIRECTORY : Path.Type.FILE;
	}

	/**
		Create a new <code>Path</code> object.
		
		@param directoryPath The directory path
		@param filename The filename part of the path
	*/
	Path(Path directoryPath, String filename)
	{
		this(directoryPath.userlogin, directoryPath.directories, filename);
	}

	/**
		Get the type of the path.
		@return The {@link Path.Type} of the Path
	*/
	public Path.Type getType()
	{
		return type;
	}

	/**
		Get the userlogin part of the path.
		@return The login of the user
	*/
	public String getUserLogin()
	{
		return userlogin;
	}

	/**
		Get the number of directories on the path.
		@return The number of directories
	*/
	public int getDirectoryCount()
	{
		return directories == null ? 0 : directories.length;
	}

	/**
		Get the name of the i:th directory on the path, starting with 0.
		@param i Should be between 0 and {@link #getDirectoryCount() getDirectoryCount-1}
		@return The name of the directory
	*/
	public String getDirectory(int i)
	{
		return directories[i];
	}

	/**
		Get the filename part of the path.
		@return The name of the file
	*/
	public String getFilename()
	{
		return filename;
	}

	/**
		Get the string representation of the path.
		
		@return The string representation of this path.
	*/
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		if (userlogin != null)
		{
			sb.append("~").append(userlogin);
		}
		for (String directory : directories)
		{
			sb.append("/").append(directory);
		}
		if (filename != null && getType() == Type.FILE)
		{
			sb.append("/").append(filename);
		}
		if (sb.length() == 0) sb.append("/"); // For the root directory
		return sb.toString();
	}

	/**
		An enum that describes what type the path is
	 
		@author enell
		@version 2.0
	*/
	public enum Type
	{
		/**
			The path is a path to a {@link File}.
		*/
		FILE, 
		
		/**
			The path is a path to a {@link Directory}.
		*/
		DIRECTORY;
	}
}
