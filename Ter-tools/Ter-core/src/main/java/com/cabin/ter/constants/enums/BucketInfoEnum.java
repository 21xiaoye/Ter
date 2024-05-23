package com.cabin.ter.constants.enums;

import com.cabin.ter.constants.constants.BucketNameConstants;
import com.qcloud.cos.model.CannedAccessControlList;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author xiaoye
 * @date Created in 2024-05-21 20:21
 */
@Getter
@AllArgsConstructor
public enum BucketInfoEnum {
    USER_AVATAR_BUCKET(BucketNameConstants.USER_AVATAR_BUCKET,CannedAccessControlList.PublicRead,"ap-guangzhou"),

    TEST_BUCKET("text-bucket-1313538777",CannedAccessControlList.PublicRead,"ap-guangzhou");
    /**
     * 存储桶名称
     */
    private String bucketName;
    /**
     * 存储桶权限
     */
    private CannedAccessControlList access;
    /**
     * 存储桶所在区域
     */
    private String region;
}
