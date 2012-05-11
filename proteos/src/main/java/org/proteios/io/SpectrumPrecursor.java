/*
 $Id: SpectrumPrecursor.java 3277 2009-05-18 14:43:48Z olle $

 Copyright (C) 2008 Fredrik Levander

 Files are copyright by their respective authors. The contributions to
 files where copyright is not explicitly stated can be traced with the
 source code revision system.

 This file is part of Proteios.
 Available at http://www.proteios.org/

 Proteios-2.x is free software; you can redistribute it and/or
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
 */
package org.proteios.io;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class contains spectrum precursor information. Comparable to the
 * org.proteios.core.Precursor class, but not for database storage
 * 
 * @author fredrik
 */
public class SpectrumPrecursor
{
    /**
     * all extension of SpectrumPrecursor
      */
    private static Class<? extends SpectrumPrecursor> gSpectrumPrecursorClass;

    public static Class<? extends SpectrumPrecursor> getSpectrumPrecursorClass() {
        return gSpectrumPrecursorClass;
    }

    public static void setSpectrumPrecursorClass(final Class<? extends SpectrumPrecursor> pSpectrumPrecursorClass) {
        gSpectrumPrecursorClass = pSpectrumPrecursorClass;
    }

    public static SpectrumPrecursor buildSpectrumPrecursor()
    {
        if(gSpectrumPrecursorClass == null)
            return new SpectrumPrecursor();
        try {
            return (SpectrumPrecursor)gSpectrumPrecursorClass.newInstance();
        }
        catch (InstantiationException e) {
            throw new RuntimeException(e);

        }
        catch (IllegalAccessException e) {
            throw new RuntimeException(e);

        }
    }
	/*
	 * Spectrum Data
	 */
	private Double MassToChargeRatio;
	private Double Intensity;
	private Integer Charge;
	private List<PrecursorSelectedIon> selectedIonList = null;
	private List<StringPairInterface> extraDataList = null;
	private FragmentationType fragmentationType = null;

    /**
     * added slewis to allow tracking construction
     */
    protected SpectrumPrecursor() {
    }

    /**
	 * @return Returns the massToChargeRatio.
	 */
	public Double getMassToChargeRatio()
	{
		return MassToChargeRatio;
	}


	/**
	 * @param massToChargeRatio The massToChargeRatio to set.
	 */
	public void setMassToChargeRatio(Double massToChargeRatio)
	{
		MassToChargeRatio = massToChargeRatio;
        PrecursorSelectedIon ion = fetchLastSelectedIon();
        ion.setMassToChargeRatio(massToChargeRatio);
	}


	/**
	 * @return Returns the intensity.
	 */
	public Double getIntensity()
	{
		return Intensity;
	}


	/**
	 * @param intensity The intensity to set.
	 */
	public void setIntensity(Double intensity)
	{
		Intensity = intensity;
		fetchLastSelectedIon().setIntensity(intensity);
	}


	/**
	 * @return Returns the charge.
	 */
	public Integer getCharge()
	{
		return Charge;
	}


	/**
	 * @param charge The charge to set.
	 */
	public void setCharge(Integer charge)
	{
		Charge = charge;
		fetchLastSelectedIon().setCharge(charge);
	}


	/**
	 * Get the spectrum precursor selected ion list.
	 * 
	 * @return List<PrecursorSelectedIon> The spectrum precursor selected ion list.
	 */
	public List<PrecursorSelectedIon> getSelectedIonList()
	{
		if (this.selectedIonList == null)
		{
			this.selectedIonList = new ArrayList<PrecursorSelectedIon>();
		}
		return this.selectedIonList;
	}


	/**
	 * Create new selected ion for the current precursor.
	 */
	public void newSelectedIon()
	{
		getSelectedIonList().add(new PrecursorSelectedIon());
	}


	/**
	 * Fetch the last selected ion for the current precursor.
	 * If there is none, one is generated.
	 * 
	 * @return PrecursorSelectedIon The last selected ion.
	 */
	private PrecursorSelectedIon fetchLastSelectedIon()
	{
		PrecursorSelectedIon selectedIon = null;
		List<PrecursorSelectedIon> selectedIons = getSelectedIonList();
		if (selectedIons.isEmpty())
		{
			getSelectedIonList().add(new PrecursorSelectedIon());
		}
		int nSelectedIons = getSelectedIonList().size();
		selectedIon = getSelectedIonList().get(nSelectedIons - 1);
		return selectedIon;
	}


