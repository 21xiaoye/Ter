package com.cabin.ter.adapter;

import com.cabin.ter.constants.constants.BucketNameConstants;
import com.cabin.ter.constants.enums.BucketInfoEnum;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.Headers;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.BasicSessionCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpMethodName;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.model.Bucket;
import com.qcloud.cos.model.CreateBucketRequest;
import com.qcloud.cos.model.GeneratePresignedUrlRequest;
import com.qcloud.cos.model.ResponseHeaderOverrides;
import com.qcloud.cos.region.Region;
import com.qcloud.cos.utils.DateUtils;
import com.qcloud.cos.utils.Jackson;
import com.tencent.cloud.CosStsClient;
import com.tencent.cloud.Policy;
import com.tencent.cloud.Response;
import com.tencent.cloud.Statement;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.*;

@Component
public class TxObjectStorageAdapter  {
    private static volatile COSClient singletonCOSClient;
    private final static String SECRET_ID  = "";
    private final static String SECRET_KEY = "";
    private final static String REGION = "ap-guangzhou";
    static{
        if(Objects.isNull(singletonCOSClient)){
            synchronized (COSClient.class){
                if (Objects.isNull(singletonCOSClient)){
                    COSCredentials cred = new BasicCOSCredentials(SECRET_ID, SECRET_KEY);
                    ClientConfig clientConfig = new ClientConfig(new Region(REGION));
                    singletonCOSClient = new COSClient(cred, clientConfig);
                }
            }
        }
    }

    /**
     * 上传链接
     *
     * @param absolutePath
     * @return
     */
    public  String generateUploadUrl(String absolutePath) {
        Date expirationTime = new Date(System.currentTimeMillis() + 30 * 60 * 1000);
        Map<String, String> headers = new HashMap<>();
        Map<String, String> params = new HashMap<>();

        URL url = singletonCOSClient.generatePresignedUrl(BucketNameConstants.USER_AVATAR_BUCKET, absolutePath, expirationTime, HttpMethodName.PUT, headers, params);
        return url.toString();
    }
    /**
     * 创建存储桶
     *
     * @param bucketEnum
     * @return
     */

    public Bucket createdBucket(BucketInfoEnum bucketEnum) {
        CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketEnum.getBucketName());
        createBucketRequest.setCannedAcl(bucketEnum.getAccess());
        Bucket bucket = singletonCOSClient.createBucket(createBucketRequest);
        return bucket;
    }

    /**
     * 删除存储桶
     *
     * @param bucketEnum
     */

    public void deleteBucket(BucketInfoEnum bucketEnum) {
        singletonCOSClient.deleteBucket(bucketEnum.getBucketName());
    }

    /**
     * 判断存储桶是否存在
     *
     * @param bucketEnum
     * @return
     */

    public boolean judgeBucketExist(BucketInfoEnum bucketEnum) {
        return singletonCOSClient.doesBucketExist(bucketEnum.getBucketName());
    }

    /**
     * 下载链接
     *
     * @param bucketName
     * @param key
     * @return
     */
    public String zeroHourDownload(String bucketName,String key){
        COSClient cosClient = this.createCOSClient();

        GeneratePresignedUrlRequest req =
                new GeneratePresignedUrlRequest(bucketName, key, HttpMethodName.GET);

        ResponseHeaderOverrides responseHeaders = new ResponseHeaderOverrides();
        String responseContentType = "image/x-icon";
        String responseContentLanguage = "zh-CN";

        String responseContentDispositon = key;
        String responseCacheControl = "no-cache";
        String cacheExpireStr =
                DateUtils.formatRFC822Date(new Date(System.currentTimeMillis() + 24L * 3600L * 1000L));
        responseHeaders.setContentType(responseContentType);
        responseHeaders.setContentLanguage(responseContentLanguage);
        responseHeaders.setContentDisposition(responseContentDispositon);
        responseHeaders.setCacheControl(responseCacheControl);
        responseHeaders.setExpires(cacheExpireStr);

        req.setResponseHeaders(responseHeaders);

        req.putCustomRequestHeader(Headers.HOST, cosClient.getClientConfig().getEndpointBuilder().buildGeneralApiEndpoint(bucketName));

        URL url = cosClient.generatePresignedUrl(req);
        cosClient.shutdown();
        return url.toString();
    }


    private COSClient createCOSClient() {
        Response credential = this.getBasicCredential();
        String tmpSecretId = credential.credentials.tmpSecretId;
        String tmpSecretKey = credential.credentials.tmpSecretKey;
        String sessionToken = credential.credentials.sessionToken;

        COSCredentials cred = new BasicSessionCredentials(tmpSecretId, tmpSecretKey, sessionToken);

        ClientConfig clientConfig = new ClientConfig();

        clientConfig.setRegion(new Region("ap-guangzhou"));

        clientConfig.setHttpProtocol(HttpProtocol.https);
        clientConfig.setSocketTimeout(30*1000);
        clientConfig.setConnectionTimeout(30*1000);

        return new COSClient(cred, clientConfig);
    }

    private Response getBasicCredential(){
        TreeMap<String, Object> config = new TreeMap<>();
        try {
            config.put("secretId", SECRET_ID);
            config.put("secretKey", SECRET_KEY);

            // 初始化 policy
            Policy policy = new Policy();


            config.put("durationSeconds", 1800);
            config.put("bucket", BucketNameConstants.USER_AVATAR_BUCKET);
            config.put("region", "ap-guangzhou");

            Statement statement = new Statement();
            statement.setEffect("allow");
            statement.addActions(new String[]{
                    "cos:PutObject",
                    // 表单上传、小程序上传
                    "cos:PostObject",
                    // 分块上传
                    "cos:InitiateMultipartUpload",
                    "cos:ListMultipartUploads",
                    "cos:ListParts",
                    "cos:UploadPart",
                    "cos:CompleteMultipartUpload",
                    // 处理相关接口一般为数据万象产品 权限中以ci开头
                    // 创建媒体处理任务
                    "ci:CreateMediaJobs",
                    // 文件压缩
                    "ci:CreateFileProcessJobs"
            });

            statement.addResources(new String[]{
                    "qcs::cos:ap-guangzhou:uid/1313538777:user-avatar-1313538777/*",
                    "qcs::ci:ap-guangzhou:uid/1313538777:bucket/user-avatar-1313538777/*"});

            policy.addStatement(statement);
            config.put("policy", Jackson.toJsonPrettyString(policy));

            Response response = CosStsClient.getCredential(config);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("no valid secret !");
        }
    }
    @PreDestroy
    private void clientShutdown(){
        singletonCOSClient.shutdown();
    }
}
