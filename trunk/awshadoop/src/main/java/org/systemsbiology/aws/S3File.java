package org.systemsbiology.aws;

import com.amazonaws.services.s3.*;
import com.amazonaws.services.s3.model.*;

import java.util.*;

/**
 * org.systemsbiology.aws.S3File
 * User: steven
 * Date: Aug 10, 2010
 */
public abstract class S3File implements Comparable<S3File> {
    public static final S3File[] EMPTY_ARRAY = {};

//    public static S3File[] getSubfiles(S3Directory parent)
//    {
//        AmazonS3 s3 = AWSUtilities.getS3();
//        ListObjectsRequest request = new ListObjectsRequest();
//        S3Bucket bucket = parent.getBucket();
//        request.setBucketName(bucket.getName());
//        String path = parent.getPath();
//        request.setPrefix(path);
//        ObjectListing objectListing = s3.listObjects(request);
//        List<S3ObjectSummary> bucketList = objectListing.getObjectSummaries();
//
//        List<S3File> holder = new ArrayList<S3File>();
//
//        for(S3ObjectSummary obj : bucketList)  {
//            String s = obj.getKey();
//            String name = s.substring(path.length() + 1);
//
//            S3File file = new S3File(bucket,name,parent);
//        }
//        S3File[] ret = new S3File[holder.size()];
//         holder.toArray(ret);
//         return ret;
//
//
//    }

    private final String m_Name;
    private final S3Bucket m_Bucket;
    private final S3Directory m_Parent;
    private final boolean m_Directory;


    protected S3File(final S3Bucket pBucket, final S3Directory pParent, String name, final boolean pDirectory) {
        m_Bucket = pBucket;
        m_Parent = pParent;
        validateName(name);
        m_Name = name;
        m_Directory = pDirectory;
    }

    protected void validateName(String name) {
        if (name == null || name.length() == 0)
            throw new IllegalStateException("name cannot be empty");
    }

    public String getName() {
        return m_Name;
    }

    public abstract Date getModificationDate();

    public abstract long getSize();

    public abstract S3File getFileByName(String name);


    public S3File[] getFiles() {
        return null;
    }

    public boolean isDirectory() {
        return m_Directory;
    }

    public S3Bucket getBucket() {
        return m_Bucket;
    }

    public S3Directory getParent() {
        return m_Parent;
    }

    public String getPath() {
        S3Directory parent = getParent();
        if (parent == null)
            return "";
        StringBuilder sb = new StringBuilder();
        String path = parent.getPath();
        if (path.length() > 0)
            path += "/";
        return path + getName();

    }


    @Override
    public String toString() {
        return getName();
    }

    @Override
    public int compareTo(final S3File o) {
        return getName().toUpperCase().compareTo(o.getName().toUpperCase());
    }


}