	/**
	 * Get the spectrum precursor extra data list.
	 * 
	 * @return List<StringPairInterface> The spectrum precursor extra data list.
	 */
	public List<StringPairInterface> getExtraDataList()
	{
		return this.extraDataList;
	}


	/**
	 * Add spectrum precursor extra data.
	 * 
	 * @param extraData StringPairInterface The spectrum precursor extra data to add.
	 */
	public void addExtraData(StringPairInterface extraData)
	{
		if (this.extraDataList == null)
		{
			this.extraDataList = new ArrayList<StringPairInterface>();
		}
		this.extraDataList.add(extraData);
	}


	/**
	 * Get the fragmentation type.
	 * 
	 * @return FragmentationType The fragmentation type.
	 */
	public FragmentationType getFragmentationType()
	{
		return this.fragmentationType;
	}


	/**
	 * Set the fragmentation type.
	 * 
	 * @param fragmentationType FragmentationType The fragmentation type to set.
	 */
	public void setFragmentationType(FragmentationType fragmentationType)
	{
		this.fragmentationType = fragmentationType;
	}


	/**
	 * Precursor fragmentation type enum.
	 */
	public enum FragmentationType
	{
		/**
		 * Unknown fragmentation type.
		 */
		UNKNOWN(0, "", "unknown", ""),
		/**
		 * Collision-Induced Dissociation.
		 */
		CID(1, "CID", "collision-induced dissociation", "MS:1000133"),
		/**
		 * Electron Transfer Dissociation.
		 */
		ETD(2, "ETD", "electron transfer dissociation", "MS:1000598"),
		/**
		 * High-energy Collision-induced Dissociation.
		 */
		HCD(3, "HCD", "high-energy collision-induced dissociation", "MS:1000422"),
		/**
		 * Electron Capture Dissociation.
		 */
		ECD(4, "ECD", "electron capture dissociation", "MS:1000250"),
		/**
		 * Pulsed-Q Dissociation.
		 */
		PQD(5, "PQD", "pulsed-q dissociation", "MS:1000599"),
		/**
		 * Post-Source Decay.
		 */
		PSD(6, "PSD", "post-source decay", "MS:1000135");


		/**
		 * Maps the integer value to a fragmentation type.
		 */
		private static final Map<Integer, FragmentationType> valueMapping = new HashMap<Integer, FragmentationType>();
		static
		{
			for (FragmentationType ft : FragmentationType.values())
			{
				FragmentationType f = valueMapping.put(ft.getValue(), ft);
				assert f == null : "Another SpectrumPrecursor.FragmentationType with the value " + ft
					.getValue() + " already exists";
			}
		}


		/**
		 * Maps the abbreviation string to a fragmentation type.
		 */
		private static final Map<String, FragmentationType> abbreviationMapping = new HashMap<String, FragmentationType>();
		static
		{
			for (FragmentationType ft : FragmentationType.values())
			{
				FragmentationType f = abbreviationMapping.put(ft.getAbbreviation(), ft);
				assert f == null : "Another SpectrumPrecursor.FragmentationType with the abbreviation " + ft
					.getAbbreviation() + " already exists";
			}
		}


		/**
		 * Maps the displayValue string to a fragmentation type.
		 */
		private static final Map<String, FragmentationType> displayValueMapping = new HashMap<String, FragmentationType>();
		static
		{
			for (FragmentationType ft : FragmentationType.values())
			{
				FragmentationType f = displayValueMapping.put(ft.getDisplayValue(), ft);
				assert f == null : "Another SpectrumPrecursor.FragmentationType with the displayValue " + ft
					.getDisplayValue() + " already exists";
			}
		}


