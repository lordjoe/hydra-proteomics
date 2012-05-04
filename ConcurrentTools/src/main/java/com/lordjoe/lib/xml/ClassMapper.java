/*--- formatted by Jindent 2.1, (www.c-lab.de/~jindent) ---*/

package com.lordjoe.lib.xml;

import com.lordjoe.lib.xml.*;
import java.util.*;
import com.lordjoe.utilities.*;


/**
 * com.lordjoe.lib.xml.ClassMapper
 * This holds a two way map between keys and values
 * It can also be used as a one Way map
 * 
 */
public class ClassMapper  implements  ITagHandler {
	public static final String TAG_TO_FILE_NAME = "TagToFile.xml";
	protected static Map	   gNameToMapper;
	private Map				   m_KeyToValue;
	private Map				   m_ValueToKey;
	private String			   m_Name;

	/**
	 * look up a mapper
	 * @param in non-null name
	 * @return possibly null mapper
	 */
	public static synchronized ClassMapper getMapper(String in) {
		String TestName = in.toUpperCase();
                initMapper();

		ClassMapper ret = (ClassMapper) gNameToMapper.get(TestName);

		if (ret != null) {
			return (ret);
		} 

		String FileName = tagToFile(TestName);

		ret = buildFromFile(FileName);		     
		
		gNameToMapper.put(TestName, ret);

		return (ret);
	} 
	
	protected static synchronized void initMapper()
	{
	    
	    if (gNameToMapper == null) {

			// first call
			XMLSerializer.registerTag("Mapping", 
									  com.lordjoe.lib.xml.ClassMapper.class);
			XMLSerializer.registerTag("ClassMapping", 
									  com.lordjoe.lib.xml.ClassMapper.class);

			gNameToMapper = new HashMap();
		} 
    }

	/**
	 * This maps a tag into a known string
	 * @param TestTag non-null tag
	 * @return non-null string
	 */
	public static String tagToFile(String TestTag) {
		ClassMapper TagsToFiles = (ClassMapper) gNameToMapper.get("TagToFile");

		if (TagsToFiles == null) {
			TagsToFiles = buildTagsToFiles();
		} 

		String ret = TagsToFiles.getValueString(TestTag);

		if (ret != null) {
			return (ret);
		} 

		String FileName = TagsToFiles.getValueString(TestTag);

		if (FileName != null) {
			return (FileName);
		} 

		return (TagsToFiles.getValueString(TestTag.toUpperCase()));
	} 

	/**
	 * Method <Add Comment Here>
	 * @return
	 * @see
	 */
	protected static ClassMapper buildTagsToFiles() {
		ClassMapper ret = null;

		try {
		    ret = buildFromFile(TAG_TO_FILE_NAME);		     
		} catch (Throwable t) {
		    throw new IllegalStateException("Could not build ClassMapper from " + TAG_TO_FILE_NAME +
						   " -- " + t.getMessage());
		} 		

		gNameToMapper.put("TagToFile", ret);

		return (ret);
	} 

	/**
	 * Constructor 
	 * @see
	 */
	public ClassMapper() {
		m_KeyToValue = new HashMap();
		m_ValueToKey = new HashMap();
	}

	/**
	 * Method <Add Comment Here>
	 * @return
	 * @see
	 */
	public String getName() {
		return (m_Name);
	} 

	/**
	 * Method <Add Comment Here>
	*
	 * @param name
	 * @see
	 */
	public void setName(String name) {
		if (m_Name != null) {
			if (name.equals(m_Name)) {
				return;
			} 

			gNameToMapper.remove(m_Name.toUpperCase());
		} 

		m_Name = name;

		gNameToMapper.put(m_Name.toUpperCase(), this);
	} 

	/**
	 * Method <Add Comment Here>
	*
	 * @param FileName
	 * @return
	 * @see
	 */
	public static ClassMapper buildFromFile(String FileName) {
	    initMapper();
		try {
			Object   ret1 = XMLSerializer.parseFile(FileName);
			ClassMapper ret = (ClassMapper) ret1;

			return (ret);
		} 
		catch (Exception ex) {
		    // how about this instead ... MWR
		    if (ex instanceof XMLSerializerException) {
			throw (XMLSerializerException)ex;
		    }	
		    //ex.printStackTrace()	
		    //throw new IllegalArgumentException(ex.toString());
		   throw new IllegalStateException(ex.toString());
		} 
	} 

	/*
	 * Add key value pairs
	 * @param Key non-null key
	 * @param Value non-null Value
	 */

	/**
	 * Method <Add Comment Here>
	*
	 * @param Key
	 * @param Value
	 * @see
	 */
	public void addKeyValue(Object Key, Object Value) {
		Object OldValue = m_KeyToValue.get(Key);

		m_KeyToValue.put(Key, Value);

		if (OldValue != null) {
			m_ValueToKey.remove(OldValue);
		} 

		m_ValueToKey.put(Value, Key);
	} 

	/**
	 * return the object corresponding to a key
	 * @param Key non-null Key
	 * @return possibly null Value
	 */
	public Object getValue(Object Key) {
		return (m_KeyToValue.get(Key));
	} 

	/**
	 * return the object corresponding to a key
	 * @param Key non-null Key
	 * @return possibly null String
	 */
	public String getValueString(Object Key) {
		Object item = getValue(Key);

		if (item != null) {
			return (item.toString());
		} 

		return (null);
	} 

	/**
	 * return a Set of all keys
	 * @return possibly null Set
	 */
	public Set getKeys() {
		return (m_KeyToValue.keySet());
	} 

	/**
	 * return the Key corresponding to a Value
	 * @param Value non-null Value
	 * @return possibly null Key
	 */
	public Object getKey(Object Value) {
		return (m_ValueToKey.get(Value));
	} 

	/**
	 * This returns the object which will handle the tag - the handler
	 * may return itself or create a sub-object to manage the tag
	 * @param TagName non-null name of the tag
	 * @param attributes non-null array of name-value pairs
	 * @return possibly null handler
	 */
	public Object handleTag(String TagName, NameValue[] attributes) {
		Object ret = null;

		if (TagName.equals("NameValue")) {
			Object Name = null;
			Object Value = null;

			for (int i = 0; i < attributes.length; i++) {
				if (attributes[i].m_Name.equals("name")) {
					Name = attributes[i].m_Value;
				} 

				if (attributes[i].m_Name.equals("value")) {
					Value = attributes[i].m_Value;
				} 
			} 

			if (Name == null || Value == null) {
				throw new IllegalArgumentException("Poorly formed NameValue Tag");
			} 

			addKeyValue(Name, Value);

			return (null);	  // no further processing
		} 

		return (ret);
	} 
	
	/**
	* Assume that names are tags and values are the handling classes
	* now register the classes with XMLSerializer
	* @throws AssertionFailureException if class not available
	*/
	public void registerTagClasses()
	{
	    String KeyName;
	    Object Key;
	    Class  RegisteredClass;
	    Iterator it = getKeys().iterator();
	    while(it.hasNext()) {
	        Key = it.next();
	        Object ClassName = getValue(Key);
	        try {
	            RegisteredClass = Class.forName(ClassName.toString());
	            XMLSerializer.registerTag(Key.toString(), RegisteredClass);
	        }
	        catch(ClassNotFoundException ex) {
	            throw new RuntimeException(ex);
	        }
	    }
	}


}



/*--- formatting done in "Onvia Java Convention" style on 05-19-2000 ---*/

