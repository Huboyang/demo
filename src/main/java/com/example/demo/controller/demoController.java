package com.example.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.ctstudio.common.exception.BusinessException;
import com.ctstudio.common.util.security.digest.DigestAlgorithm;
import com.example.demo.SAP.SAPDAO;
import com.example.demo.SAP.SAPJSONCaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import sun.security.krb5.internal.crypto.HmacSha1Aes128CksumType;

import javax.xml.ws.RequestWrapper;
import java.net.URI;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class demoController {

  private static Logger LOG = LoggerFactory.getLogger(demoController.class);


  @Autowired
  private SAPDAO sapdao;

  @Autowired
  private RestTemplate restTemplate;

  private static final String APPID = "088ad376b6514ed0a191067308c284f2";
  private static final String APPKEY = "de8240ae9a";
  private static final Integer EXPIRE = 1;

  @RequestMapping
  public String test() throws NoSuchAlgorithmException {
    long time = System.currentTimeMillis();
    String str = String.format("%s%s%d", APPID, APPKEY, time);
    String str2 = DigestAlgorithm.getMD5().digestHexLowercase(str);
    String url = "http://hrkf-t.sunac.com.cn/open/platform/getAccessToken.json?"
                                + "appId=" + APPID
                                + "&createTime=" + time
                                + "&sign=" + str2
                                + "&expire="  + EXPIRE;
    RequestEntity<String> requestEntity  = new RequestEntity<>(HttpMethod.GET, URI.create(url));
    ResponseEntity<String> exchange = restTemplate.exchange(requestEntity, String.class);
    return exchange.getBody();
  }

  @RequestMapping("/token")
  public String demo2(String token){
    Map <String,Object> map = new HashMap<>();
    JSONObject data = new JSONObject();
    data.put("email","ZHANGZHONGZHUANG@SUNAC.COM.CN");
    data.put("type",1);
    map.put("action","console_direct_url");
    map.put("access_token",token);
    map.put("data",data.toJSONString());

    String url = "http://hrkf-t.sunac.com.cn/open/platform/api.json";
    HttpHeaders hh = new HttpHeaders();
    hh.add(HttpHeaders.CONTENT_TYPE, "application/json");
    RequestEntity<Map> requestEntity  = new RequestEntity<>(map,hh,HttpMethod.POST, URI.create(url));
    ResponseEntity<String> exchange = restTemplate.exchange(requestEntity, String.class);
    return exchange.getBody();
  }


  @RequestMapping
  public String demo(){

    SAPJSONCaller caller = null;

    try {
      caller = sapdao.jsonCaller().setFunction("ZHRFM_GET_PUB_DATA")
          .addInput("IV_PERNR", "6")
          .addInput("IV_USER", "6")
          .subscribe();
    } catch (BusinessException e) {
      LOG.error("发生错误:",e);
      e.printStackTrace();
    }

    return caller.getOutput("ES_MAIN");
  }
}
