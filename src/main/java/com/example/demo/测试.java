package com.example.demo;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;

import cn.hutool.core.date.DateUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.DateUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Test;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 所有API接口对应业务参数sign都以key=value&拼接，非必传项如不传则不加入sign字符串中，
 * 待sign后在进行赋空值 如accountBalance（）方法放在sign字符串后
 *
 * @Auther: cuiLei
 * @Date: 2021/4/22 14:12
 * @Description:
 */
public class SaasApiDemo {

    public static String version = "1";

    public static String signType = "RSA";

    public static String encType = "DES";

    public static String merId = "6000010711";

    public static String tenantCode = "0525";

    public static String levyCode = "1000030412";

    public static String bankCode = "6000015812";

    static String charset = "UTF-8";

    public static String host = "http://47.95.0.114:20000/business/";
    //接口配置里面的ApiKey
    public static String desKey = "FE78B8C26F574DE6B7A2B3E90119FAD8";

    // 商户私钥
    public static String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKUfKjKXuXXo5jCccpkDbRIEaRdEUjhTvoy7eGz/FW6TgDsnCTdSnZkfn0hskgqkbvQaJOlay2ZoUjO1xNQIJOA6Gl5CAAcFXlOQ6Wq5iHhig/hf5jSo4VoTE9I3TXTFTk41zr+GRQPT0qrYOBCL6QQ27e3QLqMBPb4VWdNpXqXlAgMBAAECgYAbptdrN4tgWotJ8rkiNLesP77VnaRjOGr9qFtnbWRKazsbLNNXR2eLAAetmjjuvVCzYjlyNrk/QhMaA8OdemuqOFQELvztWnyjnop8NzrwUmOxJzBpnzwxrv76a1xXckgYJt3/KIoyJOR88TUoakVGh12RexDKjaGQLvtcdk91gQJBAM5y4fKSSD7E5aoLyWmBQWA3pwMJhMG6LseDYOXylcQxrG+A/hYtxS9jY91CfEvRlrvqMS6MSeL8jKzrp8+GqsUCQQDMwPf7tcRjO1qnZNrpFozi/qFlaM8q6ydlNQT2OhRh5qtcxk1tKrSrI0M1aUiYURH7Za9qCucpfrURq1HuzEChAkBrfCKpUYHttEPCkdpuZM5t0uvQR/9ngZDjbUJg1jklaZtWfnlLgM7+CigvRXNDYj6xXY/3pTC+zjBLw3GeOYXlAkAbOYU/mLmWICw7mbNpzq/I9uZq33a8VCwWzufr/Wj8Y3lhipR88XK4VqX3Ehqu9giYDrs2NGDsKYbV5JuuTsZBAkEAvgL1TYPw5wIuzuu26058H77D3UwCHygGDVWIFoJ3WShIFRrvNnLPjTlRuWukpghXeX6jo8dLCVuQlWna5oh4oQ==";
    //MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCk826JUN9kahSAHTzyChuZz0wDNAs00z5z6vkiMkItoSs8I35ZgnU21IFJZZXJw8D8LzdZLcXuBjYrshkMqtSQyaEyNu9cn5h7bvSzVuDBzwkhrRSaT48a7bYa99nUMZ7hsyXkPTII+SGoZHgysw0ei/JZ0N/Qrj9rcA+hcKuXuQIDAQAB
    // 运营公钥
    public static String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCbfWSbWP0tXtPSxgXX4kcdG5G8hPFdrTMEu4xVDDJYRAQ8W2wHl6Ts1Q+JYXaVbvgEqbqrRY6ezWL2TJiA7iobb8dFI53ED+eSVHJg1Sc5HO/P/n9tlMWpR+t2QsuB9BRc6k7u9M9w9yeIZ0meq/jjjVt1Ut9ZV/hpifO+BVeO0QIDAQAB";

    public static void main(String[] args) throws Exception {
        /**
         * 签约相关接口
         */
//        sohoSign();
//        sohoQuery();
//        rescindSoho();
//        batchSoho();
        /**
         * 商户相关接口
         */
//        accountBalance();
//        calculation();
//        accTransDetail();
//        levyInfo();
//        merchantInfo();
//        accNoInfo();
//        openMerchant();
//        selectMerchant();
//        openMerchantV2();
//        supplementInfo();
//        selectMerchantV2();
        /**
         * 充值相关接口
         */
//        accReviewRecord();
        /**
         * 项目相关接口
         */
//        createProject();
//        selectProjectMcc();
        /**
         * 付款交易相关接口
         */
//        payment();
//        queryPayment();
//        downloadReceipt();
//        downloadByDateReceipt();
//        merplatOrderPageQuery();
        /**
         * 开票相关接口
         */
//        invoiceType();
//        invoiceMerSumAmt();
//        invoiceOpen();
//        invoicePic();
//        invoiceInfo();
        /**
         * 回调接口
         */
//        notifyPay();
//        notifySoho();
//        notifyReview();
    }

    /**
     * @return
     * @Author cuiLei
     * @Description 费率计算接口
     * @Date 14:45 2021/4/22
     * @Param
     **/
    public static void calculation() throws Exception {
        String reqId = DateUtils.formatDate(new Date(), "yyyyMMddHHmmss");
        BigDecimal amt = new BigDecimal(10000);
        String levyCode = "1000030412";
        Map<String, Object> params = new HashMap<>();
        params.put("amt", amt.toString());
        params.put("levyCode", levyCode);
        params.put("merId", merId);
        params.put("version", version);
        params.put("reqId", reqId);
        String linkString = createLinkString(params, false);
        String sign = sign(linkString, privateKey);
        params.put("sign", sign);
        params.put("encType", encType);
        params.put("signType", signType);

        String s = JSON.toJSONString(params);
        System.out.println(s);
        String resData = sendPost(host + "feeRate/calculation/" + tenantCode, s);
        System.out.println(resData);
        JSONObject jsonObject = JSON.parseObject(resData);
        Map<String, Object> signMap = new HashMap<>();
        signMap.put("amt", jsonObject.getString("amt"));
        signMap.put("feeRate", jsonObject.getString("feeRate"));
        signMap.put("reqId", jsonObject.getString("reqId"));
        String signString = sortKey(signMap);
        boolean sign1 = verify(signString, jsonObject.getString("sign"), publicKey);
        System.out.println(sign1);

    }


    /**
     * @return
     * @Author cuiLei
     * @Description soho签约
     * @Date 14:10 2021/4/25
     * @Param
     **/
    @Test
    public static void sohoSign() throws Exception {
        String reqId = DateUtils.formatDate(new Date(), "yyyyMMddHHmmss");
        Map<String, Object> params = new HashMap<>();
        params.put("name", encrypt("齐二宇", desKey));
        params.put("levyCode", levyCode);
        params.put("merId", merId);
        params.put("version", version);
        params.put("reqId", reqId);

        params.put("cardNo", encrypt("18511111111", desKey));
        params.put("idCard", encrypt("411324198706242222", desKey));
        params.put("mobile", encrypt("18511111111", desKey));
        params.put("idCardType", "A");
//        params.put("remark1","123");
//        params.put("remark2","234");
        String linkString = createLinkString(params, false);
        String sign = sign(linkString, privateKey);

//        params.put("idCardFront", bytesToHexString(fileRead("D:\\11.jpg")));
//        params.put("idCardBack", bytesToHexString(fileRead("D:\\11.jpg")));
        params.put("sign", sign);
        params.put("encType", encType);
        params.put("signType", signType);

        String s = JSON.toJSONString(params);
        System.out.println(s);
        String resData = sendPost(host + "soho/singleSoho/" + tenantCode, s);
        System.out.println(resData);
        JSONObject jsonObject = JSON.parseObject(resData);
        Map<String, Object> signMap = new HashMap<>();
        signMap.put("remark1", jsonObject.getString("remark1"));
        signMap.put("remark2", jsonObject.getString("remark2"));
        signMap.put("reqId", jsonObject.getString("reqId"));
        String signString = sortKey(signMap);
        boolean sign1 = verify(signString, jsonObject.getString("sign"), publicKey);
        System.out.println(sign1);
    }

    /**
     * @return
     * @Author cuiLei
     * @Description soho解约
     * @Date 14:10 2021/4/25
     * @Param
     **/
    @Test
    public static void rescindSoho() throws Exception {
        String reqId = DateUtils.formatDate(new Date(), "yyyyMMddHHmmss");
        Map<String, Object> params = new HashMap<>();
        params.put("name", "王哈哈");
        params.put("idCard", "411324198706246321");
        params.put("merId", merId);
        params.put("levyCode", levyCode);
        params.put("version", version);
        params.put("reqId", reqId);
        String linkString = createLinkString(params, false);
        System.out.println(linkString);
        String sign = sign(linkString, privateKey);
        params.put("sign", sign);
        params.put("encType", encType);
        params.put("signType", signType);
        String s = JSON.toJSONString(params);
        System.out.println(s);
        String resData = cn.hutool.http.HttpUtil.post(host + "soho/rescindSoho/" + tenantCode, s);
        System.out.println(resData);
        JSONObject jsonObject = JSON.parseObject(resData);
        Map<String, Object> signMap = new HashMap<>();
        signMap.put("merId", jsonObject.getString("merId"));
        signMap.put("levyCode", jsonObject.getString("levyCode"));
        signMap.put("name", jsonObject.getString("name"));
        signMap.put("idCard", jsonObject.getString("idCard"));
        signMap.put("reqId", jsonObject.getString("reqId"));
        String signString = sortKey(signMap);
        //boolean sign1 = verify(signString, jsonObject.getString("sign"), publicKey);
        //System.out.println(sign1);
    }
    /**
     * H5签约查询接口
     * @throws Exception
     */
    public static void h5sohoQuery() throws Exception {
        String reqId = formatDate(new Date(), "yyyyMMddHHmmss");
        Map<String, Object> params = new HashMap<>();
        params.put("requestId", "213asdwq");
        params.put("merId", merId);
        params.put("version", version);
        params.put("reqId", reqId);
        params.put("levyCode", levyCode);
        String linkString = createLinkString(params, false);
        System.out.println(linkString);
        String sign = sign(linkString, privateKey);
        params.put("sign", sign);
        params.put("encType", encType);
        params.put("signType", signType);

        String s = JSON.toJSONString(params);
        System.out.println(s);
        String resData = sendPost(host + "soho/queryHFiveSoho/" + tenantCode, s);
        System.out.println(resData);
        JSONObject jsonObject = JSON.parseObject(resData);
        Map<String, Object> signMap = new HashMap<>();
        signMap.put("name", jsonObject.getString("name"));
        signMap.put("idCard", jsonObject.getString("idCard"));
        signMap.put("bankCard", jsonObject.getString("bankCard"));
        signMap.put("mobile", jsonObject.getString("mobile"));
        signMap.put("idCardFront", jsonObject.getString("idCardFront"));
        signMap.put("idCardBack", jsonObject.getString("idCardBack"));
        signMap.put("requestId", jsonObject.getString("requestId"));
        signMap.put("state", jsonObject.getString("state"));
        signMap.put("reqId", jsonObject.getString("reqId"));
        String signString = sortKey(signMap);
        boolean sign1 = verify(signString, jsonObject.getString("sign"), publicKey);
        System.out.println(sign1);
    }

