package com.wbd_soap.model;

import java.util.Date;

public class Logging {
    private Integer id;
    private String description;
    private String ip;
    private String endpoint;
    private Date requested_at;

    public Logging (Integer id, String desc, String ip, String ep, Date req){
        this.id = id;
        this.description = desc;
        this.ip = ip;
        this.endpoint = ep;
        this.requested_at = req;
    }
    public Integer getId() { return this.id;}

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
