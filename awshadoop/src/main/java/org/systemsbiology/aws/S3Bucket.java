package org.systemsbiology.aws;

import com.amazonaws.services.s3.*;
import com.amazonaws.services.s3.model.*;

import java.util.*;

/**
 * org.systemsbiology.aws.S3Bucket
 * User: steven
 * Date: Aug 10, 2010
 */
public class S3Bucket implements Comparable<S3Bucket> {
    public static final S3Bucket[] EMPTY_ARRAY = {};

    public static final Map<String,S3Bucket>  gBuckets = new HashMap<String,S3Bucket>();



    public static S3Bucket[] getBuckets()
    {
        AmazonS3 s3 = AWSUtilities.getS3();
        ListBucketsRequest request = new ListBucketsRequest();

        List<Bucket> bucketList = s3.listBuckets(request);

        for(Bucket obj : bucketList)  {
            String s = obj.getName();
            if(!gBuckets.containsKey(s)) {
                gBuckets.put(s,new S3Bucket(s));
            }
        }

        S3Bucket[] buckets = gBuckets.values().toArray(EMPTY_ARRAY);
        Arrays.sort(buckets);
        return buckets;
    }


    @Override
    public int compareTo(final S3Bucket o) {
        return getName().toUpperCase().compareTo(o.getName().toUpperCase());
    }

    public static S3Bucket  getBucket(String name)
    {
       S3Bucket[] items = getBuckets();
        for (int i = 0; i < items.length; i++) {
            S3Bucket item = items[i];
            if(item.getName().equals(name))
                  return item;
       }
       throw new IllegalArgumentException("No bucket for name " + name + " exists");
    }

    private final String m_Name;
    private final S3Directory  m_Root;

    protected S3Bucket(final String pName) {
        m_Name = pName;
        m_Root = new S3RootDirectory(this);
    }

    public S3Directory getRoot() {
        return m_Root;
    }

    public S3File[] getFiles() {
        return getRoot().getFiles();
    }

    public String getName() {
        return m_Name;
    }

    public S3File  getFileByName(String name) {
          return getRoot().getFileByName(name);
      }
    
}
