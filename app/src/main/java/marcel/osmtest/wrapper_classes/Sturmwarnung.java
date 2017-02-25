package marcel.osmtest.wrapper_classes;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * Alle wichtigen Daten zu den Sturmwarnungen werden durch diese
 * Klasse in einem Objekt gebündelt.
 *
 * Erstellt von Marcel B., am 25.02.2017.
 */
public class Sturmwarnung {
    private String location; //Orte, für die Sturmwarnungen vorliegen
    private String date;//Datum, zu dem die Strumwarnungen empfangen wurden

    public Sturmwarnung(String location, String date) {
        this.location = location;
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public String getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "Ort: " + location;
    }

    /**
     * Ein JSON-String mit Sturmwarnungen wird in ein Objekt bzw.
     * mehrere Objekte dieser Klasse umgewandelt und als ArrayList dem
     * Aufrufer rückgegeben.
     *
     * @param jsonResult unbearbeiter JSON-String mit Sturmwarnungen als Inhalt
     * @return ArrayList: Objekte, welche dem JSON-String entnommen wurden;
     *                  liefert null, wenn keine gültigen Objekte im JSON-String
     *                  vorhanden sind
     */
    public static ArrayList<Sturmwarnung> parseSturmwarnungen(String jsonResult){
        ArrayList<Sturmwarnung> tempRes = new ArrayList<>();
        try{
            JSONObject jsonObj = new JSONObject(jsonResult);
            JSONArray jsonArr = jsonObj.getJSONArray("seewetterSturmwarnungen");
            for (int i = 0; i < jsonArr.length(); i++) {
                jsonObj = jsonArr.getJSONObject(i);
                //Extrahieren der Daten aus den JSON-String
                String location = jsonObj.getString("location");
                String date = jsonObj.getString("datum");
                tempRes.add(new Sturmwarnung(location, date));
            }
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
        return tempRes;
    }
}
