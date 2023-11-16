package com.wbd_soap.service;

import com.sun.net.httpserver.HttpExchange;
import com.wbd_soap.model.Database;
import com.wbd_soap.model.Logging;
import com.wbd_soap.model.Reference;
import com.wbd_soap.utils.Randomizer;

import javax.annotation.Resource;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

@WebService
public class ReferenceService {

    @Resource
    private WebServiceContext wsContext;

    @WebMethod
    public String Testing(String str){
        return String.format("Testing whether this function returns "+str);
    }

    @WebMethod
    public void insertNewLog(String desc){
        Database db = new Database();
        MessageContext msgContext = this.wsContext.getMessageContext();
        HttpExchange httpExchange = (HttpExchange) msgContext.get("com.sun.xml.ws.http.exchange");
        String ip = httpExchange.getRemoteAddress().getAddress().getHostAddress();
        String endpoint = httpExchange.getRequestURI().toString();
        String apiKey = httpExchange.getRequestHeaders().getFirst("X-API-KEY");
        String description = apiKey + ": " + desc;
        Logging newLog = new Logging(null, description, ip, endpoint, null);
        db.insertLogDatabase(newLog);
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
    public String insertReference(int anime_id){
        if (!checkAPIKey()){
            String msg = "Invalid API Key";
            this.insertNewLog("[INSERT] "+msg);
            return msg;
        }
        Database db = new Database();
        Reference refDataCheckAnime = db.getReferenceIDWithAnimeID(anime_id);
        if (refDataCheckAnime != null){
            String msg = "Data with anime_account_id: "+anime_id+" already exist";
            this.insertNewLog("[INSERT] "+msg);
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
            db.insertReferenceDatabase(ref);
            String msg = "Successfully insert Data with anime_account_id: "+anime_id;
            this.insertNewLog("[INSERT] "+msg);
            return msg;
        }
    }

    @WebMethod
    public String insertReferenceMany(String many_anime_id){
        // Format data "1;2;3;4;5;6;7", split by ";"
        if (!checkAPIKey()){
            String msg = "Invalid API Key";
            this.insertNewLog("[INSERT] "+msg);
            return msg;
        }
        String[] arrOfId = many_anime_id.split(";");
        String msg = "";
        for(String s: arrOfId){
            try {
                Integer id = Integer.parseInt(s);
                String temp = insertReference(id);
                msg += "{ "+temp+" }, ";
            } catch (Exception e){
                msg = "Invalid input detected. System discards the operation at inserting anime_account_id: "+s;
                this.insertNewLog("[INSERT] "+msg);
                return msg;
            }
        }
        this.insertNewLog("[INSERT] "+msg);
        return msg;

    }

    @WebMethod
    public String updateReferenceEstablishLink(int forum_id, int anime_id){
        if (!checkAPIKey()){
            String msg = "Invalid API Key";
            this.insertNewLog("[UPDATE] "+msg);
            return msg;
        }
        Database db = new Database();
        Reference refDataCheckAnime = db.getReferenceIDWithAnimeID(anime_id);
        // Check if data anime_id exist
        if (refDataCheckAnime == null){
            String msg = "Data with anime_account_id: "+anime_id+" does not exist";
            this.insertNewLog("[UPDATE] "+msg);
            return msg;
        } else {
            Reference refDataCheckForum = db.getReferenceIDWithForumID(forum_id);
            // Check if data forum_id already occupied somewhere
            if (refDataCheckForum == null){
                Integer dataForumId = refDataCheckAnime.getForumAccountId();
                if (dataForumId == 0 || dataForumId == null){
                    Reference ref = new Reference(
                            refDataCheckAnime.getId(),
                            anime_id,
                            forum_id,
                            refDataCheckAnime.getReferalCode(),
                            refDataCheckAnime.getPoint()
                    );
                    db.updateReferenceDatabase(ref);
                    String msg = "Successfully link Data with anime_account_id: "+anime_id+" and forum_account_id: "+forum_id;
                    this.insertNewLog("[UPDATE] "+msg);
                    return msg;
                } else {
                    String msg = "The account with anime_account_id: "+ anime_id + " has been occupied";
                    this.insertNewLog("[UPDATE] "+msg);
                    return msg;
                }
            } else {
                String msg = "The account with forum_account_id: "+ forum_id +" has been linked somewhere";
                this.insertNewLog("[UPDATE] "+msg);
                return msg;
            }

        }
    }

    @WebMethod
    public String updateReferenceUnlink(int forum_id, int anime_id){
        if (!checkAPIKey()){
            String msg = "Invalid API Key";
            this.insertNewLog("[UPDATE] "+msg);
            return msg;
        }
        Database db = new Database();
        Reference refDataCheckAnime = db.getReferenceIDWithAnimeID(anime_id);
        if (refDataCheckAnime == null){
            String msg = "Data with anime_account_id: "+anime_id+" does not exist";
            this.insertNewLog("[UPDATE] "+msg);
            return msg;
        } else if (!refDataCheckAnime.getForumAccountId().equals(forum_id)){
            String msg = "Invalid data detected";
            this.insertNewLog("[UPDATE] "+msg);
            return msg;
        } else {
            Integer dataForumId = refDataCheckAnime.getForumAccountId();
            if (dataForumId == 0 || dataForumId == null){
                String msg = "The account with anime_account_id: "+ anime_id + " and forum_account_id: "+ forum_id+ " has no link";
                this.insertNewLog("[UPDATE] "+msg);
                return msg;
            } else {
                Reference ref = new Reference(
                        refDataCheckAnime.getId(),
                        anime_id,
                        null,
                        refDataCheckAnime.getReferalCode(),
                        refDataCheckAnime.getPoint()
                );
                db.updateReferenceDatabase(ref);
                String msg = "Successfully unlink Data with forum_account_id: "+forum_id+" from anime_account_id: "+anime_id;
                this.insertNewLog("[UPDATE] "+msg);
                return msg;
            }
        }
    }

    @WebMethod
    public String updateReferenceChangePoint(int forum_id, int anime_id, int numChange){
        if (!checkAPIKey()){
            String msg = "Invalid API Key";
            this.insertNewLog("[UPDATE] "+msg);
            return msg;
        }
        Database db = new Database();
        Reference refDataCheckAnime = db.getReferenceIDWithAnimeID(anime_id);
        if (refDataCheckAnime == null){
            String msg = "Data with anime_account_id: "+anime_id+" does not exist";
            this.insertNewLog("[UPDATE] "+msg);
            return msg;
        } else if (!refDataCheckAnime.getForumAccountId().equals(forum_id)){
            String msg = "Invalid data detected";
            this.insertNewLog("[UPDATE] "+msg);
            return msg;
        } else {
            Integer dataForumId = refDataCheckAnime.getForumAccountId();
            if (dataForumId == 0 || dataForumId == null){
                String msg = "The account with anime_account_id: "+ anime_id + " and forum_account_id: "+ forum_id+ " has no link";
                this.insertNewLog("[UPDATE] "+msg);
                return msg;
            } else {
                Reference ref = new Reference(
                        refDataCheckAnime.getId(),
                        anime_id,
                        forum_id,
                        refDataCheckAnime.getReferalCode(),
                        refDataCheckAnime.getPoint() + numChange
                );
                db.updateReferenceDatabase(ref);
                String msg = "Successfully change the point of Data with forum_account_id: "+forum_id+" and anime_account_id: "+anime_id;
                this.insertNewLog("[UPDATE] "+msg);
                return msg;
            }
        }
    }

    @WebMethod
    public String updateReferenceChangePointWithAnimeId(int anime_id, int numChange){
        if (!checkAPIKey()){
            String msg = "Invalid API Key";
            this.insertNewLog("[UPDATE] "+msg);
            return msg;
        }
        Database db = new Database();
        Reference refDataCheckAnime = db.getReferenceIDWithAnimeID(anime_id);
        if (refDataCheckAnime == null){
            String msg = "Data with anime_account_id: "+anime_id+" does not exist";
            this.insertNewLog("[UPDATE] "+msg);
            return msg;
        } else {
                Reference ref = new Reference(
                        refDataCheckAnime.getId(),
                        anime_id,
                        refDataCheckAnime.getForumAccountId(),
                        refDataCheckAnime.getReferalCode(),
                        refDataCheckAnime.getPoint() + numChange
                );
                db.updateReferenceDatabase(ref);
                String msg = "Successfully change the point of Data with anime_account_id: "+anime_id;
                this.insertNewLog("[UPDATE] "+msg);
                return msg;
        }
    }

    @WebMethod
    public String updateReferenceChangePointWithForumId(int forum_id, int numChange){
        if (!checkAPIKey()){
            String msg = "Invalid API Key";
            this.insertNewLog("[UPDATE] "+msg);
            return msg;
        }
        Database db = new Database();
        Reference refDataCheckForum = db.getReferenceIDWithForumID(forum_id);
        if (refDataCheckForum == null){
            String msg = "Data with forum_account_id: "+forum_id+" does not exist";
            this.insertNewLog("[UPDATE] "+msg);
            return msg;
        } else {
            Reference ref = new Reference(
                    refDataCheckForum.getId(),
                    refDataCheckForum.getAnimeAccountId(),
                    forum_id,
                    refDataCheckForum.getReferalCode(),
                    refDataCheckForum.getPoint() + numChange
            );
            db.updateReferenceDatabase(ref);
            String msg = "Successfully change the point of Data with forum_account_id: "+forum_id;
            this.insertNewLog("[UPDATE] "+msg);
            return msg;
        }
    }

    @WebMethod
    public String giveAllReferenceData(){
        if (!checkAPIKey()) {
            String msg = "Invalid API Key";
            this.insertNewLog("[SELECT] "+msg);
            return msg;
        }
        Database db = new Database();
        ArrayList<Reference> refsData = db.getAllReference();
        if (refsData.isEmpty()){
            String msg = "{\"data\": []}";
            this.insertNewLog("[SELECT] "+msg);
            return msg;
        } else {
            String msg = "{\"data\": [";
            for (Reference ref: refsData) {
                msg += String.format("{\"id\": %s, \"anime_account_id\": %s, \"forum_account_id\": %s, \"referral_code\": \"%s\", \"point\": %s},",
                        ref.getId(),
                        ref.getAnimeAccountId(),
                        ref.getForumAccountId(),
                        ref.getReferalCode(),
                        ref.getPoint());
            }
            msg = msg.substring(0, msg.length() - 1); //to Delete last comma
            msg += "]}";
            this.insertNewLog("[SELECT] Succesfully select all reference data");
            return msg;
        }
    }

    @WebMethod
    public String giveAllReferenceDataLimitOffset(Integer limit, Integer offset){
        if (!checkAPIKey()) {
            String msg = "Invalid API Key";
            this.insertNewLog("[SELECT] "+msg);
            return msg;
        }
        Database db = new Database();
        ArrayList<Reference> refsData = db.getAllReferenceLimitOffset(limit, offset);
        if (refsData.isEmpty()){
            String msg = "{\"data\": []}";
            this.insertNewLog("[SELECT] "+msg);
            return msg;
        } else {
            String msg = "{\"data\": [";
            for (Reference ref: refsData) {
                msg += String.format("{\"id\": %s, \"anime_account_id\": %s, \"forum_account_id\": %s, \"referral_code\": \"%s\", \"point\": %s},",
                        ref.getId(),
                        ref.getAnimeAccountId(),
                        ref.getForumAccountId(),
                        ref.getReferalCode(),
                        ref.getPoint());
            }
            msg = msg.substring(0, msg.length() - 1); //to Delete last comma
            msg += "]}";
            this.insertNewLog("[SELECT] Succesfully select limited reference data");
            return msg;
        }
    }


    @WebMethod
    public String giveReferenceDataWithAnimeAccountID(Integer anime_id){
        if (!checkAPIKey()){
            String msg = "Invalid API Key";
            this.insertNewLog("[SELECT] "+msg);
            return msg;
        }
        Database db = new Database();
        Reference ref = db.getReferenceIDWithAnimeID(anime_id);
        if (ref == null){
            String msg = "{\"data\": []}";
            this.insertNewLog("[SELECT] "+msg);
            return msg;
        } else {
            String msg = String.format("{\"data\": [{\"id\": %s, \"anime_account_id\": %s, \"forum_account_id\": %s, \"referral_code\": \"%s\", \"point\": %s}]}",
                    ref.getId(),
                    ref.getAnimeAccountId(),
                    ref.getForumAccountId(),
                    ref.getReferalCode(),
                    ref.getPoint());
            this.insertNewLog("[SELECT] Successfully select data with anime_account_id: "+anime_id);
            return msg;
        }

    }

    @WebMethod
    public String giveReferenceDataWithForumAccountID(Integer forum_id){
        if (!checkAPIKey()){
            String msg = "Invalid API Key";
            this.insertNewLog("[SELECT] "+msg);
            return msg;
        }
        Database db = new Database();
        Reference ref = db.getReferenceIDWithForumID(forum_id);
        if (ref == null){
            String msg = "{\"data\": []}";
            this.insertNewLog("[SELECT] "+msg);
            return msg;
        } else {
            String msg = String.format("{\"data\": [{\"id\": %s, \"anime_account_id\": %s, \"forum_account_id\": %s, \"referral_code\": \"%s\", \"point\": %s}]}",
                    ref.getId(),
                    ref.getAnimeAccountId(),
                    ref.getForumAccountId(),
                    ref.getReferalCode(),
                    ref.getPoint());
            this.insertNewLog("[SELECT] Successfully select data with forum_account_id: " + forum_id);
            return msg;
        }
    }

    @WebMethod
    public String deleteReference(Integer anime_id){
        if (!checkAPIKey()){
            String msg = "Invalid API Key";
            this.insertNewLog("[DELETE] "+msg);
            return msg;
        }
        Database db = new Database();
        Reference ref = db.getReferenceIDWithAnimeID(anime_id);
        if (ref == null){
            String msg = "Data does not exist";
            this.insertNewLog("[DELETE] "+msg);
            return msg;
        } else {
            db.deleteReferenceDatabase(ref.getId());
            String msg = "Successfully delete reference data with anime_account_id "+anime_id;
            this.insertNewLog("[DELETE] "+msg);
            return msg;
        }
    }

}
