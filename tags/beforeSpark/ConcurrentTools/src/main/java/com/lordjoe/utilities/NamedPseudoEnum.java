package com.lordjoe.utilities;

import java.util.*;

/**
 * com.lordjoe.utilities.NamedPseudoEnum
 *
 * @author Steve Lewis
 * @date Jun 5, 2008
 */
public abstract class NamedPseudoEnum<T extends NamedPseudoEnum> extends PseudoEnum<T> implements  Comparable
{
    public static NamedPseudoEnum[] EMPTY_ARRAY = {};
    public static Class THIS_CLASS = NamedPseudoEnum.class;

    private static Map<PseudoEnum, Map> valuesByNameSubTypeMap = new HashMap<PseudoEnum,Map>();
    private static Map<PseudoEnum,NamedPseudoEnum[]> valueOptionsSubTypeMap = new HashMap<PseudoEnum,NamedPseudoEnum[]>();

    protected static final String NO_SORT_MARKER = "%DoNotSort%";
    protected static final String EMPTY_VALUE_MARKER = "%Blank%";

	protected NamedPseudoEnum( String enumName ) {
		super( enumName );
	}
   /**
     * Clear out static class variables.
     * Used for WCS testing.
     *
     */
    protected void clear() {
    	PseudoEnum keyClass = this.getExemplar();
    	valuesByNameSubTypeMap.put( keyClass, new HashMap<String, NamedPseudoEnum>() );
    	valueOptionsSubTypeMap.put( keyClass, null );
    }
    /**
     * get all the allowed values
     */
    public T[] getValues() {
    	NamedPseudoEnum[] gValues = valueOptionsSubTypeMap.get( this.getExemplar() );
        if ( null == gValues ) {
            throw new IllegalStateException( this.getClass().getName() + " values not initialized" );
        }
        //cast to actual type
        return (T[])gValues;
    }
    /**
     * Set up allowed values.
     * Can only do this once.
     *
     */
    protected void populateValues(String s) {
        if ( null != valueOptionsSubTypeMap.get( this.getExemplar() ) ) {
            throw new IllegalStateException( this.getClass().getName() + " values already initialized" );
        }
        String[] items = s.split(SEPARATOR_CHAR);
        boolean sort = true;
        int startWith = 0;
        int itemsLength = items.length;
        if ( 0 < items.length && NO_SORT_MARKER.equals(items[0]) ) {
        	startWith = 1;
        	sort = false;
        	itemsLength = items.length-1;
        }
        NamedPseudoEnum[] gValues = makeBlankNativeArray(itemsLength);
        for (int i = startWith; i < items.length; i++) {
        	String item = items[i].trim();
        	if (EMPTY_VALUE_MARKER.equals(item)) {
        		item = "";
        	}
        	gValues[i-startWith] = createInstance(item);
        }
        if ( sort ) {
        	Arrays.sort( gValues );
        }
        valueOptionsSubTypeMap.put( this.getExemplar(), gValues );
    }

    public T getValueOf(String name) {
        T aClass = (T)valuesByNameSubTypeMap.get( this.getExemplar() ).get( name );
        if(aClass == null)
            throw new IllegalArgumentException( this.getClass().getName() + " has no value named '" + name + "'" );
        return aClass;
    }

    protected void addName( T key, String value, T instance ) {
    	if ( null == valuesByNameSubTypeMap.get( key ) ) {
    		valuesByNameSubTypeMap.put( key, new HashMap<String, NamedPseudoEnum>() );
    	}
    	Map<String, T> instanceValueByNameMap =  valuesByNameSubTypeMap.get( key );
    	instanceValueByNameMap.put( value, instance );
    }
    
    public abstract T[] makeBlankNativeArray(int size);

    public abstract T createInstance(String value);

}