		/**
		 * Maps the msOntologyAccessionNumber string to a fragmentation type.
		 */
		private static final Map<String, FragmentationType> msOntologyAccessionNumberMapping = new HashMap<String, FragmentationType>();
		static
		{
			for (FragmentationType ft : FragmentationType.values())
			{
				FragmentationType f = msOntologyAccessionNumberMapping.put(ft.getMsOntologyAccessionNumber(), ft);
				assert f == null : "Another SpectrumPrecursor.FragmentationType with the msOntologyAccessionNumber " + ft
					.getMsOntologyAccessionNumber() + " already exists";
			}
		}


		/**
		 * Get the <code>SpectrumPrecursor.FragmentationType</code> object when you know the integer
		 * value.
		 * 
		 * @param value int The integer value of the fragmentation type.
		 * @return FragmentationType The fragmentation type object or null if the value is unknown
		 */
		public static FragmentationType fromValue(int value)
		{
			FragmentationType ft = valueMapping.get(value);
			assert ft != null : "FragmentationType == null for value " + value;
			return ft;
		}

		/**
		 * Get the <code>SpectrumPrecursor.FragmentationType</code> object
		 * when you know the abbreviation string.
		 * 
		 * @param abbreviation String The abbreviation string of the fragmentation type.
		 * @return FragmentationType The fragmentation type object or null if the abbreviation is unknown
		 */
		public static FragmentationType fromAbbreviation(String abbreviation)
		{
			FragmentationType ft = abbreviationMapping.get(abbreviation);
			assert ft != null : "FragmentationType == null for abbreviation " + abbreviation;
			return ft;
		}

		/**
		 * Get the <code>SpectrumPrecursor.FragmentationType</code> object
		 * when you know the displayValue string.
		 * 
		 * @param displayValue String The displayValue string of the fragmentation type.
		 * @return FragmentationType The fragmentation type object or null if the displayValue is unknown
		 */
		public static FragmentationType fromDisplayValue(String displayValue)
		{
			FragmentationType ft = displayValueMapping.get(displayValue);
			assert ft != null : "FragmentationType == null for displayValue " + displayValue;
			return ft;
		}

		/**
		 * Get the <code>SpectrumPrecursor.FragmentationType</code> object
		 * when you know the msOntologyAccessionNumber string.
		 * 
		 * @param msOntologyAccessionNumber String The msOntologyAccessionNumber string of the fragmentation type.
		 * @return FragmentationType The fragmentation type object or null if the msOntologyAccessionNumber is unknown
		 */
		public static FragmentationType fromMsOntologyAccessionNumber(String msOntologyAccessionNumber)
		{
			FragmentationType ft = msOntologyAccessionNumberMapping.get(msOntologyAccessionNumber);
			assert ft != null : "FragmentationType == null for msOntologyAccessionNumber " + msOntologyAccessionNumber;
			return ft;
		}

		private final int value;
		private final String abbreviation;
		private final String displayValue;
		private final String msOntologyAccessionNumber;


		private FragmentationType(int value, String abbreviation, String displayValue, String msOntologyAccessionNumber)
		{
			this.value = value;
			this.abbreviation = abbreviation;
			this.displayValue = displayValue;
			this.msOntologyAccessionNumber = msOntologyAccessionNumber;
		}


		@Override
		public String toString()
		{
			return displayValue;
		}


		/**
		 * Get the integer value for a
		 * <code>SpectrumPrecursor.FragmentationType</code>.
		 * 
		 * @return int The integer value of this fragmentation type
		 */
		public int getValue()
		{
			return value;
		}


		/**
		 * Get the abbreviation for a
		 * <code>SpectrumPrecursor.FragmentationType</code>.
		 * 
		 * @return String The abbreviation string of this fragmentation type
		 */
		public String getAbbreviation()
		{
			return abbreviation;
		}


		/**
		 * Get the displayValue for a
		 * <code>SpectrumPrecursor.FragmentationType</code>.
		 * 
		 * @return String The displayValue string of this fragmentation type
		 */
		public String getDisplayValue()
		{
			return displayValue;
		}

	
		/**
		 * Get the msOntologyAccessionNumber for a
		 * <code>SpectrumPrecursor.FragmentationType</code>.
		 * 
		 * @return String The msOntologyAccessionNumber string of this fragmentation type
		 */
		public String getMsOntologyAccessionNumber()
		{
			return msOntologyAccessionNumber;
		}
	}
}
