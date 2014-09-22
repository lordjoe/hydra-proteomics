/*
	$Id: AnnotatableData.java 3207 2009-04-09 06:48:11Z gregory $

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
	An annotatable item is an item that can be annotated with
	name/value pairs. All annotations are held in an {@link
	AnnotationSetData} object.
	<p>
	This interface defines Hibernate database mapping for the
	<code>annotationSet</code> association to a column with
	the name <code>annotationset_id</code>. If a subclass wants
	to map the association to another column, it should override
	the {@link #getAnnotationSet()} method and add a Hibernate tag 
	in the comment.
	<p>
	
	<b>Reference implementation</b><br>
	<pre class="code">
private AnnotationSetData annotationSet;
public AnnotationSetData getAnnotationSet()
{
   return annotationSet;
}
public void setAnnotationSet(AnnotationSetData annotationSet)
{
   this.annotationSet = annotationSet;
}
</pre>

	@author Nicklas
	@version 2.0
	@see AnnotatedData
	@see AnnotationSetData
	@see AnnotationData
	@see AnnotationTypeData
	@see org.proteios.core.Annotatable
	@see <a href="../../../../../../../development/overview/data/annotations.html">Annotations overview</a>
*/
public interface AnnotatableData
	extends IdentifiableData
{
	/**
		Get the annotation set that holds the annotations for an item.
		@return An <code>AnnotationSetData</code> item or null if no annotations exist
		@hibernate.many-to-one column="`annotationset_id`" not-null="false" unique="true" 
			outer-join="false" cascade="delete"
	*/
	public AnnotationSetData getAnnotationSet();

	/**
		Change the annotation set. Use null to remove the annotations.
	*/
	public void setAnnotationSet(AnnotationSetData annotationSet);
}
