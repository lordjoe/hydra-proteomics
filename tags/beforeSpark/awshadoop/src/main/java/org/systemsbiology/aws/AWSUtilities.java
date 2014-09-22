package org.systemsbiology.aws;

import com.amazonaws.*;
import com.amazonaws.auth.*;
import com.amazonaws.services.ec2.*;
import com.amazonaws.services.elasticmapreduce.*;
import com.amazonaws.services.elasticmapreduce.model.*;
import com.amazonaws.services.s3.*;
import com.amazonaws.services.s3.model.*;
import com.lordjoe.utilities.*;
import org.apache.hadoop.conf.*;
import org.systemsbiology.hadoop.*;
import org.systemsbiology.remotecontrol.*;
import org.systemsbiology.xtandem.*;

import java.io.*;
import java.util.*;

/**
 * org.systemsbiology.aws.AWSUtilities User: steven Date: May 27, 2010
 */
public class AWSUtilities {
    public static final AWSUtilities[] EMPTY_ARRAY = {};

    public static final String AWS_URL = "s3n://";

    private static AWSLocation gDefaultLocation = AWSLocation.East;
    private static AmazonS3 gS3;
    private static String gDefaultKeyName = "CHANGE_THIS"; // "alaron";
    private static String gDefaultBucketName = "CHANGE_THIS"; //"lordjoe";
    private static final Set<String> gExistingBuckets = new HashSet<String>();
    private static AmazonEC2 gAmazon;
    private static final Properties gAccessProperties = new Properties();

    public static Properties getAccessProperties() {
        if (gAccessProperties.size() == 0) {
            try {
                final InputStream stream = AWSUtilities.class
                        .getResourceAsStream("AwsCredentials.properties");
                gAccessProperties.load(stream);
            }
            catch (IOException e) {
                throw new RuntimeException(e);

            }

        }
        return gAccessProperties;
    }

    public static synchronized AmazonEC2 getAmazonEC2() {
        if (gAmazon == null)
            gAmazon = new AmazonEC2Client(getCredentials());
        return gAmazon;
    }

    public static String getDefaultKeyName() {
        if ("CHANGE_THIS".equals(gDefaultKeyName))
            gDefaultKeyName = getAccessProperties().getProperty("defaultKey");
        if (gDefaultKeyName == null)
            throw new IllegalStateException("AWSCredentials.properties must have a default key");
        return gDefaultKeyName;
    }


    public static String getDefaultBucketName() {
        if ("CHANGE_THIS".equals(gDefaultBucketName))
            gDefaultBucketName = getAccessProperties().getProperty("defaultBucket");
        if (gDefaultBucketName == null)
            throw new IllegalStateException("AWSCredentials.properties must have a default bucket");
        return gDefaultBucketName;
    }

    public static void setDefaultBucketName(final String pDefaultBucketName) {
        gDefaultBucketName = pDefaultBucketName;
    }

    /**
     * default does nothing
     */
    public static final IConfigureFileSystem AWS_CONFIGURE_FILE_SYSTEM = new AWSFileConfigurer();


    private static int gDefaultNumberReduceTasks;

    public static int getDefaultNumberReduceTasks() {
        return gDefaultNumberReduceTasks;
    }

    public static void setDefaultNumberReduceTasks(int pDefaultNumberReduceTasks) {
        gDefaultNumberReduceTasks = pDefaultNumberReduceTasks;
    }


    private static class AWSFileConfigurer implements IConfigureFileSystem {
        private AWSFileConfigurer() {
        }

        /**
         * do nothing - use the default
         *
         * @param conf      !null  configuration
         * @param otherData algorithm specific other data    0 is bucket name 1 is output directory
         */
        @Override
        public void configureFileSystem(final Configuration conf, final Object... otherData) {

            String bucketName = bucketFromPath((String) otherData[0]);
            if (bucketName == null)
                return; // I guess we are not on s3
            AWSCredentials credentials = AWSUtilities.getCredentials();
            conf.set("fs.default.name", AWSUtilities.getHadoopFileSystemURL(bucketName));
            conf.set("fs.s3n.awsAccessKeyId", credentials.getAWSAccessKeyId());
            conf.set("fs.s3n.awsSecretAccessKey", credentials.getAWSSecretKey());
        }

    }

