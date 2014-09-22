/*
	$Id: ProteinData.java 3207 2009-04-09 06:48:11Z gregory $

*/
package org.proteios.core.data;

/**
	This represents a Protein (ParentMolecule for of a PolyPeptide

	@author Fredrik
	@version 2.0
	@see org.proteios.core.Protein
	@base.modified $Date: 2009-04-08 23:48:11 -0700 (Wed, 08 Apr 2009) $
	@hibernate.subclass discriminator-value="1"
*/
public class ProteinData
	extends PolyPeptideData
{

	public ProteinData()
	{}

}