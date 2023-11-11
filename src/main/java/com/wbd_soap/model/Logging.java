package com.wbd_soap.model;

import java.util.Date;

public class Logging {
    private String description;
    private String ip;
    private String endpoint;
    private Date requested_at;

    public Logging (String desc, String ip, String ep, Date req){
        this.description = desc;
        this.ip = ip;
        this.endpoint = ep;
        this.requested_at = req;
    }

    public String getDescription(){
        return this.description;
    }

    public String getIp(){
        return this.ip;
    }

    public String getEndpoint(){
        return this.endpoint;
    }

    public Date getRequestedAt(){
        return this.requested_at;
    }

}
