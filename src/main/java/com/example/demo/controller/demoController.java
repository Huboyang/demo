package com.example.demo.controller;

import com.ctstudio.common.exception.BusinessException;
import com.example.demo.SAP.SAPDAO;
import com.example.demo.SAP.SAPJSONCaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.ws.RequestWrapper;

@RestController
public class demoController {

  private static Logger LOG = LoggerFactory.getLogger(demoController.class);


  @Autowired
  private SAPDAO sapdao;

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
