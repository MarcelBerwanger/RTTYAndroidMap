package marcel.osmtest.wrapper_classes;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Alle wichtigen Daten zu den Stationsmeldungen werden durch diese
 * Klasse in einem Objekt gebündelt.
 *
 * Erstellt von Marcel B., am 25.02.2017.
 */
public class Stationsmeldung {
    private String location;//Ort den die Stationsmeldung betrifft
    private double lon;//Longitude des Ortes
    private double lat;//latitude des Ortes
    private long parsetime;//Datum, zu dem der Bericht empfangen wurde
    private String windrichtung;
    private String windstaerke;//Windstaerke in Beaufort
    private String wetter;
    private String temperatur;//Temperatur in °C
    private String luftdruck;//Luftdruck in hPa

    public Stationsmeldung(String location, double lon, double lat, long parsetime, String windrichtung, String windstaerke, String wetter, String temperatur, String luftdruck) {
        this.location = location;
        this.lon = lon;
        this.lat = lat;
        this.parsetime = parsetime;
        this.windrichtung = windrichtung;
        this.windstaerke = windstaerke;
        this.wetter = wetter;
        this.temperatur = temperatur;
        this.luftdruck = luftdruck;
    }

    public String getLocation() {
        return location;
    }

    public double getLon() {
        return lon;
    }

    public double getLat() {
        return lat;
    }

    public long getParsetime() {
        return parsetime;
    }

    public String getWindrichtung() {
        return windrichtung;
    }

    public String getWindstaerke() {
        return windstaerke;
    }

    public String getWetter() {
        return wetter;
    }

    public String getTemperatur() {
        return temperatur;
    }

    public String getLuftdruck() {
        return luftdruck;
    }

    //Methoden der Klasse
    public String getDate(){
        Timestamp ts = new Timestamp(parsetime);
        Date dt = new Date(ts.getTime());
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy kk:mm");
        return df.format(dt);
    }

    @Override
    public String toString() {
        return "Luftdruck: " + luftdruck + " hPa \n" +
                "Temperatur: " + temperatur + " °C \n" +
                "Wetter: " + wetter + "\n" +
                "Windstärke: " + windstaerke + " Bft \n" +
                "Windrichtung: " + windrichtung;
    }

    /**
     * Ein JSON-String mit Stationsmeldungen wird in ein Objekt bzw.
     * mehrere Objekte dieser Klasse umgewandelt und als ArrayList dem
     * Aufrufer rückgegeben.
     *
     * @param jsonResult unbearbeiter JSON-String mit Stationsmeldungen als Inhalt
     * @return ArrayList: Objekte, welche dem JSON-String entnommen wurden;
     *                  liefert null, wenn keine gültigen Objekte im JSON-String
     *                  vorhanden sind
     */
    public static ArrayList<Stationsmeldung> parseStationsmeldungen(String jsonResult){
        ArrayList<Stationsmeldung> tempRes = new ArrayList<>();
        try{
            JSONObject jsonObj = new JSONObject(jsonResult);
            JSONArray jsonArr = jsonObj.getJSONArray("stationsmeldungen");
            for (int i = 0; i < jsonArr.length(); i++) {
                jsonObj = jsonArr.getJSONObject(i);
                //Extrahieren der Daten aus dem JSON-String
                String location = jsonObj.getString("location");
                double lon = Double.valueOf(jsonObj.getString("longitude"));
                double lat = Double.valueOf(jsonObj.getString("latitude"));
                long parsetime = Long.valueOf(jsonObj.getString("parsetime"));
                String windrichtung = jsonObj.getString("windrichtung");
                String windstaerke = jsonObj.getString("windstaerke");
                String wetter = jsonObj.getString("wetter");
                String temperatur = jsonObj.getString("temperatur");
                String luftdruck = jsonObj.getString("luftdruck");
                tempRes.add(new Stationsmeldung(location, lon, lat, parsetime, windrichtung, windstaerke, wetter, temperatur, luftdruck));
            }
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
        return tempRes;
    }
}
