package org.systemsbiology.aws;

import java.util.*;

/**
 * org.systemsbiology.aws.S3DataFile
 * User: steven
 * Date: Aug 10, 2010
 */
public class S3DataFile extends S3File {
    public static final S3DataFile[] EMPTY_ARRAY = {};

    private long m_Length;
    private Date m_ModificationDate;

    public S3DataFile(final S3Bucket pBucket, final S3Directory pParent, final String name) {
        super(pBucket, pParent, name,false);
    }

    public long getSize() {
        return getLength();
    }

    public long getLength() {
        return m_Length;
    }

    public void setLength(final long pLength) {
        m_Length = pLength;
    }

    public Date getModificationDate() {
        return m_ModificationDate;
    }

    public void setModificationDate(final Date pModificationDate) {
        m_ModificationDate = new Date(pModificationDate.getTime());
    }

    @Override
    public S3File getFileByName(final String name) {
        if(getName().equals(name))
            return this;
        return null;
    }
}
