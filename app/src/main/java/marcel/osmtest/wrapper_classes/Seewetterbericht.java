package marcel.osmtest.wrapper_classes;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Alle wichtigen Daten zu den Seewetterberichten werden durch diese
 * Klasse in einem Objekt gebündelt.
 *
 * Erstellt von Marcel B., am 25.02.2017.
 */
public class Seewetterbericht {
    private String location; //Ort den der Seewetterbericht betrifft
    private String nachricht; //Nachrichtentext des Seewetterberichtes
    private String gueltigkeit; //Gueltigkeitsdatum des Seewetterberichts
    private long parsetime; //Datum, zu dem der Seewetterbericht empfangen wurde

    public Seewetterbericht(String location, String nachricht, String gueltigkeit, long parsetime) {
        this.location = location;
        this.nachricht = nachricht;
        this.gueltigkeit = gueltigkeit;
        this.parsetime = parsetime;
    }

    public String getLocation() {
        return location;
    }

    public String getNachricht() {
        return nachricht;
    }

    public String getGueltigkeit() {
        return gueltigkeit;
    }

    public String getDate(){
        Timestamp ts = new Timestamp(parsetime);
        Date dt = new Date(ts.getTime());
        DateFormat df = new SimpleDateFormat("dd-MM-yyyyy kk:mm");
        return df.format(dt);
    }

    @Override
    public String toString() {
        return  "Nachricht: " + nachricht + "\n" +
                "Gueltigkeit: " + gueltigkeit + "\n" +
                "Datum: " + getDate();
    }

    /**
     * Ein JSON-String mit Seewetterberichten wird in ein Objekt bzw.
     * mehrere Objekte dieser Klasse umgewandelt und als ArrayList dem
     * Aufrufer rückgegeben.
     *
     * @param jsonResult unbearbeiter JSON-String mit Seewetterberichten als Inhalt
     * @return ArrayList: Objekte, welche dem JSON-String entnommen wurden;
     *                  liefert null, wenn keine gültigen Objekte im JSON-String
     *                  vorhanden sind
     */
    public static ArrayList<Seewetterbericht> parseSeewtter(String jsonResult){
        ArrayList<Seewetterbericht> tempRes = new ArrayList<>();
        try{
            JSONObject jsonObj = new JSONObject(jsonResult);
            JSONArray jsonArr = jsonObj.getJSONArray("seewetterberichte");
            for (int i = 0; i < jsonArr.length(); i++) {
                jsonObj = jsonArr.getJSONObject(i);
                //Daten aus dem JSON-String extrahieren
                String location = jsonObj.getString("location");
                String nachricht = jsonObj.getString("nachricht");
                String gueltigkeit = jsonObj.getString("gueltigkeitsraum");
                long parsetime = Long.valueOf(jsonObj.getString("parsetime"));

                tempRes.add(new Seewetterbericht(location, nachricht, gueltigkeit, parsetime));
            }
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
        return tempRes;
    }
}
