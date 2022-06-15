package com.lubaspc.traveltolucos.service.model;

/* loaded from: classes.dex */
public class Carril {
    private float costoAuto;
    private String descripcion;
    private Integer id;
    private String nombre;
    private String tipo;
    private PlazaCobro.Ubicacion ubicacion;

    public boolean isEntrada() {
        return "ENTRADA".equalsIgnoreCase(this.tipo);
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer num) {
        this.id = num;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String str) {
        this.nombre = str;
    }

    public String getDescripcion() {
        return this.descripcion;
    }

    public void setDescripcion(String str) {
        this.descripcion = str;
    }

    public float getCostoAuto() {
        return this.costoAuto;
    }

    public void setCostoAuto(float f) {
        this.costoAuto = f;
    }

    public PlazaCobro.Ubicacion getUbicacion() {
        return this.ubicacion;
    }

    public void setUbicacion(PlazaCobro.Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getTipo() {
        return this.tipo;
    }

    public void setTipo(String str) {
        this.tipo = str;
    }
}
