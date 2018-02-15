package com.tarija.tresdos.tarijasegura.other;

/**
 * Created by Tresdos on 2/15/2018.
 */

public class PersonClass {
    private String name;
    private String lastname;
    private String email;
    private String ci;
    private String family;

    public PersonClass(){}
    public PersonClass(String name, String lastname, String email, String ci, String family){
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.ci = ci;
        this.family = family;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCi() {
        return ci;
    }

    public void setCi(String ci) {
        this.ci = ci;
    }

    public String getfamily() {
        return family;
    }

    public void setfamily(String family) {
        this.family = family;
    }
}
