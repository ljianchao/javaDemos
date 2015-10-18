package cn.jc.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Properties;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.CopyObjectResult;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;

public class CephUtils {
	//配置文件名称
	private final static String CONFIGNAME = "ceph.properties";
	private final static String CONFIG_ACCESSKEY = "accessKey";
	private final static String CONFIG_SECRETKEY = "secretKey";
	private final static String CONFIG_ENDPOINT = "endpoint";
	
	private static String accessKey = null;
	private static String secretKey = null;
	private static String endPoint = null;		
	private static AmazonS3 conn = null;

	/**
	 * 设置ceph连接的基本配置
	 * @param projectResourcPath 项目配置文件夹的路径
	 * @throws IOException
	 */
	public static void setCephConnConfig(String projectResourcPath, Protocol protocol) throws IOException{
		if (projectResourcPath == null || projectResourcPath.isEmpty()) {
			throw new NullPointerException("配置文件的路径为null");
		}
		
		//判断是否已初始化
		if (accessKey != null && secretKey != null &&  endPoint != null) {
			return;
		}
		
		String configPath = String.format("%s%s", projectResourcPath, CONFIGNAME);	
		
		String value = null;
		
		Properties properties = new Properties();
		InputStream in = new FileInputStream(configPath);
		properties.load(in);
		value = properties.getProperty(CONFIG_ACCESSKEY);
		
		if (value != null) {
			accessKey = value;
		}
		
		value = properties.getProperty(CONFIG_SECRETKEY);
		if (value != null) {
			secretKey = value;
		}
		
		value = properties.getProperty(CONFIG_ENDPOINT);
		if (value != null) {
			endPoint = value;
		}
		
		//初始化AmazonS3
		getClientConn(protocol);
	}		
	
	/**
	 * create a connection
	 * @return
	 * @throws IOException 
	 */
	private static void getClientConn(Protocol protocol) throws IOException{
		if (CephUtils.accessKey == null || CephUtils.accessKey.isEmpty()) {
			 throw new NullPointerException("accessKey为null");
		}
		
		if (CephUtils.secretKey == null || CephUtils.secretKey.isEmpty()) {
			throw new NullPointerException("secretKey为null");
		}
		
		if (CephUtils.endPoint == null || CephUtils.endPoint.isEmpty()) {
			throw new NullPointerException("endPoint为null");
		}
		
		AWSCredentials credentials = new BasicAWSCredentials(CephUtils.accessKey, CephUtils.secretKey);	
		if (protocol != null) {
			ClientConfiguration clientConfiguration = new ClientConfiguration();
			clientConfiguration.setProtocol(protocol);
					
			conn = new AmazonS3Client(credentials, clientConfiguration);
		}else {
			//默认HTTPS访问
			conn = new AmazonS3Client(credentials);
		}
		
		conn.setEndpoint(CephUtils.endPoint);
	}

	/**
	 * 获取bucket列表
	 * @param conn
	 * @return
	 */
	public static List<Bucket> getOwnedBuckets(){	
		if (conn == null) {
			throw new NullPointerException("ceph连接为null");
		}
		List<Bucket> buckets = conn.listBuckets();		
		return buckets;
	}
	
	/**
	 * 根据bucket名称查询某个bucket是否存在
	 * @param bucketName
	 * @return
	 */
	public static boolean buckeIsExist(String bucketName) {
		List<Bucket> buckets = getOwnedBuckets();
		for (Bucket bucket : buckets) {
			if (bucket.getName().equals(bucketName)) {
				return true;
			}	
		}

		return false;
	}
	
	/**
	 * 创建bucket
	 * @param bucketName
	 * @return
	 */
	public static Bucket createBucket(String bucketName){
		if (conn == null) {
			throw new NullPointerException("ceph连接为null");
		}
		return conn.createBucket(bucketName);
	}
	
	/**
	 * 删除bucket（The Bucket must be empty! Otherwise it won’t work!）
	 * @param bucketName
	 */
	public static void deleteBucket(String bucketName){
		if (conn == null) {
			throw new NullPointerException("ceph连接为null");
		}
		conn.deleteBucket(bucketName);
	}
	
