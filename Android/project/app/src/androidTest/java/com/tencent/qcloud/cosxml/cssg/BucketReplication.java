package com.tencent.qcloud.cosxml.cssg;

import android.support.test.runner.AndroidJUnit4;
import android.support.test.InstrumentationRegistry;

import com.tencent.cos.xml.*;
import com.tencent.cos.xml.common.*;
import com.tencent.cos.xml.exception.*;
import com.tencent.cos.xml.listener.*;
import com.tencent.cos.xml.model.*;
import com.tencent.cos.xml.model.object.*;
import com.tencent.cos.xml.model.bucket.*;
import com.tencent.cos.xml.model.tag.*;
import com.tencent.cos.xml.transfer.*;
import com.tencent.qcloud.core.auth.*;
import com.tencent.qcloud.core.common.*;
import com.tencent.qcloud.core.http.*;
import com.tencent.cos.xml.model.service.*;
import com.tencent.qcloud.cosxml.cssg.InitCustomProvider.MyCredentialProvider;

import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;

import android.content.Context;
import android.util.Log;

import java.net.*;
import java.util.*;
import java.nio.charset.Charset;
import java.io.*;

@RunWith(AndroidJUnit4.class)
public class BucketReplication {

    private Context context;
    private CosXmlService cosXmlService;

