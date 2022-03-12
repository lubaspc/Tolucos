package com.lubaspc.traveltolucos.service.model;

import java.math.BigDecimal;

/* loaded from: classes.dex */
public class Tag {
    public static final String BRAND_CARRITO = "IAVE";
    public static final String BRAND_PASE = "PASE";
    public static final String STATUS_ACTIVO = "Activo";
    public static final String STATUS_INACTIVO = "Inactivo";
    private Boolean activo;
    private Movimiento.CrucesFilter crucesFilter;
    private int digitoVerificador;
    private String fechaActivacionTDU;
    private Long id;
    private int index;
    private String marca;
    private String marcaTdc;
    private String nombre;
    private int numero;
    private Long orden;
    private boolean other;
    private String prefijo;
    private Boolean promocionTDU;
    private BigDecimal saldo;
    private String terminacionTdc;
    private Boolean tieneAdeudos;
    private TipoCobro tipoCobro;
    private Boolean virgen;

    /* loaded from: classes.dex */
    public enum TipoCobro {
        PREPAGO_CORPORATIVO("Prepago"),
        PREPAGO_EFECTIVO("Prepago"),
        PREPAGO_DOMICILIADO("Prepago"),
        CORPORATIVO("Corporativo"),
        POSPAGO("Pospago");
        
        private final String alias;

        TipoCobro(String str) {
            this.alias = str;
        }

        public String getAlias() {
            return this.alias;
        }
    }

    public Tag(String str, int i, String str2, TipoCobro tipoCobro) {
        this.saldo = null;
        this.promocionTDU = false;
        this.other = false;
        this.tieneAdeudos = false;
        this.prefijo = str;
        this.numero = i;
        this.nombre = str2;
        this.tipoCobro = tipoCobro;
    }

    public Tag(String str, int i) {
        this(str, i, null, null);
    }

    public Tag() {
        this(null, 0, null, null);
    }

    public String toString() {
        return this.other ? "Otro tag..." : getNumeroFormat();
    }

    public String getNumeroFormat() {
        return getPrefijo() + " " + String.format("%08d", Integer.valueOf(getNumero())) + "-" + this.digitoVerificador;
    }

    public String getNumeroSimpleFormat() {
        return getPrefijo() + " " + String.format("%08d", Integer.valueOf(getNumero()));
    }

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int i) {
        this.index = i;
    }

    public int getDigitoVerificador() {
        return this.digitoVerificador;
    }

    public void setDigitoVerificador(int i) {
        this.digitoVerificador = i;
    }

    public Long getOrden() {
        return this.orden;
    }

    public void setOrden(Long l) {
        this.orden = l;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long l) {
        this.id = l;
    }

    public int getNumero() {
        return this.numero;
    }

    public void setNumero(int i) {
        this.numero = i;
    }

    public int getFullNumero() {
        return Integer.valueOf("" + getPrefijoCode() + getNumero()).intValue();
    }

    public int hashCode() {
        return (getPrefijo() + getNumero()).hashCode();
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Tag)) {
            return false;
        }
        Tag tag = (Tag) obj;
        return tag.getNumero() == getNumero() && tag.getPrefijo().equals(getPrefijo());
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String str) {
        this.nombre = str;
    }

    public BigDecimal getSaldo() {
        return this.saldo;
    }

    public String getSaldoFormat() {
        return FormatUtils.formatPesos(getSaldo());
    }

    public void setSaldo(BigDecimal bigDecimal) {
        this.saldo = bigDecimal;
    }

    public TipoCobro getTipoCobro() {
        return this.tipoCobro;
    }

    public void setTipoCobro(TipoCobro tipoCobro) {
        this.tipoCobro = tipoCobro;
    }

    public Boolean isActivo() {
        return this.activo;
    }

    public void setActivo(Boolean bool) {
        this.activo = bool;
    }

    public boolean isVirgen() {
        Boolean bool = this.virgen;
        if (bool == null) {
            return false;
        }
        return bool.booleanValue();
    }

    public void setVirgen(Boolean bool) {
        this.virgen = bool;
    }

    public String getMarca() {
        return this.marca;
    }

    public void setMarca(String str) {
        this.marca = str;
    }

    public String getPrefijo() {
        return this.prefijo;
    }

    public void setPrefijo(String str) {
        this.prefijo = str;
    }

    public int getPrefijoCode() {
        String prefijo = getPrefijo();
        if (prefijo != null) {
            if (prefijo.equals("CPFI")) {
                return 1;
            }
            if (prefijo.equals("IMDM")) {
                return 2;
            }
            if (prefijo.equals("005")) {
                return 3;
            }
            if (prefijo.equals("006")) {
                return 4;
            }
        }
        return -1;
    }

    public void setCrucesFilter(Movimiento.CrucesFilter crucesFilter) {
        this.crucesFilter = crucesFilter;
    }

    public Movimiento.CrucesFilter getCrucesFilter() {
        return this.crucesFilter;
    }

    public boolean isOther() {
        return this.other;
    }

    public void setOther(boolean z) {
        this.other = z;
    }

    public boolean isPrepago() {
        return getTipoCobro() != null && (getTipoCobro() == TipoCobro.PREPAGO_DOMICILIADO || getTipoCobro() == TipoCobro.PREPAGO_EFECTIVO || getTipoCobro() == TipoCobro.PREPAGO_CORPORATIVO);
    }

    public String getMarcaTdc() {
        return this.marcaTdc;
    }

    public void setMarcaTdc(String str) {
        this.marcaTdc = str;
    }

    public String getTerminacionTdc() {
        return this.terminacionTdc;
    }

    public void setTerminacionTdc(String str) {
        this.terminacionTdc = str;
    }

    public Boolean getTieneAdeudos() {
        return this.tieneAdeudos;
    }

    public void setTieneAdeudos(Boolean bool) {
        this.tieneAdeudos = bool;
    }

    public Boolean isPromocionTDU() {
        return this.promocionTDU;
    }

    public void setPromocionTDU(Boolean bool) {
        this.promocionTDU = bool;
    }

    public String getFechaActivacionTDU() {
        return this.fechaActivacionTDU;
    }

    public void setFechaActivacionTDU(String str) {
        this.fechaActivacionTDU = str;
    }
}
