/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sholattime;

/**
 *
 * @author AlirCute
 */
public class PrayerTime {
    enum Calender {UmmAlQuraUniv,EgytionGeneralAuthorityofSurvey,UnivOfIslamicScincesKarachi,IslamicSocietyOfNorthAmerica,MuslimWorldLeague};
    enum Mazhab {Default,Hanafi};
    enum Season {Winter,Summer};
    enum Unit{Nautical, Kilometers, Statute};

    private String Sholat [][]=new String[7][2];
    private int m_year,m_month,m_day;
    final double DegToRad=0.017453292,RadToDeg=57.29577951,KIBLA_LAT=21.423333,KIBLA_LON=39.823333;
    private double dec,m_longitude,m_zone,m_latitude;
    Calender m_calender;
    Mazhab m_mazhab;
    Season m_season;

    PrayerTime(){
        Sholat[0][0]="Fajar";
        Sholat[1][0]="Shuroq";
        Sholat[2][0]="Dzuhur";
        Sholat[3][0]="Ashar";
        Sholat[4][0]="Maghrib";
        Sholat[5][0]="Isya";
    }

    double equation(double alt){
	return RadToDeg*Math.acos((Math.sin(alt*DegToRad)-Math.sin(dec*DegToRad)*
                Math.sin(m_latitude*DegToRad))/(Math.cos(dec*DegToRad)*Math.cos(m_latitude*DegToRad)));
    }
    public void getCalculation(){
        double julianDay=(367*m_year)-(int)(((m_year+(int)((m_month+9)/12))*7)/4)+(int)(275*m_month/9)+m_day-730531.5;
        double sunLength=removeDublication(280.461+0.9856474*julianDay);
        double middleSun=removeDublication(357.528+0.9856003*julianDay);
        double lambda=removeDublication(sunLength+1.915*Math.sin(middleSun*DegToRad)+0.02*Math.sin(2*middleSun*DegToRad));
        double obliquity=23.439-0.0000004*julianDay;
	double alpha=RadToDeg*Math.atan(Math.cos(obliquity*DegToRad)*Math.tan(lambda*DegToRad));

        if( lambda > 90 && lambda < 180 ) alpha+=180;
	else if( lambda > 180 && lambda < 360 ) alpha+=360;

        double ST=removeDublication(100.46+0.985647352*julianDay);
        dec=RadToDeg*Math.asin(Math.sin(obliquity*DegToRad)*Math.sin(lambda*DegToRad));
        double noon=alpha-ST;

	if(noon<0) noon+=360;
	double UTNoon=noon-m_longitude;

	double localNoon=(UTNoon/15)+m_zone;

        double zuhr=localNoon; //Zuhr Time.

	double maghrib=localNoon+equation(-0.8333)/15;  // Maghrib Time
	double shrouk=localNoon-equation(-0.8333)/15;   // Shrouk Time

        double fajrAlt=0;
	double ishaAlt=0;

	if( m_calender ==Calender.UmmAlQuraUniv ) fajrAlt=-19;
	else if ( m_calender == Calender.EgytionGeneralAuthorityofSurvey ){
            fajrAlt=-19.5;
            ishaAlt=-17.5;
	}else if ( m_calender == Calender.MuslimWorldLeague ){
            fajrAlt=-18;
            ishaAlt=-17;
	}else if ( m_calender == Calender.IslamicSocietyOfNorthAmerica )  fajrAlt=ishaAlt=-15;
	else if ( m_calender == Calender.UnivOfIslamicScincesKarachi) fajrAlt=ishaAlt=-18;

        double fajr=localNoon-equation(fajrAlt)/15;  // Fajr Time
	double isha=localNoon+equation(ishaAlt)/15;  // Isha Time

	if( m_calender == Calender.UmmAlQuraUniv ) isha=maghrib+1.5;

        double asrAlt;

	if( m_mazhab == Mazhab.Hanafi)	asrAlt=90-RadToDeg*Math.atan(2+Math.tan(Math.abs(m_latitude-dec)*DegToRad));
	else asrAlt=90-RadToDeg*Math.atan(1+Math.tan(Math.abs(m_latitude-dec)*DegToRad));

	double asr=localNoon+equation(asrAlt)/15;

	if( m_season == Season.Summer){
		fajr++;
		shrouk++;
		zuhr++;
		asr++;
		maghrib++;
		isha++;
	}

        setWaktuSholat("Fajar",convertIntoTime(fajr));
        setWaktuSholat("Shuroq",convertIntoTime(shrouk));
        setWaktuSholat("Dzuhur",convertIntoTime(zuhr));
        setWaktuSholat("Ashar",convertIntoTime(asr));
        setWaktuSholat("Maghrib",convertIntoTime(maghrib));
        setWaktuSholat("Isya",convertIntoTime(isha));

    }

    String convertIntoTime(double var) {
        String time="";

        if((int)(var) < 10)
            time+=("0"+String.valueOf((int)var));
        else if((int)(var) > 23)
            time+=String.valueOf(((int)var)-12) ;
        else
            time+=String.valueOf((int)var);

        time+=":";

        var-=(int)var;
        var*=60;
        int min=(int)var;

        if(min < 10)
            time+=("0"+String.valueOf((int)min));
        else
            time+=String.valueOf((int)min);

        time+=":";

        var-=(int)var;
        var*=60;
        int sec=(int)var;

        if(sec < 10)
            time+=("0"+String.valueOf((int)sec));
        else
            time+=String.valueOf((int)sec);

        return time;
    }

