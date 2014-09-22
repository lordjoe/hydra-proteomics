package com.lordjoe.dataapi;

/**
 * com.lordjoe.dataapi.IColumn
 * User: Steve
 * Date: 4/11/12
 */
public interface IColumn {
    public static final IColumn[] EMPTY_ARRAY = {};

    /**
     * uinique column name or if not "col" + index
     * @return  !null name
     */
    public String getName();

    /**
     * 1 based index in the table
     * @return  as above
     */
    public Integer getIndex();

}
