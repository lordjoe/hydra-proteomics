package com.lordjoe.logging;

import com.lordjoe.utilities.*;

import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 * com.lordjoe.logging.AbstractLoggedObjectListCellRenderer
 *
 * @author Steve Lewis
 * @date Jun 30, 2008
 */
public abstract class AbstractLoggedObjectListCellRenderer<T> extends DefaultListCellRenderer
{
    public static AbstractLoggedObjectListCellRenderer[] EMPTY_ARRAY = {};
    public static Class THIS_CLASS = AbstractLoggedObjectListCellRenderer.class;

    /**
     * Return a component that has been configured to display the specified
     * value. That component's <code>paint</code> method is then called to
     * "render" the cell.  If it is necessary to compute the dimensions
     * of a list because the list cells do not have a fixed size, this method
     * is called to generate a component on which <code>getPreferredSize</code>
     * can be invoked.
     *
     * @param list         The JList we're painting.
     * @param value        The value returned by list.getModel().getElementAt(org.systemsbiology.couldera.training.index).
     * @param index        The cells org.systemsbiology.couldera.training.index.
     * @param isSelected   True if the specified cell was selected.
     * @param cellHasFocus True if the specified cell has the focus.
     * @return A component whose paint() method will render the specified value.
     * @see javax.swing.JList
     * @see javax.swing.ListSelectionModel
     * @see javax.swing.ListModel
     */
    public Component getListCellRendererComponent(JList list, Object value, int index,
                                                  boolean isSelected, boolean cellHasFocus)
    {
        LoggedObject<T> evt = (LoggedObject<T>) value;
        String realvalue = buildDisplayString(evt);
        return super.getListCellRendererComponent(list, realvalue, index,
                isSelected, cellHasFocus);
    }

    protected String buildDisplayString(LoggedObject<T> evt)
    {
        Calendar buildTimeString = evt.getTime();
        T data = evt.getData();
        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        sb.append(buildTimeString(buildTimeString));
        sb.append(" ");
        sb.append(buildDataString(data));
        sb.append("</html>");
        return sb.toString();
    }


    /**
     * utility method to format time
     *
     * @param time
     * @return
     */
    protected String buildTimeString(Calendar time)
    {
        StringBuilder sb = new StringBuilder();

        int hour = time.get(Calendar.HOUR_OF_DAY);
        String hourStr = Util.formatInt(hour, 2);
        int minute = time.get(Calendar.MINUTE);
        String minuteStr = Util.formatInt(minute, 2);
        int second = time.get(Calendar.SECOND);
        String secondStr = Util.formatInt(second, 2);
        sb.append(hourStr + ":" + minuteStr + ":" + secondStr);
        return sb.toString();
    }


    /**
     * fill in the way to format data
     *
     * @param data non-null data
     * @return a String to represent data - usually assuming html
     */
    protected abstract String buildDataString(T data);

}
