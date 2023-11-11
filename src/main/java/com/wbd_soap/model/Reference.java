package com.wbd_soap.model;

public class Reference {
    private Integer anime_account_id;
    private Integer forum_account_id;
    private String referal_code;
    private Integer point;

    public Reference(Integer aad, Integer fad, String ref, Integer point){
        this.anime_account_id = aad;
        this.forum_account_id = fad;
        this.referal_code = ref;
        this.point = point;
    }

    public Integer getAnimeAccountId(){
        return this.anime_account_id;
    }

    public Integer getForumAccountId(){
        return this.forum_account_id;
    }
    public String getReferalCode(){
        return this.referal_code;
    }
    public Integer getPoint(){
        return this.point;
    }

    public void increasePoint(Integer num){
        this.point += num;
    }

    public void decreasePoint(Integer num){
        this.point -= num;
    }
}
