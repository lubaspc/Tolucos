package com.lubaspc.traveltolucos.service.model;

import java.math.BigDecimal;
import java.util.Date;

/* loaded from: classes.dex */
public class CruceAdeudo {
    private Long consecar;
    private Date fechaHora;
    private String idCarril;
    private Long idCaseta;
    private String mensaje;
    private BigDecimal monto;
    private String nombreCarril;
    private String nombreCaseta;
    private boolean reintentar;

    public String getIdCarril() {
        return this.idCarril;
    }

    public void setIdCarril(String str) {
        this.idCarril = str;
    }

    public Long getIdCaseta() {
        return this.idCaseta;
    }

    public void setIdCaseta(Long l) {
        this.idCaseta = l;
    }

    public String getNombreCarril() {
        return this.nombreCarril;
    }

    public void setNombreCarril(String str) {
        this.nombreCarril = str;
    }

    public String getNombreCaseta() {
        return this.nombreCaseta;
    }

    public void setNombreCaseta(String str) {
        this.nombreCaseta = str;
    }

    public Long getConsecar() {
        return this.consecar;
    }

    public void setConsecar(Long l) {
        this.consecar = l;
    }

    public boolean isReintentar() {
        return this.reintentar;
    }

    public void setReintentar(boolean z) {
        this.reintentar = z;
    }

    public String getMensaje() {
        return this.mensaje;
    }

    public void setMensaje(String str) {
        this.mensaje = str;
    }

    public Date getFechaHora() {
        return this.fechaHora;
    }

    public void setFechaHora(Date date) {
        this.fechaHora = date;
    }

    public BigDecimal getMonto() {
        return this.monto;
    }

    public void setMonto(BigDecimal bigDecimal) {
        this.monto = bigDecimal;
    }

    public String getFechaHoraFormat() {
        return getFechaHora() != null ? FormatUtils.formatDateAdeudo(getFechaHora()) : "N/D";
    }

    public String getMontoFormat() {
        return FormatUtils.formatPesos(getMonto());
    }
}
