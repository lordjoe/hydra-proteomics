package com.lordjoe.dataapi;

/**
 * com.lordjoe.dataapi.IDataCell
 * User: Steve
 * Date: 4/11/12
 */
public interface IDataCell {
    public static final IDataCell[] EMPTY_ARRAY = {};

    /**
     * row 1 based first data row is 2
     * @return row
     */
    public int getRow();

    /**
     * row 1 based first data col is 1
     * @return row
     */
    public int getCol();

    /**
     * get the value
     * @return
     */
    public String getValue();

    /**
     * set the cell
     * @param s possibly null value
     */
    public void setValue(String s);
}
