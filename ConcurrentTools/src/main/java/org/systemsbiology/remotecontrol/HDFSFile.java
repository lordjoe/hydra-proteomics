package org.systemsbiology.remotecontrol;

import java.io.*;
import java.text.*;
import java.util.*;

/**
 * org.systemsbiology.remotecontrol.HDFSFile
 * User: steven
 * Date: Jun 4, 2010
 */
public class HDFSFile {
    public static final HDFSFile[] EMPTY_ARRAY = {};

    public static final String HADOOP_DATE_FORMAT = "yyyy-MM-dd HH:mm";

    private boolean m_File;
    private String m_Name;
    private String m_Parent;
    private long m_Length;
    private Date m_ModificationDate;

    public HDFSFile(String s) {
        String[] items = s.split(" ");
        String fileName = items[items.length - 1];

        String[] nameItems = parseFileName(fileName);
        m_Parent = nameItems[0];
        m_Name = nameItems[1];
        String timeStr = items[items.length - 2];
        String dateStr = items[items.length - 3];

        m_Length = Long.parseLong(items[items.length - 4]);

        char startCh = items[0].charAt(0);
        if (startCh == '-')
            m_File = true;

        try {
            m_ModificationDate = new SimpleDateFormat(HADOOP_DATE_FORMAT).parse(dateStr + " " + timeStr);
        }
        catch (ParseException e) {
            throw new RuntimeException(e);
        }

      //  throw new UnsupportedOperationException("Fix This"); // ToDo
    }

    public HDFSFile(String fileName,long length,Date modificationDate) {

        String[] nameItems = parseFileName(fileName);
        m_Parent = nameItems[0];
        m_Name = nameItems[1];

        m_Length = length;

        m_ModificationDate = new Date(modificationDate.getTime());

        m_File = m_Length > 0;

      //  throw new UnsupportedOperationException("Fix This"); // ToDo
    }


    public boolean equivalent(File f)
    {
        String n = getName();
        if(!n.equals(f.getName()))
            return false;

        long l = getLength();
        if(l != f.length())
            return false;

        long t = getModificationDate().getTime();
        if(t != f.lastModified() )
            return false;

        return true;
    }


    private String[] parseFileName(final String pFileName) {
        String[] ret = new String[2];
        int index = pFileName.lastIndexOf("/");
        if (index == -1) {
            ret[0] = "";
            ret[1] = pFileName;
            return ret;
        }
        ret[0] = pFileName.substring(0, index);
        ret[1] = pFileName.substring(index + 1);

        return ret;
    }

    public boolean isDirectory() {
        return !isFile();
    }

    public boolean isFile() {
        return m_File;
    }

    public String getName() {
        return m_Name;
    }

    public String getParent() {
        return m_Parent;
    }

    public long getLength() {
        return m_Length;
    }

    public Date getModificationDate() {
        return m_ModificationDate;
    }


    @Override
    public String toString() {
        return (getParent().length() > 0 ? getParent() + "/" : "") + getName();
    }
}
