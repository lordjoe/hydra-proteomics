/*
  $Id: AnnotationData.java 3207 2009-04-09 06:48:11Z gregory $

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

import java.util.Set;

/**
	This class holds information about an annotation
	
	@author Nicklas
	@version 2.0
	@see org.proteios.core.Annotation
	@see <a href="../../../../../../../development/overview/data/annotations.html">Annotations overview</a>
	@hibernate.class table="`Annotations`" lazy="false"
 */
public class AnnotationData
	extends BasicData
{
	public AnnotationData()
	{}

	private AnnotationSetData annotationSet;
	/**
		Get the annotation set this annotation primarily belongs to.
		@hibernate.many-to-one outer-join="false" update="false"
		@hibernate.column name="`annotationset_id`" not-null="true" unique-key="uniquetype"
	*/
	public AnnotationSetData getAnnotationSet()
	{
		return annotationSet;
	}
	public void setAnnotationSet(AnnotationSetData annotationSet)
	{
		this.annotationSet = annotationSet;
	}

	private AnnotationTypeData annotationType;
	/**
		Get the type of this annotation.
		@hibernate.many-to-one outer-join="false" update="false"
		@hibernate.column name="`annotationtype_id`" not-null="true" unique-key="uniquetype"
	*/
	public AnnotationTypeData getAnnotationType()
	{
		return annotationType;
	}
	public void setAnnotationType(AnnotationTypeData annotationType)
	{
		this.annotationType = annotationType;
	}
	
	private ParameterValueData<?> values;
	/**
		The values of this annotation.
		@hibernate.many-to-one column="`value_id`" not-null="true" update="false" cascade="all" unique="true"
	*/
	public ParameterValueData<?> getValues()
	{
		return values;
	}
	public void setValues(ParameterValueData<?> values)
	{
		this.values = values;
	}
	
	private Set<AnnotationSetData> inheritingSets;
	/**
		The annotation sets which inherit this annotation. This is the inverse end.
		@see AnnotationSetData#getInherited()
		@hibernate.set table="`InheritedAnnotations`" lazy="true"
		@hibernate.collection-key column="`annotation_id`"
		@hibernate.collection-many-to-many column="`annotationset_id`" class="org.proteios.core.data.AnnotationSetData"
	*/
	Set<AnnotationSetData> getInheritingSets()
	{
		return inheritingSets;
	}
	void setInheritingSets(Set<AnnotationSetData> inheritingSets)
	{
		this.inheritingSets = inheritingSets;
	}


}
