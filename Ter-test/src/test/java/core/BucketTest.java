package core;

import com.cabin.ter.TerApplication;
import com.cabin.ter.adapter.TxObjectStorageAdapter;
import com.cabin.ter.constants.enums.BucketInfoEnum;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


import static org.junit.Assert.assertEquals;

@SpringBootTest(classes = TerApplication.class)
@RunWith(SpringRunner.class)
public class BucketTest {
    @Autowired
    private TxObjectStorageAdapter cosBucket;
    @Test
    public void testBucket(){
        boolean bucketExist = cosBucket.judgeBucketExist(BucketInfoEnum.USER_AVATAR_BUCKET);
        assertEquals(true, bucketExist);
        boolean bucketExist1 = cosBucket.judgeBucketExist(BucketInfoEnum.TEST_BUCKET);
        assertEquals(false, bucketExist1);
        cosBucket.createdBucket(BucketInfoEnum.TEST_BUCKET);
        boolean bucketExist2 = cosBucket.judgeBucketExist(BucketInfoEnum.TEST_BUCKET);
        assertEquals(true, bucketExist2);
        cosBucket.deleteBucket(BucketInfoEnum.TEST_BUCKET);
        boolean bucketExist3 = cosBucket.judgeBucketExist(BucketInfoEnum.TEST_BUCKET);
        assertEquals(false, bucketExist3);
    }

//    @Test
//    public void putBucket(){
//        String key = "abc/def.txt";
//
//        int inputStreamLength = 1024 * 1024;
//        byte data[] = new byte[inputStreamLength];
//        URL url = cosBucket.putInputStream(BucketInfoEnum.USER_AVATAR_BUCKET, data, inputStreamLength);
//        System.out.println(url);
//        String previewLink = cosBucket.getPreviewLink(BucketInfoEnum.USER_AVATAR_BUCKET, key);
//        System.out.println(previewLink);
//    }

}
