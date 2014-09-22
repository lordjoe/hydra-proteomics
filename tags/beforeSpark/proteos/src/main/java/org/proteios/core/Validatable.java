/*
	$Id: Validatable.java 3207 2009-04-09 06:48:11Z gregory $

	Copyright (C) 2006 Gregory Vincic, Jari Hakkinen, Olle Mansson
	Copyright (C) 2007 Gregory Vincic

	This file is part of Proteios.
	Available at http://www.proteios.org/

	Proteios is free software; you can redistribute it and/or modify it
	under the terms of the GNU General Public License as published by
	the Free Software Foundation; either version 2 of the License, or
	(at your option) any later version.

	Proteios is distributed in the hope that it will be useful, but
	WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FI	BioArray Software Environment (Proteios) - http://base.thep.lu.se/
	Copyright (C) 2002-2004 Lao Saal, Carl Troein,
	Johan Vallon-Christersson, Jari H�kkinen, Nicklas Nordborg

	This file is part of Proteios.

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
TNESS FOR A PARTICULAR PURPOSE.  See the GNU
	General Public License for more details.

	You should have received a copy of the GNU General Public License
	along with this program; if not, write to the Free Software
	Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
	02111-1307, USA.
*/
package org.proteios.core;

/**
	This interface is a tagging interface for items that needs case 2
	validation. See <a 
	href="../../../../../../development/overview/core/datavalidation,html">Core API overview - 
	Data validation</a> and <a 
	href="../../../../../../development/coding/item/index.html#validation">Coding rules and 
	guidelines for item classes</a>
	<p>
	By implementing this interface an item tells the <code>DbControl</code>
	to call the {@link BasicItem#validate()} whenever commit is called.
	<p>

	@author Nicklas
	@version 2.0
	@base.modified $Date: 2009-04-08 23:48:11 -0700 (Wed, 08 Apr 2009) $
*/
public interface Validatable
	extends Controlled
{}
