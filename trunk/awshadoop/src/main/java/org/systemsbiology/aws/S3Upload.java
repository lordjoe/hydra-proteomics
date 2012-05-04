package org.systemsbiology.aws;

/*
 * Copyright 2010 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

import com.amazonaws.*;
import com.amazonaws.auth.*;
import com.amazonaws.services.s3.*;
import com.amazonaws.services.s3.model.*;
import org.systemsbiology.common.*;

import java.io.*;
import java.util.*;

/**
 * This sample demonstrates how to make basic requests to Amazon S3 using the
 * AWS SDK for Java.
 * <p/>
 * <b>Prerequisites:</b> You must have a valid Amazon Web Services developer
 * account, and be signed up to use Amazon S3. For more information on Amazon
 * S3, see http://aws.amazon.com/s3.
 * <p/>
 * <b>Important:</b> Be sure to fill in your AWS access credentials in the
 * AwsCredentials.properties file before you try to run this sample.
 * http://aws.amazon.com/security-credentials
 */
public class S3Upload {
	public static final Random RND = new Random();
	private final AmazonS3 m_S3;

	public S3Upload() {
	 		m_S3 = new AmazonS3Client(AWSUtilities.getCredentials());
		}

	public String uploadFiles(String directory, File[] files) {
		directory = guaranteeBucket(directory);

		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			uploadFile(directory, file, true);
		}
		return directory;
	}

	public void uploadFile(String directory, File file) {
		uploadFile(directory, file, false); // not sure we have a directory
	}

	protected void uploadFile(String directory, File file, boolean bucketExists) {
		if (!bucketExists)
			guaranteeBucket(directory);
		AmazonS3 s3 = getS3();

		String fileName = file.getName();

		/*
		 * Upload an object to your bucket - You can easily upload a file to S3,
		 * or upload directly an InputStream if you know the length of the data
		 * in the stream. You can also specify your own metadata when uploading
		 * to S3, which allows you set a variety of options like content-type
		 * and content-encoding, plus additional metadata specific to your
		 * applications.
		 */
		PutObjectRequest request = new PutObjectRequest(directory, fileName,
				file);
		try {
			s3.putObject(request);
		}
		catch (AmazonClientException ex) {
			throw new RuntimeException(ex);
		}

	}

	public AmazonS3 getS3() {
		return m_S3;
	}

	public String guaranteeBucket(String bucketName) {
		AmazonS3 s3 = getS3();
		try {
			for (Bucket bucket : s3.listBuckets()) {
				if (bucketName.equals(bucket.getName()))
					return bucketName;
			}
			s3.createBucket(bucketName);
			return bucketName;
		}
		catch (AmazonClientException e) {
			return guaranteeBucket(bucketName + ('0' + RND.nextInt(10)));
		}

	}

	public static void usage() {
		System.out.println("S3Upload <directory> fileSpec ");
		System.out.println(" i.e. S3Upload moby moby___STAR___.txt ");
		System.out.println("NOTE use ___STAR___ for wild card ");
	}

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		List<File> holder = new ArrayList<File>();

		if (!(args.length >= 2)) {
			usage();
			return;
		}

		String directory = args[0];

		File theFile = new File(args[1]);
		File[] files = CommonUtilities.getWildCardFiles(theFile
				.getAbsolutePath());
		// for (int i = 1; i < args.length; i++) {
		// String arg = args[i];
		// File f = new File(arg);
		// if(f.exists())
		// holder.add(f);
		// }
		// File[] files = new File[holder.size()];
		if (files.length == 0) {
			usage();
			return;

		}
		S3Upload ul = new S3Upload();

		EventTimer evt = new EventTimer();
		ul.uploadFiles(directory, files);
		System.out.println("Uploaded " + files.length + " files in " + evt);
	}

}