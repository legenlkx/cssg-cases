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
public class {{name}} {
    {{#defines}}
    {{{snippet}}}
    {{/defines}}

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

    {{#methods}}
    private void {{name}}()
    {
        {{{snippet}}}
    }
    {{/methods}}

    {{^isDemo}}
    private void initService() {
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        CosXmlServiceConfig serviceConfig = new CosXmlServiceConfig.Builder()
            .isHttps(true)
            .setRegion("{{{region}}}")
            .builder();

        QCloudCredentialProvider credentialProvider = new ShortTimeCredentialProvider({{{secretId}}}, {{{secretKey}}}, 3600);
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
        {{#setup}}
        {{name}}();
        {{/setup}}
    }

    @After public void tearDown() {
        {{#teardown}}
        {{name}}();
        {{/teardown}}
    }
    {{/isDemo}}

    @Test public void test{{name}}() {
        {{#steps}}
        {{name}}();
        {{/steps}}
    }
}
