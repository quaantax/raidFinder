package com.raid.jordi.raidfinder;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class Raid {
    List<String> participantes;
    String fecha;
    String hora;
    String pokemon;

    public Raid(List<String> participantes, String fecha, String hora, String pokemon) {
        this.participantes = participantes;
        this.fecha = fecha;
        this.hora = hora;
        this.pokemon = pokemon;
    }

    public Raid(){
        this.participantes=new ArrayList<>();
    }

    public List<String> getParticipantes() {
        return participantes;
    }

    public void setParticipantes(List<String> participantes) {
        this.participantes = participantes;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getPokemon() {
        return pokemon;
    }

    public void setPokemon(String pokemon) {
        this.pokemon = pokemon;
    }

    public String objectToJson(){
        Gson gson=new Gson();
        return gson.toJson(this);
    }
    public Raid JsonToObject(String json){
        Gson gson=new Gson();
        return gson.fromJson(json,Raid.class);
    }


}
