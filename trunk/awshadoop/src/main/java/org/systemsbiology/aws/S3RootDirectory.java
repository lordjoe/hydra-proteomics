package org.systemsbiology.aws;

import org.systemsbiology.remotecontrol.*;

/**
 * org.systemsbiology.aws.S3RootDirectory
 * User: steven
 * Date: Aug 10, 2010
 */
public class S3RootDirectory extends S3Directory {
    public static final S3RootDirectory[] EMPTY_ARRAY = {};


    public S3RootDirectory(final S3Bucket pBucket) {
        super(pBucket, null, "");
    }

    @Override
    public String toString() {
        return getBucket().getName();
    }


    protected void validateName(String name)  {
        if(  name.length() != 0)
            throw new IllegalStateException("name must be empty"); 
    }


    protected void guaranteeFile(HDFSFile file) {
         String parent = file.getParent();
         if (parent == null || parent.length() == 0) {
             addFile(file);
         }
         else {
             S3Directory parentDir = guaranteeParentDir(file.getParent());
             parentDir.addFile(file);
         }

     }

       protected S3Directory guaranteeParentDir(String file) {
           if(file == null)
                return this;
           if(file.length() == 0)
                return this;
         return super.guaranteeParentDir(file);

    }
}