    /**
     * @return
     * @Author cuiLei
     * @Description 批量签约
     * @Date 14:10 2021/4/25
     * @Param
     **/
    @Test
    public static void batchSoho() throws Exception {
        String reqId = formatDate(new Date(), "yyyyMMddHHmmss");
        Map<String, Object> params = new HashMap<>();
        params.put("version", version);
        params.put("reqId", reqId);
        params.put("merId", merId);

        JSONArray sohoItemArray = new JSONArray();
        JSONObject sohoItem = new JSONObject();
        sohoItem.put("name", encrypt("王老三1", desKey));
        sohoItem.put("cardNo", encrypt("6214832408526486", desKey));
        sohoItem.put("idCard", encrypt("110222200008164214", desKey));
        sohoItem.put("mobile", encrypt("13602143430", desKey));
        sohoItem.put("levyCode",levyCode);
        sohoItem.put("idCardType", "A");
        sohoItemArray.add(sohoItem);

        JSONObject sohoItem1 = new JSONObject();
        sohoItem1.put("name", encrypt("王老二", desKey));
        sohoItem1.put("cardNo", encrypt("6214832408526486", desKey));
        sohoItem1.put("idCard", encrypt("110222200008161111", desKey));
        sohoItem1.put("mobile", encrypt("13622222345", desKey));
        sohoItem1.put("levyCode",levyCode);
        sohoItem1.put("idCardType", "A");
        sohoItemArray.add(sohoItem1);

//        JSONObject sohoItem2 = new JSONObject();
//        sohoItem2.put("name", encrypt("王老四", desKey));
//        sohoItem2.put("cardNo", encrypt("6214832408526486", desKey));
//        sohoItem2.put("idCard", encrypt("110222200008162222", desKey));
//        sohoItem2.put("mobile", encrypt("18502143430", desKey));
//        sohoItem2.put("levyCode",levyCode);
//        sohoItem2.put("idCardType", "A");
//        sohoItemArray.add(sohoItem2);

        String encode = encode(JSON.toJSONString(sohoItemArray).getBytes());
        params.put("sohoItems", encode);
        String linkString = createLinkString(params, false);
        System.out.println(linkString);
        String sign = sign(linkString, privateKey3);
        params.put("sign", sign);
        params.put("encType", encType);
        params.put("signType", signType);
        String s = JSON.toJSONString(params);
        System.out.println(s);
        String resData = sendPost(host + "soho/batchSoho/" + tenantCode, s);
        System.out.println(resData);
        JSONObject jsonObject = JSON.parseObject(resData);
        String data = jsonObject.getString("data");
        String dataString = new String(decode(data), charset);
        System.out.println(dataString);
    }

    /**
     * @return
     * @Author cuiLei
     * @Description 新开户接口
     * @Date 14:10 2021/4/25
     * @Param
     **/
    @Test
    public static void openMerchantV2() throws Exception {
        String reqId = DateUtils.formatDate(new Date(), "yyyyMMddHHmmss");
        Map<String, Object> params = new HashMap<>();
        params.put("version", version);
        params.put("reqId", reqId);
        params.put("agentCode", "3000001211");
        params.put("companyName", "北京泽峰");
        params.put("companyNo", "10086111");
        params.put("mobile", "15531851579");
        params.put("taxNo", "123123123123");
        params.put("taxAddress", "北京");
        params.put("taxMobile", "15531851579");
        params.put("taxOpenBank", "北京银行");
        params.put("taxAcc", "1008611");
        params.put("postName", "北京");
        params.put("postMobile", "15531851579");
        params.put("postAddress", "北京");
        params.put("uploadKey", "F");
        params.put("merEncType", "DES");
        params.put("merSignType", "RSA");
        params.put("isPlaceOnFile", "T");
        params.put("licenseAndContract", "https://taxation-dev.oss-cn-zhangjiakou.aliyuncs.com/GZX001/2021/06/7721fc7c-c9a8-41f1-b782-c017a859ee3b.pdf");
        params.put("licence", "https://taxation-dev.oss-cn-zhangjiakou.aliyuncs.com/GZX001/2021/06/7721fc7c-c9a8-41f1-b782-c017a859ee3b.pdf");
        params.put("legalIdCardFront", "https://taxation-dev.oss-cn-zhangjiakou.aliyuncs.com/GZX001/2021/06/7721fc7c-c9a8-41f1-b782-c017a859ee3b.pdf");
        params.put("legalIdCardBack", "https://taxation-dev.oss-cn-zhangjiakou.aliyuncs.com/GZX001/2021/06/7721fc7c-c9a8-41f1-b782-c017a859ee3b.pdf");
        params.put("companyRoomNumber", "https://taxation-dev.oss-cn-zhangjiakou.aliyuncs.com/GZX001/2021/06/7721fc7c-c9a8-41f1-b782-c017a859ee3b.pdf");
        params.put("levies", "W3siYmFua3MiOlsiNjAwMDAxNTgxMiJdLCJmZWVSYXRlIjowLjA1LCJmZWVSYXRlVHlwZSI6Ik9ORSIsImxldnlDb2RlIjoiMTAwMDAzMDQxMiJ9XQ==");
        String linkString = createLinkString(params, false);
        System.out.println(linkString);
        String sign = sign(linkString, privateKey);
        params.put("sign", sign);
        params.put("encType", encType);
        params.put("signType", signType);
        String s = JSON.toJSONString(params);
        System.out.println(s);
        String resData = cn.hutool.http.HttpUtil.post(host + "account/openMerchant/v2/" + tenantCode, s);
        System.out.println(resData);
        JSONObject jsonObject = JSON.parseObject(resData);
        Map<String, Object> signMap = new HashMap<>();
        signMap.put("reqId", jsonObject.getString("reqId"));
        signMap.put("merId", jsonObject.getString("merId"));
        String signString = sortKey(signMap);
        boolean sign1 = verify(signString, jsonObject.getString("sign"), publicKey);
        System.out.println(sign1);
    }

    /**
     * @return
     * @Author cuiLei
     * @Description 开户信息补充接口
     * @Date 14:10 2021/4/25
     * @Param
     **/
    @Test
    public static void supplementInfo() throws Exception {
        String reqId = DateUtils.formatDate(new Date(), "yyyyMMddHHmmss");
        Map<String, Object> params = new HashMap<>();
        params.put("version", version);
        params.put("reqId", reqId);
        params.put("agentCode", "3000001211");
        params.put("companyNo", "10086111");
        params.put("isPlaceOnFile", "F");
        params.put("levies", "W3siYmFua3MiOlsiNjAwMDAxNTgxMiJdLCJmZWVSYXRlIjowLjA1LCJmZWVSYXRlVHlwZSI6Ik9ORSIsImxldnlDb2RlIjoiMTAwMDAzMDQxMiJ9XQ==");
        String linkString = createLinkString(params, false);
        System.out.println(linkString);
        String sign = sign(linkString, privateKey);
        params.put("sign", sign);
        params.put("encType", encType);
        params.put("signType", signType);
        String s = JSON.toJSONString(params);
        System.out.println(s);
        String resData = cn.hutool.http.HttpUtil.post(host + "account/supplementInfo/" + tenantCode, s);
        System.out.println(resData);
        JSONObject jsonObject = JSON.parseObject(resData);
        Map<String, Object> signMap = new HashMap<>();
        signMap.put("reqId", jsonObject.getString("reqId"));
        signMap.put("merId", jsonObject.getString("merId"));
        String signString = sortKey(signMap);
        boolean sign1 = verify(signString, jsonObject.getString("sign"), publicKey);
        System.out.println(sign1);
    }

    /**
     * @return
     * @Author cuiLei
     * @Description 开户查询
     * @Date 14:52 2021/4/25
     * @Param
     **/
    public static void selectMerchantV2() throws Exception {
        String reqId = DateUtils.formatDate(new Date(), "yyyyMMddHHmmss");
        Map<String, Object> params = new HashMap<>();
        params.put("version", version);
        params.put("reqId", reqId);
        params.put("agentCode","3000001211");
        params.put("companyNo","10086111");
        String linkString = createLinkString(params, false);
        System.out.println(linkString);
        String sign = sign(linkString, privateKey);
        params.put("sign", sign);
        params.put("encType", encType);
        params.put("signType", signType);
        String s = JSON.toJSONString(params);
        System.out.println(s);
        String resData = sendPost(host + "account/selectMerchant/v2/" + tenantCode, s);
        System.out.println(resData);
        JSONObject jsonObject = JSON.parseObject(resData);
        Map<String, Object> signMap = new HashMap<>();
        signMap.put("reqId", jsonObject.getString("reqId"));
        signMap.put("agentCode", jsonObject.getString("agentCode"));
        signMap.put("companyNo", jsonObject.getString("companyNo"));
        signMap.put("merId", jsonObject.getString("merId"));
        signMap.put("levies", jsonObject.getString("levies"));
        signMap.put("apiKey", jsonObject.getString("apiKey"));
        signMap.put("publicKey", jsonObject.getString("publicKey"));
        signMap.put("merPrivateKey", jsonObject.getString("merPrivateKey"));
        signMap.put("md5Key", jsonObject.getString("md5Key"));
        String signString = sortKey(signMap);
        boolean sign1 = verify(signString, jsonObject.getString("sign"), publicKey);
        System.out.println(sign1);
        String levies = jsonObject.getString("levies");
        String leviesString = new String(decode(levies), charset);
        System.out.println(leviesString);

    }

    /**
     * @return
     * @Author cuiLei
     * @Description 开户
     * @Date 14:10 2021/4/25
     * @Param
     **/
    @Test
    public static void openMerchant() throws Exception {
        String reqId = DateUtils.formatDate(new Date(), "yyyyMMddHHmmss");
        Map<String, Object> params = new HashMap<>();
        params.put("version", version);
        params.put("reqId", reqId);
        params.put("agentCode", "3000001211");
        params.put("companyName", "北京抖音");
        params.put("companyNo", "1008611");
        params.put("mobile", "15531851579");
        params.put("taxNo", "123123123123");
        params.put("taxAddress", "北京");
        params.put("taxMobile", "15531851579");
        params.put("taxOpenBank", "北京银行");
        params.put("taxAcc", "1008611");
        params.put("postName", "北京");
        params.put("postMobile", "15531851579");
        params.put("postAddress", "北京");
        params.put("whiteIp", "106.39.22.50");
        params.put("uploadKey", "F");
        params.put("merEncType", "DES");
        params.put("apiKey", "");
        params.put("merSignType", "RSA");
        params.put("publicKey", "");
        params.put("md5Key", "");
        params.put("signNotifyUrl", "");
        params.put("paymentNotifyUrl", "");
        params.put("rechargeNotifyUrl", "");
        params.put("invoiceNotifyUrl", "");
        params.put("levies", "WyIxMDAwMDMwNDEyIl0=");
        String linkString = createLinkString(params, false);
        System.out.println(linkString);
        String sign = sign(linkString, privateKey);
        params.put("sign", sign);
        params.put("encType", encType);
        params.put("signType", signType);
        String s = JSON.toJSONString(params);
        System.out.println(s);
        String resData = cn.hutool.http.HttpUtil.post(host + "account/openMerchant/" + tenantCode, s);
        System.out.println(resData);
        JSONObject jsonObject = JSON.parseObject(resData);
        Map<String, Object> signMap = new HashMap<>();
        signMap.put("reqId", jsonObject.getString("reqId"));
        signMap.put("merId", jsonObject.getString("merId"));
        String signString = sortKey(signMap);
        boolean sign1 = verify(signString, jsonObject.getString("sign"), publicKey);
        System.out.println(sign1);
    }

