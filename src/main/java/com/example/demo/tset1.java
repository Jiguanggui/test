//package com.example.demo;
//
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * @Description:
// * @Author: Ji GuangGui
// * @Date: 2023-05-18-10:20
// */
//public class tset1 {
//    public static String version = "1";
//
//    public static String signType = "RSA";
//
//    public static String encType = "DES";
//
//    public static String merId = "6000010711";
//
//    public static String tenantCode = "0525";
//
//    public static String levyCode = "1000030412";
//
//    public static String bankCode = "6000015812";
//
//    static String charset = "UTF-8";
//
//    public static String host = "http://47.95.0.114:20000/business/";
//    //接口配置里面的ApiKey
//    public static String desKey = "FE78B8C26F574DE6B7A2B3E90119FAD8";
//
//    // 商户私钥
//    public static String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKUfKjKXuXXo5jCccpkDbRIEaRdEUjhTvoy7eGz/FW6TgDsnCTdSnZkfn0hskgqkbvQaJOlay2ZoUjO1xNQIJOA6Gl5CAAcFXlOQ6Wq5iHhig/hf5jSo4VoTE9I3TXTFTk41zr+GRQPT0qrYOBCL6QQ27e3QLqMBPb4VWdNpXqXlAgMBAAECgYAbptdrN4tgWotJ8rkiNLesP77VnaRjOGr9qFtnbWRKazsbLNNXR2eLAAetmjjuvVCzYjlyNrk/QhMaA8OdemuqOFQELvztWnyjnop8NzrwUmOxJzBpnzwxrv76a1xXckgYJt3/KIoyJOR88TUoakVGh12RexDKjaGQLvtcdk91gQJBAM5y4fKSSD7E5aoLyWmBQWA3pwMJhMG6LseDYOXylcQxrG+A/hYtxS9jY91CfEvRlrvqMS6MSeL8jKzrp8+GqsUCQQDMwPf7tcRjO1qnZNrpFozi/qFlaM8q6ydlNQT2OhRh5qtcxk1tKrSrI0M1aUiYURH7Za9qCucpfrURq1HuzEChAkBrfCKpUYHttEPCkdpuZM5t0uvQR/9ngZDjbUJg1jklaZtWfnlLgM7+CigvRXNDYj6xXY/3pTC+zjBLw3GeOYXlAkAbOYU/mLmWICw7mbNpzq/I9uZq33a8VCwWzufr/Wj8Y3lhipR88XK4VqX3Ehqu9giYDrs2NGDsKYbV5JuuTsZBAkEAvgL1TYPw5wIuzuu26058H77D3UwCHygGDVWIFoJ3WShIFRrvNnLPjTlRuWukpghXeX6jo8dLCVuQlWna5oh4oQ==";
//    //MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCk826JUN9kahSAHTzyChuZz0wDNAs00z5z6vkiMkItoSs8I35ZgnU21IFJZZXJw8D8LzdZLcXuBjYrshkMqtSQyaEyNu9cn5h7bvSzVuDBzwkhrRSaT48a7bYa99nUMZ7hsyXkPTII+SGoZHgysw0ei/JZ0N/Qrj9rcA+hcKuXuQIDAQAB
//    // 运营公钥
//    public static String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCbfWSbWP0tXtPSxgXX4kcdG5G8hPFdrTMEu4xVDDJYRAQ8W2wHl6Ts1Q+JYXaVbvgEqbqrRY6ezWL2TJiA7iobb8dFI53ED+eSVHJg1Sc5HO/P/n9tlMWpR+t2QsuB9BRc6k7u9M9w9yeIZ0meq/jjjVt1Ut9ZV/hpifO+BVeO0QIDAQAB";
//
//    public static void main(String[] args) throws Exception{
//        String reqId = DateUtils.formatDate(new Date(), "yyyyMMddHHmmss");
//        Map<String, Object> params = new HashMap<>();
//        params.put("version", version);
//        params.put("reqId", reqId);
//        params.put("agentCode","123456");
//        params.put("thirdOrderId","232523");
//
//    }
//}