    private static void assertError(Exception e, boolean isMatch) {
        if (!isMatch) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private static void assertError(Exception e) {
        assertError(e, false);
    }

    private String uploadId;
    private String part1Etag;

    private void PutBucket()
    {
        String bucket = "bucket-cssg-android-temp-1253653367";
        PutBucketRequest putBucketRequest = new PutBucketRequest(bucket);
        
        //定义存储桶的 ACL 属性。有效值：private，public-read-write，public-read；默认值：private
        putBucketRequest.setXCOSACL("private");
        
        //赋予被授权者读的权限
        ACLAccount readACLS = new ACLAccount();
        readACLS.addAccount("1278687956", "1278687956");
        putBucketRequest.setXCOSGrantRead(readACLS);
        
        //赋予被授权者写的权限
        ACLAccount writeACLS = new ACLAccount();
        writeACLS.addAccount("1278687956", "1278687956");
        putBucketRequest.setXCOSGrantRead(writeACLS);
        
        //赋予被授权者读写的权限
        ACLAccount writeandReadACLS = new ACLAccount();
        writeandReadACLS.addAccount("1278687956", "1278687956");
        putBucketRequest.setXCOSGrantRead(writeandReadACLS);
        //设置签名校验Host, 默认校验所有Header
        Set<String> headerKeys = new HashSet<>();
        headerKeys.add("Host");
        putBucketRequest.setSignParamsAndHeaders(null, headerKeys);
        //使用同步方法
        try {
            PutBucketResult putBucketResult = cosXmlService.putBucket(putBucketRequest);
        } catch (CosXmlClientException e) {
            e.printStackTrace();
            assertError(e);
        } catch (CosXmlServiceException e) {
            e.printStackTrace();
            assertError(e, e.getStatusCode() == 409);
        }
        
        // 使用异步回调请求
        cosXmlService.putBucketAsync(putBucketRequest, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                // todo Put Bucket success
                PutBucketResult putBucketResult = (PutBucketResult)result;
            }
        
            @Override
            public void onFail(CosXmlRequest cosXmlRequest, CosXmlClientException clientException, CosXmlServiceException serviceException)  {
                // todo Put Bucket failed because of CosXmlClientException or CosXmlServiceException...
            }
        });
    }
    private void PutBucketVersioning()
    {
        String bucket = "bucket-cssg-android-temp-1253653367"; //格式：BucketName-APPID
        PutBucketVersioningRequest putBucketVersioningRequest = new PutBucketVersioningRequest(bucket);
        putBucketVersioningRequest.setEnableVersion(true); //true: 开启版本控制; false:暂停版本控制
        //设置签名校验Host, 默认校验所有Header
        Set<String> headerKeys = new HashSet<>();
        headerKeys.add("Host");
        putBucketVersioningRequest.setSignParamsAndHeaders(null, headerKeys);
        // 使用同步方法
        try {
            PutBucketVersioningResult putBucketVersioningResult = cosXmlService.putBucketVersioning(putBucketVersioningRequest);
        } catch (CosXmlClientException e) {
            e.printStackTrace();
            assertError(e);
        } catch (CosXmlServiceException e) {
            e.printStackTrace();
            assertError(e);
        }
        // 使用异步回调请求
        cosXmlService.putBucketVersionAsync(putBucketVersioningRequest, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                // todo PUT Bucket versioning success
                PutBucketVersioningResult putBucketVersioningResult = (PutBucketVersioningResult)result;
            }
        
            @Override
            public void onFail(CosXmlRequest cosXmlRequest, CosXmlClientException clientException, CosXmlServiceException serviceException)  {
                // todo PUT Bucket versioning failed because of CosXmlClientException or CosXmlServiceException...
            }
        });
        
    }
    private void PutBucketReplication()
    {
        String bucket = "bucket-cssg-android-temp-1253653367"; //格式：BucketName-APPID
        String ownerUin = "1278687956"; //发起者身份标示:OwnerUin
        String subUin = "1278687956"; //发起者身份标示:SubUin
        PutBucketReplicationRequest putBucketReplicationRequest = new PutBucketReplicationRequest(bucket);
        putBucketReplicationRequest.setReplicationConfigurationWithRole(ownerUin, subUin);
        PutBucketReplicationRequest.RuleStruct ruleStruct = new PutBucketReplicationRequest.RuleStruct();
        ruleStruct.id = "replication_01"; //用来标注具体 Rule 的名称
        ruleStruct.isEnable = true; //标识 Rule 是否生效 :true, 生效； false, 不生效
        ruleStruct.region = "ap-beijing"; //目标存储桶地域信息
        ruleStruct.bucket = "bucket-cssg-assist-1253653367";  // 目标存储桶
        ruleStruct.prefix = "34"; //前缀匹配策略，
        putBucketReplicationRequest.setReplicationConfigurationWithRule(ruleStruct);
        //设置签名校验Host, 默认校验所有Header
        Set<String> headerKeys = new HashSet<>();
        headerKeys.add("Host");
        putBucketReplicationRequest.setSignParamsAndHeaders(null, headerKeys);
        // 使用同步方法
        try {
            PutBucketReplicationResult putBucketReplicationResult = cosXmlService.putBucketReplication(putBucketReplicationRequest);
        } catch (CosXmlClientException e) {
            e.printStackTrace();
            assertError(e);
        } catch (CosXmlServiceException e) {
            e.printStackTrace();
            assertError(e);
        }
        // 使用异步回调请求
        cosXmlService.putBucketReplicationAsync(putBucketReplicationRequest, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                // todo PUT Bucket replication success
                PutBucketReplicationResult putBucketReplicationResult = (PutBucketReplicationResult)result;
            }
        
            @Override
            public void onFail(CosXmlRequest cosXmlRequest, CosXmlClientException clientException, CosXmlServiceException serviceException)  {
                // todo PUT Bucket replication failed because of CosXmlClientException or CosXmlServiceException...
            }
        });
    }
    private void GetBucketReplication()
    {
        String bucket = "bucket-cssg-android-temp-1253653367"; //格式：BucketName-APPID
        GetBucketReplicationRequest getBucketReplicationRequest = new GetBucketReplicationRequest(bucket);
        //设置签名校验Host, 默认校验所有Header
        Set<String> headerKeys = new HashSet<>();
        headerKeys.add("Host");
        getBucketReplicationRequest.setSignParamsAndHeaders(null, headerKeys);
        // 使用同步方法
        try {
            GetBucketReplicationResult getBucketReplicationResult = cosXmlService.getBucketReplication(getBucketReplicationRequest);
        } catch (CosXmlClientException e) {
            e.printStackTrace();
            assertError(e);
        } catch (CosXmlServiceException e) {
            e.printStackTrace();
            assertError(e);
        }
        // 使用异步回调请求
        cosXmlService.getBucketReplicationAsync(getBucketReplicationRequest, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                // todo GET Bucket replication success
                GetBucketReplicationResult getBucketReplicationResult = (GetBucketReplicationResult)result;
            }
        
            @Override
            public void onFail(CosXmlRequest cosXmlRequest, CosXmlClientException clientException, CosXmlServiceException serviceException)  {
                // todo GET Bucket replication failed because of CosXmlClientException or CosXmlServiceException...
            }
        });
        
    }
    private void DeleteBucketReplication()
    {
        String bucket = "bucket-cssg-android-temp-1253653367"; //格式：BucketName-APPID
        DeleteBucketReplicationRequest deleteBucketReplicationRequest = new DeleteBucketReplicationRequest(bucket);
        //设置签名校验Host, 默认校验所有Header
        Set<String> headerKeys = new HashSet<>();
        headerKeys.add("Host");
        deleteBucketReplicationRequest.setSignParamsAndHeaders(null, headerKeys);
        // 使用同步方法
        try {
            DeleteBucketReplicationResult deleteBucketReplicationResult = cosXmlService.deleteBucketReplication(deleteBucketReplicationRequest);
        } catch (CosXmlClientException e) {
            e.printStackTrace();
            assertError(e);
        } catch (CosXmlServiceException e) {
            e.printStackTrace();
            assertError(e);
        }
        // 使用异步回调请求
        cosXmlService.deleteBucketReplicationAsync(deleteBucketReplicationRequest, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                // todo DELETE Bucket replication success
                DeleteBucketReplicationResult deleteBucketReplicationResult = (DeleteBucketReplicationResult)result;
            }
        
            @Override
            public void onFail(CosXmlRequest cosXmlRequest, CosXmlClientException clientException, CosXmlServiceException serviceException)  {
                // todo DELETE Bucket replication failed because of CosXmlClientException or CosXmlServiceException...
            }
        });
        
    }
    private void DeleteBucket()
    {
        String bucket = "bucket-cssg-android-temp-1253653367"; //格式：BucketName-APPID
        DeleteBucketRequest deleteBucketRequest = new DeleteBucketRequest(bucket);
        //设置签名校验Host, 默认校验所有Header
        Set<String> headerKeys = new HashSet<>();
        headerKeys.add("Host");
        deleteBucketRequest.setSignParamsAndHeaders(null, headerKeys);
        // 使用同步方法
        try {
            DeleteBucketResult deleteBucketResult = cosXmlService.deleteBucket(deleteBucketRequest);
        } catch (CosXmlClientException e) {
            e.printStackTrace();
            assertError(e);
        } catch (CosXmlServiceException e) {
            e.printStackTrace();
            assertError(e);
        }
        
        // 使用异步回调请求
        cosXmlService.deleteBucketAsync(deleteBucketRequest, new CosXmlResultListener() {
            @Override
            public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                // todo Delete Bucket success
          DeleteBucketResult deleteBucketResult = (DeleteBucketResult)result;
            }
        
            @Override
            public void onFail(CosXmlRequest cosXmlRequest, CosXmlClientException clientException, CosXmlServiceException serviceException)  {
                // todo Delete Bucket failed because of CosXmlClientException or CosXmlServiceException...
            }
        });
    }

    private void initService() {
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        CosXmlServiceConfig serviceConfig = new CosXmlServiceConfig.Builder()
            .isHttps(true)
            .setRegion("ap-guangzhou")
            .builder();

        QCloudCredentialProvider credentialProvider = new ShortTimeCredentialProvider(BuildConfig.COS_SECRET_ID, BuildConfig.COS_SECRET_KEY, 3600);
        cosXmlService = new CosXmlService(context, serviceConfig, credentialProvider);

        try {
            File srcFile = new File(context.getExternalCacheDir(), "object4android");
            if (!srcFile.exists() && srcFile.createNewFile()) {
                RandomAccessFile raf = new RandomAccessFile(srcFile, "rw");
                raf.setLength(10);
                raf.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Before public void setUp() {
        initService();
        PutBucket();
    }

    @After public void tearDown() {
        DeleteBucket();
    }

    @Test public void testBucketReplication() {
        PutBucketVersioning();
        PutBucketReplication();
        GetBucketReplication();
        DeleteBucketReplication();
    }
}
