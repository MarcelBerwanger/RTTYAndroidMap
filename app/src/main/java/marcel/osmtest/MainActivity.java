package marcel.osmtest;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import org.osmdroid.tileprovider.cachemanager.CacheManager;
import org.osmdroid.tileprovider.constants.OpenStreetMapTileProviderConstants;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.BoundingBoxE6;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import marcel.osmtest.data_management.AsyncResponse;
import marcel.osmtest.data_management.DataManager;
import marcel.osmtest.visual_objects.CustomClickListener;
import marcel.osmtest.visual_objects.DataMarker;
import marcel.osmtest.visual_objects.MarkerWithLabel;
import marcel.osmtest.wrapper_classes.Stationsmeldung;
import marcel.osmtest.wrapper_classes.Zeitreihenbericht;

/**
 * Alle Funktionen der Karte werden in dieser Klasse realisiert. Es werden die Daten
 * von der API bzogen, aufbearbeitet und in der Karte dargestellt.
 *
 * Erstellt von Marcel B., am 17.01.2017.
 */
public class MainActivity extends AppCompatActivity implements AsyncResponse {
    private MapView mMapView; //Das Objekt der grafischen Darstellung der Karte
    private BoundingBox mapLimits; //Ein rechteck, welches die Grenzen der Karte darstellt (Long/Lat-Format)
    private int minZoom; //Die minimale Zoom-Stufe der Karte
    private int maxZoom; //Die maximale Zoom-Stufe der Karte

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Laden der Kartengrenzen
        mapLimits= new BoundingBox(Double.valueOf(getResources().getString(R.string.bounding_box_limits_north)),
                Double.valueOf(getResources().getString(R.string.bounding_box_limits_east)),
                Double.valueOf(getResources().getString(R.string.bounding_box_limits_south)),
                Double.valueOf(getResources().getString(R.string.bounding_box_limits_west)) );
        //Laden der erlaubten Zoom-Layer
        minZoom = Integer.valueOf(getResources().getString(R.string.min_zoom_layer_map));
        maxZoom = Integer.valueOf(getResources().getString(R.string.max_zoom_layer_map));

        //Erzeugen der MapView und Konfiguration der grundlegende Einstellungen
        OpenStreetMapTileProviderConstants.setUserAgentValue(BuildConfig.APPLICATION_ID);
        setContentView(R.layout.activity_main);
        mMapView = (MapView) findViewById(R.id.map);
        mMapView.setScrollableAreaLimit(
                new BoundingBoxE6(mapLimits.getLatNorth(),
                mapLimits.getLonEast(),
                mapLimits.getLatSouth(),
                mapLimits.getLonWest()) );
        mMapView.setTileSource(TileSourceFactory.MAPNIK);
        mMapView.setMinZoomLevel(minZoom);
        mMapView.setMaxZoomLevel(maxZoom);
        mMapView.setBuiltInZoomControls(true);
        mMapView.setMultiTouchControls(true);
        mMapView.setUseDataConnection(false);

        //Anpassen der Ansicht der Karte (Kartenmitte in der Ansicht zentrieren
        //& Zoom-Stufe festlegen)
        mMapView.getController().setZoom(minZoom);
        mMapView.getController().setCenter(mapLimits.getCenter());

        //Prüfen des Alters der gecachten Map-Tiles
        checkChacheData();

