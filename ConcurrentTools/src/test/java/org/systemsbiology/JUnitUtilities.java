package org.systemsbiology;

import com.lordjoe.utilities.*;
import org.apache.hadoop.fs.*;
import org.junit.*;

import java.io.*;
import java.util.*;

/**
 * org.systemsbiology.JUnitUtilities
 * User: steven
 * Date: 1/31/12
 */
public class JUnitUtilities {
    public static final JUnitUtilities[] EMPTY_ARRAY = {};

    private final List<File> m_Opened = new ArrayList<File>();

    public boolean guaranteeResourceFile(String resource) {
        return guaranteeResourceFile(JUnitUtilities.class, resource);
    }

    public boolean guaranteeResourceFile(Class res, String resource) {
        InputStream is = res.getResourceAsStream(resource);
        if (is == null)
            throw new IllegalStateException("cannnot read resource  " + resource + " from class " + res);
        File f = new File(resource);
        String outfile = f.getName();
        File out = new File(outfile);
        if (f.exists())
            throw new IllegalStateException("cannot overwrite file " + resource);
        try {
            FileUtilities.copyStream(is,new FileOutputStream(out));
        }
        catch (IOException e) {
            throw new RuntimeException(e);

        }
        return true;
    }

    public void cleanUpFiles() {
        for (File f : m_Opened) {
            if (f.exists())
                Assert.assertTrue(f.delete());

        }
        m_Opened.clear();
    }

}
