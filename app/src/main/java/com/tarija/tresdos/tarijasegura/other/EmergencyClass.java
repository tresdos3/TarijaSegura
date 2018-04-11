package com.tarija.tresdos.tarijasegura.other;

public class EmergencyClass {
    private int icon;
    private String name;
    private String number;

    public EmergencyClass() {

    }

    public EmergencyClass(int icon ,String name, String number) {
        this.icon = icon;
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