    /**
     * @return
     * @Author cuiLei
     * @Description 开户查询
     * @Date 14:52 2021/4/25
     * @Param
     **/
    public static void selectMerchant() throws Exception {
        String reqId = DateUtils.formatDate(new Date(), "yyyyMMddHHmmss");
        Map<String, Object> params = new HashMap<>();
        params.put("version", version);
        params.put("reqId", reqId);
        params.put("agentCode","3000001211");
        params.put("companyNo","1008611");
        String linkString = createLinkString(params, false);
        System.out.println(linkString);
        String sign = sign(linkString, privateKey);
        params.put("sign", sign);
        params.put("encType", encType);
        params.put("signType", signType);
        String s = JSON.toJSONString(params);
        System.out.println(s);
        String resData = sendPost(host + "account/selectMerchant/" + tenantCode, s);
        System.out.println(resData);
        JSONObject jsonObject = JSON.parseObject(resData);
        Map<String, Object> signMap = new HashMap<>();
        signMap.put("reqId", jsonObject.getString("reqId"));
        signMap.put("agentCode", jsonObject.getString("agentCode"));
        signMap.put("companyNo", jsonObject.getString("companyNo"));
        signMap.put("merId", jsonObject.getString("merId"));
        signMap.put("levies", jsonObject.getString("levies"));
        signMap.put("apiKey", jsonObject.getString("apiKey"));
        signMap.put("publicKey", jsonObject.getString("publicKey"));
        signMap.put("merPrivateKey", jsonObject.getString("merPrivateKey"));
        signMap.put("md5Key", jsonObject.getString("md5Key"));
        String signString = sortKey(signMap);
        boolean sign1 = verify(signString, jsonObject.getString("sign"), publicKey);
        System.out.println(sign1);
        String levies = jsonObject.getString("levies");
        String leviesString = new String(decode(levies), charset);
        System.out.println(leviesString);

    }

    /**
     * Soho解约，仅限特定客户业务对接
     **/
    @Test
    public static void terminateContract() throws Exception {
        String reqId = DateUtil.format(new Date(), "yyyyMMddHHmmss");
        Map<String, Object> params = new HashMap<>();
        params.put("merId", merId);
        params.put("version", version);
        params.put("reqId", reqId);

        params.put("requestId", encrypt("testA", desKey));

        String linkString = createLinkString(params, false);
        String sign = sign(linkString, privateKey);

        params.put("sign", sign);
        params.put("encType", encType);
        params.put("signType", signType);

        String s = JSON.toJSONString(params);
        System.out.println(s);
        String resData = sendPost(host + "soho/terminateContract/" + tenantCode, s);
        System.out.println(resData);
        JSONObject jsonObject = JSON.parseObject(resData);
        Map<String, Object> signMap = new HashMap<>();
        signMap.put("reqId", jsonObject.getString("reqId"));
        String signString = sortKey(signMap);
        boolean sign1 = verify(signString, jsonObject.getString("sign"), publicKey);
        System.out.println(sign1);
    }

    /**
     * @return
     * @Author cuiLei
     * @Description 查询soho签约结果
     * @Date 14:52 2021/4/25
     * @Param
     **/
    public static void sohoQuery() throws Exception {
        String reqId = DateUtils.formatDate(new Date(), "yyyyMMddHHmmss");
        Map<String, Object> params = new HashMap<>();
        params.put("name", encrypt("齐铎宇", desKey));
        params.put("merId", merId);
        params.put("version", version);
        params.put("reqId", reqId);

//        params.put("cardNo", encrypt("6214831021834665", desKey));
        params.put("idCard", encrypt("411324198706241111", desKey));
//        params.put("mobile", encrypt("18511110833", desKey));
//        params.put("remark1","");
//        params.put("remark2","");
        params.put("levyCode", "1000030412");
        String linkString = createLinkString(params, false);
        System.out.println(linkString);
        String sign = sign(linkString, privateKey);
        params.put("sign", sign);
        params.put("encType", encType);
        params.put("signType", signType);

        String s = JSON.toJSONString(params);
        System.out.println(s);
        String resData = sendPost(host + "soho/querySoho/" + tenantCode, s);
        System.out.println(resData);
        JSONObject jsonObject = JSON.parseObject(resData);
        Map<String, Object> signMap = new HashMap<>();
        signMap.put("name", jsonObject.getString("name"));
        signMap.put("idCard", jsonObject.getString("idCard"));
        signMap.put("bankCard", jsonObject.getString("bankCard"));
        signMap.put("mobile", jsonObject.getString("mobile"));
        signMap.put("levyCode", jsonObject.getString("levyCode"));
        signMap.put("state", jsonObject.getString("state"));
        signMap.put("reqId", jsonObject.getString("reqId"));
        String signString = sortKey(signMap);
        boolean sign1 = verify(signString, jsonObject.getString("sign"), publicKey);
        System.out.println(sign1);

    }

    /**
     * @return
     * @Author cuiLei
     * @Description 商户账户余额查询
     * @Date 14:53 2021/4/25
     * @Param
     **/
    public static void accountBalance() throws Exception {
        String reqId = DateUtils.formatDate(new Date(), "yyyyMMddHHmmss");
        Map<String, Object> params = new HashMap<>();
        params.put("merId", merId);
        params.put("version", version);
        params.put("reqId", reqId);
        params.put("bankCode", "");
        params.put("levyCode", "");

        String linkString = createLinkString(params, false);
        System.out.println(linkString);
        String sign = sign(linkString, privateKey);
        params.put("sign", sign);
        params.put("encType", encType);
        params.put("signType", signType);

        String s = JSON.toJSONString(params);
        System.out.println(s);
        String resData = sendPost(host + "account/balance/" + tenantCode, s);
        System.out.println(resData);
        JSONObject jsonObject = JSON.parseObject(resData);
        Map<String, Object> signMap = new HashMap<>();
        signMap.put("data", jsonObject.getString("data"));
        signMap.put("reqId", jsonObject.getString("reqId"));
        String signString = sortKey(signMap);
        boolean sign1 = verify(signString, jsonObject.getString("sign"), publicKey);
        System.out.println(sign1);
        String data = jsonObject.getString("data");
        String dataString = new String(decode(data), charset);
        System.out.println(dataString);
    }


    /**
     * @return
     * @Author cuiLei
     * @Description 充值记录查询
     * @Date 14:53 2021/4/25
     * @Param
     **/
    public static void accReviewRecord() throws Exception {
        String reqId = DateUtils.formatDate(new Date(), "yyyyMMddHHmmss");
        Map<String, Object> params = new HashMap<>();
        params.put("merId", merId);
        params.put("version", version);
        params.put("reqId", reqId);
        params.put("bankCode", bankCode);
        params.put("levyCode", levyCode);
        //params.put("date", DateUtils.formatDate(new Date(), "yyyy-MM-dd"));
        String linkString = createLinkString(params, false);
        System.out.println(linkString);
        String sign = sign(linkString, privateKey);


        params.put("sign", sign);
        params.put("encType", encType);
        params.put("signType", signType);

        String s = JSON.toJSONString(params);
        System.out.println(s);
        String resData = sendPost(host + "account/accReviewRecord/" + tenantCode, s);
        System.out.println(resData);
        JSONObject jsonObject = JSON.parseObject(resData);
        Map<String, Object> signMap = new HashMap<>();
        signMap.put("data", jsonObject.getString("data"));
        signMap.put("reqId", jsonObject.getString("reqId"));
        String signString = sortKey(signMap);
        boolean sign1 = verify(signString, jsonObject.getString("sign"), publicKey);
        System.out.println(sign1);
        String data = jsonObject.getString("data");
        String dataString = new String(decode(data), charset);
        System.out.println(dataString);
    }

    /**
     * @return
     * @Author cuiLei
     * @Description 账户明细查询
     * @Date 14:53 2021/4/25
     * @Param
     **/
    public static void accTransDetail() throws Exception {
        String reqId = DateUtils.formatDate(new Date(), "yyyyMMddHHmmss");
        Map<String, Object> params = new HashMap<>();
        params.put("merId", merId);
        params.put("version", version);
        params.put("reqId", reqId);
        params.put("bankCode", bankCode);
        params.put("levyCode", levyCode);
        params.put("date", DateUtils.formatDate(new Date(), "yyyy-MM-dd"));
        params.put("blsign", "1");// 1:进账 -1:出账
        String linkString = createLinkString(params, false);
        System.out.println(linkString);
        String sign = sign(linkString, privateKey);
        params.put("page", "1");
        params.put("size", "10");
        params.put("sign", sign);
        params.put("encType", encType);
        params.put("signType", signType);

        String s = JSON.toJSONString(params);
        System.out.println(s);
        String resData = sendPost(host + "account/accTransDetail/" + tenantCode, s);
        System.out.println(resData);
        JSONObject jsonObject = JSON.parseObject(resData);
        Map<String, Object> signMap = new HashMap<>();
        signMap.put("data", jsonObject.getString("data"));
        signMap.put("reqId", jsonObject.getString("reqId"));
        String signString = sortKey(signMap);
        boolean sign1 = verify(signString, jsonObject.getString("sign"), publicKey);
        System.out.println(sign1);
        String data = jsonObject.getString("data");
        String dataString = new String(decode(data), charset);
        System.out.println(dataString);
    }

