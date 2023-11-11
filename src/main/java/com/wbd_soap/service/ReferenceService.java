package com.wbd_soap.service;

import com.wbd_soap.model.Database;

import javax.annotation.Resource;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.spi.http.HttpExchange;

@WebService
public class ReferenceService {
    @Resource
    private Database db;
    private WebServiceContext wsContext;

    public ReferenceService(){
        this.db = new Database();
        this.db.setupDatabase();
    }
    @WebMethod
    public void insertNewLog(String desc){
        MessageContext msgContext = this.wsContext.getMessageContext();
        HttpExchange httpExchange = (HttpExchange) msgContext.get("com.sun.xml.ws.http.exchange");
        String ip = httpExchange.getRemoteAddress().getAddress().getHostAddress();
        String endpoint = httpExchange.getRequestURI().toString();

    }

}
