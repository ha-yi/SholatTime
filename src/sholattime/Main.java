/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sholattime;

import java.util.Calendar;

/**
 *
 * @author AlirCute
 */
public class Main {

    public static void main(String[] args) {
        int Tgl,Bln,Th;
        String bulan[]={"Januari","Februari","Maret","April","Mei","Juni","Juli","Agustus","September","Oktober","November","Desember"};
        PrayerTime pt=new PrayerTime();
        Tgl=Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        Bln=Calendar.getInstance().get(Calendar.MONTH);
        Th=Calendar.getInstance().get(Calendar.YEAR);

        pt.setDay(Tgl);
        pt.setMonth(Bln);
        pt.setYear(Th);

        pt.setData( 110.3523056,7.786333333, 7, Tgl, Bln, Th, PrayerTime.Calender.MuslimWorldLeague, PrayerTime.Mazhab.Default, PrayerTime.Season.Winter);

        pt.getCalculation();

        System.out.println("Jadwal Sholat Tanggal " + Tgl + " " + bulan[Bln] + " " + Th);
        System.out.println("Subuh " + pt.getWaktuSholat("Fajar"));
        System.out.println("Shuroq " + pt.getWaktuSholat("Shuroq"));
        System.out.println("Dzuhur " + pt.getWaktuSholat("Dzuhur"));
        System.out.println("Ashar " + pt.getWaktuSholat("Ashar"));
        System.out.println("Maghrib " + pt.getWaktuSholat("Maghrib"));
        System.out.println("Isya " + pt.getWaktuSholat("Isya"));

    }

}
