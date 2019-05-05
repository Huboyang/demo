package com.example.demo.SAP;

import com.sap.conn.jco.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SAPDAO {

  @Autowired
  private JCoDestination destination;

  public SAPJSONCaller jsonCaller() {
    return new SAPJSONCaller(destination);
  }

}