    /**
     * @return
     * @Author cuiLei
     * @Description 商户信息查询
     * @Date 14:53 2021/4/25
     * @Param
     **/
    public static void merchantInfo() throws Exception {
        String reqId = DateUtils.formatDate(new Date(), "yyyyMMddHHmmss");
        Map<String, Object> params = new HashMap<>();
        params.put("merId", merId);
        params.put("version", version);
        params.put("reqId", reqId);

        String linkString = createLinkString(params, false);
        System.out.println(linkString);
        String sign = sign(linkString, privateKey);
        params.put("sign", sign);
        params.put("encType", encType);
        params.put("signType", signType);

        String s = JSON.toJSONString(params);
        System.out.println(s);
        String resData = sendPost(host + "account/merchantInfo/" + tenantCode, s);
        System.out.println(resData);
        JSONObject jsonObject = JSON.parseObject(resData);
        Map<String, Object> signMap = new HashMap<>();
        signMap.put("merId", jsonObject.getString("merId"));
        signMap.put("reqId", jsonObject.getString("reqId"));
        signMap.put("taxNo", jsonObject.getString("taxNo"));
        signMap.put("taxCompany", jsonObject.getString("taxCompany"));
        signMap.put("taxAddr", jsonObject.getString("taxAddr"));
        signMap.put("taxMobile", jsonObject.getString("taxMobile"));
        signMap.put("taxOpenBank", jsonObject.getString("taxOpenBank"));
        signMap.put("taxAcc", jsonObject.getString("taxAcc"));
        signMap.put("taxType", jsonObject.getString("taxType"));
        signMap.put("mcc1", jsonObject.getString("mcc1"));
        signMap.put("mcc2", jsonObject.getString("mcc2"));
        signMap.put("postName", jsonObject.getString("postName"));
        signMap.put("postMobile", jsonObject.getString("postMobile"));
        signMap.put("postAddr", jsonObject.getString("postAddr"));
        signMap.put("businessName", jsonObject.getString("businessName"));
        signMap.put("businessMobile", jsonObject.getString("businessMobile"));
        signMap.put("businessEmail", jsonObject.getString("businessEmail"));
        signMap.put("legalPerson", jsonObject.getString("legalPerson"));
        signMap.put("riskData", jsonObject.getString("riskData"));
        signMap.put("feeRateData", jsonObject.getString("feeRateData"));
        String signString = sortKey(signMap);
        System.out.println(signString);
        boolean sign1 = verify(signString, jsonObject.getString("sign"), publicKey);
        System.out.println(sign1);
        String riskData = jsonObject.getString("riskData");
        String riskDataString = new String(decode(riskData), charset);
        System.out.println(riskDataString);
        String feeRateData = jsonObject.getString("feeRateData");
        String feeRateDataString = new String(decode(feeRateData), charset);
        System.out.println(feeRateDataString);

        String postMobile = decrypt(jsonObject.getString("postMobile"), desKey);
        System.out.println(postMobile);
    }

    /**
     * @return
     * @Author cuiLei
     * @Description 税优地查询
     * @Date 14:53 2021/4/25
     * @Param
     **/
    public static void levyInfo() throws Exception {
        String reqId = DateUtils.formatDate(new Date(), "yyyyMMddHHmmss");
        Map<String, Object> params = new HashMap<>();
        params.put("merId", merId);
        params.put("version", version);
        params.put("reqId", reqId);
        params.put("levyCode", levyCode);
        String linkString = createLinkString(params, false);
        System.out.println(linkString);
        String sign = sign(linkString, privateKey);

        params.put("sign", sign);
        params.put("encType", encType);
        params.put("signType", signType);
        String s = JSON.toJSONString(params);
        System.out.println(s);
        String resData = sendPost(host + "levy/levyInfo/" + tenantCode, s);
        System.out.println(resData);
        JSONObject jsonObject = JSON.parseObject(resData);
        Map<String, Object> signMap = new HashMap<>();
        signMap.put("data", jsonObject.getString("data"));
        signMap.put("reqId", jsonObject.getString("reqId"));
        String signString = sortKey(signMap);
        boolean sign1 = verify(signString, jsonObject.getString("sign"), publicKey);
        System.out.println(sign1);
        String data = jsonObject.getString("data");
        String dataString = new String(decode(data), charset);
        System.out.println(dataString);
    }

    /**
     * @return
     * @Author cuiLei
     * @Description 开票类型查询
     * @Date 14:53 2021/4/25
     * @Param
     **/
    public static void invoiceType() throws Exception {
        String reqId = DateUtils.formatDate(new Date(), "yyyyMMddHHmmss");
        Map<String, Object> params = new HashMap<>();
        params.put("merId", merId);
        params.put("version", version);
        params.put("reqId", reqId);
        params.put("levyCode", levyCode);
        String linkString = createLinkString(params, false);
        System.out.println(linkString);
        String sign = sign(linkString, privateKey);

        params.put("sign", sign);
        params.put("encType", encType);
        params.put("signType", signType);
        String s = JSON.toJSONString(params);
        System.out.println(s);
        String resData = sendPost(host + "invoice/invoiceType/" + tenantCode, s);
        System.out.println(resData);
        JSONObject jsonObject = JSON.parseObject(resData);
        Map<String, Object> signMap = new HashMap<>();
        signMap.put("data", jsonObject.getString("data"));
        signMap.put("reqId", jsonObject.getString("reqId"));
        String signString = sortKey(signMap);
        boolean sign1 = verify(signString, jsonObject.getString("sign"), publicKey);
        System.out.println(sign1);
        String data = jsonObject.getString("data");
        String dataString = new String(decode(data), charset);
        System.out.println(dataString);
    }

    /**
     * @return
     * @Author cuiLei
     * @Description 开票记录查询
     * @Date 14:53 2021/4/25
     * @Param
     **/
    public static void invoiceInfo() throws Exception {
        String reqId = DateUtils.formatDate(new Date(), "yyyyMMddHHmmss");
        Map<String, Object> params = new HashMap<>();
        params.put("merId", merId);
        params.put("version", version);
        params.put("reqId", reqId);
        params.put("levyCode", levyCode);
        String linkString = createLinkString(params, false);
        System.out.println(linkString);
        String sign = sign(linkString, privateKey);
        params.put("state", "");
        params.put("auditState", "");
        params.put("date", "");
        params.put("page", "1");
        params.put("size", "10");
        params.put("sign", sign);
        params.put("encType", encType);
        params.put("signType", signType);
        String s = JSON.toJSONString(params);
        System.out.println(s);
        String resData = sendPost(host + "invoice/invoiceInfo/" + tenantCode, s);
        System.out.println(resData);
        JSONObject jsonObject = JSON.parseObject(resData);
        Map<String, Object> signMap = new HashMap<>();
        signMap.put("data", jsonObject.getString("data"));
        signMap.put("reqId", jsonObject.getString("reqId"));
        String signString = sortKey(signMap);
        boolean sign1 = verify(signString, jsonObject.getString("sign"), publicKey);
        System.out.println(sign1);
        String data = jsonObject.getString("data");
        String dataString = new String(decode(data), charset);
        System.out.println(dataString);
    }

    /**
     * @return
     * @Author cuiLei
     * @Description 代付
     * @Date 14:53 2021/4/25
     * @Param
     **/
    public static void payment() throws Exception {
        String reqId = DateUtils.formatDate(new Date(), "yyyyMMddHHmmss");
        Map<String, Object> params = new HashMap<>();
        params.put("merId", merId);
        params.put("version", version);
        params.put("reqId", reqId);
        params.put("totalCount", "1");
        params.put("totalAmt", "20.00");
        params.put("merBatchId", reqId);

        JSONObject payItem = new JSONObject();
        payItem.put("merOrderId", DateUtils.formatDate(new Date(), "yyyyMMddHHmmss"));
        payItem.put("amt", new BigDecimal(20.00));
        payItem.put("payeeName", "齐铎宇");
        payItem.put("levyCode", levyCode);
        payItem.put("bankCode", bankCode);
        payItem.put("payeeAcc", "6221801000008681351");
        payItem.put("idCard", "411324198706241111");
        payItem.put("mobile", "18511111111");
        payItem.put("inType", "API");
        payItem.put("paymentType", "CARD");
        payItem.put("accType", "PRI");

//        JSONObject payItem1 = new JSONObject();
//        payItem1.put("merOrderId", DateUtils.formatDate(new Date(), "yyyyMMddHHmmss"));
//        payItem1.put("amt", new BigDecimal(10.00));
//        payItem1.put("payeeName", "张三");
//        payItem1.put("levyCode", levyCode);
//        payItem1.put("bankCode", bankCode);
//        payItem1.put("payeeAcc", "18511110837");
//        payItem1.put("idCard", "150429199704106110");
//        payItem1.put("mobile", "18511110837");
//        payItem1.put("inType", "API");
//        payItem1.put("paymentType", "ALIPAY");
//        payItem1.put("accType", "PRI");

        JSONArray payItemArray = new JSONArray();
        payItemArray.add(payItem);
//        payItemArray.add(payItem1);
        System.out.println(payItemArray);
        String encode = encode(JSON.toJSONString(payItemArray).getBytes());
        params.put("payItems", encode);
        String linkString = createLinkString(params, false);
        System.out.println(linkString);
        String sign = sign(linkString, privateKey);
        params.put("sign", sign);
        params.put("encType", encType);
        params.put("signType", signType);
        String s = JSON.toJSONString(params);
        System.out.println(s);
        String resData = sendPost(host + "payment/pay/" + tenantCode, s);
        System.out.println(resData);
        JSONObject jsonObject = JSON.parseObject(resData);
        Map<String, Object> signMap = new HashMap<>();
        signMap.put("successNum", jsonObject.getString("successNum"));
        signMap.put("failureNum", jsonObject.getString("failureNum"));
        signMap.put("merBatchId", jsonObject.getString("merBatchId"));
        signMap.put("data", jsonObject.getString("data"));
        signMap.put("reqId", jsonObject.getString("reqId"));
        String signString = sortKey(signMap);
        boolean sign1 = verify(signString, jsonObject.getString("sign"), publicKey);
        System.out.println(sign1);
        String data = jsonObject.getString("data");
        String dataString = new String(decode(data), charset);
        System.out.println(dataString);
    }

    /**
     * 特定客户代付对接
     **/
    @Test
    public static void customPay() throws Exception {
        String reqId = DateUtil.format(new Date(), "yyyyMMddHHmmss");
        Map<String, Object> params = new HashMap<>();
        params.put("merId", merId);
        params.put("version", version);
        params.put("reqId", reqId);

        params.put("totalCount", "2");
        params.put("totalAmt", "25.99");
        params.put("merBatchId", reqId);

        JSONObject payItem = new JSONObject();
        payItem.put("merOrderId", DateUtil.format(new Date(), "yyyyMMddHHmmss"));
        payItem.put("amt", new BigDecimal(15.99));
        payItem.put("merUserId", "1111111111");
        payItem.put("levyCode", levyCode);
        payItem.put("bankCode", bankCode);
        payItem.put("payeeAcc", "18511110837");
        payItem.put("inType", "API");
        payItem.put("paymentType", "ALIPAY");
        payItem.put("accType", "PRI");

        JSONObject payItem1 = new JSONObject();
        payItem1.put("merOrderId", DateUtil.format(new Date(), "yyyyMMddHHmmss"));
        payItem1.put("amt", new BigDecimal(10.00));
        payItem1.put("merUserId", "1111111111");
        payItem1.put("levyCode", levyCode);
        payItem1.put("bankCode", bankCode);
        payItem1.put("payeeAcc", "18511110837");
        payItem1.put("inType", "API");
        payItem1.put("paymentType", "ALIPAY");
        payItem1.put("accType", "PRI");


        JSONArray payItemArray = new JSONArray();
        payItemArray.add(payItem);
        payItemArray.add(payItem1);
        System.out.println(payItemArray);
        String encode = encode(JSON.toJSONString(payItemArray).getBytes());
        params.put("payItems", encode);
        String linkString = createLinkString(params, false);
        System.out.println(linkString);
        String sign = sign(linkString, privateKey);
        params.put("sign", sign);
        params.put("encType", encType);
        params.put("signType", signType);
        String s = JSON.toJSONString(params);
        String resData = sendPost(host + "payment/customPay/"
                + tenantCode, s);
        System.out.println(resData);
        JSONObject jsonObject = JSON.parseObject(resData);
        Map<String, Object> signMap = new HashMap<>();
        signMap.put("successNum", jsonObject.getString("successNum"));
        signMap.put("failureNum", jsonObject.getString("failureNum"));
        signMap.put("merBatchId", jsonObject.getString("merBatchId"));
        signMap.put("data", jsonObject.getString("data"));
        signMap.put("reqId", jsonObject.getString("reqId"));
        String signString = sortKey(signMap);
        boolean sign1 = verify(signString, jsonObject.getString("sign"), publicKey);
        System.out.println(sign1);
        String data = jsonObject.getString("data");
        String dataString = new String(decode(data), "UTF-8");
        System.out.println(dataString);
    }

