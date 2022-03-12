package com.lubaspc.traveltolucos.service.model;


import java.util.Calendar;

/* loaded from: classes.dex */
public abstract class PagedVOImpl implements PagedVO {
    public long getGroupHeaderId() {
        Calendar instance = Calendar.getInstance();
        instance.setTime(getFecha());
        return instance.get(1) + instance.get(2);
    }

    public String getGroupHeaderTitle() {
        return getFecha() != null ? FormatUtils.formatDateMovFactHeader(getFecha()) : "N/D";
    }
}
