package com.lubaspc.traveltolucos.service.model;

import android.text.TextUtils;

import java.io.Serializable;

/* loaded from: classes.dex */
public class Domicilio implements Serializable {
    private String calle;
    private String ciudad;
    private String colonia;
    private String cp;
    private String estado;
    private Long id;
    private int idColonia;
    private boolean last = false;
    private String municipio;
    private String numeroExterior;
    private String numeroInterior;

    public boolean isLast() {
        return this.last;
    }

    public void setLast(boolean z) {
        this.last = z;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long l) {
        this.id = l;
    }

    public String getCalle() {
        return this.calle;
    }

    public void setCalle(String str) {
        this.calle = str;
    }

    public String getNumeroExterior() {
        return this.numeroExterior;
    }

    public void setNumeroExterior(String str) {
        this.numeroExterior = str;
    }

    public String getNumeroInterior() {
        return this.numeroInterior;
    }

    public void setNumeroInterior(String str) {
        this.numeroInterior = str;
    }

    public String getCp() {
        return this.cp;
    }

    public void setCp(String str) {
        this.cp = str;
    }

    public String getEstado() {
        return this.estado;
    }

    public void setEstado(String str) {
        this.estado = str;
    }

    public String getMunicipio() {
        return this.municipio;
    }

    public void setMunicipio(String str) {
        this.municipio = str;
    }

    public String getCiudad() {
        return this.ciudad;
    }

    public void setCiudad(String str) {
        this.ciudad = str;
    }

    public int getIdColonia() {
        return this.idColonia;
    }

    public void setIdColonia(int i) {
        this.idColonia = i;
    }

    public String getColonia() {
        return this.colonia;
    }

    public void setColonia(String str) {
        this.colonia = str;
    }

    public String toString() {
        String str;
        if (isLast()) {
            return "Nuevo domicilio...";
        }
        StringBuilder sb = new StringBuilder();
        String str2 = "";
        sb.append(TextUtils.isEmpty(this.calle) ? str2 : this.calle.trim());
        if (!TextUtils.isEmpty(this.numeroExterior)) {
            sb.append(" ");
            sb.append(this.numeroExterior.trim());
        }
        if (!TextUtils.isEmpty(this.numeroInterior)) {
            sb.append(" ");
            sb.append(this.numeroInterior.trim());
        }
        if (!TextUtils.isEmpty(this.colonia) || !TextUtils.isEmpty(this.municipio)) {
            sb.append("\n");
        }
        if (!TextUtils.isEmpty(this.colonia)) {
            sb.append(this.colonia.trim());
            str = ", ";
        } else {
            str = str2;
        }
        if (!TextUtils.isEmpty(this.municipio)) {
            sb.append(str);
            sb.append(this.municipio.trim());
        }
        if (!TextUtils.isEmpty(this.cp) || !TextUtils.isEmpty(this.estado)) {
            sb.append("\n");
        }
        if (!TextUtils.isEmpty(this.cp)) {
            sb.append(this.cp.trim());
            str2 = ", ";
        }
        if (!TextUtils.isEmpty(this.estado)) {
            sb.append(str2);
            sb.append(this.estado.trim());
        }
        return sb.toString();
    }
}
