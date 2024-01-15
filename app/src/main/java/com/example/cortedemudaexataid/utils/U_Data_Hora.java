package com.example.cortedemudaexataid.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.provider.Settings;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

@SuppressLint("SimpleDateFormat")
public class U_Data_Hora {
    private static final String CATEGORIA = "U_Data_Hora";

    public static final String DDMM = "dd/MM";
    public static final String DDMMYY = "dd/MM/yy";
    public static final String DDMMYYYY = "dd/MM/yyyy";
    public static final String MM = "MM";
    public static final String YYYY = "yyyy";
    public static final String DD = "dd";
    public static final String HH = "HH";
    public static final String MI = "mm";
    public static final String HHMM = "HH:mm";
    public static final String HHMMSS = "HH:mm:ss";
    public static final String DDMMYYYY_HHMM = "dd/MM/yyyy HH:mm";
    public static final String DDMMYYYY_HHMMSS = "dd/MM/yyyy HH:mm:ss";
    public static final String DD_MM_YYYY = "dd_MM_yyyy";
    public static final String HH_MM_SS = "HH_mm_ss";
    public static final String SS = "ss";
    public static final String YYYYMMDD = "yyyyMMdd";
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String YYYY_MM_DD_HH_MM_SS_SSS = "yyyy-MM-dd HH:mm:ss.sss";


    public static String retornaHora(String formato){
        Date agora = new Date();
        SimpleDateFormat formata = new SimpleDateFormat(formato);

        return formata.format(agora);
    }

    public static String retornaHora(long dataMilissegundos, String formato){
        String hora = "";
        Date agora = new Date(dataMilissegundos);
        SimpleDateFormat formata = new SimpleDateFormat(formato);

        if (formato.equals(HH)) {
            hora = formata.format(agora) + ":00:00";
        }
        if (formato.equals(HHMM)) {
            hora = formata.format(agora) + ":00";
        }
        if (formato.equals(HHMMSS)) {
            hora = formata.format(agora);
        }

        return hora;
    }

    public static Date retornaData() {
        Calendar calendario = Calendar.getInstance();
        return calendario.getTime();
    }

    public static Date retornaData(int dias) {
        Calendar calendario = Calendar.getInstance();
        calendario.add(Calendar.DATE, dias);
        return calendario.getTime();
    }

    public static String retornaData(String formato){
        Date agora = new Date();
        SimpleDateFormat formata = new SimpleDateFormat(formato);

        return formata.format(agora);
    }

    public static String retornaData(int dias, String formato){
        String Data;
        Calendar calendario = Calendar.getInstance();
        calendario.add(Calendar.DATE, dias);
        SimpleDateFormat formata = new SimpleDateFormat(formato);
        Data = formata.format(calendario.getTime());
        return Data;
    }


//    public static Date retornaDataSemHora(int dias, String formato){
//        String Data;
//        Calendar calendario = Calendar.getInstance();
//        calendario.add(Calendar.DATE, dias);
//        SimpleDateFormat formata = new SimpleDateFormat(formato);
//        Data = formata.format(calendario.getTime());
//        return Data;
//    }

    public static Long retornaDataLong (int dias){

        return retornaData(dias).getTime();
    }

    public static long diferenciarTempo(String dataInicio, String formatoInicio, String dataFim, String formatoFim, String formatoDiferenca) {
        try {
            SimpleDateFormat sdf1 = new SimpleDateFormat(formatoInicio, Locale.getDefault());
            SimpleDateFormat sdf2 = new SimpleDateFormat(formatoFim, Locale.getDefault());

            Date date1 = sdf1.parse(dataInicio);
            Date date2 = sdf2.parse(dataFim);

            long differenceMilliSeconds = date2.getTime() - date1.getTime();

            if (formatoDiferenca.equals(U_Data_Hora.MI)) {
                return differenceMilliSeconds/1000/60;
            } else if (formatoDiferenca.equals(U_Data_Hora.HH)) {
                return differenceMilliSeconds/1000/60/60;
            } else if (formatoDiferenca.equals(U_Data_Hora.DD)) {
                return differenceMilliSeconds/1000/60/60/24;
            }

        } catch (ParseException e) {
            Log.e(CATEGORIA + " - DifTempo", e.toString());
            e.printStackTrace();
        }
        return Long.parseLong("0");
    }

    public static String formatarData(String data, String formatoEntrada, String formatoSaida) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(formatoEntrada, Locale.getDefault());
            Date novaData;

            novaData = sdf.parse(data);

            SimpleDateFormat formata = new SimpleDateFormat(formatoSaida);

            return formata.format(novaData);
        } catch (Exception e) {
            Log.e(CATEGORIA, "formatarData: " + e.toString());
            return "";
        }
    }
    public static String formatarData(Date data, String formatoSaida) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(formatoSaida, Locale.getDefault());
            return sdf.format(data);
        } catch (Exception e) {
            Log.e(CATEGORIA, "formatarData: " + e.toString());
            return "";
        }
    }

    public static String formatarDataPadraoAmericano(Date data, String formatoSaida) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            return sdf.format(data);
        } catch (Exception e) {
            Log.e(CATEGORIA, "formatarData: " + e.toString());
            return "";
        }
    }

    public static String formatarData(long data, String formato) {
        String retorno = "";

        try {
            SimpleDateFormat sdf1 = new SimpleDateFormat(formato);

            if (!formato.equals("")) {
                retorno = sdf1.format(new Date(data));
            }
        } catch (Exception e) {
            retorno = "";
            Log.e(CATEGORIA + " - formData", e.toString());
        }

        return retorno;
    }

    public static Date formatarData(String data, String formatoEntrada) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(formatoEntrada, Locale.getDefault());

            return sdf.parse(data);
        } catch (Exception e) {
            Log.e(CATEGORIA, "formatarData: " + e.toString());
            return null;
        }
    }
    public static Long retornaDataMeiaNoite(){
        long data;
        LocalDateTime hoje = LocalDateTime.now();
        LocalDateTime inicio = hoje.with(LocalTime.MIDNIGHT);
        ZonedDateTime zdt = ZonedDateTime.of(inicio, ZoneId.systemDefault());
        data = zdt.toInstant().toEpochMilli();
        return data;
    }
    public static Long retornaDataAgora(){
        long data;
        LocalDateTime hoje = LocalDateTime.now();
        LocalDateTime inicio = hoje.with(LocalTime.now());
        ZonedDateTime zdt = ZonedDateTime.of(inicio, ZoneId.systemDefault());
        data = zdt.toInstant().toEpochMilli();
        return data;
    }

    public static String retornaDataDiaSeguinteMeiaNoite(String data){

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date d = null;
        try {
            d = sdf.parse(data);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(d);
        gc.add(Calendar.HOUR, 24);
        String daquiHaCincoAnos = sdf.format(gc.getTime());
        return daquiHaCincoAnos;
    }

    public static boolean validarDataHoraAuto(Context ctx) {
        if (!PERSONALIZADO.DATA_FORCAR_AUTO) {
            return true;
        }
        boolean ret = Settings.Global.getInt(ctx.getContentResolver(), Settings.Global.AUTO_TIME, 1) == 1;
        return ret;
    }
    public static class PERSONALIZADO{
        public static boolean DATA_FORCAR_AUTO=true;

        private PERSONALIZADO(){}
    }









}
