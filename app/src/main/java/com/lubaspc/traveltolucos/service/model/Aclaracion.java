package com.lubaspc.traveltolucos.service.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/* loaded from: classes.dex */
public class Aclaracion extends PagedVOImpl implements Serializable {
    private String comentario;
    private String corredor;
    private BigDecimal devolucion;
    private String dictamen;
    private Date fechaAclaracion;
    private Date fechaBonifiacion;
    private Date fechaCruce;
    private Date fechaResolucion;
    private String folio;
    private String idCarril;
    private String idCaseta;
    private Motivo motivo;
    private String status;

    public String getCorredor() {
        return this.corredor;
    }

    public void setCorredor(String str) {
        this.corredor = str;
    }

    public String getIdCaseta() {
        return this.idCaseta;
    }

    public void setIdCaseta(String str) {
        this.idCaseta = str;
    }

    public String getIdCarril() {
        return this.idCarril;
    }

    public void setIdCarril(String str) {
        this.idCarril = str;
    }

    public Date getFechaCruce() {
        return this.fechaCruce;
    }

    public void setFechaCruce(Date date) {
        this.fechaCruce = date;
    }

    public String getFolio() {
        return this.folio;
    }

    public void setFolio(String str) {
        this.folio = str;
    }

    public String getStatus() {
        return this.status;
    }

    public String getStatusifNull() {
        String str = this.status;
        return str != null ? str : "PENDIENTE";
    }

    public void setStatus(String str) {
        this.status = str;
    }

    public String getDictamen() {
        String str = this.dictamen;
        return str != null ? str : "N/D";
    }

    public void setDictamen(String str) {
        this.dictamen = str;
    }

    public BigDecimal getDevolucion() {
        return this.devolucion;
    }

    public void setDevolucion(BigDecimal bigDecimal) {
        this.devolucion = bigDecimal;
    }

    public String getDevolucionFormat() {
        return FormatUtils.formatPesos(getDevolucion());
    }

    public Date getFechaAclaracion() {
        return this.fechaAclaracion;
    }

    public String getFechaAltaFormat() {
        return getFechaAclaracion() != null ? FormatUtils.formatDateExtend(getFechaAclaracion()) : "N/D";
    }

    public void setFechaAclaracion(Date date) {
        this.fechaAclaracion = date;
    }

    public Date getFechaResolucion() {
        return this.fechaResolucion;
    }

    public String getFechaResolucionFormat() {
        return getFechaResolucion() != null ? FormatUtils.formatDateExtend(getFechaResolucion()) : "N/D";
    }

    public void setFechaResolucion(Date date) {
        this.fechaResolucion = date;
    }

    public Date getFechaBonificacion() {
        return this.fechaBonifiacion;
    }

    public String getFechaBonificacionFormat() {
        return getFechaBonificacion() != null ? FormatUtils.formatDateExtend(getFechaBonificacion()) : "N/D";
    }

    public void setFechaBonificacion(Date date) {
        this.fechaBonifiacion = date;
    }

    public String getComentario() {
        String str = this.comentario;
        return str != null ? str : "N/D";
    }

    public Motivo getMotivo() {
        return this.motivo;
    }

    public void setMotivo(Motivo motivo) {
        this.motivo = motivo;
    }

    public void setComentario(String str) {
        this.comentario = str;
    }

    /* loaded from: classes.dex */
    public class Motivo {
        private String descripcion;
        private int id;

        public Motivo() {
        }

        public int getId() {
            return this.id;
        }

        public void setId(int i) {
            this.id = i;
        }

        public String getDescripcion() {
            return this.descripcion;
        }

        public void setDescripcion(String str) {
            this.descripcion = str;
        }
    }

    @Override // com.idmexico.tutaglib.domain.PagedVO
    public Date getFecha() {
        return getFechaAclaracion();
    }
}
