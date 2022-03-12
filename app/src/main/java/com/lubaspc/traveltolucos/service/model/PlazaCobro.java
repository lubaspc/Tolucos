package com.lubaspc.traveltolucos.service.model;

import java.util.List;

/* loaded from: classes.dex */
public class PlazaCobro {
    private List<Carril> carriles = null;
    private Integer id;
    private String idRef;
    private String nombre;
    private Boolean telepeaje;
    private Ubicacion ubicacion;

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

    public Ubicacion getUbicacion() {
        return this.ubicacion;
    }

    public void setUbicacion(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
    }

    public List<Carril> getCarriles() {
        return this.carriles;
    }

    public void setCarriles(List<Carril> list) {
        this.carriles = list;
    }

    public Boolean getTelepeaje() {
        return this.telepeaje;
    }

    public void setTelepeaje(Boolean bool) {
        this.telepeaje = bool;
    }

    public String getIdRef() {
        return this.idRef;
    }

    public void setIdRef(String str) {
        this.idRef = str;
    }

    /* loaded from: classes.dex */
    public class Ubicacion {
        private List<Coord> coords = null;
        private String type;

        public Ubicacion() {
        }

        public String getType() {
            return this.type;
        }

        public void setType(String str) {
            this.type = str;
        }

        public List<Coord> getCoords() {
            return this.coords;
        }

        public void setCoords(List<Coord> list) {
            this.coords = list;
        }

        /* loaded from: classes.dex */
        public class Coord {
            private Double x;
            private Double y;

            public Coord() {
            }

            public Double getX() {
                return this.x;
            }

            public void setX(Double d) {
                this.x = d;
            }

            public Double getY() {
                return this.y;
            }

            public void setY(Double d) {
                this.y = d;
            }
        }
    }
}
