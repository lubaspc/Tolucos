package com.lubaspc.traveltolucos.service.model;
/* loaded from: classes.dex */
public class Poblado {
    private Long codigo;
    private Estado estado;
    private Long id;
    private String nombre;

    public Poblado() {
    }

    public Poblado(Long l, String str) {
        this.id = l;
        this.nombre = str;
    }

    public String toString() {
        return this.id + "," + this.nombre + ", " + this.estado.getNombre();
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long l) {
        this.id = l;
    }

    public Long getCodigo() {
        return this.codigo;
    }

    public void setCodigo(Long l) {
        this.codigo = l;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String str) {
        this.nombre = str;
    }

    public Estado getEstado() {
        return this.estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }
}
