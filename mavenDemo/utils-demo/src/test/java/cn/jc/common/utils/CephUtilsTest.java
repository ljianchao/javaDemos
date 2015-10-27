package cn.jc.common.utils;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.junit.Test;

import com.amazonaws.Protocol;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.util.StringUtils;

public class CephUtilsTest {

	static{
		String cephConfigPath = null;
		try {
			cephConfigPath = java.net.URLDecoder.decode(CephUtilsTest.class.getResource("/").getPath(),"utf-8");
			CephUtils.setCephConnConfig(cephConfigPath, Protocol.HTTP);
			System.out.println("cephConfigPath: " + cephConfigPath);
		} catch (UnsupportedEncodingException e1) {				
			e1.printStackTrace();
		} catch (IOException e) { 
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testReplace() {
		try {
			String str = "E:\\texstbucekt2\\sql.txt";
			String replaceStr =  "E:\\texstbucekt2\\";
			
			int first = str.indexOf(replaceStr);
			if (first != -1) {
				str = str.substring(replaceStr.length() - 1, str.length());
			}
			System.out.println(str);
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	@Test
	public void testGetOwnedBuckets() {
		List<Bucket> buckets = CephUtils.getOwnedBuckets();
		if (buckets != null && buckets.size() > 0) {
			for (Bucket bucket : buckets) {
				System.out.println(bucket.getName() + "\t" +
						StringUtils.fromDate(bucket.getCreationDate()));
			}
		}		
	}

	@Test
	public void testBuckeIsExist() {
		fail("Not yet implemented");
	}

	@Test
	public void testCreateBucket() {
		Bucket bucket = CephUtils.createBucket("testbucket4");
		System.out.println(bucket.getName());
	}

	@Test
	public void testDeleteBucket() {
		String bucketName = "testbucket4";
		ObjectListing objects = CephUtils.getObjectListing(bucketName);
		try {
			//判断桶是否为空
			if (objects.getObjectSummaries().size() == 0) {
				CephUtils.deleteBucket(bucketName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testGetObjectListing() {
		ObjectListing objects = CephUtils.getObjectListing("testbucket2");
		System.out.println(objects.getObjectSummaries().size());
		do {
			for (S3ObjectSummary objectSummary : objects.getObjectSummaries()) {
				System.out.println(objectSummary.getKey() + "\t" +
			objectSummary.getSize() + "\t" + StringUtils.fromDate(objectSummary.getLastModified()));
			}
			
		} while (objects.isTruncated());
	}

	@Test
	public void testCreateObjectStringStringInputStreamObjectMetadata() throws IOException {
		String bucketName = "testbucket3";
		String key = "hello.txt";
		ByteArrayInputStream input = new ByteArrayInputStream("Swicth Hello World!".getBytes());
		try {
			CephUtils.createObject(bucketName, key, input, new ObjectMetadata());
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			if (input != null) {
				input.close();
			}
		}
	}

	@Test
	public void testCreateObjectStringStringFile() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetObjectACL() {
		String bucketName = "testbucket3";
		String key = "hello.txt";
		try {
			CephUtils.setObjectACL(bucketName, key, CannedAccessControlList.PublicRead);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testRenameObject() {
		fail("Not yet implemented");
	}

	@Test
	public void testCopyObject() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetS3Object() {
		String bucketName = "mplus";
		String key = "测试上传图片  中文名称.png";
		try {
			S3Object s3Object = CephUtils.getS3Object(bucketName, key);
			InputStream in = s3Object.getObjectContent();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testGetS3ObjectToInputStream() {
		fail("Not yet implemented");
	}

	@Test
	public void testDownloadObjectToFile() {
		String bucketName = "testbucket3";
		String key = "hello.txt";
		String filePath = "D:/hello.txt";
		try {
			CephUtils.downloadObjectToFile(bucketName, key, filePath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testDeleteObject() {
		String bucketName = "testbucket3";
		//String key = "hello.txt";
		String key = "aaaa.txt";
		try {
			CephUtils.deleteObject(bucketName, key);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testGenerateObjectUrls() {
		String bucketName = "testbucket3";
		String key = "hello.txt";
		try {
			System.out.println(CephUtils.generateObjectUrls(bucketName, key));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
