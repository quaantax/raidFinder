package com.example.jordi.raidfinder;

public class User {

    private String email;
    private String nombre;
    private int equipo;
    private int nivel;

    public User() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getEquipo() {
        return equipo;
    }

    public void setEquipo(int equipo) {
        this.equipo = equipo;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public User(String email, String nombre, int equipo, int nivel) {
        this.email = email;
        this.nombre = nombre;
        this.equipo = equipo;
        this.nivel = nivel;
    }

}
