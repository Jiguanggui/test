package com.example.demo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author: Ji GuangGui
 * @Date: 2023-05-22-10:36
 */
public class test3 {

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
}
