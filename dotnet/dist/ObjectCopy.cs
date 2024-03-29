using COSXML.Common;
using COSXML.CosException;
using COSXML.Model;
using COSXML.Model.Object;
using COSXML.Model.Tag;
using COSXML.Model.Bucket;
using COSXML.Model.Service;
using COSXML.Utils;
using COSXML.Auth;
using COSXML.Transfer;
using NUnit.Framework;
using System;
using COSXML;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Reflection;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace COSSample
{
    public class ObjectCopySample {


      string uploadId;
      string etag;

      public void CopyObject()
      {
        CosXmlConfig config = new CosXmlConfig.Builder()
          .SetConnectionTimeoutMs(60000)  //设置连接超时时间，单位毫秒，默认45000ms
          .SetReadWriteTimeoutMs(40000)  //设置读写超时时间，单位毫秒，默认45000ms
          .IsHttps(true)  //设置默认 HTTPS 请求
          .SetAppid("1253653367") //设置腾讯云账户的账户标识 APPID
          .SetRegion("ap-guangzhou") //设置一个默认的存储桶地域
          .Build();
        
        string secretId = Environment.GetEnvironmentVariable("COS_KEY");   //云 API 密钥 SecretId
        string secretKey = Environment.GetEnvironmentVariable("COS_SECRET"); //云 API 密钥 SecretKey
        long durationSecond = 600;          //每次请求签名有效时长，单位为秒
        QCloudCredentialProvider qCloudCredentialProvider = new DefaultQCloudCredentialProvider(secretId, 
          secretKey, durationSecond);
        
        CosXml cosXml = new CosXmlServer(config, qCloudCredentialProvider);
        
        try
        {
          string sourceAppid = "1253653367"; //账号 appid
          string sourceBucket = "bucket-cssg-test-1253653367"; //"源对象所在的存储桶
          string sourceRegion = "ap-guangzhou"; //源对象的存储桶所在的地域
          string sourceKey = "sourceObject"; //源对象键
          //构造源对象属性
          CopySourceStruct copySource = new CopySourceStruct(sourceAppid, sourceBucket, 
            sourceRegion, sourceKey);
        
          string bucket = "bucket-cssg-test-1253653367"; //存储桶，格式：BucketName-APPID
          string key = "object4dotnet"; //对象在存储桶中的位置，即称对象键
          CopyObjectRequest request = new CopyObjectRequest(bucket, key);
          //设置签名有效时长
          request.SetSign(TimeUtils.GetCurrentTime(TimeUnit.SECONDS), 600);
          //设置拷贝源
          request.SetCopySource(copySource);
          //设置是否拷贝还是更新,此处是拷贝
          request.SetCopyMetaDataDirective(COSXML.Common.CosMetaDataDirective.COPY);
          //执行请求
          CopyObjectResult result = cosXml.CopyObject(request);
          //请求成功
          Console.WriteLine(result.GetResultInfo());
        }
        catch (COSXML.CosException.CosClientException clientEx)
        {
          //请求失败
          Console.WriteLine("CosClientException: " + clientEx);
          Assert.Null(clientEx);
        }
        catch (COSXML.CosException.CosServerException serverEx)
        {
          //请求失败
          Console.WriteLine("CosServerException: " + serverEx.GetInfo());
          Assert.Null(serverEx);
        }
      }   

      public void InitMultiUpload()
      {
        CosXmlConfig config = new CosXmlConfig.Builder()
          .SetConnectionTimeoutMs(60000)  //设置连接超时时间，单位毫秒，默认45000ms
          .SetReadWriteTimeoutMs(40000)  //设置读写超时时间，单位毫秒，默认45000ms
          .IsHttps(true)  //设置默认 HTTPS 请求
          .SetAppid("1253653367") //设置腾讯云账户的账户标识 APPID
          .SetRegion("ap-guangzhou") //设置一个默认的存储桶地域
          .Build();
        
        string secretId = Environment.GetEnvironmentVariable("COS_KEY");   //云 API 密钥 SecretId
        string secretKey = Environment.GetEnvironmentVariable("COS_SECRET"); //云 API 密钥 SecretKey
        long durationSecond = 600;          //每次请求签名有效时长，单位为秒
        QCloudCredentialProvider qCloudCredentialProvider = new DefaultQCloudCredentialProvider(secretId, 
          secretKey, durationSecond);
        
        CosXml cosXml = new CosXmlServer(config, qCloudCredentialProvider);
        
        try
        {
          string bucket = "bucket-cssg-test-1253653367"; //存储桶，格式：BucketName-APPID
          string key = "object4dotnet"; //对象在存储桶中的位置，即称对象键
          InitMultipartUploadRequest request = new InitMultipartUploadRequest(bucket, key);
          //设置签名有效时长
          request.SetSign(TimeUtils.GetCurrentTime(TimeUnit.SECONDS), 600);
          //执行请求
          InitMultipartUploadResult result = cosXml.InitMultipartUpload(request);
          //请求成功
          string uploadId = result.initMultipartUpload.uploadId; //用于后续分块上传的 uploadId
          this.uploadId = uploadId;
          Console.WriteLine(result.GetResultInfo());
        }
        catch (COSXML.CosException.CosClientException clientEx)
        {
          //请求失败
          Console.WriteLine("CosClientException: " + clientEx);
          Assert.Null(clientEx);
        }
        catch (COSXML.CosException.CosServerException serverEx)
        {
          //请求失败
          Console.WriteLine("CosServerException: " + serverEx.GetInfo());
          Assert.Null(serverEx);
        }
      }   

      public void UploadPartCopy()
      {
        CosXmlConfig config = new CosXmlConfig.Builder()
          .SetConnectionTimeoutMs(60000)  //设置连接超时时间，单位毫秒，默认45000ms
          .SetReadWriteTimeoutMs(40000)  //设置读写超时时间，单位毫秒，默认45000ms
          .IsHttps(true)  //设置默认 HTTPS 请求
          .SetAppid("1253653367") //设置腾讯云账户的账户标识 APPID
          .SetRegion("ap-guangzhou") //设置一个默认的存储桶地域
          .Build();
        
        string secretId = Environment.GetEnvironmentVariable("COS_KEY");   //云 API 密钥 SecretId
        string secretKey = Environment.GetEnvironmentVariable("COS_SECRET"); //云 API 密钥 SecretKey
        long durationSecond = 600;          //每次请求签名有效时长，单位为秒
        QCloudCredentialProvider qCloudCredentialProvider = new DefaultQCloudCredentialProvider(secretId, 
          secretKey, durationSecond);
        
        CosXml cosXml = new CosXmlServer(config, qCloudCredentialProvider);
        
        try
        {
          string sourceAppid = "1253653367"; //账号 appid
          string sourceBucket = "bucket-cssg-test-1253653367"; //"源对象所在的存储桶
          string sourceRegion = "ap-guangzhou"; //源对象的存储桶所在的地域
          string sourceKey = "sourceObject"; //源对象键
          //构造源对象属性
          COSXML.Model.Tag.CopySourceStruct copySource = new CopySourceStruct(sourceAppid, 
            sourceBucket, sourceRegion, sourceKey);
        
          string bucket = "bucket-cssg-test-1253653367"; //存储桶，格式：BucketName-APPID
          string key = "object4dotnet"; //对象在存储桶中的位置，即称对象键
          string uploadId = "example-uploadId"; //初始化分块上传返回的uploadId
          int partNumber = 1; //分块编号，必须从1开始递增
          uploadId = this.uploadId;
          UploadPartCopyRequest request = new UploadPartCopyRequest(bucket, key, 
            partNumber, uploadId);
          //设置签名有效时长
          request.SetSign(TimeUtils.GetCurrentTime(TimeUnit.SECONDS), 600);
          //设置拷贝源
          request.SetCopySource(copySource);
          //设置复制分块（指定块的范围，如 0 ~ 1M）
          request.SetCopyRange(0, 1024 * 1024);
          //执行请求
          UploadPartCopyResult result = cosXml.PartCopy(request);
          //请求成功
          //获取返回分块的eTag,用于后续CompleteMultiUploads
          this.etag = result.copyObject.eTag;
          Console.WriteLine(result.GetResultInfo());
        }
        catch (COSXML.CosException.CosClientException clientEx)
        {
          //请求失败
          Console.WriteLine("CosClientException: " + clientEx);
          Assert.Null(clientEx);
        }
        catch (COSXML.CosException.CosServerException serverEx)
        {
          //请求失败
          Console.WriteLine("CosServerException: " + serverEx.GetInfo());
          Assert.Null(serverEx);
        }
      }   

      public void CompleteMultiUpload()
      {
        CosXmlConfig config = new CosXmlConfig.Builder()
          .SetConnectionTimeoutMs(60000)  //设置连接超时时间，单位毫秒，默认45000ms
          .SetReadWriteTimeoutMs(40000)  //设置读写超时时间，单位毫秒，默认45000ms
          .IsHttps(true)  //设置默认 HTTPS 请求
          .SetAppid("1253653367") //设置腾讯云账户的账户标识 APPID
          .SetRegion("ap-guangzhou") //设置一个默认的存储桶地域
          .Build();
        
        string secretId = Environment.GetEnvironmentVariable("COS_KEY");   //云 API 密钥 SecretId
        string secretKey = Environment.GetEnvironmentVariable("COS_SECRET"); //云 API 密钥 SecretKey
        long durationSecond = 600;          //每次请求签名有效时长，单位为秒
        QCloudCredentialProvider qCloudCredentialProvider = new DefaultQCloudCredentialProvider(secretId, 
          secretKey, durationSecond);
        
        CosXml cosXml = new CosXmlServer(config, qCloudCredentialProvider);
        
        try
        {
          string bucket = "bucket-cssg-test-1253653367"; //存储桶，格式：BucketName-APPID
          string key = "object4dotnet"; //对象在存储桶中的位置，即称对象键
          string uploadId = "example-uploadId"; //初始化分块上传返回的uploadId
          uploadId = this.uploadId;
          CompleteMultipartUploadRequest request = new CompleteMultipartUploadRequest(bucket, 
            key, uploadId);
          //设置签名有效时长
          request.SetSign(TimeUtils.GetCurrentTime(TimeUnit.SECONDS), 600);
          //设置已上传的parts,必须有序，按照partNumber递增
          // request.SetPartNumberAndETag(1, "Example Etag");
          string etag = "example etag";
          etag = this.etag;
          request.SetPartNumberAndETag(1, etag);
          //执行请求
          CompleteMultipartUploadResult result = cosXml.CompleteMultiUpload(request);
          //请求成功
          Console.WriteLine(result.GetResultInfo());
        }
        catch (COSXML.CosException.CosClientException clientEx)
        {
          //请求失败
          Console.WriteLine("CosClientException: " + clientEx);
          Assert.Null(clientEx);
        }
        catch (COSXML.CosException.CosServerException serverEx)
        {
          //请求失败
          Console.WriteLine("CosServerException: " + serverEx.GetInfo());
          Assert.Null(serverEx);
        }
      }   

      [SetUp()]
      public void setup() {
      }

      [Test()]
      public void testObjectCopy() {
        CopyObject();
        InitMultiUpload();
        UploadPartCopy();
        CompleteMultiUpload();
      }

      [TearDown()]
      public void teardown() {
      }
    }
}