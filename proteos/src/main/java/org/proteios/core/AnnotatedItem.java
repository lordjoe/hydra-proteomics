/*
 $Id: AnnotatedItem.java 3207 2009-04-09 06:48:11Z gregory $

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

import org.proteios.core.data.AnnotatedData;

/**
 * This is a helper class for items that need to implement the
 * {@link Annotatable} interface. Most of those items should be able to extend
 * this class.
 * 
 * @author Nicklas
 * @version 2.0
 * @see Annotatable
 * @base.modified $Date: 2009-04-08 23:48:11 -0700 (Wed, 08 Apr 2009) $
 */
public abstract class AnnotatedItem<D extends AnnotatedData>
		extends CommonItem<D>
		implements Annotatable
{
	AnnotatedItem(D annotatedData)
	{
		super(annotatedData);
	}
}
