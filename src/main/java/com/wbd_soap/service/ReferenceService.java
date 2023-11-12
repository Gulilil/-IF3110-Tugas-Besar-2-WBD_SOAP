package com.wbd_soap.service;

import com.sun.net.httpserver.HttpExchange;
import com.wbd_soap.model.Database;
import com.wbd_soap.model.Logging;

import javax.annotation.Resource;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import java.sql.ResultSet;
import java.sql.Statement;

@WebService
public class ReferenceService {
    @Resource
    private Database db = new Database();
    private WebServiceContext wsContext;

//    public ReferenceService(){
//        this.db = new Database();
//        this.db.setupDatabase();
//    }
    @WebMethod
    public void insertNewLog(String desc){
        MessageContext msgContext = this.wsContext.getMessageContext();
        HttpExchange httpExchange = (HttpExchange) msgContext.get("com.sun.xml.ws.http.exchange");
        String ip = httpExchange.getRemoteAddress().getAddress().getHostAddress();
        String endpoint = httpExchange.getRequestURI().toString();
        String apiKey = httpExchange.getRequestHeaders().getFirst("X-API-KEY");
        String description = apiKey + ": " + desc;
        Logging newLog = new Logging(null, description, ip, endpoint, null);
        this.db.insertLogDatabase(newLog);
    }

    @WebMethod
    public boolean checkAPIKey(){
        String[] ValidKeys = { "PHP", "REST", "SPA", "Postman"};
        MessageContext msg = wsContext.getMessageContext();
        HttpExchange httpExchange = (HttpExchange) msg.get("com.sun.xml.ws.http.exchange");
        String apiKey = httpExchange.getRequestHeaders().getFirst("X-API-KEY");
        if (apiKey == null){
            return false;
        } else {
            for (String key : ValidKeys) {
                if (apiKey.equals(key)){
                    return true;
                }
            }
            return false;
        }
    }

    @WebMethod
    public void connectToPhp(){

    }

    @WebMethod
    public String insertReference(int anime_id){
        return "";
    }

    @WebMethod
    public String updateReferenceEstablishLink(int forum_id, int anime_id){
        if (!checkAPIKey()){
            return "Invalid API Key";
        }
        int refId = db.getReferenceIDWithAnimeID(anime_id);
        if (refId <= 0){
            String msg = "Data with anime_id: "+anime_id+" does not exist";
            return msg;
        } else {
            ResultSet res = db.getReferenceWithID(refId);
            try {
                int fid = res.getInt("forum_account_id");
                if (res.wasNull()){
                    // Update
                    return "";
                } else {
                    // Occupied
                    return "The account with anime_account_id: "+anime_id+" has been occupied";
                }

            } catch (Exception e){
                return "Failed getting data";
            }
        }
    }

    @WebMethod
    public String updateReferenceUnlink(int forum_id, int anime_id){
        return "";
    }

    @WebMethod
    public String updateReferenceChangePoint(int forum_id, int anime_id, int num){
        return "";
    }


}
