package com.example.jordi.raidfinder;

public class Gym {
    String gym_id;

    public Gym(){
        //constructor vacío requerido para las llamadas al Datasnapshot.getValue(Gym.class)
    }

    public Gym(String gym_id, String name, String url, float latitude, float longitude){
        this.gym_id=gym_id;
        this.name=name;
        this.url=url;
        this.latitude=latitude;
        this.longitude=longitude;
    }
    public String getGym_id() {
        return gym_id;
    }

    public void setGym_id(String gym_id) {
        this.gym_id = gym_id;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
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

    float latitude;
    float longitude;
    String name;
    String url;
}
