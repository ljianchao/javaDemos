package cn.jc.Utils;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;

public class CephUtils {
	private static String accessKey = "GEGLJDFLYF6AHMO1Z6B9";
	private static String secretKey = "K2JEnshG+mWk1XT2+SoT5yO0vKYUCvEQ4jp0qJsE";
	private static String endPoint = "192.168.87.83";	
	private static AmazonS3 conn;
	
	static{
		conn = getClientConn(Protocol.HTTP);
	}
	
	/**
	 * create a connection
	 * @return
	 */
	public static AmazonS3 getClientConn(Protocol protocol){
		AWSCredentials credentials = new BasicAWSCredentials(CephUtils.accessKey, CephUtils.secretKey);	
		AmazonS3 conn = null;
		if (protocol != null) {
			ClientConfiguration clientConfiguration = new ClientConfiguration();
			clientConfiguration.setProtocol(protocol);
					
			conn = new AmazonS3Client(credentials, clientConfiguration);
		}else {
			//默认HTTPS访问
			conn = new AmazonS3Client(credentials);
		}
		
		conn.setEndpoint(CephUtils.endPoint);
		return conn;
	}
	
	public static List<Bucket> getOwnedBuckets(){
		//AmazonS3 conn = getClientConn();
		List<Bucket> buckets = conn.listBuckets();
		
		return buckets;
	}
	
	/**
	 * 创建bucket
	 * @param bucketName
	 * @return
	 */
	public static Bucket createBucket(String bucketName){
		return conn.createBucket(bucketName);
	}
	
	/**
	 * 删除bucket（The Bucket must be empty! Otherwise it won’t work!）
	 * @param bucketName
	 */
	public static void deleteBucket(String bucketName){
		conn.deleteBucket(bucketName);
	}
	
	/**
	 * listing a bucket's content
	 * @param bucketName
	 * @return
	 */
	public static ObjectListing getObjectListing(String bucketName) {
		return conn.listObjects(bucketName);
	}
		
	/**
	 * CREATING AN OBJECT
	 * @param bucketName
	 * @param key
	 * @param input
	 */
	public static void createObject(String bucketName, String key, InputStream input) {
		conn.putObject(bucketName, key, input, new ObjectMetadata());
	}
	
	/**
	 * CREATING AN OBJECT
	 * @param bucketName
	 * @param key
	 * @param file
	 */
	public static void createObject(String bucketName, String key, File file) {
		conn.putObject(bucketName, key, file);
	}
	
	/**
	 * CHANGE AN OBJECT’S ACL
	 * @param bucketName
	 * @param key
	 * @param alcType, 0:PublicRead,1:Private
	 */
	public static void setObjectACL(String bucketName, String key, CannedAccessControlList cannedAccessControlList){
		conn.setObjectAcl(bucketName, key, cannedAccessControlList);
	}
	
	/**
	 * DOWNLOAD AN OBJECT (TO A FILE)
	 * 
	 */
	public static void downloadObjectToFile(String bucketName, String key, String filePath) {
		conn.getObject(new GetObjectRequest(bucketName, key), new File(filePath));
	}
	
	/**
	 * DELETE AN OBJECT
	 * @param bucketName
	 * @param key
	 */
	public static void deleteObject(String bucketName, String key) {
		conn.deleteObject(bucketName, key);
	}
	
	/**
	 * GENERATE OBJECT DOWNLOAD URLS (SIGNED)
	 * @param bucketName
	 * @param key
	 */
	public static URL generateObjectUrls(String bucketName, String key) {
		GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucketName, key);
		return conn.generatePresignedUrl(request);
	}
}
