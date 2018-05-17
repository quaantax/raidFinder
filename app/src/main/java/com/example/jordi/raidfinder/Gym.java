package com.example.jordi.raidfinder;

import com.google.gson.Gson;

public class Gym {
    String gym_id;
    String latitude;
    String longitude;
    String name;
    String url;
    Raid raid;

    public Raid getRaid() {
        return raid;
    }

    public void setRaid(Raid raid) {
        this.raid = raid;
    }

    public Gym(){
        //constructor vacío requerido para las llamadas al Datasnapshot.getValue(Gym.class)
    }

    public Gym(String gym_id, String name, String url, String latitude, String longitude,Raid raid){
        this.gym_id=gym_id;
        this.name=name;
        this.url=url;
        this.latitude=latitude;
        this.longitude=longitude;
        this.raid=raid;
    }
    public String getGym_id() {
        return gym_id;
    }

    public void setGym_id(String gym_id) {
        this.gym_id = gym_id;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String objectToJson(){
        Gson gson=new Gson();
        return gson.toJson(this);
    }
    public Gym JsonToObject(String json){
        Gson gson=new Gson();
        return gson.fromJson(json,Gym.class);
    }


}
