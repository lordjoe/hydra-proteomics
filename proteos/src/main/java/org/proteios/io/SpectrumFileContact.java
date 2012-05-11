/*
 $Id: SpectrumFileContact.java 3207 2009-04-09 06:48:11Z gregory $

 Copyright (C) 2009 Gregory Vincic

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

/**
 * This class contains spectrum file contact information.
 * 
 * Some field data may be redundant, since it was
 * designed to transfer as detailed information as
 * possible from mzData and mzML spectrum files,
 * which store contact info in different formats.
 * MzData files use a general "contactInfo" XML tag,
 * where mzML files use "address", "url", and "email"
 * XML tags. MzData files has a specific "institution"
 * XML tag with information that would be located in
 * the "address" XML tag in mzML files.
 * 
 * 
 * @author olle
 */
public class SpectrumFileContact implements SpectrumFileContactInterface
{
	/*
	 * Spectrum Contact Data
	 */
	private String name;
	private String institution;
	private String contactInfo;
	private String address;
	private String url;
	private String email;


	/**
	 * Get the contact name.
	 * 
	 * @return String The contact name.
	 */
	public String getName()
	{
		return this.name;
	}


	/**
	 * Set the contact name.
	 * 
	 * @param name String The contact name to set.
	 */
	public void setName(String name)
	{
		this.name = name;
	}


	/**
	 * Get the contact institution.
	 * 
	 * @return String The contact institution.
	 */
	public String getInstitution()
	{
		return this.institution;
	}


	/**
	 * Set the contact institution.
	 * 
	 * @param institution String The contact institution to set.
	 */
	public void setInstitution(String institution)
	{
		this.institution = institution;
	}


	/**
	 * Get the contact info.
	 * 
	 * @return String The contact info.
	 */
	public String getContactInfo()
	{
		return this.contactInfo;
	}


	/**
	 * Set the contact info.
	 * 
	 * @param contactInfo String The contact info to set.
	 */
	public void setContactInfo(String contactInfo)
	{
		this.contactInfo = contactInfo;
	}


	/**
	 * Get the contact address.
	 * 
	 * @return String The contact address.
	 */
	public String getAddress()
	{
		return this.address;
	}


	/**
	 * Set the contact address.
	 * 
	 * @param address String The contact address to set.
	 */
	public void setAddress(String address)
	{
		this.address = address;
	}


	/**
	 * Get the contact url.
	 * 
	 * @return String The contact url.
	 */
	public String getUrl()
	{
		return this.url;
	}


	/**
	 * Set the contact url.
	 * 
	 * @param url String The contact url to set.
	 */
	public void setUrl(String url)
	{
		this.url = url;
	}

	
	/**
	 * Get the contact email.
	 * 
	 * @return String The contact email.
	 */
	public String getEmail()
	{
		return this.email;
	}


	/**
	 * Set the contact email.
	 * 
	 * @param email String The contact email to set.
	 */
	public void setEmail(String email)
	{
		this.email = email;
	}
}
