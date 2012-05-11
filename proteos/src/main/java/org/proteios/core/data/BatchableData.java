/*
	$Id: BatchableData.java 3207 2009-04-09 06:48:11Z gregory $

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
	This is a tagging interface only.
	Data classes that want to have batch functionality must
	implement this interface, plus that a subclass of the 
	abstract <code>BasicBatcher</code> class in the core must be created.
	
	@author Samuel
	@version 2.0
	@see org.proteios.core.BasicBatcher
	@see <a href="../../../../../../../development/overview/data/basic.html">Basic classes and interfaces</a>
	@see <a href="../../../../../../../development/overview/core/index.html#batch">Batch processing</a>
*/
public interface BatchableData
	extends IdentifiableData
{}
