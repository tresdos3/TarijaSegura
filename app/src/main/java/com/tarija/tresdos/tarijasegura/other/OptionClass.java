package com.tarija.tresdos.tarijasegura.other;

/**
 * Created by Tresdos on 2/26/2018.
 */

public class OptionClass {
    private int icon;
    private String name;
    private String description;

    public OptionClass(){

    }

    public OptionClass(int icon, String name, String description) {
        this.setIcon(icon);
        this.setName(name);
        this.setDescription(description);
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
