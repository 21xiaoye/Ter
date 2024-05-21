package core;

import com.cabin.ter.TerApplication;
import com.cabin.ter.constants.enums.BucketEnum;
import com.cabin.ter.os.third.bucket.CosBucket;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URL;

import static org.junit.Assert.assertEquals;

@SpringBootTest(classes = TerApplication.class)
@RunWith(SpringRunner.class)
public class BucketTest {
    @Autowired
    private CosBucket cosBucket;
    @Test
    public void testBucket(){
        boolean bucketExist = cosBucket.judgeBucketExist(BucketEnum.USER_AVATAR_BUCKET);
        assertEquals(true, bucketExist);
        boolean bucketExist1 = cosBucket.judgeBucketExist(BucketEnum.TEST_BUCKET);
        assertEquals(false, bucketExist1);
        cosBucket.createdBucket(BucketEnum.TEST_BUCKET);
        boolean bucketExist2 = cosBucket.judgeBucketExist(BucketEnum.TEST_BUCKET);
        assertEquals(true, bucketExist2);
        cosBucket.deleteBucket(BucketEnum.TEST_BUCKET);
        boolean bucketExist3 = cosBucket.judgeBucketExist(BucketEnum.TEST_BUCKET);
        assertEquals(false, bucketExist3);
    }

    @Test
    public void putBucket(){
        String key = "abc/def.txt";

        int inputStreamLength = 1024 * 1024;
        byte data[] = new byte[inputStreamLength];
        URL url = cosBucket.putInputStream(BucketEnum.USER_AVATAR_BUCKET, data, inputStreamLength);
        System.out.println(url);
        String previewLink = cosBucket.getPreviewLink(BucketEnum.USER_AVATAR_BUCKET, key);
        System.out.println(previewLink);
    }

}