    /**
     * @return
     * @Author cuiLei
     * @Description 代付查询
     * @Date 14:53 2021/4/25
     * @Param
     **/
    public static void queryPayment() throws Exception {
        String reqId = DateUtils.formatDate(new Date(), "yyyyMMddHHmmss");
        Map<String, Object> params = new HashMap<>();
        params.put("merId", merId);
        params.put("version", version);
        params.put("reqId", reqId);
        params.put("merBatchId", "20221021022211");

        JSONObject payItem = new JSONObject();
        //代付请求时上送的订单号
        payItem.put("merOrderId", "20221021022211");
        //平台返回给商户的订单号
        payItem.put("payItemId", "1583282473414950912");
        JSONArray payItemArray = new JSONArray();
        payItemArray.add(payItem);
        System.out.println(payItemArray);
        String encode = encode(JSON.toJSONString(payItemArray).getBytes());
        params.put("queryItems", encode);
        String linkString = createLinkString(params, false);
        System.out.println(linkString);
        String sign = sign(linkString, privateKey);
        params.put("sign", sign);
        params.put("encType", encType);
        params.put("signType", signType);
        String s = JSON.toJSONString(params);
        System.out.println(s);
        String resData = sendPost(host + "payment/queryBatch/" + tenantCode, s);
        System.out.println(resData);
        JSONObject jsonObject = JSON.parseObject(resData);
        Map<String, Object> signMap = new HashMap<>();
        signMap.put("merBatchId", jsonObject.getString("merBatchId"));
        signMap.put("data", jsonObject.getString("data"));
        signMap.put("reqId", jsonObject.getString("reqId"));
        signMap.put("merId", jsonObject.getString("merId"));
        String signString = sortKey(signMap);
        boolean sign1 = verify(signString, jsonObject.getString("sign"), publicKey);
        System.out.println(sign1);
        String data = jsonObject.getString("data");
        String dataString = new String(decode(data), charset);
        System.out.println(dataString);
    }

    /**
     * 电子回单接口
     */
    public static void downloadReceipt() throws Exception {
        String path = "/payment/downloadReceipt/0525";
        String reqId = DateUtils.formatDate(new Date(), "yyyyMMddHHmmss");
        JSONObject param = new JSONObject();
        param.put("version", version);
        param.put("reqId", reqId);
        param.put("merId", merId);
        param.put("payItemId", "1599947765285126144");
        String linkString = createLinkString(param, false);
        System.out.println(linkString);
        String sign = sign(linkString, privateKey);
        System.out.println(">>>>  " + sign);
        param.put("sign", sign);
        param.put("encType", encType);
        param.put("signType", signType);
        System.out.println(param.toString());
        String resData = sendPost(host + path, param.toString());
        System.out.println(resData);
        JSONObject jsonObject = JSON.parseObject(resData);
        Map<String, Object> signMap = new HashMap<>();

        String data = jsonObject.getString("data");
        String decrypt = decrypt(data, desKey);
        //String dataString = new String(decode(data), charset);
        System.out.println(decrypt);
    }

    /**
     * 批量电子回单接口
     */
    public static void downloadByDateReceipt() throws Exception {
        String path = "/payment/downloadReceiptByDate/0525";
        String reqId = DateUtils.formatDate(new Date(), "yyyyMMddHHmmss");
        JSONObject param = new JSONObject();
        param.put("version", version);
        param.put("reqId", reqId);
        param.put("merId", "6000010711");
        param.put("date", "2022-12-06");
        String linkString = createLinkString(param, false);
        System.out.println(linkString);
        String sign = sign(linkString, privateKey);
        System.out.println(">>>>  " + sign);
        param.put("sign", sign);
        param.put("encType", encType);
        param.put("signType", signType);
        System.out.println(param.toString());
        String resData = sendPost(host + path, param.toString());
        System.out.println(resData);
        JSONObject jsonObject = JSON.parseObject(resData);
        Map<String, Object> signMap = new HashMap<>();

        String data = jsonObject.getString("data");
        String decrypt = decrypt(data, desKey);
        //String dataString = new String(decode(data), charset);
        System.out.println(decrypt);
    }

    /**
     * 结算订单明细查询接口
     */
    public static void merplatOrderPageQuery() throws Exception {
        String reqId = formatDate(new Date(), "yyyyMMddHHmmss");
        JSONObject param = new JSONObject();
        param.put("reqId", reqId);
        param.put("merId", merId);
        param.put("version", version);
        param.put("current", 1);
        param.put("size", 30);
        param.put("startTime", "2022-12-07");
        param.put("endTime","2022-12-07");
        param.put("levyCode",levyCode);
        String linkString = createLinkString(param, false);
        System.out.println(linkString);
        String sign = sign(linkString, privateKey3);
        param.put("sign", sign);
        param.put("encType", encType);
        param.put("signType", signType);
        System.out.println(param);
        String resData = cn.hutool.http.HttpUtil.post(host + "payment/queryMerplatOrderPage/" + tenantCode, param.toJSONString());
        System.out.println(resData);
        JSONObject jsonObject = JSONObject.parseObject(resData);
        String data = jsonObject.getString("data");
        String decrypt = decrypt(data, desKey);
        System.out.println(decrypt);
    }

    /**
     * @return
     * @Author cuiLei
     * @Description 可开余额查询
     * @Date 11:34 2021/5/10
     * @Param
     **/
    public static void invoiceMerSumAmt() throws Exception {
        String reqId = DateUtils.formatDate(new Date(), "yyyyMMddHHmmss");
        Map<String, Object> params = new HashMap<>();
        params.put("merId", merId);
        params.put("version", version);
        params.put("reqId", reqId);
        params.put("levyCode", levyCode);
        String linkString = createLinkString(params, false);
        System.out.println(linkString);
        String sign = sign(linkString, privateKey);
        params.put("sign", sign);
        params.put("encType", encType);
        params.put("signType", signType);
        String s = JSON.toJSONString(params);
        System.out.println(s);
        String resData = sendPost(host + "invoice/invoiceMerSumAmt/" + tenantCode, s);
        System.out.println(resData);
        JSONObject jsonObject = JSON.parseObject(resData);
        Map<String, Object> signMap = new HashMap<>();
        signMap.put("reqId", jsonObject.getString("reqId"));
        signMap.put("accAmt", jsonObject.getString("accAmt"));
        String signString = sortKey(signMap);
        boolean sign1 = verify(signString, jsonObject.getString("sign"), publicKey);
        System.out.println(sign1);
        String accAmt = jsonObject.getString("accAmt");
        System.out.println(accAmt);
    }

    /**
     * @return
     * @Author cuiLei
     * @Description 开票申请
     * @Date 11:34 2021/5/10
     * @Param
     **/
    public static void invoiceOpen() throws Exception {
        String reqId = DateUtils.formatDate(new Date(), "yyyyMMddHHmmss");
        Map<String, Object> params = new HashMap<>();
        params.put("merId", merId);
        params.put("version", version);
        params.put("reqId", reqId);
        params.put("levyCode", levyCode);
        params.put("invoiceId", "01234598");
        params.put("amt", "1.00");
        params.put("category", "ZHUAN");
        params.put("contact", encrypt("邮寄联系人", desKey));
        params.put("mobile", encrypt("18511110837", desKey));
        params.put("taxMobile", encrypt("18511110837", desKey));
        params.put("trackNo", "123123123");
        params.put("typeCode", "000848");
        params.put("taxNo", "321321");
        params.put("taxAddr", encrypt("北京1", desKey));
        params.put("taxOpenBank", "sdasdad");
        params.put("taxAcc", "123123");
        params.put("postAddress", encrypt("北京", desKey));
        params.put("taxCompany", encrypt("公司", desKey));
        String linkString = createLinkString(params, false);
        System.out.println(linkString);
        String sign = sign(linkString, privateKey);
        params.put("sign", sign);
        params.put("encType", encType);
        params.put("signType", signType);
        params.put("memo", "");
        String s = JSON.toJSONString(params);
        System.out.println(s);
        String resData = sendPost(host + "invoice/invoiceOpen/" + tenantCode, s); //"{\"reqId\":\"20210510173533\",\"resCode\":\"0000\",\"resMsg\":\"成功\",\"sign\":\"baMYK5/C3JEFO4ZGTMzIdZOeINqchLpJia7a9MvSeK1OrTw9EMnKx5vkAIHpWupMT6h/uUPVgFEWyjadDN5DBUkHfFlhHC0suLpvl+kcy9EoGONuuAJcS/IACP9yHRQWUb+mpEEDhJgUYN89urKXAu7a4iX9fmVbD2wP/qRysPQ=\",\"levyCode\":\"1000028712\",\"amt\":\"0.1\",\"postAddress\":\"RlB/jBne0Dk=\",\"contact\":\"Ix1fa36K0jFb+h1Hsqe1+w==\",\"mobile\":\"oPunS+pKd1pO6FQNdIu5eQ==\",\"trackNo\":\"123123123\",\"memo\":\"\",\"typeId\":123123123,\"taxCompany\":\"qCGuwV45Kuc=\",\"taxNo\":\"321321\",\"taxAddr\":\"UpZGf6xUiY0=\",\"taxMobile\":\"oPunS+pKd1pO6FQNdIu5eQ==\",\"taxOpenBank\":\"sdasdad\",\"taxAcc\":\"123123\",\"merId\":\"6000010811\",\"invoiceId\":\"7\"}";//sendPost("http://192.168.191.51:20087/invoice/invoiceOpen/0428", s);
        System.out.println(resData);
        JSONObject jsonObject = JSON.parseObject(resData);
        Map<String, Object> signMap = new HashMap<>();
        signMap.put("reqId", jsonObject.getString("reqId"));
        signMap.put("levyCode", jsonObject.getString("levyCode"));
        signMap.put("amt", jsonObject.getString("amt"));
        signMap.put("postAddress", jsonObject.getString("postAddress"));
        signMap.put("contact", jsonObject.getString("contact"));
        signMap.put("mobile", jsonObject.getString("mobile"));
        signMap.put("trackNo", jsonObject.getString("trackNo"));
        signMap.put("memo", jsonObject.getString("memo"));
        signMap.put("typeId", jsonObject.getString("typeId"));
        signMap.put("taxCompany", jsonObject.getString("taxCompany"));
        signMap.put("taxNo", jsonObject.getString("taxNo"));
        signMap.put("taxAddr", jsonObject.getString("taxAddr"));
        signMap.put("taxMobile", jsonObject.getString("taxMobile"));
        signMap.put("taxOpenBank", jsonObject.getString("taxOpenBank"));
        signMap.put("taxAcc", jsonObject.getString("taxAcc"));
        signMap.put("merId", jsonObject.getString("merId"));
        signMap.put("invoiceId", jsonObject.getString("invoiceId"));
        String signString = sortKey(signMap);
        boolean sign1 = verify(signString, jsonObject.getString("sign"), publicKey);
        System.out.println(sign1);
        String contact = decrypt(jsonObject.getString("contact"), desKey);
        System.out.println(contact);
        String mobile = decrypt(jsonObject.getString("mobile"), desKey);
        System.out.println(mobile);
        String postAddress = decrypt(jsonObject.getString("postAddress"), desKey);
        System.out.println(postAddress);
    }

