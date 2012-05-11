/*
  $Id: AnnotationSetData.java 3207 2009-04-09 06:48:11Z gregory $

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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
	This class holds information about a set of annotations.
	
	@author Nicklas
	@version 2.0
	@see org.proteios.core.AnnotationSet
	@see <a href="../../../../../../../development/overview/data/annotations.html">Annotations overview</a>
	@hibernate.class table="`AnnotationSets`" lazy="true" batch-size="10"
 */
public class AnnotationSetData
	extends BasicData
{
	public AnnotationSetData()
	{}

	private int itemType;
	/**
		Get the item type this annotation set is associated with.
		@hibernate.property column="`item_type`" type="int" not-null="true" update="false"
	*/
	public int getItemType()
	{
		return itemType;
	}
	public void setItemType(int itemType)
	{
		this.itemType = itemType;
	}

	private Map<AnnotationTypeData, AnnotationData> annotations;
	/**
		The primary annotations in this annotation set. This is the inverse end.
		@see AnnotationData#getAnnotationSet()
		@see AnnotationData#getAnnotationType()
		@hibernate.map lazy="true" cascade="delete" inverse="true"
		@hibernate.collection-key column="`annotationset_id`"
		@hibernate.index-many-to-many column="`annotationtype_id`" class="org.proteios.core.data.AnnotationTypeData"
		@hibernate.collection-one-to-many class="org.proteios.core.data.AnnotationData"
	*/
	public Map<AnnotationTypeData, AnnotationData> getAnnotations()
	{
		if (annotations == null)
		{
			annotations = new HashMap<AnnotationTypeData, AnnotationData>();
		}
		return annotations;
	}
	void setAnnotations(Map<AnnotationTypeData, AnnotationData> annotations)
	{
		this.annotations = annotations;
	}
	
	private Set<AnnotationData> inherited;
	/**
		Annotations that are inherited from other annotation sets.
		@see #getInheritingSets()
		@hibernate.set table="`InheritedAnnotations`" lazy="true"
		@hibernate.collection-key column="`annotationset_id`"
		@hibernate.collection-many-to-many column="`annotation_id`" class="org.proteios.core.data.AnnotationData"
	*/
	public Set<AnnotationData> getInherited()
	{
		if (inherited == null)
		{
			inherited = new HashSet<AnnotationData>();
		}
		return inherited;
	}
	void setInherited(Set<AnnotationData> inherited)
	{
		this.inherited = inherited;
	}
	
	private Set<AnnotationSetData> inheritedSets;
	/**
		Annotation sets from which this set should inherit all annotations.
		@see #getInheritingSets()
		@hibernate.set table="`InheritedAnnotationSets`" lazy="true"
		@hibernate.collection-key column="`annotationset_id`"
		@hibernate.collection-many-to-many column="`inherited_id`" class="org.proteios.core.data.AnnotationSetData"
	*/
	public Set<AnnotationSetData> getInheritedSets()
	{
		if (inheritedSets == null)
		{
			inheritedSets = new HashSet<AnnotationSetData>();
		}
		return inheritedSets;
	}
	void setInheritedSets(Set<AnnotationSetData> inheritedSets)
	{
		this.inheritedSets = inheritedSets;
	}

	private Set<AnnotationSetData> inheritingSets;
	/**
		Annotation sets inherting annotations from this set.
		This is the inverse end.
		@see #getInheritedSets()
		@hibernate.set table="`InheritedAnnotationSets`" lazy="true"
		@hibernate.collection-key column="`inherited_id`"
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
