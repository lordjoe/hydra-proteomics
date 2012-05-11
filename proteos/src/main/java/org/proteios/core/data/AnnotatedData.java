/*
	$Id: AnnotatedData.java 3207 2009-04-09 06:48:11Z gregory $

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
	This class extends the {@link CommonData} class and implements the
	{@link AnnotatableData} interface. Ie. by extending this class a data item 
	becomes annotatable.

	@author Nicklas
	@version 2.0
	@see AnnotatableData
	@see org.proteios.core.AnnotatedItem
	@see <a href="../../../../../../../development/overview/data/annotations.html">Annotation overview</a>
*/
public abstract class AnnotatedData
	extends CommonData
	implements AnnotatableData
{

	public AnnotatedData()
	{}
	
	/*
		From the AnnotatableData interface
		-------------------------------------------
	*/
	private AnnotationSetData annotationSet;
	public AnnotationSetData getAnnotationSet()
	{
		return annotationSet;
	}
	public void setAnnotationSet(AnnotationSetData annotationSet)
	{
		this.annotationSet = annotationSet;
	}
	// -------------------------------------------

}