        //Datenbeschaffung aus der API
        //TODO die IP aus Shared Preference
        new DataManager("http://80.109.141.34"+getResources().getString(R.string.api_call_map_data), this).execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sample_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.exit_menu:
                System.exit(0);
                return true;
            case R.id.manual_refresh_map:
                manuallyRefreshMap();
                return true;
        }
        return true;
    }

    /**
     * Die vom DataManager erfassten JSON-Objekte werden in Marker umgewandelt
     * und in der Karte dargestellt. Dazu werden die Informationen aus den Stations-
     * meldungsobjekten extrahiert.
     * Über das Delegate-Prinzip wird diese Methode aus der Klasse "DataManager"
     * aufgerufen
     *
     * @param result JSON-String mit Stationsmeldungen
     */
    @Override
    public void processFinish(String result) {
        MarkerWithLabel tempMarker;
        GeoPoint gp;
        CustomClickListener tempListerner = new CustomClickListener(this);
        ArrayList<DataMarker> mapData = new ArrayList<>();
        ArrayList<Stationsmeldung> arrResults = Stationsmeldung.parseStationsmeldungen(result);
        //Wenn die ArrayList keine Einträge beinhaltet, stimmt etwas mit der Verbindung nicht
        if(arrResults==null){
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle(getResources().getString(R.string.alert_header_popup));
            dialog.setMessage(getResources().getString(R.string.alert_no_data_recived));
            dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
            dialog.show();
            return;
        }
        //Erzeugen der DataMarker-Objekte und daraus resultierend die Marker in der Kartenansicht
        for (Stationsmeldung st: arrResults){
            gp = new GeoPoint(st.getLon(), st.getLat());
            mapData.add(new DataMarker(gp, st.getLocation(), st.toString()));
        }
        for (DataMarker dm: mapData) {
            tempMarker = new MarkerWithLabel(mMapView, dm.getLocationString());
            tempMarker.setTitle(dm.getLocationString());
            tempMarker.setSnippet(dm.getWeatherData());
            tempMarker.setOnMarkerClickListener(tempListerner);
            tempMarker.setPosition(dm.getPoint());
            mMapView.getOverlays().add(tempMarker);
        }
    }

    /**
     * Diese Methode bildet eine Schnittstelle zur der restlichen App. Andere Activites können
     * einen belibigen Punkt auf der Map, bei Eingabe von Lon & Lat, zentrieren.
     * Ueblicherweise wird diese Methode in Verbindung mit den bestehenden Orten der Datenbank
     * verwednet.
     * @param lon Longitude des Punktes
     * @param lat Latitude des Punktes
     */
    public void centerPos(double lon, double lat){
        mMapView.getController().setCenter(new GeoPoint(lon, lat));
    }

    /**
     * Diese Methode wird bei jedem Start der Kartenansicht ausgeführt. Sie überprüft,
     * ob die zu letzt heruntergeladenen Tiles bereits älter, als ein Monat sind.
     * Sollten die Tiles älter al ein Monat sein, so werden neue Tiles heruntergeladen. Der
     * Download-Prozess wird in einem Popup eingeblendet.
     * Das Alter wird anhand des Datums des zu letzt durchgeführten Downloads bestimmt.
     */
    private void checkChacheData(){

        //In dieser shared Preference liegt das Datum des letzten Downloads
        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        //Lesen aus der Preference
        int defaultValue = -1;
        int date = sharedPref.getInt(getString(R.string.preference_field_last_download), defaultValue);
        int tempDate = getTempDate();
        //Überprüfung des Alters
        if(date==-1 | date<tempDate){
            //Prüfen der Internetverbindung, wenn nicht vorhanden wird eine Meldung am Bildschirm ausgegeben
            if(!checkInternetConnection())return;

            //Das aktuelle Datum wird in die SharedPreference geschrieben
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt(getString(R.string.preference_field_last_download), tempDate);
            editor.apply();

            //Downloaden der Daten innerhalb eines Rechtecks, mit festgelegten Zoom-Stufen
            CacheManager cm = new CacheManager(mMapView);
            cm.downloadAreaAsync(this, mapLimits, minZoom, maxZoom);
        }else{return;}
    }

    /**
     * Arbeitet gleich, wie die Methode "checkCacheDate", jedoch gibt es hier keine Ueberpruefung des Alters der Tiles.
     * Diese Methode wird nur aufgerufen, wenn der Benutzer es soll will (Menueintrag im "Optionsmenu")
     */
    private void manuallyRefreshMap(){
        //Prüfen der Internetverbindung
        if(!checkInternetConnection())return;
        //Das Datum des Downloads wird festgehalten
        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getString(R.string.preference_field_last_download), getTempDate());
        editor.apply();

        //Downloaden der Daten innerhalb eines Rechtecks, mit festgelegten Zoom-Stufen
        CacheManager cm = new CacheManager(mMapView);
        cm.downloadAreaAsync(this, mapLimits, minZoom, maxZoom);
    }

    /**
     * Diese Methode liefert das aktuelle Datum in einem vorgegebenen Format als String Objekt.
     * Es ist für die Verwendung in den Methoden "manuallyRefreshMap" & "checkCacheData" gedacht.
     * @return String: Datum im Format yyyyMM
     */
    private int getTempDate(){
        //Lesen des Datums
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat(getResources().getString(R.string.date_format));
        return Integer.valueOf(format.format(cal.getTime()));
    }

    /**
     * Diese Methode prüft, ob eine aktive Internetverbindung vorhanden ist. Sollte keine
     * Internetverbindung vorhanden sein, so wird ein Pop-Up mit der entsprechenden Meldung
     * am Bildschirm ausgegeben.
     * @return boolean: true - Internetverbindung vorhanden
     *                  false - Internetverbindung nicht vorhanden
     */
    private boolean checkInternetConnection(){
        //Prüfen der Internetverbindung
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        if(netInfo==null){
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle(getResources().getString(R.string.alert_header_popup));
            dialog.setMessage(getResources().getString(R.string.alert_no_internet_connection));
            dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
            });
            dialog.show();
            return false; //keine Internetverbindung
        }
        else{return true;}
    }
}
