package com.cabin.ter.util;

import com.qcloud.cos.COSClient;
import org.roaringbitmap.RoaringBitmap;

import java.util.Objects;

/**
 * @author xiaoye
 * @date Created in 2024-05-26 20:38
 */
public class BitMapUtil{

    private static volatile RoaringBitmap bitMap;
    static {
        if(Objects.isNull(bitMap)){
            synchronized (COSClient.class){
                if (Objects.isNull(bitMap)){
                    bitMap = new RoaringBitmap();
                }
            }
        }
    }


}