    /**
     * partse loge which have counters to get them back since (&*^))(*&%^*(^%^&* Amazon does not allow access to job
     *
     * @param s
     * @return
     */
    public static void parseCounters(String s, final Map<String, Long> map) {
        String[] lines = s.split("\n");
        int firstCounter = lines.length;
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            if (line.contains(": Counters:")) {
                firstCounter = i + 1;
                break;
            }
        }
        for (int i = firstCounter; i < lines.length; i++) {
            String line = lines[i];
            handleCounterLine(line, map);
        }
    }

    private static void handleCounterLine(String lineOrig, final Map<String, Long> map) {
        String line = lineOrig;
        if (!line.contains("="))
            return;
        int index = line.lastIndexOf(":");
        if (index == -1 || index >= line.length() - 1)
            return;
        line = line.substring(index + 1);
        line = line.trim();
        String[] items = line.split("=");
        long value = 0;
        try {
            value = Long.parseLong(items[1].trim());
        }
        catch (NumberFormatException e) {
            System.err.println("Cannot handle line " + lineOrig);

        }
        String key = items[0].trim();
        if (map.containsKey(key)) {
            value += map.get(key);
        }
        map.put(key, value);
    }

    public static String bucketFromPath(String path) {
        if (path.startsWith("s3n://")) {
            String ret = path.substring("s3n://".length());
            ret = ret.substring(0, ret.indexOf("/"));
            return ret;
        }
        return null;
    }

    private static AmazonElasticMapReduceClient gClient;

    public static AmazonElasticMapReduceClient getClient() {
        return gClient;
    }

    public static void setClient(final AmazonElasticMapReduceClient pClient) {
        gClient = pClient;
    }


    public static boolean isProbablyBinary(S3File file) {
        String name = file.getName().toLowerCase();
        if (name.endsWith(".gz"))
            return true;
        if (name.endsWith(".tar"))
            return true;
        if (name.endsWith(".zip"))
            return true;
        if (name.endsWith(".seq"))
            return true;
        return false;
    }


    public static AWSLocation getDefaultLocation() {
        return gDefaultLocation;
    }

    public static void setDefaultLocation(final AWSLocation pDefaultLocation) {
        gDefaultLocation = pDefaultLocation;
    }

    public static PlacementType getDefaultPlacement() {
        PlacementType ret = new PlacementType();
        ret.setAvailabilityZone(getDefaultLocation().toString());
        return ret;
    }

    public static AWSCredentials getCredentials() {
        Properties prop = getAccessProperties();
        String key = null;
        String value = prop.getProperty("accessKeyEncrypted");
        if (value != null)
            key = Encrypt.decryptString(value);
        else
            key = prop.getProperty("accessKey");

        value = prop.getProperty("secretKeyEncrypted");
        String secretKey = null;
        if (value != null)
            secretKey = Encrypt.decryptString(value);
        else
            secretKey = prop.getProperty("secretKey");

        if (key == null || secretKey == null)
            throw new BadAWSCredendialsException();
        AWSCredentials credentials = new BasicAWSCredentials(key, secretKey);
        return credentials;
    }


    public static String getHadoopFileSystemURL(String bucket) {
        return "s3n://" + bucket;
    }


    private static boolean stateInDesired(String state, String... desired) {
        for (int i = 0; i < desired.length; i++) {
            String s = desired[i];
            if (state.equals(s))
                return true;
        }
        return false;

    }

    public static void waitForStates(JobMonitor monitor, String... desired) {
        System.out.print("Waiting for states:");
        for (int i = 0; i < desired.length; i++) {
            String s = desired[i];
            System.out.print(s + " ");
        }
        System.out.println();
        String state = monitor.showJobStatus();
        if (stateInDesired(state, desired))
            return;
        String startState = state;
        ElapsedTimer et = new ElapsedTimer();
        while (true) {
            if (stateInDesired(state, desired)) {
                et.showElapsed("Change from " + startState + " to " + state);
                return;
            }
            AWSUtilities.waitFor(JobMonitor.MINIMUM_QUERY_TIME);
            state = monitor.showJobStatus();
        }


    }

    /**
     * make a bucket if it doesn't exist
     *
     * @param bucketName
     * @param creds
     * @throws exception if bucket cannot be created
     */
    public static void guaranteeBucket(String bucketName)
            throws IllegalArgumentException {
        guaranteeBucket(bucketName, getCredentials());
    }

    /**
     * return the names of all buckets
     *
     * @return
     */
    public static String[] getBucketNames() {
        AmazonS3 s3 = AWSUtilities.getS3();
        ListBucketsRequest request = new ListBucketsRequest();

        List<Bucket> bucketList = s3.listBuckets(request);
        List<String> holder = new ArrayList<String>();

        for (Bucket obj : bucketList) {
            String s = obj.getName();
            holder.add(s);
        }

        String[] ret = new String[holder.size()];
        holder.toArray(ret);
        return ret;
    }


    /**
     * make a bucket if it doesn't exist
     *
     * @param bucketName
     * @param creds
     * @throws exception if bucket cannot be created
     */
    public static void guaranteeBucket(String bucketName, AWSCredentials creds)
            throws IllegalArgumentException {
        if (gExistingBuckets.contains(bucketName))
            return;
        AmazonS3 s3 = getS3();
        try {
            if (s3.doesBucketExist(bucketName)) {
                gExistingBuckets.add(bucketName);
                ; // remember
                return;
            }
            // it does not exist so make one
            s3.createBucket(bucketName);
            // look again
            if (!s3.doesBucketExist(bucketName))
                throw new IllegalArgumentException("cannot create bucket "
                        + bucketName);

            gExistingBuckets.add(bucketName);
        }
        catch (AmazonClientException ex) {
            throw new RuntimeException(ex);
        }
        // remember
    }

    /**
     * make a bucket if it doesn't exist
     *
     * @param bucketName
     * @param creds
     * @throws exception if bucket cannot be created
     */
    public static String[] listFiles(String bucketName)
            throws IllegalArgumentException {
        return listFiles(bucketName, "");
    }

    /**
     * make a bucket if it doesn't exist
     *
     * @param bucketName
     * @param creds
     * @throws exception if bucket cannot be created
     */
    public static String[] listFiles(String bucketName, String directoryName)
            throws IllegalArgumentException {
        AmazonS3 s3 = getS3();
        try {

            ListObjectsRequest lo = new ListObjectsRequest();
            lo.setBucketName(bucketName);
            lo.setPrefix(directoryName);
            lo.setMaxKeys(Integer.MAX_VALUE);
            final ObjectListing objectListing = s3.listObjects(lo);
            List<String> holder = new ArrayList<String>();

            final List<S3ObjectSummary> summaryList = objectListing
                    .getObjectSummaries();
            for (S3ObjectSummary os : summaryList) {
                final String key = os.getKey();
                holder.add(key);
            }
            String[] ret = new String[holder.size()];
            holder.toArray(ret);
            return ret;
        }
        catch (AmazonClientException ex) {
            throw new RuntimeException(ex);
        }

    }


    /**
     * get all files in an S3 bucket
     *
     * @param bucketName
     * @param creds
     * @throws exception if bucket cannot be created
     */
    public static HDFSFile[] getFiles(String bucketName, String directory)
            throws IllegalArgumentException {
        AmazonS3 s3 = getS3();
        try {
            ListObjectsRequest lo = new ListObjectsRequest();
            lo.setBucketName(bucketName);
            lo.setPrefix(directory);
            lo.setMaxKeys(Integer.MAX_VALUE);
            final ObjectListing objectListing = s3.listObjects(lo);
            List<HDFSFile> holder = new ArrayList<HDFSFile>();

            final List<S3ObjectSummary> summaryList = objectListing
                    .getObjectSummaries();
            for (S3ObjectSummary os : summaryList) {
                HDFSFile file = buildHDFSFile(os);
                holder.add(file);
            }
            HDFSFile[] ret = new HDFSFile[holder.size()];
            holder.toArray(ret);
            return ret;
        }
        catch (AmazonClientException ex) {
            throw new RuntimeException(ex);
        }

    }

    /**
     * get all files in an S3 bucket
     *
     * @param bucketName
     * @param creds
     * @throws exception if bucket cannot be created
     */
    public static HDFSFile[] getDirectories(String bucketName, String directory)
            throws IllegalArgumentException {
        AmazonS3 s3 = getS3();
        try {
            ListObjectsRequest lo = new ListObjectsRequest();
            lo.setBucketName(bucketName);
            lo.setPrefix(directory);
            lo.setMaxKeys(2000);
            lo.setDelimiter("/");
            final ObjectListing objectListing = s3.listObjects(lo);
            List<HDFSFile> holder = new ArrayList<HDFSFile>();

            final List<S3ObjectSummary> summaryList = objectListing
                    .getObjectSummaries();
            for (S3ObjectSummary os : summaryList) {
                HDFSFile file = buildHDFSFile(os);
                holder.add(file);
            }
            HDFSFile[] ret = new HDFSFile[holder.size()];
            holder.toArray(ret);
            return ret;
        }
        catch (AmazonClientException ex) {
            throw new RuntimeException(ex);
        }

    }

    public static HDFSFile buildHDFSFile(S3ObjectSummary os) {
        String key = os.getKey();
        Date date = os.getLastModified();
        String bucket = os.getBucketName();
        long size = os.getSize();
        HDFSFile ret = new HDFSFile(key, size, date);
        return ret;

    }

    public static String uploadFiles(String bucket, String directory,
                                     File[] files) {
        guaranteeBucket(bucket);

        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            uploadFile(bucket, directory, file, true);
        }
        return bucket;
    }

    public static void uploadFile(String bucket, String directory, File file) {
        uploadFile(bucket, directory, file, false); // not sure we have a
        // directory
    }

    public static void uploadStream(String bucket, String fileName, InputStream file) {
        throw new UnsupportedOperationException("Fix This"); // ToDo
//        AmazonS3 s3 = getS3();
//         try {
//
//             /*
//                 * Upload an object to your bucket - You can easily upload a file to
//                 * S3, or upload directly an InputStream if you know the length of
//                 * the data in the stream. You can also specify your own metadata
//                 * when uploading to S3, which allows you set a variety of options
//                 * like content-type and content-encoding, plus additional metadata
//                 * specific to your applications.
//                 */
//             PutObjectRequest request = new PutObjectRequest(bucket, fileName,
//                     file);
//             s3.putObject(request);
//
//         }
//         catch(IOException ex)  {
//             throw new RuntimeException(ex);
//         }

    }

    public synchronized static AmazonS3 getS3() {
        try {
            if (gS3 == null) {
                AWSCredentials credentials = getCredentials();
                gS3 = new AmazonS3Client(credentials);
            }
            return gS3;
        }
        catch (AmazonClientException ex) {
            throw new RuntimeException(ex);
        }

    }

    public static String getFileData(String bucket, String fileName) {
        AmazonS3 s3 = getS3();
        GetObjectRequest request = new GetObjectRequest(bucket, fileName);
        S3Object object = s3.getObject(request);
        InputStream content = object.getObjectContent();
        LineNumberReader rdr = new LineNumberReader(new InputStreamReader(content));
        StringBuilder sb = new StringBuilder();
        try {
            String line = rdr.readLine();
            while (line != null) {
                if (sb.length() > 0) sb.append("\n");
                sb.append(line);
                line = rdr.readLine();
            }
            return sb.toString();
        }
        catch (IOException e) {
            throw new RuntimeException(e);

        }
    }

    protected static void uploadFile(String bucket, String directory,
                                     File file, boolean bucketExists) {
        if (!bucketExists)
            guaranteeBucket(bucket);
        AmazonS3 s3 = getS3();
        try {
            String fileName = buildS3FileName(directory, file);

            /*
            * Upload an object to your bucket - You can easily upload a file to
            * S3, or upload directly an InputStream if you know the length of
            * the data in the stream. You can also specify your own metadata
            * when uploading to S3, which allows you set a variety of options
            * like content-type and content-encoding, plus additional metadata
            * specific to your applications.
            */
            PutObjectResult putObjectResult = s3.putObject(bucket, fileName, file);
            //       PutObjectRequest request = new PutObjectRequest(bucket, fileName,
            //                file);
            //       s3.putObject(request);

        }
        catch (AmazonClientException ex) {
            throw new RuntimeException(ex);
        }

    }

    /**
     * Creates a temporary file with text data to demonstrate uploading a file
     * to Amazon S3
     *
     * @return A newly created temporary file with text data.
     * @throws IOException
     */
    public static File createSampleFile(String text) {
        PrintWriter pw = null;
        try {
            File file = File.createTempFile("aws-java-sdk-", ".txt");
            file.deleteOnExit();

            Writer writer = new OutputStreamWriter(new FileOutputStream(file));
            pw = new PrintWriter(writer);
            pw.print(text);
            return file;
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        finally {
            if (pw != null)
                pw.close();
        }


    }

    protected static void uploadText(String bucket, String fileName,
                                     String text) {
        File sampleFile = null;
        try {
            AmazonS3 s3 = getS3();
            //             ObjectMetadata omd = new ObjectMetadata();
//             omd.setContentLength(text.length() * 4);
//             omd.setContentType("text/plain");
//             omd.setLastModified(new Date());
            /*
            * Upload an object to your bucket - You can easily upload a file to
            * S3, or upload directly an InputStream if you know the length of
            * the data in the stream. You can also specify your own metadata
            * when uploading to S3, which allows you set a variety of options
            * like content-type and content-encoding, plus additional metadata
            * specific to your applications.
            */
            sampleFile = createSampleFile(text);
            PutObjectRequest request = new PutObjectRequest(bucket, fileName, sampleFile);
            s3.putObject(request);

        }

        catch (AmazonClientException ex) {
            throw new RuntimeException(ex);
        }
        finally {
            if (sampleFile != null)
                sampleFile.delete();
        }

    }

    public static String getBucket(String pArg) {
        return pArg.substring(0, pArg.indexOf("/"));
    }

    public static String getRest(String pArg) {
        return pArg.substring(pArg.indexOf("/") + 1);
    }

    public static String[] readConfigFileS3(String pArg) {
        AmazonS3 s3 = getS3();
        pArg = pArg.replace("s3n://", "");
        System.err.println("reading s3 object " + pArg);

        String bucket = getBucket(pArg);
        String file = getRest(pArg);
        System.err.println("reading s3 bucket " + bucket + " key " + file);
        try {
            S3Object object = s3.getObject(bucket, file);
            InputStreamReader rdr = new InputStreamReader(
                    object.getObjectContent());
            List<String> holder = new ArrayList<String>();

            try {
                LineNumberReader inp = new LineNumberReader(rdr);
                String line = inp.readLine();
                while (line != null) {
                    holder.add(line);
                    line = inp.readLine();
                }
                String[] ret = new String[holder.size()];
                holder.toArray(ret);
                // show for debugging
                // for (int i = 0; i < ret.length; i++) {
                // String s = ret[i];
                // System.err.println(s);
                // }
                return ret;
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        catch (AmazonClientException ex) {
            throw new RuntimeException(ex);
        }

    }


    public static InputStream openFileS3(String bucket, String file) {
        AmazonS3 s3 = getS3();
        System.err.println("reading s3 bucket " + bucket + " key " + file);
        try {
            S3Object object = s3.getObject(bucket, file);
            InputStream objectContent = object.getObjectContent();
            return objectContent;
        }
        catch (AmazonClientException ex) {
            throw new RuntimeException(ex);
        }

    }

    public static OutputStream openFileForWrite(String bucket, String file) {
        throw new UnsupportedOperationException("Fix This"); // ToDo
//        AmazonS3 s3 = getS3();
//           System.err.println("reading s3 bucket " + bucket + " key " + file);
//        try {
//            UploadPartRequest req = new UploadPartRequest();
//
//            s3.uploadPart()
//            S3Object object = s3.getObject(bucket, file);
//            InputStream objectContent = object.
//             return objectContent;
//        }
//        catch (AmazonClientException ex) {
//            throw new RuntimeException(ex);
//        }

    }


    public static String readFileTextS3(String pArg) {
        String[] lines = readConfigFileS3(pArg);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            if (sb.length() > 0)
                sb.append("\n");
            sb.append(line);
        }
        return sb.toString();
    }

    protected static String downloadFile(String bucket, String fileAndDirectory) {
        AmazonS3 s3 = getS3();

        try {
            S3Object object = s3.getObject(bucket, fileAndDirectory);
            S3ObjectInputStream objectContent = object.getObjectContent();
            InputStreamReader rdr = new InputStreamReader(objectContent);
            try {
                LineNumberReader inp = new LineNumberReader(rdr);
                String line = inp.readLine();
                StringBuilder sb = new StringBuilder();
                while (line != null) {
                    if (sb.length() > 0)
                        sb.append("\n");
                    sb.append(line);
                    line = inp.readLine();
                }
                return sb.toString();
            }
            catch (IOException e) {
                throw new RuntimeException(e);

            }
        }
        catch (AmazonClientException ex) {
            throw new RuntimeException(ex);
        }

    }

    protected static String buildS3FileName(String directory, File file) {
        String fileName = file.getName();

        if (directory != null && directory.length() > 0)
            fileName = directory + "/" + fileName;
        return fileName;
    }

    public static String getItemDirectory(String name) {
        int index = name.lastIndexOf("/");
        if (index == -1)
            return null;
        return name.substring(0, index);
    }

    public static String getItemName(String name) {
        int index = name.lastIndexOf("/");
        if (index == -1)
            return name;
        return name.substring(index + 1);
    }

    public static boolean hasFilex(String bucket, String name) {

        AmazonS3 s3 = getS3();
        try {
            guaranteeBucket(bucket);
            String dir = getItemDirectory(name);
            ObjectListing list = null;
            if (dir != null)
                list = s3.listObjects(bucket, name);
            else
                list = s3.listObjects(name);
            return doesListContainFile(list, name);
        }
        catch (AmazonClientException ex) {
            throw new RuntimeException(ex);
        }

    }

    public static boolean hasFile(String bucket, String name) {

        AmazonS3 s3 = getS3();
        try {
            ObjectListing list = s3.listObjects(bucket, name);
            List<S3ObjectSummary> objectSummaries = list.getObjectSummaries();
            return true;
        }
        catch (AmazonS3Exception e) {
            if (e.getStatusCode() == 404)
                return false;
            //if(e.g)
            String msg = e.getMessage();
            return false;
        }

    }


    public static boolean exists(String bucket, String name) {

        if (isFile(bucket, name))
            return true;
        if (isDirectory(bucket, name))
            return true;
        return false;
    }

    public static long fileLength(String bucket, String name) {

        AmazonS3 s3 = getS3();
        try {
            ObjectMetadata md = s3.getObjectMetadata(bucket, name);
            return md.getContentLength();
        }
        catch (AmazonClientException e) {
            return 0;
        }

    }


    public static boolean isFile(String bucket, String name) {

        AmazonS3 s3 = getS3();
        try {
            ObjectMetadata md = s3.getObjectMetadata(bucket, name);
            return true;
        }
        catch (AmazonS3Exception e) {
            if (e.getStatusCode() == 404)
                return false;
            //if(e.g)
            String msg = e.getMessage();
            return false;
        }

    }


    public static boolean isDirectory(String bucket, String name) {
        AmazonS3 s3 = getS3();
        try {
            ObjectListing list = s3.listObjects(bucket, name);
            List<S3ObjectSummary> objectSummaries = list.getObjectSummaries();
            return objectSummaries.size() > 0;
        }
        catch (AmazonS3Exception e) {
            if (e.getStatusCode() == 404)
                return false;
            //if(e.g)
            String msg = e.getMessage();
            return false;
        }

    }

    public static boolean hasFile(File model, String bucket, String name) {

        AmazonS3 s3 = getS3();
        try {
//            ObjectListing objectListing = s3.listObjects(bucket);
//             List<S3ObjectSummary> objectSummaries = objectListing.getObjectSummaries();
//             for (S3ObjectSummary os : objectSummaries ) {
//                 String key = os.getKey();
//                 System.out.println(key);
//             }
//            ObjectMetadata md1 = s3.getObjectMetadata(bucket, "jobs");
//            ObjectListing list = s3.listObjects(bucket, "jobs");
//             List<S3ObjectSummary> objectSummaries = list.getObjectSummaries();
//               for (S3ObjectSummary os : objectSummaries ) {
//                 String key = os.getKey();
//                 System.out.println(key);
//             }
//
            ObjectMetadata md = s3.getObjectMetadata(bucket, name);
            return md.getContentLength() == model.length();
        }
        catch (AmazonClientException e) {
            return false;

        }

    }

    public static void deleteFile(String bucket, String name) {
        try {
            AmazonS3 s3 = getS3();
            s3.deleteObject(bucket, name);
        }
        catch (AmazonClientException ex) {
            throw new RuntimeException(ex);
        }

    }

    public static void expunge(String bucket, String name) {
        try {
            AmazonS3 s3 = getS3();
            s3.deleteObject(bucket, name);
        }
        catch (AmazonClientException ex) {
            throw new RuntimeException(ex);
        }

    }

    public static boolean doesListContainFile(ObjectListing list, String file) {
        for (S3ObjectSummary os : list.getObjectSummaries()) {
            if (os.getKey().equals(file)) {
                return true;
            }
        }
        return false;
    }

    private static void showFiles() {
        final String[] strings = listFiles("moby50");
        for (int i = 0; i < strings.length; i++) {
            String s = strings[i];
            System.out.println(s);
        }
    }

    private static void testExists() {
        String key = "moby/moby.127";
        final String bucket = "moby50";
        boolean exists = hasFile(bucket, key);

        exists = hasFile(bucket, "foo/bar");

    }

    public static void waitFor(long millisec) {
        try {
            Thread.sleep(millisec);
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {

        //        String s =  FileUtilities.readInFile(args[0]) ;
//         Map<String, Long> map = new HashMap<String, Long>();
//         parseCounters(s,map);
        // showFiles();
        // testExists();
//        final String bucket = "moby50";
//        File upJar = new File("E:/data/AWAMR-165818_J19.jar");
//        if (upJar.exists()) {
//            final String dir = "jars";
//            uploadFile(bucket, dir, upJar);
//            final String key = buildS3FileName(dir, upJar);
//            boolean exists = hasFile(bucket, key);
//        }

    }

}
