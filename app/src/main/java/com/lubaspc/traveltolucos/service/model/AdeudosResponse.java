package com.lubaspc.traveltolucos.service.model;


import java.util.List;

/* loaded from: classes.dex */
public class AdeudosResponse {
    private List<CruceAdeudo> crucesPendientes;
    private boolean puedeReintentar;

    public boolean isPuedeReintentar() {
        return this.puedeReintentar;
    }

    public void setPuedeReintentar(boolean z) {
        this.puedeReintentar = z;
    }

    public List<CruceAdeudo> getCrucesPendientes() {
        return this.crucesPendientes;
    }

    public void setCrucesPendientes(List<CruceAdeudo> list) {
        this.crucesPendientes = list;
    }
}