    /**
     * @return
     * @Author cuiLei
     * @Description 开票附件查询
     * @Date 16:11 2021/5/12
     * @Param
     **/
    public static void invoicePic() throws Exception {
        String reqId = DateUtils.formatDate(new Date(), "yyyyMMddHHmmss");
        Map<String, Object> params = new HashMap<>();
        params.put("merId", merId);
        params.put("version", version);
        params.put("reqId", reqId);
        //params.put("levyCode", levyCode);
        params.put("invoiceId", 94);
        params.put("type", "INVOICE");
        String linkString = createLinkString(params, false);
        System.out.println(linkString);
        String sign = sign(linkString, privateKey);
        params.put("sign", sign);
        params.put("encType", encType);
        params.put("signType", signType);
        String s = JSON.toJSONString(params);
        System.out.println(s);
        String resData = sendPost(host + "invoice/invoicePic/" + tenantCode, s);
        System.out.println(resData);
        JSONObject jsonObject = JSON.parseObject(resData);
        Map<String, Object> signMap = new HashMap<>();
        signMap.put("reqId", jsonObject.getString("reqId"));
        signMap.put("data", jsonObject.get("data").toString());
        String signString = sortKey(signMap);
        boolean sign1 = verify(signString, jsonObject.getString("sign"), publicKey);
        System.out.println(sign1);
        String data = jsonObject.getString("data");
        String dataString = new String(decode(data), charset);
        System.out.println(dataString);
    }

    /**
     * @return
     * @Author cuiLei
     * @Description 商户打款账户信息查询
     * @Date 14:53 2021/4/25
     * @Param
     **/
    public static void accNoInfo() throws Exception {
        String reqId = DateUtils.formatDate(new Date(), "yyyyMMddHHmmss");
        Map<String, Object> params = new HashMap<>();
        params.put("reqId", reqId);
        params.put("merId", merId);
        params.put("version", version);
        params.put("levyCode", "1000030412");
        params.put("bankCode", "6000015812");
        String linkString = createLinkString(params, false);
        System.out.println(linkString);
        String sign = sign(linkString, privateKey);


        params.put("sign", sign);
        params.put("encType", encType);
        params.put("signType", signType);

        String s = JSON.toJSONString(params);
        System.out.println(s);
        String resData = sendPost(host + "account/accNoInfo/" + tenantCode, s);
        System.out.println(resData);
        JSONObject jsonObject = JSON.parseObject(resData);
        Map<String, Object> signMap = new HashMap<>();
        signMap.put("reqId", jsonObject.getString("reqId"));
        signMap.put("levies", jsonObject.getString("levies"));
        signMap.put("companyNo", jsonObject.getString("companyNo"));
        signMap.put("companyName", jsonObject.getString("companyName"));
        String signString = sortKey(signMap);
        System.out.println(signString);
        boolean sign1 = verify(signString, jsonObject.getString("sign"), publicKey);
        System.out.println(sign1);
        String levies = jsonObject.getString("levies");
        String leviesString = new String(decode(levies), charset);
        System.out.println(leviesString);
    }

    /**
     * @return
     * @Author cuiLei
     * @Description 查询项目分类信息
     * @Date 14:53 2021/4/25
     * @Param
     **/
    public static void selectProjectMcc() throws Exception {
        String reqId = DateUtils.formatDate(new Date(), "yyyyMMddHHmmss");
        Map<String, Object> params = new HashMap<>();
        params.put("merId", merId);
        params.put("version", version);
        params.put("reqId", reqId);
        params.put("levyCode", levyCode);

        String linkString = createLinkString(params, false);
        System.out.println(linkString);
        String sign = sign(linkString, privateKey);
        params.put("sign", sign);
        params.put("encType", encType);
        params.put("signType", signType);
        String s = JSON.toJSONString(params);
        System.out.println(s);
        String resData = sendPost(host + "projectMcc/list/" + tenantCode, s);
        System.out.println(resData);
        JSONObject jsonObject = JSON.parseObject(resData);
        Map<String, Object> signMap = new HashMap<>();
        signMap.put("data", jsonObject.getString("data"));
        signMap.put("reqId", jsonObject.getString("reqId"));
        signMap.put("merId", jsonObject.getString("merId"));
        String signString = sortKey(signMap);
        boolean sign1 = verify(signString, jsonObject.getString("sign"), publicKey);
        System.out.println(sign1);
        String data = jsonObject.getString("data");
        String dataString = new String(decode(data), charset);
        System.out.println(dataString);
    }

    /**
     * @return
     * @Author cuiLei
     * @Description 创建项目接口
     * @Date 14:53 2021/4/25
     * @Param
     **/
    public static void createProject() throws Exception {
        String reqId = DateUtils.formatDate(new Date(), "yyyyMMddHHmmss");
        Map<String, Object> params = new HashMap<>();
        params.put("merId", merId);
        params.put("version", version);
        params.put("reqId", reqId);
        params.put("levyCode", levyCode);
        params.put("projectId", reqId); //贵司项目ID
        params.put("name", "name");
        params.put("demo", "demo");
        params.put("projectMccId", "1397816853157380096");
        params.put("projectStartTime", "2022-10-10 00:00:00");
        params.put("projectEndTime", "2023-10-10 00:00:00");
        params.put("projectStartAmt", 0);
        params.put("projectEndAmt", 10000);
        params.put("demand", "demand");
        params.put("standard", "standard");

        String linkString = createLinkString(params, false);
        System.out.println(linkString);
        String sign = sign(linkString, privateKey);
        params.put("sign", sign);
        params.put("encType", encType);
        params.put("signType", signType);
        String s = JSON.toJSONString(params);
        System.out.println(s);
        String resData = sendPost(host + "project/create/" + tenantCode, s);
        System.out.println(resData);
        JSONObject jsonObject = JSON.parseObject(resData);
        Map<String, Object> signMap = new HashMap<>();
        signMap.put("data", jsonObject.getString("data"));
        signMap.put("reqId", jsonObject.getString("reqId"));
        signMap.put("merId", jsonObject.getString("merId"));
        String signString = sortKey(signMap);
        boolean sign1 = verify(signString, jsonObject.getString("sign"), publicKey);
        System.out.println(sign1);
        String data = jsonObject.getString("data");
        String dataString = new String(decode(data), charset);
        System.out.println(dataString);
    }

    /**
     * @return
     * @Author cuiLei
     * @Description 车行定制化结算订单查询
     * @Date 14:52 2021/4/25
     * @Param
     **/
    public static void queryByThirdOrderId() throws Exception {
        String reqId = formatDate(new Date(), "yyyyMMddHHmmss");
        JSONObject param = new JSONObject();
        param.put("version", version);
        param.put("reqId", reqId);
        param.put("agentCode", "3000001211");

        ArrayList<Object> list = new ArrayList<>();
        list.add("160919489889075200");
        list.add("161569585672650752");
        String encode = encode(JSON.toJSONString(list).getBytes());
        param.put("thirdOrderIdItems", encode);
        param.put("current", 1);
        param.put("size", 30);
        String linkString = createLinkString(param, false);
        System.out.println(linkString);

        String sign = sign(linkString, "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKUfKjKXuXXo5jCccpkDbRIEaRdEUjhTvoy7eGz/FW6TgDsnCTdSnZkfn0hskgqkbvQaJOlay2ZoUjO1xNQIJOA6Gl5CAAcFXlOQ6Wq5iHhig/hf5jSo4VoTE9I3TXTFTk41zr+GRQPT0qrYOBCL6QQ27e3QLqMBPb4VWdNpXqXlAgMBAAECgYAbptdrN4tgWotJ8rkiNLesP77VnaRjOGr9qFtnbWRKazsbLNNXR2eLAAetmjjuvVCzYjlyNrk/QhMaA8OdemuqOFQELvztWnyjnop8NzrwUmOxJzBpnzwxrv76a1xXckgYJt3/KIoyJOR88TUoakVGh12RexDKjaGQLvtcdk91gQJBAM5y4fKSSD7E5aoLyWmBQWA3pwMJhMG6LseDYOXylcQxrG+A/hYtxS9jY91CfEvRlrvqMS6MSeL8jKzrp8+GqsUCQQDMwPf7tcRjO1qnZNrpFozi/qFlaM8q6ydlNQT2OhRh5qtcxk1tKrSrI0M1aUiYURH7Za9qCucpfrURq1HuzEChAkBrfCKpUYHttEPCkdpuZM5t0uvQR/9ngZDjbUJg1jklaZtWfnlLgM7+CigvRXNDYj6xXY/3pTC+zjBLw3GeOYXlAkAbOYU/mLmWICw7mbNpzq/I9uZq33a8VCwWzufr/Wj8Y3lhipR88XK4VqX3Ehqu9giYDrs2NGDsKYbV5JuuTsZBAkEAvgL1TYPw5wIuzuu26058H77D3UwCHygGDVWIFoJ3WShIFRrvNnLPjTlRuWukpghXeX6jo8dLCVuQlWna5oh4oQ==");
        param.put("sign", sign);
        param.put("encType", encType);
        param.put("signType", signType);
        String s = JSON.toJSONString(param);
        System.out.println(s);
        String resData = sendPost(host + "payment/queryByThirdOrderId/" + tenantCode, s);
        System.out.println(resData);
        JSONObject jsonObject = JSONObject.parseObject(resData);
        Map<String, Object> signMap = new HashMap<>();
        signMap.put("reqId", jsonObject.getString("reqId"));
        signMap.put("data", jsonObject.getString("data"));
        String signString = sortKey(signMap);
        boolean sign1 = verify(signString, jsonObject.getString("sign"), publicKey);
        System.out.println(sign1);
        String data = jsonObject.getString("data");
        String decrypt = decrypt(data, "708BCE2BFB1B49E784CA1084405F9ECE");
        System.out.println(decrypt);
    }
    /**
     * @return
     * @Author cuiLei
     * @Description 交易通知
     * @Date 11:28 2021/5/10
     * @Param
     **/
    public static void notifyPay() throws Exception {
        String a = "{\"levyCode\":\"1000028712\",\"splitFlag\":\"NOSPLIT\",\"sign\":\"2df8a3bfe78a366ddebf19ed436c2ee6\",\"resCode\":\"0000\",\"amt\":10.00,\"merId\":\"6000010811\",\"state\":\"SETSUCC\",\"resMsg\":\"成功\",\"payItemId\":\"1390952870148308992\",\"reqId\":\"20210508165300\",\"merOrderId\":\"20210508165300\"}";
        JSONObject jsonObject = JSONObject.parseObject(a);
        String sign = jsonObject.getString("sign");
        jsonObject.remove("sign");
        jsonObject.remove("resCode");
        jsonObject.remove("resMsg");
        Map<String, Object> map = JSON.parseObject(jsonObject.toJSONString(), Map.class);
        String signString = sortKey(map);
        System.out.println(signString);
        boolean verify = verify(signString, sign, publicKey);
        System.out.println(verify);
    }

