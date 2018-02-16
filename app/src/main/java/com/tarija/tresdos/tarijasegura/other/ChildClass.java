package com.tarija.tresdos.tarijasegura.other;

/**
 * Created by Tresdos on 2/16/2018.
 */

public class ChildClass {
    private String nombre;
    private String token;
    private String estado;
    private String key;
    private String alerta;
    private String fecha;
    private int SDK;
    public  ChildClass(){

    }
    public  ChildClass(String token, String estado){
        this.setToken(token);
        this.setEstado(estado);
    }
    public  ChildClass(String nombre, String token, String estado, String alerta){
        this.setNombre(nombre);
        this.setToken(token);
        this.setEstado(estado);
        this.setAlerta(alerta);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getAlerta() {
        return alerta;
    }

    public void setAlerta(String alerta) {
        this.alerta = alerta;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getSDK() {
        return SDK;
    }

    public void setSDK(int SDK) {
        this.SDK = SDK;
    }
}
