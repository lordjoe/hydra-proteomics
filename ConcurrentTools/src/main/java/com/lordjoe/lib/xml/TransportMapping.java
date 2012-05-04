
package com.lordjoe.lib.xml;
import java.util.Hashtable;
 
import com.lordjoe.utilities.FileUtilities;
import com.lordjoe.utilities.Util;

/**
 * Class to represent mapping between Token and class name used by transport
 * reads the tokens from a text file set up as a collection of lines token:class
 *  i.e. Transport:com.onvia.lib.xml.Transport
 * Sample use
 *   String test = TransportMapping.tokenToClassName("Transport");
 *       test = TransportMapping.tokenToClassName("Collection");
 *      test = TransportMapping.classNameToToken(test);
 * @author Steve Lewis
 * @since 15 July 1999
 */

public abstract class TransportMapping
{
    protected static Hashtable gTokenToClassName;
    protected static Hashtable gOutClassNameToToken;
    /** Name of the resource file containing the token mappings.  Defaults to ClassTokens.txt */
    protected static String gTokenFile = "ClassTokens.txt";
    
    protected static final String[] DefaultClassTokens = {
  //      "ObjectMetaData:com.onvia.metadata.OCPropertyMetaDataCollection",
  //      "PropertyMetaData:com.onvia.metadata.OCPropertyMetaData",
   //     "NamedOptions:com.onvia.metadata.OCOptionSet",
  //      "NamedItem:com.onvia.metadata.OCOptionItem",
  //      "PropertyBag:com.onvia.data.OCDataBag",
  //      "CatagoryItem:com.lordjoe.dataobject.OCCatagoryItem",
  //      "PropertyValue:com.onvia.property.OCProperty",
   //     "Category:com.lordjoe.dataobject.OCCatagory",
  //      "Product:com.lordjoe.dataobject.OCCatagoryItem"
    };

    /** Name of the resource file containing the token mappings.  Defaults to ClassTokens.txt */
    protected static String gOutTokenFile = "OutClassTokens.txt";
    
    protected static final String[] DefaultOutClassTokens = {
     //   "ObjectMetaData:com.onvia.metadata.OCMetaData",
    //    "PropertyMetaData:com.onvia.metadata.OCPropertyMetaData",
    //    "NamedOptions:com.onvia.metadata.OCOptionSet",
    //    "NamedItem:com.onvia.metadata.OCOptionItem"
    };
    /** Set the name of the token file.  Used to distingush between client and server names. */
    public static void setTokenFile(String file) {
        if(gTokenToClassName != null) {
            throw new IllegalStateException("Must be set before initializing this class");
        }
        gTokenFile = file;
    }
    /** Set the name of the token file.  Used to distingush between client and server names. */
    public static void setOutTokenFile(String file) {
        if(gTokenToClassName != null) {
            throw new IllegalStateException("Must be set before initializing this class");
        }
        gOutTokenFile = file;
    }

    /** Get the name of the token file.  Used to distingush between client and server names. */
    public static String getTokenFile() {
        return gTokenFile;
    }
    /** Get the name of the token file.  Used to distingush between client and server names. */
    public static String getOutTokenFile() {
        return gOutTokenFile;
    }

    protected static synchronized void initTables() 
    {
        if(gTokenToClassName != null)
            return; // already done
            
        gTokenToClassName = new Hashtable();
        String[] maps = null;
        // BIG Hack - cannot get Netscape to reat a resource
        maps = FileUtilities.readInResourceLines(TransportMapping.class, getTokenFile());
        if(maps == null)
            maps = DefaultClassTokens;
        if(maps != null) {
            for(int i = 0; i < maps.length; i++)
                handleOneMap(maps[i]);
        }
        
        gOutClassNameToToken = new Hashtable();
        // BIG Hack - cannot get Netscape to reat a resource
        maps = FileUtilities.readInResourceLines(TransportMapping.class, getOutTokenFile());
        if(maps == null)
            maps = DefaultOutClassTokens;
        if(maps != null) {
            for(int i = 0; i < maps.length; i++)
                handleOutOneMap(maps[i]);
        }
    }

    /**
     * Add an additional mapping to this mapping system.
     * @param tokenName The name to map to.
     * @param objectName The object whos name we are mapping.
     */
    public static void addMapping(String tokenName, String objectName) {
        if(gTokenToClassName == null) {
            initTables();
        }
        gTokenToClassName.put(tokenName, objectName);
    }

    protected static void  handleOneMap(String map) 
    {
        String[] items = Util.parseTokenDelimited(map,':');
        gTokenToClassName.put(items[0],items[1]);
    }
    
    protected static void  handleOutOneMap(String map) 
    {
        String[] items = Util.parseTokenDelimited(map,':');
        gOutClassNameToToken.put(items[1],items[0]);
    }
    
    /**
        convert a token to a class name
        @param non-null token
        @return full class name or null if no mapping
        */
    public static String tokenToClassName(String Token) 
    {
        if(gTokenToClassName == null)
            initTables();
        return((String)gTokenToClassName.get(Token));
    }
    
    /**
        convert a classname to a token
        @param non-null full classname
        @return token or null if no mapping
        */
    public static String classNameToToken(String ClassName) 
    {
        if(gTokenToClassName == null)
            initTables();
        return((String)gOutClassNameToToken.get(ClassName));
    }
}
