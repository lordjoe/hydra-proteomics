/*
 $Id: UpdateEventData.java 3207 2009-04-09 06:48:11Z gregory $

 Copyright (C) 2008 Gregory Vincic, Olle Mansson

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
 * This represents an UpdateEventData which is a change of a MeasuredItem.
 * 
 * @author Olle
 * @version 2.0
 * @see org.proteios.core.UpdateEvent
 * @see <a
 *      href="../../../../../../../development/overview/data/updateevent.html">UpdateEvent
 *      overview</a>
 * @proteios.modified $Date: 2009-04-08 23:48:11 -0700 (Wed, 08 Apr 2009) $
 * @hibernate.subclass discriminator-value="5"
 */
public class UpdateEventData
		extends BioMaterialEventData
{
	public UpdateEventData()
	{}

	// -------------------------------------------
}
