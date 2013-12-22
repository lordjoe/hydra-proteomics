package com.lordjoe.utilities;

import java.io.*;

/**
 * com.lordjoe.utilities.DirectoryVisitor
 * User: Steve
 * Date: 12/17/13
 */
public class DirectoryVisitor {

    private final File m_File;

    public DirectoryVisitor(final File pFile) {
        m_File = pFile;
    }

    public File getFile() {
        return m_File;
    }

    public void process(TypedVisitor<File> visitor) {
        process(visitor, getFile());
    }

    protected void process(TypedVisitor<File> visitor, File file) {
        visitor.visit(file);
        if (file.isDirectory()) {
            File[] subfiles = file.listFiles();
            if (subfiles != null) {
                for (int i = 0; i < subfiles.length; i++) {
                    File subfile = subfiles[i];
                      process(visitor, subfile);
                }
            }
        }
    }

}