    public static void notifySoho() throws Exception {
        String a = "{\"bankCard\":\"BuwY6ft3iZ5YMJWnWvK1SiAP2jIdpCfA\",\"idCard\":\"UyHDOM1Ob3JARVmaIoScEiAP2jIdpCfA\",\"levyCode\":\"1000030412\",\"merId\":\"6000010711\",\"mobile\":\"ZMF+IE/lHjKex8SeNiim0A==\",\"name\":\"eDwf5flSMi0jtOynOmtziA==\",\"remark3\":\"VzcSVCcbw5k=\",\"reqId\":\"\",\"resCode\":\"0000\",\"resMsg\":\"成功\",\"sign\":\"ZdH/7l3XyilbYSVwEn57r8VfFsrzWz3BlKPVa3odAIztl4Cv6hwcxYzI6qVols79MwQsgDr5XhjttXShkRl5RiAlX5A8JWdFPGGEEVHURWBSlUK9x4Gfuy1mvcQgm7gyn7nB84aU3FYBKVEQ/tjC43uqleJ1KdlNKj3LLkgiuO8=\",\"state\":\"SIGN\"}";
        JSONObject jsonObject = JSONObject.parseObject(a);
        String sign = jsonObject.getString("sign");
        jsonObject.remove("sign");
        jsonObject.remove("resCode");
        jsonObject.remove("resMsg");
        Map<String, Object> map = JSON.parseObject(jsonObject.toJSONString(), Map.class);
        String signString = sortKey(map);
        boolean verify = verify(signString, sign, publicKey);
        String bankCard = jsonObject.getString("idCard");
        String decrypt = decrypt(bankCard, desKey);
        System.out.println(decrypt);
        System.out.println(verify);
    }

    public static void notifyReview() throws Exception {
        String a = "{\"bankCode\":\"6000014012\",\"changeAmt\":2.20,\"levyCode\":\"1000028712\",\"merId\":\"6000010811\",\"orderId\":\"1392771270734512128\",\"platFee\":0.00,\"reqId\":\"1392771270734512128\",\"resCode\":\"0000\",\"resMsg\":\"成功\",\"sign\":\"140143079f61f390d8bab58548f14875\"}";
        JSONObject jsonObject = JSONObject.parseObject(a);
        String sign = jsonObject.getString("sign");
        jsonObject.remove("sign");
        jsonObject.remove("resCode");
        jsonObject.remove("resMsg");
        Map<String, Object> map = JSON.parseObject(jsonObject.toJSONString(), Map.class);
        String signString = sortKey(map);
        boolean verify = verify(signString, sign, publicKey);
        System.out.println(verify);
    }


    public static boolean verify(String text, String sign, String key, String input_charset) {
        text = text + key;
        String mysign = DigestUtils.md5Hex(getContentBytes(text, input_charset));
        if (mysign.equals(sign)) {
            return true;
        } else {
            return false;
        }
    }

    private static byte[] getContentBytes(String content, String charset) {
        if (charset == null || "".equals(charset)) {
            return content.getBytes();
        }
        try {
            return content.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("MD5签名过程中出现错误,指定的编码集不对,您目前指定的编码集是:" + charset);
        }
    }

