package marcel.osmtest.wrapper_classes;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Alle wichtigen Daten zu den nautischen Warnmeldungen werden durch diese
 * Klasse in einem Objekt gebündelt.
 *
 * Erstellt von Marcel B., am 25.02.2017.
 */
public class NautischeWarnungen {
    private String text; //Der Berichttext
    private long timestamp; //Die Uhrzeit, zu der der Bericht empfangen wurde

    public NautischeWarnungen(String text, long timestamp) {
        this.text = text;
        this.timestamp = timestamp;
    }

    public String getLocation() {
        return text;
    }

    public String getDate(){
        Timestamp ts = new Timestamp(timestamp);
        Date dt = new Date(ts.getTime());
        DateFormat df = new SimpleDateFormat("dd-MM-yyyyy kk:mm");
        return df.format(dt);
    }

    @Override
    public String toString() {
        return "Nachricht: " + text ;
    }

    /**
     * Ein JSON-String mit nautischen Warnmeldungen wird in ein Objekt bzw.
     * mehrere Objekte dieser Klasse umgewandelt und als ArrayList dem
     * Aufrufer rückgegeben.
     *
     * @param jsonResult unbearbeiter JSON-String mit nautischen Warnmeldungen als Inhalt
     * @return ArrayList: Objekte, welche dem JSON-String entnommen wurden;
     *                  liefert null, wenn keine gültigen Objekte im JSON-String
     *                  vorhanden sind
     */
    public static ArrayList<NautischeWarnungen> parseNautische(String jsonResult){
        ArrayList<NautischeWarnungen> tempRes = new ArrayList<>();
        try{
            JSONObject jsonObj = new JSONObject(jsonResult);
            JSONArray jsonArr = jsonObj.getJSONArray("nautischeWarnmeldungen");
            for (int i = 0; i < jsonArr.length(); i++) {
                jsonObj = jsonArr.getJSONObject(i);
                //Extrahieren der Daten
                String text = jsonObj.getString("rawtext");
                long timestamp = Long.valueOf(jsonObj.getString("rectime"));
                tempRes.add(new NautischeWarnungen(text, timestamp));
            }
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
        return tempRes;
    }
}
