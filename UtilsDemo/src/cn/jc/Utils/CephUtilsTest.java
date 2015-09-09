package cn.jc.Utils;

import java.io.ByteArrayInputStream;
import java.util.List;

import org.junit.Test;

import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.util.StringUtils;

public class CephUtilsTest {

	@Test
	public void testGetOwndBuckets() {
		List<Bucket> buckets = CephUtils.getOwnedBuckets();
		if (buckets != null && buckets.size() > 0) {
			for (Bucket bucket : buckets) {
				System.out.println(bucket.getName() + "\t" +
						StringUtils.fromDate(bucket.getCreationDate()));
			}
		}
	}
	
	@Test
	public void testCreateBucket() {
		Bucket bucket = CephUtils.createBucket("testbucket4");
		System.out.println(bucket.getName());
	}
	
	@Test
	public void testDeleteBucket(){
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
	public void testGetObjectListing(){
		ObjectListing objects = CephUtils.getObjectListing("testbucket3");
		do {
			for (S3ObjectSummary objectSummary : objects.getObjectSummaries()) {
				System.out.println(objectSummary.getKey() + "\t" +
			objectSummary.getSize() + "\t" + StringUtils.fromDate(objectSummary.getLastModified()));
			}
		} while (objects.isTruncated());
	}
	
	@Test
	public void testCreateObject(){
		String bucketName = "testbucket3";
		String key = "hello.txt";
		ByteArrayInputStream input = new ByteArrayInputStream("Swicth Hello World!".getBytes());
		try {
			CephUtils.createObject(bucketName, key, input);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testSetObjectACL(){
		String bucketName = "testbucket3";
		String key = "hello.txt";
		try {
			CephUtils.setObjectACL(bucketName, key, CannedAccessControlList.PublicRead);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testDownloadObjectToFile(){
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
	public void testDeleteObject(){
		String bucketName = "testbucket3";
		String key = "hello.txt";
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
