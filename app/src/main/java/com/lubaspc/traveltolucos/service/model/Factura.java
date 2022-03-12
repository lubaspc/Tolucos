package com.lubaspc.traveltolucos.service.model;


import java.math.BigDecimal;
import java.util.Date;

/* loaded from: classes.dex */
public class Factura extends PagedVOImpl {
    private Date fecha;
    private BigDecimal iva;
    private String pdf;
    private String periodo;
    private String razonSocial;
    private String rfc;
    private Long rowId;
    private BigDecimal total;
    private String xml;

    public String toString() {
        return String.format(FormatUtils.locale, "%s, %tF, total=$%.2f, iva=$%.2f", this.rfc, this.fecha, this.total, this.iva);
    }

    public String getMontoFormat() {
        return FormatUtils.formatPesos(getTotal());
    }

    public String getFechaFormat() {
        return getFecha() != null ? FormatUtils.formatDateMovFact(getFecha()) : "N/D";
    }

    public Long getRowId() {
        return this.rowId;
    }

    public void setRowId(Long l) {
        this.rowId = l;
    }

    @Override // com.idmexico.tutaglib.domain.PagedVO
    public Date getFecha() {
        return this.fecha;
    }

    public void setFecha(Date date) {
        this.fecha = date;
    }

    public String getRfc() {
        return this.rfc;
    }

    public void setRfc(String str) {
        this.rfc = str;
    }

    public String getRazonSocial() {
        return this.razonSocial;
    }

    public void setRazonSocial(String str) {
        this.razonSocial = str;
    }

    public String getPeriodo() {
        return this.periodo;
    }

    public void setPeriodo(String str) {
        this.periodo = str;
    }

    public BigDecimal getTotal() {
        return this.total;
    }

    public void setTotal(BigDecimal bigDecimal) {
        this.total = bigDecimal;
    }

    public BigDecimal getIva() {
        return this.iva;
    }

    public void setIva(BigDecimal bigDecimal) {
        this.iva = bigDecimal;
    }

    public String getPdf() {
        return this.pdf;
    }

    public void setPdf(String str) {
        this.pdf = str;
    }

    public String getXml() {
        return this.xml;
    }

    public void setXml(String str) {
        this.xml = str;
    }
}