	/**
	 * listing a bucket's content
	 * @param bucketName
	 * @return
	 */
	public static ObjectListing getObjectListing(String bucketName) {
		if (conn == null) {
			throw new NullPointerException("ceph连接为null");
		}
		return conn.listObjects(bucketName);
	}
		
	/**
	 * CREATING AN OBJECT
	 * @param bucketName
	 * @param key
	 * @param input
	 */
	public static void createObject(String bucketName, String key, InputStream input, ObjectMetadata metadata) {
		if (conn == null) {
			throw new NullPointerException("ceph连接为null");
		}
		conn.putObject(bucketName, key, input, metadata);
	}
	
	/**
	 * CREATING AN OBJECT
	 * @param bucketName
	 * @param key
	 * @param file
	 */
	public static void createObject(String bucketName, String key, File file) {
		if (conn == null) {
			throw new NullPointerException("ceph连接为null");
		}
		conn.putObject(bucketName, key, file);
	}
	
	/**
	 * CHANGE AN OBJECT’S ACL
	 * @param bucketName
	 * @param key
	 * @param alcType, 0:PublicRead,1:Private
	 */
	public static void setObjectACL(String bucketName, String key, CannedAccessControlList cannedAccessControlList){
		if (conn == null) {
			throw new NullPointerException("ceph连接为null");
		}
		conn.setObjectAcl(bucketName, key, cannedAccessControlList);
	}
	
	/**
	 * RENAME AN OBJECT
	 * @param bucketName
	 * @param oldKey
	 * @param newKey
	 */
	public static void renameObject(String bucketName, String oldKey, String newKey) {
		if (conn == null) {
			throw new NullPointerException("ceph连接为null");
		}
		
		//复制新文件
		conn.copyObject(bucketName, oldKey, bucketName, newKey);
		//删除旧文件
		conn.deleteObject(bucketName, oldKey);
	}
	
	/**
	 * COPY AN OBJECT
	 * @param bucketName
	 * @param oldKey
	 * @param newKey
	 */
	public static CopyObjectResult copyObject(String sourceBucketName, String sourceKey,
            String destinationBucketName, String destinationKey) {
		if (conn == null) {
			throw new NullPointerException("ceph连接为null");
		}
		return conn.copyObject(sourceBucketName, sourceKey, destinationBucketName, destinationKey);
	}
	
	/**
	 * GET AN OBJECT
	 * @param bucketName
	 * @param key
	 * @return
	 */
	public static S3Object getS3Object(String bucketName, String key) {
		if (conn == null) {
			throw new NullPointerException("ceph连接为null");
		}
		return conn.getObject(bucketName, key);
	}
	
	/**
	 * DOWNLOAD AN OBJECT(TO INPUTSTREAM)
	 * @param bucketName
	 * @param key
	 * @return
	 */
	public static InputStream getS3ObjectToInputStream(String bucketName, String key){
		if (conn == null) {
			throw new NullPointerException("ceph连接为null");
		}
		S3Object s3Object = conn.getObject(bucketName, key);
		if (s3Object == null) {
			return null;
		}else {
			return s3Object.getObjectContent();
		}
	}
	
	/**
	 * DOWNLOAD AN OBJECT (TO A FILE)
	 * 
	 */
	public static void downloadObjectToFile(String bucketName, String key, String filePath) {
		if (conn == null) {
			throw new NullPointerException("ceph连接为null");
		}
		conn.getObject(new GetObjectRequest(bucketName, key), new File(filePath));
	}
	
	/**
	 * DELETE AN OBJECT
	 * @param bucketName
	 * @param key
	 */
	public static void deleteObject(String bucketName, String key) {
		if (conn == null) {
			throw new NullPointerException("ceph连接为null");
		}
		conn.deleteObject(bucketName, key);
	}
	
	/**
	 * GENERATE OBJECT DOWNLOAD URLS (SIGNED)
	 * @param bucketName
	 * @param key
	 */
	public static URL generateObjectUrls(String bucketName, String key) {
		if (conn == null) {
			throw new NullPointerException("ceph连接为null");
		}
		GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucketName, key);
		return conn.generatePresignedUrl(request);
	}
}

