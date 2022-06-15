package com.lubaspc.traveltolucos.service.model;

import android.os.Build;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/* loaded from: classes.dex */
public class FormatUtils {
    public static final Locale locale = new Locale("es", "MX");

    public static final String formatDateUTF8(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", locale).format(date);
    }

    public static final String formatDateLong(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", locale).format(date).replaceAll("\\.", "");
    }

    public static final String formatDateExtend(Date date) {
        return new SimpleDateFormat("dd/MMM/yyyy HH:mm", locale).format(date).replaceAll("\\.", "");
    }

    public static final String formatDateMovFact(Date date) {
        return new SimpleDateFormat("EEE d '@' hh:mm a", locale).format(date).replaceAll("\\.", "");
    }

    public static final String formatDateMovFactHeader(Date date) {
        return new SimpleDateFormat("MMMM yyyy", locale).format(date).replaceAll("\\.", "").toUpperCase();
    }

    public static final String formatDateAdeudo(Date date) {
        return new SimpleDateFormat("yyyy MMM d '@' hh:mm a", locale).format(date).replaceAll("\\.", "");
    }

    public static final String formatPesos(BigDecimal bigDecimal) {
        NumberFormat numberFormat;
        if (bigDecimal == null) {
            return "";
        }
        if (Build.VERSION.SDK_INT < 21) {
            DecimalFormatSymbols instance = DecimalFormatSymbols.getInstance(locale);
            instance.setDecimalSeparator('.');
            instance.setGroupingSeparator(',');
            instance.setMonetaryDecimalSeparator('.');
            numberFormat = new DecimalFormat("¤#,##0.00", instance);
        } else {
            numberFormat = DecimalFormat.getCurrencyInstance();
        }
        numberFormat.setRoundingMode(RoundingMode.HALF_EVEN);
        return numberFormat.format(bigDecimal);
    }

    public static final String formatTiempoEnSegundos(int i) {
        return String.format(locale, "%02d:%02d", Long.valueOf((i / 3600) % 24), Long.valueOf((i / 60) % 60));
    }

    public static final String formatTiempoEnMinutos(int i) {
        return String.format(locale, "%02d:%02d", Integer.valueOf(i < 60 ? 0 : Math.abs(i / 60)), Integer.valueOf(i < 60 ? i : i % 60));
    }

    public static final String formatDistanciaKms(long j) {
        return formatDistanciaKms(j, false);
    }

    public static final String formatDistanciaKms(long j, boolean z) {
        String str;
        NumberFormat numberInstance = NumberFormat.getNumberInstance();
        numberInstance.setMaximumFractionDigits(2);
        if (j < 1000) {
            str = " m";
        } else {
            j /= 1000;
            str = " km";
        }
        if (!z) {
            return numberInstance.format(j);
        }
        return numberInstance.format(j) + str;
    }
}
