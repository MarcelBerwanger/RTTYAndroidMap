package marcel.osmtest.wrapper_classes;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Alle wichtigen Daten zu den Zeitreihenberichten werden durch diese
 * Klasse in einem Objekt gebündelt.
 *
 * Erstellt von Marcel B., am 25.02.2017.
 */
public class Zeitreihenbericht {
    private String location; //Ort, den dieser Bericht betrifft
    private String wochentag;//Wochentag der Vorhersage
    private String datum;//Datum der Vorhersage
    private String zeit;//Zeit der Vorhersage
    private String windrichtung;
    private String windstaerke;//Windstaerke in Beaufort
    private String boeen;//Boenstaerke in Beaufort
    private String wellenhoehe;//Wellenhoehe in meter
    private long parsetime;
    private String wetter;

    public Zeitreihenbericht(String location, String wochentag, String datum, String zeit, String windrichtung, String windstaerke, String boeen, String wellenhoehe, long parsetime, String wetter) {
        this.location = location;
        this.wochentag = wochentag;
        this.datum = datum;
        this.zeit = zeit;
        this.windrichtung = windrichtung;
        this.windstaerke = windstaerke;
        this.boeen = boeen;
        this.wellenhoehe = wellenhoehe;
        this.parsetime = parsetime;
        this.wetter = wetter;
    }

    public String getLocation() {
        return location;
    }

    public String getWochentag() {
        return wochentag;
    }

    public String getDatum() {
        return datum;
    }

    public String getZeit() {
        return zeit;
    }

    public String getWindrichtung() {
        return windrichtung;
    }

    public String getWindstaerke() {
        return windstaerke;
    }

    public String getBoeen() {
        return boeen;
    }

    public String getWellenhoehe() {
        return wellenhoehe;
    }

    public String getWetter() {
        return wetter;
    }

    public String getDate(){
        Timestamp ts = new Timestamp(parsetime);
        Date dt = new Date(ts.getTime());
        DateFormat df = new SimpleDateFormat("dd-MM-yyyyy kk:mm");
        return df.format(dt);
    }

    @Override
    public String toString() {
        return  "Wochentag: " + wochentag + '\n' +
                "Datum: " + datum + '\n' +
                "Zeit: " + zeit + '\n' +
                "Windrichtung: " + windrichtung + '\n' +
                "Windstärke: " + windstaerke + '\n' +
                "Boeen: " + boeen + '\n' +
                "Wellenhöhe: " + wellenhoehe + '\n' +
                "Wetter: " + wetter + '\n';
    }

    /**
     * Ein JSON-String mit Zeitreihenberichten wird in ein Objekt bzw.
     * mehrere Objekte dieser Klasse umgewandelt und als ArrayList dem
     * Aufrufer rückgegeben.
     *
     * @param jsonResult unbearbeiter JSON-String mit Zeitreihenberichten als Inhalt
     * @return ArrayList: Objekte, welche dem JSON-String entnommen wurden;
     *                  liefert null, wenn keine gültigen Objekte im JSON-String
     *                  vorhanden sind
     */
    public static ArrayList<Zeitreihenbericht> parseZeitreihe(String jsonResult){
        ArrayList<Zeitreihenbericht> tempRes = new ArrayList<>();
        try{
            JSONObject jsonObj = new JSONObject(jsonResult);

            // Getting JSON Array node
            JSONArray jsonStation = jsonObj.getJSONArray("zeitreihenberichte");
            // looping through All Contacts
            for (int i = 0; i < jsonStation.length(); i++) {
                JSONObject c = jsonStation.getJSONObject(i);

                String location = c.getString("location");
                String wochentag = c.getString("wochentag");
                String datum = c.getString("datum");
                String zeit = c.getString("zeit");
                String windrichtung = c.getString("windrichtung");
                String windstaerke = c.getString("windstaerke");
                String boeen = c.getString("boeen");
                String wellenhoehe = c.getString("wellenhoehe");
                long parsetime = Long.valueOf(c.getString("parsetime"));
                String wetter = c.getString("wetter");
                tempRes.add(new Zeitreihenbericht(location, wochentag, datum, zeit, windrichtung, windstaerke, boeen, wellenhoehe,  parsetime, wetter));
            }
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
        return tempRes;
    }
}