    double removeDublication(double var){
	// TODO: use mod operator %. Ini perhitungan mod untuk tipe double
	if(var > 360 )	{
            var/=360;
            var-=(int)(var);
            var*=360;
	}
	return var;
    }

    void setWaktuSholat(String nm,String wkt){
        boolean ketemu=false;
        byte i=0;
        do{
            if(Sholat[i][0].equalsIgnoreCase(nm)){
                Sholat[i][1]=wkt;
                ketemu=true;
            }
            i++;
        }while((i<6)&&(!ketemu));
    }

    String getWaktuSholat(String nm){
        String waktu="";
        boolean ketemu=false;
        byte i=0;
        do{
            if(Sholat[i][0].equalsIgnoreCase(nm)){
                waktu=Sholat[i][1];
                ketemu=true;
            }
            i++;
        }while((i<6)&&(!ketemu));
        return waktu;
    }

    void setDate(int day,int month,int year){
	m_day=day;
	m_month=month;
	m_year=year;
    }

    void setYear(int year){
        m_year=year;
    }

    void setMonth(int month) {
        m_month=month;
    }

    void setDay(int day) {
        m_day=day;
    }

    void setData(double lot,double lat,int zone,int day,int month,int year,Calender calender,Mazhab mazhab,Season season){
            m_longitude=lot;
            m_latitude=lat;
            m_zone=zone;

            m_day=day;
            m_month=month;
            m_year=year;

            m_calender=calender;
            m_mazhab=mazhab;
            m_season=season;
    }


    void setData(double lot,double lat,int zone,int day,int month,int year,String calender,String mazhab,String season)
    {
            m_longitude=lot;
            m_latitude=lat;
            m_zone=zone;


            m_day=day;
            m_month=month;
            m_year=year;

            m_calender = strToCalender(calender);
            m_mazhab = strToMazhab(mazhab);
            m_season = strToSeason(season);
    }

    Calender strToCalender(String calender){
        Calender tmp;
        if(calender.equalsIgnoreCase("UmmAlQuraUniv")==true)
            tmp=Calender.UmmAlQuraUniv;
        else if(calender.equalsIgnoreCase("EgytionGeneralAuthorityofSurvey"))
            tmp=Calender.EgytionGeneralAuthorityofSurvey;
        else if(calender.equalsIgnoreCase("UnivOfIslamicScincesKarachi"))
            tmp=Calender.UnivOfIslamicScincesKarachi;
        else if(calender.equalsIgnoreCase("IslamicSocietyOfNorthAmerica"))
            tmp=Calender.IslamicSocietyOfNorthAmerica;
        else
            tmp=Calender.MuslimWorldLeague;

        return tmp;
    }

    Mazhab strToMazhab(String mazhab) {
        Mazhab tmp;
        if( mazhab.equalsIgnoreCase("Default" ))
            tmp=Mazhab.Default;
        else
            tmp=Mazhab.Hanafi;

        return tmp;
    }

    Season strToSeason(String season){
        Season tmp;
        if( season.equalsIgnoreCase("Winter" ))
            tmp=Season.Winter;
        else
            tmp=Season.Summer;

        return tmp;
    }

    String calenderToStr(Calender calender){
        String tmp;
        if( calender == Calender.UmmAlQuraUniv )
            tmp="UmmAlQuraUniv";
        else if( calender == Calender.EgytionGeneralAuthorityofSurvey )
            tmp="EgytionGeneralAuthorityofSurvey";
        else if( calender == Calender.UnivOfIslamicScincesKarachi )
            tmp="UnivOfIslamicScincesKarachi";
        else if( calender == Calender.IslamicSocietyOfNorthAmerica )
            tmp="IslamicSocietyOfNorthAmerica";
        else
            tmp="MuslimWorldLeague";

        return tmp;
    }

    String mazhabToStr(Mazhab mazhab){
        String tmp;
        if( mazhab == Mazhab.Default )
            tmp="Default";
        else
            tmp="Hanafi";

        return tmp;
    }

    String seasonToStr(Season season) {
        String tmp;
        if( season == Season.Winter )
            tmp="Winter";
        else
            tmp="Summer";

        return tmp;
    }

    double qiblaDirection(double lon, double lat){
        double BM = KIBLA_LAT*DegToRad;
        double LM = KIBLA_LON*DegToRad;
        double L =  lon*DegToRad;
        double B = lat*DegToRad;

        return Math.atan2(Math.sin(L-LM), (Math.cos(B)*Math.tan(BM)-Math.sin(B)*Math.cos(L-LM)))*RadToDeg;
    }

    double qiblaDistance(double lon, double lat, Unit unit)
    {
        double BM = KIBLA_LAT*DegToRad;
        double LM = KIBLA_LON*DegToRad;
        double L =  lon*DegToRad;
        double B = lat*DegToRad;
        double R;

        switch(unit)
        {
            case Nautical:
                R = 3437.74677;
                break;
            case Kilometers:
                R = 6378.7;
                break;
            case Statute:
                R = 3963.0;
                break;
            default:
                R = 6378.7;
        }

        return Math.acos(Math.sin(BM) * Math.sin(B) + Math.cos(BM) * Math.cos(B) * Math.cos(L-LM)) * R;
    }


}
