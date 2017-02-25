package marcel.osmtest.data_management;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Alle API-Verbundenen Daten werden ueber diese Klasse bezogen. Der Download der Daten laeuft im Hintergrund.
 * Das Ergebnis wird ueber die Delegate-Technik dem Aufrufer zurückgegeben. Gestartet wird der Download mit erstellen
 * einer Klasseninstantz und dem Aufruf der "execute"-Methode.
 *
 * Erstellt von Marcel B., am 25.02.2017.
 */
public class DataManager extends AsyncTask<String, String, String>{
    private String phpString;//String, der alles für den API-Aufruf spezifiziert (Parameter, Adresse)
    private AsyncResponse delegate; //Zum weiterleiten der Daten an den Aufrufer

    public DataManager(String phpString, AsyncResponse delegate){
        super();
        this.phpString = phpString;
        this.delegate = delegate;
    }

    /**
     * Der eigentliche Download findet in dieser Methode statt. Es wird der Empfangene JSON-String
     * zurückgegeben oder "failed", wenn der download nicht erfolgreich war.
     *
     * @param params wird nicht verwendet
     * @return String: JSON-Stirng oder "faild" bei einem Fehler
     */
    @Override
    protected String doInBackground(String... params) {
        try {
            //Der eigentliche Download
            URL u = new URL(phpString);
            HttpURLConnection c = (HttpURLConnection) u.openConnection();
            c.setRequestMethod("GET");
            c.setRequestProperty("Content-length", "0");
            c.setUseCaches(false);
            c.setAllowUserInteraction(false);
            c.setConnectTimeout(5000);
            c.setReadTimeout(5000);
            c.connect();
            int status = c.getResponseCode();
            //Status 200 besagt, dass alles in Ordnung ist
            if(status==200){
                //Auslesen der Rückmeldung; diese beinhaltet den JSON-String
                BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line+"\n");
                }
                br.close();
                return sb.toString();
            }else{return "failed";}
        }
        catch(Exception e){
            e.printStackTrace();
            return "failed";
        }
    }

    @Override
    protected void onPostExecute(String result){
        delegate.processFinish(result);//Rückgeben des Ergebnises an den Aufrufer
    }
}
