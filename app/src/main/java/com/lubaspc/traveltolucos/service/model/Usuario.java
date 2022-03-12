package com.lubaspc.traveltolucos.service.model;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/* loaded from: classes.dex */
public class Usuario implements Serializable {
    private String apellidoMaterno;
    private String apellidoPaterno;
    private Domicilio domicilio;
    private String email;
    private long id;
    private String nombre;
    private String password;
    private List<Tag> tags;
    private String telefono;
    private Date telefonoTs;

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.id);
        sb.append(":");
        sb.append(this.email);
        sb.append(", tags:");
        List<Tag> list = this.tags;
        sb.append(list == null ? 0 : list.size());
        return sb.toString();
    }

    public long getId() {
        return this.id;
    }

    public void setId(long j) {
        this.id = j;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String str) {
        this.email = str;
    }

    public List<Tag> getTags() {
        return this.tags;
    }

    public void setTags(List<Tag> list) {
        this.tags = list;
    }

    public String getTelefono() {
        return this.telefono;
    }

    public void setTelefono(String str) {
        this.telefono = str;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String str) {
        this.password = str;
    }

    public String getNombreCompleto() {
        String str;
        String str2 = "";
        String nombre = !TextUtils.isEmpty(getNombre()) ? getNombre() : str2;
        StringBuilder sb = new StringBuilder();
        sb.append(nombre);
        if (!TextUtils.isEmpty(getApellidoPaterno())) {
            str = " " + getApellidoPaterno();
        } else {
            str = str2;
        }
        sb.append(str);
        String sb2 = sb.toString();
        StringBuilder sb3 = new StringBuilder();
        sb3.append(sb2);
        if (!TextUtils.isEmpty(getApellidoMaterno())) {
            str2 = " " + getApellidoMaterno();
        }
        sb3.append(str2);
        return sb3.toString();
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String str) {
        this.nombre = str;
    }

    public String getApellidoPaterno() {
        return this.apellidoPaterno;
    }

    public void setApellidoPaterno(String str) {
        this.apellidoPaterno = str;
    }

    public String getApellidoMaterno() {
        return this.apellidoMaterno;
    }

    public void setApellidoMaterno(String str) {
        this.apellidoMaterno = str;
    }

    public Domicilio getDomicilio() {
        return this.domicilio;
    }

    public void setDomicilio(Domicilio domicilio) {
        this.domicilio = domicilio;
    }

    public Date getTelefonoTs() {
        return this.telefonoTs;
    }

    public void setTelefonoTs(Date date) {
        this.telefonoTs = date;
    }
}
