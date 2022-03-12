package com.lubaspc.traveltolucos.service.model;
/* loaded from: classes.dex */
public class Estado {
    private String abreviatura;
    private int id;
    private String nombre;

    public Estado() {
    }

    public Estado(String str) {
        setNombre(str);
    }

    public int getId() {
        return this.id;
    }

    public void setId(int i) {
        this.id = i;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String str) {
        this.nombre = str;
    }

    public String getAbreviatura() {
        return this.abreviatura;
    }

    public void setAbreviatura(String str) {
        this.abreviatura = str;
    }
}
