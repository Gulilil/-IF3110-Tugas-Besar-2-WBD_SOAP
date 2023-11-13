package com.wbd_soap.service;

import com.sun.net.httpserver.HttpExchange;
import com.wbd_soap.model.Database;
import com.wbd_soap.model.Logging;
import com.wbd_soap.model.Reference;
import com.wbd_soap.utils.Randomizer;

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
    private Database db;
    private WebServiceContext wsContext;

    public ReferenceService(){
        this.db = new Database();
    }
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
        if (!checkAPIKey()){
            return "Invalid API Key";
        }
        Reference refDataCheckAnime = db.getReferenceIDWithAnimeID(anime_id);
        if (refDataCheckAnime != null){
            String msg = "Data with anime_account_id: "+anime_id+" already exist";
            return msg;
        } else {
            Randomizer r = new Randomizer();
            String randRefCode = r.generateRandomWord(20);
            Reference refDataCheckRef = db.getReferenceWithReferralCode(randRefCode);
            while (refDataCheckRef != null){
                randRefCode = r.generateRandomWord(20);
                refDataCheckRef = db.getReferenceWithReferralCode(randRefCode);
            }
            Reference ref = new Reference(null, anime_id, null, randRefCode, 0 );
            this.db.insertReferenceDatabase(ref);
            String msg = "Successfully insert Data with anime_account_id: "+anime_id;
            return msg;
        }
    }

    @WebMethod
    public String updateReferenceEstablishLink(int forum_id, int anime_id){
        if (!checkAPIKey()){
            return "Invalid API Key";
        }
        Reference refDataCheckAnime = db.getReferenceIDWithAnimeID(anime_id);
        if (refDataCheckAnime == null){
            String msg = "Data with anime_account_id: "+anime_id+" does not exist";
            return msg;
        } else {
            Integer dataForumId = refDataCheckAnime.getForumAccountId();
            if (dataForumId == 0 || dataForumId == null){
                Reference ref = new Reference(
                        refDataCheckAnime.getId(),
                        anime_id,
                        forum_id,
                        refDataCheckAnime.getReferalCode(),
                        refDataCheckAnime.getPoint()
                );
                this.db.updateReferenceDatabase(ref);
                String msg = "Successfully link Data with anime_account_id: "+anime_id+" and forum_account_id: "+forum_id;
                return msg;
            } else {
                String msg = "The account with anime_account_id: "+ anime_id + " has been occupied";
                return msg;
            }
        }
    }

    @WebMethod
    public String updateReferenceUnlink(int forum_id, int anime_id){
        if (!checkAPIKey()){
            return "Invalid API Key";
        }
        Reference refDataCheckAnime = db.getReferenceIDWithAnimeID(anime_id);
        if (refDataCheckAnime == null){
            String msg = "Data with anime_account_id: "+anime_id+" does not exist";
            return msg;
        } else if (!refDataCheckAnime.getForumAccountId().equals(forum_id)){
            String msg = "Invalid data detected";
            return msg;
        } else {
            Integer dataForumId = refDataCheckAnime.getForumAccountId();
            if (dataForumId == 0 || dataForumId == null){
                String msg = "The account with anime_account_id: "+ anime_id + " and forum_account_id: "+ forum_id+ " has no link";
                return msg;
            } else {
                Reference ref = new Reference(
                        refDataCheckAnime.getId(),
                        anime_id,
                        null,
                        refDataCheckAnime.getReferalCode(),
                        refDataCheckAnime.getPoint()
                );
                this.db.updateReferenceDatabase(ref);
                String msg = "Successfully unlink Data with forum_account_id: "+forum_id+" from anime_account_id: "+anime_id;
                return msg;
            }
        }
    }

    @WebMethod
    public String updateReferenceChangePoint(int forum_id, int anime_id, int numChange){
        if (!checkAPIKey()){
            return "Invalid API Key";
        }
        Reference refDataCheckAnime = db.getReferenceIDWithAnimeID(anime_id);
        if (refDataCheckAnime == null){
            String msg = "Data with anime_account_id: "+anime_id+" does not exist";
            return msg;
        } else if (!refDataCheckAnime.getForumAccountId().equals(forum_id)){
            String msg = "Invalid data detected";
            return msg;
        } else {
            Integer dataForumId = refDataCheckAnime.getForumAccountId();
            if (dataForumId == 0 || dataForumId == null){
                String msg = "The account with anime_account_id: "+ anime_id + " and forum_account_id: "+ forum_id+ " has no link";
                return msg;
            } else {
                Reference ref = new Reference(
                        refDataCheckAnime.getId(),
                        anime_id,
                        forum_id,
                        refDataCheckAnime.getReferalCode() + numChange,
                        refDataCheckAnime.getPoint()
                );
                this.db.updateReferenceDatabase(ref);
                String msg = "Successfully change the point of Data with forum_account_id: "+forum_id+" and anime_account_id: "+anime_id;
                return msg;
            }
        }
    }


}