    public static String sha256_HMAC(String message, String secret, String charset) {
        String hash = "";
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(charset), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            byte[] bytes = sha256_HMAC.doFinal(message.getBytes(charset));
            hash = byteArrayToHexString(bytes);
        } catch (Exception e) {
            System.out.println("Error HmacSHA256 ===========" + e.getMessage());
        }
        return hash;
    }


    /**
     * 3DES解密ECB、pkcs5方式
     *
     * @param text
     * @param key
     * @return
     * @throws Exception
     */
    public static String decrypt(String text, String key) throws Exception {
        return new String(decrypt(decode(text), key, "UTF-8"), "UTF-8");
    }

    public static byte[] decrypt(byte[] encryptText, String desKey, String charset) throws Exception {
        SecureRandom sr = new SecureRandom();
        DESKeySpec dks = new DESKeySpec(desKey.getBytes(charset));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM_DES);
        SecretKey key = keyFactory.generateSecret(dks);
        Cipher cipher = Cipher.getInstance(PKCS5);
        cipher.init(Cipher.DECRYPT_MODE, key, sr);
        return cipher.doFinal(encryptText);
    }

    /**
     * @return
     * @Author cuiLei
     * @Description 文件转Byte
     * @Date 11:54 2021/4/25
     * @Param
     **/
    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (byte aSrc : src) {
            int v = aSrc & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    //读取文件
    public static byte[] fileRead(String fileName) {
        File file = new File(fileName);
        byte[] data = new byte[(int) file.length()];
        try {
            FileInputStream fis = new FileInputStream(file);
            fis.read(data);
            fis.close();
        } catch (IOException e) {
            System.out.println("文件读取异常" + e);
            return null;
        }
        return data;
    }


    public static String ALGORITHM_DES = "DES";
    public static String PKCS5 = "DES/ECB/PKCS5Padding";
    public static String ALGORITHM_DES_EDE = "DESede";
    public static String PKCS7 = "DESede/ECB/PKCS7Padding";
    public static String CBC_PKCS5 = "DESede/CBC/PKCS5Padding";


    public static String encrypt(String text, String key) throws Exception {
        return encode(encrypt(text.getBytes("UTF-8"), key, "UTF-8"));
    }


    public static byte[] encrypt(byte[] plainText, String desKey, String charset) throws Exception {
        SecureRandom sr = new SecureRandom();
        DESKeySpec dks = new DESKeySpec(desKey.getBytes(charset));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM_DES);
        SecretKey key = keyFactory.generateSecret(dks);
        Cipher cipher = Cipher.getInstance(PKCS5);
        cipher.init(Cipher.ENCRYPT_MODE, key, sr);
        return cipher.doFinal(plainText);
    }


    public static String SIGN_ALGORITHMS = "SHA1WithRSA";// 摘要加密算饭

    public static String CHAR_SET = "UTF-8";


    public static boolean verify(String content, String sign, String public_key)
            throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] encodedKey = decode(public_key);
        PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(
                encodedKey));

        java.security.Signature signature = java.security.Signature
                .getInstance(SIGN_ALGORITHMS);

        signature.initVerify(pubKey);
        signature.update(content.getBytes(CHAR_SET));

        boolean bverify = signature.verify(decode(sign));
        return bverify;

    }

    public static String sortKey(Map<String, Object> parameters) {
        SortedMap<String, Object> sortMap = new TreeMap<>();
        Set<String> keySet = parameters.keySet();
        for (String key : keySet) {
            sortMap.put(key, parameters.get(key));
        }
        return createSign(sortMap);
    }

    public static String createSign(SortedMap<String, Object> parameters) {
        StringBuffer sb = new StringBuffer();
        Set<?> es = parameters.entrySet();
        Iterator<?> it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            Object v = entry.getValue();
            if ("" != v && "null" != v && null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        // signature 不用添加到SortMap里面去，单独处理，编码方式采用UTF-8
        return sb.toString().substring(0, sb.length() - 1);
    }

    public static String sendPost(String curl, String param) {
        String result = "";// 返回的结果
        BufferedReader in = null;// 读取响应输入流
        try {
            //创建连接
            URL url = new URL(curl);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("Content-Type",
                    "application/json");

            connection.connect();

            //POST请求
            DataOutputStream out = new DataOutputStream(
                    connection.getOutputStream());

            out.writeBytes(param);
            out.flush();
            out.close();

            //读取响应
            // 定义BufferedReader输入流来读取URL的响应,并设置编码方式
            in = new BufferedReader(new InputStreamReader(connection
                    .getInputStream(), StandardCharsets.UTF_8));
            String line;
            // 读取返回的内容
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Http请求方法内部问题");
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    public static String httpClientPost(String urlParam, Map<String, Object> params, String charset) {
        StringBuffer resultBuffer = null;
        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(urlParam);
        // 构建请求参数
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        Iterator<Map.Entry<String, Object>> iterator = params.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Object> elem = iterator.next();
            list.add(new BasicNameValuePair(elem.getKey(), String.valueOf(elem.getValue())));
        }
        BufferedReader br = null;
        try {
            if (list.size() > 0) {
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, charset);
                httpPost.setEntity(entity);
            }
            HttpResponse response = client.execute(httpPost);
            // 读取服务器响应数据
            resultBuffer = new StringBuffer();
            br = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), charset));
            String temp;
            while ((temp = br.readLine()) != null) {
                resultBuffer.append(temp);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    br = null;
                    throw new RuntimeException(e);
                }
            }
        }
        return resultBuffer.toString();
    }

    /**
     * @return
     * @Author cuiLei
     * @Description 将Map以key=value&格式拼接
     * @Date 14:47 2021/4/7
     **/
    public static String createLinkString(Map<String, Object> params, boolean encode) {
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);//不按首字母排序, 需要按首字母排序请打开
        StringBuilder prestrSB = new StringBuilder();
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            Object value = params.get(key);
            if (StringUtils.isEmpty(value) || "null".equals(value) || "sign".equals(value) || "key".equals(value)) {
                continue;
            }
            if (encode) {
                try {
                    value = URLEncoder.encode(value.toString(), "GBK");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
                prestrSB.append(key).append("=").append(value);
            } else {
                prestrSB.append(key).append("=").append(value).append("&");
            }
        }
        return prestrSB.toString();
    }


    public static String sign(String content, String privateKey)
            throws Exception {
        byte[] str = decode(privateKey);
        PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(str);
        KeyFactory keyf = KeyFactory.getInstance("RSA");
        PrivateKey priKey = keyf.generatePrivate(priPKCS8);

        java.security.Signature signature = java.security.Signature
                .getInstance(SIGN_ALGORITHMS);

        signature.initSign(priKey);
        signature.update(content.getBytes(CHAR_SET));

        byte[] signed = signature.sign();

        return encode(signed);
    }


    static private final int BASELENGTH = 128;
    static private final int LOOKUPLENGTH = 64;
    static private final int TWENTYFOURBITGROUP = 24;
    static private final int EIGHTBIT = 8;
    static private final int SIXTEENBIT = 16;
    static private final int FOURBYTE = 4;
    static private final int SIGN = -128;
    static private final char PAD = '=';
    static private final boolean fDebug = false;
    static final private byte[] base64Alphabet = new byte[BASELENGTH];
    static final private char[] lookUpBase64Alphabet = new char[LOOKUPLENGTH];

    static {
        for (int i = 0; i < BASELENGTH; ++i) {
            base64Alphabet[i] = -1;
        }
        for (int i = 'Z'; i >= 'A'; i--) {
            base64Alphabet[i] = (byte) (i - 'A');
        }
        for (int i = 'z'; i >= 'a'; i--) {
            base64Alphabet[i] = (byte) (i - 'a' + 26);
        }

        for (int i = '9'; i >= '0'; i--) {
            base64Alphabet[i] = (byte) (i - '0' + 52);
        }

        base64Alphabet['+'] = 62;
        base64Alphabet['/'] = 63;

        for (int i = 0; i <= 25; i++) {
            lookUpBase64Alphabet[i] = (char) ('A' + i);
        }

        for (int i = 26, j = 0; i <= 51; i++, j++) {
            lookUpBase64Alphabet[i] = (char) ('a' + j);
        }

        for (int i = 52, j = 0; i <= 61; i++, j++) {
            lookUpBase64Alphabet[i] = (char) ('0' + j);
        }
        lookUpBase64Alphabet[62] = (char) '+';
        lookUpBase64Alphabet[63] = (char) '/';

    }

    private static boolean isWhiteSpace(char octect) {
        return (octect == 0x20 || octect == 0xd || octect == 0xa || octect == 0x9);
    }

    private static boolean isPad(char octect) {
        return (octect == PAD);
    }

    private static boolean isData(char octect) {
        return (octect < BASELENGTH && base64Alphabet[octect] != -1);
    }

    /**
     * remove WhiteSpace from MIME containing encoded Base64 data.
     *
     * @param data the byte array of base64 data (with WS)
     * @return the new length
     */
    private static int removeWhiteSpace(char[] data) {
        if (data == null) {
            return 0;
        }

        // count characters that's not whitespace
        int newSize = 0;
        int len = data.length;
        for (int i = 0; i < len; i++) {
            if (!isWhiteSpace(data[i])) {
                data[newSize++] = data[i];
            }
        }
        return newSize;
    }

    public static String encode(byte[] binaryData) {

        if (binaryData == null) {
            return null;
        }

        int lengthDataBits = binaryData.length * EIGHTBIT;
        if (lengthDataBits == 0) {
            return "";
        }

        int fewerThan24bits = lengthDataBits % TWENTYFOURBITGROUP;
        int numberTriplets = lengthDataBits / TWENTYFOURBITGROUP;
        int numberQuartet = fewerThan24bits != 0 ? numberTriplets + 1 : numberTriplets;
        char encodedData[] = null;

        encodedData = new char[numberQuartet * 4];

        byte k = 0, l = 0, b1 = 0, b2 = 0, b3 = 0;

        int encodedIndex = 0;
        int dataIndex = 0;
        if (fDebug) {
            System.out.println("number of triplets = " + numberTriplets);
        }

        for (int i = 0; i < numberTriplets; i++) {
            b1 = binaryData[dataIndex++];
            b2 = binaryData[dataIndex++];
            b3 = binaryData[dataIndex++];

            if (fDebug) {
                System.out.println("b1= " + b1 + ", b2= " + b2 + ", b3= " + b3);
            }

            l = (byte) (b2 & 0x0f);
            k = (byte) (b1 & 0x03);

            byte val1 = ((b1 & SIGN) == 0) ? (byte) (b1 >> 2) : (byte) ((b1) >> 2 ^ 0xc0);
            byte val2 = ((b2 & SIGN) == 0) ? (byte) (b2 >> 4) : (byte) ((b2) >> 4 ^ 0xf0);
            byte val3 = ((b3 & SIGN) == 0) ? (byte) (b3 >> 6) : (byte) ((b3) >> 6 ^ 0xfc);

            if (fDebug) {
                System.out.println("val2 = " + val2);
                System.out.println("k4   = " + (k << 4));
                System.out.println("vak  = " + (val2 | (k << 4)));
            }

            encodedData[encodedIndex++] = lookUpBase64Alphabet[val1];
            encodedData[encodedIndex++] = lookUpBase64Alphabet[val2 | (k << 4)];
            encodedData[encodedIndex++] = lookUpBase64Alphabet[(l << 2) | val3];
            encodedData[encodedIndex++] = lookUpBase64Alphabet[b3 & 0x3f];
        }

        // form integral number of 6-bit groups
        if (fewerThan24bits == EIGHTBIT) {
            b1 = binaryData[dataIndex];
            k = (byte) (b1 & 0x03);
            if (fDebug) {
                System.out.println("b1=" + b1);
                System.out.println("b1<<2 = " + (b1 >> 2));
            }
            byte val1 = ((b1 & SIGN) == 0) ? (byte) (b1 >> 2) : (byte) ((b1) >> 2 ^ 0xc0);
            encodedData[encodedIndex++] = lookUpBase64Alphabet[val1];
            encodedData[encodedIndex++] = lookUpBase64Alphabet[k << 4];
            encodedData[encodedIndex++] = PAD;
            encodedData[encodedIndex++] = PAD;
        } else if (fewerThan24bits == SIXTEENBIT) {
            b1 = binaryData[dataIndex];
            b2 = binaryData[dataIndex + 1];
            l = (byte) (b2 & 0x0f);
            k = (byte) (b1 & 0x03);

            byte val1 = ((b1 & SIGN) == 0) ? (byte) (b1 >> 2) : (byte) ((b1) >> 2 ^ 0xc0);
            byte val2 = ((b2 & SIGN) == 0) ? (byte) (b2 >> 4) : (byte) ((b2) >> 4 ^ 0xf0);

            encodedData[encodedIndex++] = lookUpBase64Alphabet[val1];
            encodedData[encodedIndex++] = lookUpBase64Alphabet[val2 | (k << 4)];
            encodedData[encodedIndex++] = lookUpBase64Alphabet[l << 2];
            encodedData[encodedIndex++] = PAD;
        }

        return new String(encodedData);
    }

    /**
     * Decodes Base64 data into octects
     *
     * @param encoded string containing Base64 data
     * @return Array containind decoded data.
     */
    public static byte[] decode(String encoded) {

        if (encoded == null) {
            return null;
        }

        char[] base64Data = encoded.toCharArray();
        // remove white spaces
        int len = removeWhiteSpace(base64Data);

        if (len % FOURBYTE != 0) {
            return null;//should be divisible by four
        }

        int numberQuadruple = (len / FOURBYTE);

        if (numberQuadruple == 0) {
            return new byte[0];
        }

        byte decodedData[] = null;
        byte b1 = 0, b2 = 0, b3 = 0, b4 = 0;
        char d1 = 0, d2 = 0, d3 = 0, d4 = 0;

        int i = 0;
        int encodedIndex = 0;
        int dataIndex = 0;
        decodedData = new byte[(numberQuadruple) * 3];

        for (; i < numberQuadruple - 1; i++) {

            if (!isData((d1 = base64Data[dataIndex++])) || !isData((d2 = base64Data[dataIndex++]))
                    || !isData((d3 = base64Data[dataIndex++]))
                    || !isData((d4 = base64Data[dataIndex++]))) {
                return null;
            }//if found "no data" just return null

            b1 = base64Alphabet[d1];
            b2 = base64Alphabet[d2];
            b3 = base64Alphabet[d3];
            b4 = base64Alphabet[d4];

            decodedData[encodedIndex++] = (byte) (b1 << 2 | b2 >> 4);
            decodedData[encodedIndex++] = (byte) (((b2 & 0xf) << 4) | ((b3 >> 2) & 0xf));
            decodedData[encodedIndex++] = (byte) (b3 << 6 | b4);
        }

        if (!isData((d1 = base64Data[dataIndex++])) || !isData((d2 = base64Data[dataIndex++]))) {
            return null;//if found "no data" just return null
        }

        b1 = base64Alphabet[d1];
        b2 = base64Alphabet[d2];

        d3 = base64Data[dataIndex++];
        d4 = base64Data[dataIndex++];
        if (!isData((d3)) || !isData((d4))) {//Check if they are PAD characters
            if (isPad(d3) && isPad(d4)) {
                if ((b2 & 0xf) != 0)//last 4 bits should be zero
                {
                    return null;
                }
                byte[] tmp = new byte[i * 3 + 1];
                System.arraycopy(decodedData, 0, tmp, 0, i * 3);
                tmp[encodedIndex] = (byte) (b1 << 2 | b2 >> 4);
                return tmp;
            } else if (!isPad(d3) && isPad(d4)) {
                b3 = base64Alphabet[d3];
                if ((b3 & 0x3) != 0)//last 2 bits should be zero
                {
                    return null;
                }
                byte[] tmp = new byte[i * 3 + 2];
                System.arraycopy(decodedData, 0, tmp, 0, i * 3);
                tmp[encodedIndex++] = (byte) (b1 << 2 | b2 >> 4);
                tmp[encodedIndex] = (byte) (((b2 & 0xf) << 4) | ((b3 >> 2) & 0xf));
                return tmp;
            } else {
                return null;
            }
        } else { //No PAD e.g 3cQl
            b3 = base64Alphabet[d3];
            b4 = base64Alphabet[d4];
            decodedData[encodedIndex++] = (byte) (b1 << 2 | b2 >> 4);
            decodedData[encodedIndex++] = (byte) (((b2 & 0xf) << 4) | ((b3 >> 2) & 0xf));
            decodedData[encodedIndex++] = (byte) (b3 << 6 | b4);

        }

        return decodedData;
    }

    private final static String[] hexDigits = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
            "a", "b", "c", "d", "e", "f"};

    /**
     * 转换字节数组为16进制字串
     *
     * @param b 字节数组
     * @return 16进制字串
     */
    public static String byteArrayToHexString(byte[] b) {
        StringBuilder resultSb = new StringBuilder();
        for (byte aB : b) {
            resultSb.append(byteToHexString(aB));
        }
        return resultSb.toString();
    }

    /**
     * 转换byte到16进制
     *
     * @param b 要转换的byte
     * @return 16进制格式
     */
    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n = 256 + n;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    /**
     * MD5编码
     *
     * @param origin 原始字符串
     * @return 经过MD5加密之后的结果
     */
    public static String MD5Encode(String origin) {
        String resultString = null;
        try {
            resultString = origin;
            MessageDigest md = MessageDigest.getInstance("MD5");
            resultString = byteArrayToHexString(md.digest(resultString.getBytes("utf-8")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultString;
    }

    /**
     * MD5编码
     *
     * @param origin 原始字符串
     * @return 经过MD5加密之后的结果
     */
    public static String MD5WithOutUnicode(String origin) {
        String resultString = null;
        try {
            resultString = origin;
            MessageDigest md = MessageDigest.getInstance("MD5");
            resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultString;
    }

}

