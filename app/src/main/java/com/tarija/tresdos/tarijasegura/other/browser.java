package com.tarija.tresdos.tarijasegura.other;

/**
 * Created by tresdos on 24-07-17.
 */

public class browser {
    private String Titulo;
    private String Url;
    private Integer Visitas;
    private long Creado;
    private String Fecha_Hora;
    private String key;

    public browser(){

    }
    public browser(String key, String titulo, String url, long creado){
        this.key = key;
        this.Titulo = titulo;
        this.Url = url;
        this.Creado = creado;
    }
    public browser(String titulo, String url, int visitas, long creado, String fecha_Hora){
        this.Titulo = titulo;
        this.Url = url;
        this.Visitas = visitas;
        this.Creado = creado;
        this.Fecha_Hora = fecha_Hora;
    }

    public String getTitulo() {
        return Titulo;
    }

    public void setTitulo(String titulo) {
        Titulo = titulo;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public Integer getVisitas() {
        return Visitas;
    }

    public void setVisitas(int visitas) {
        Visitas = visitas;
    }

    public long getCreado() {
        return Creado;
    }

    public void setCreado(long creado) {
        Creado = creado;
    }

    public String getFecha_Hora() {
        return Fecha_Hora;
    }

    public void setFecha_Hora(String fecha_Hora) {
        Fecha_Hora = fecha_Hora;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
