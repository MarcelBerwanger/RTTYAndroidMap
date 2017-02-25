package marcel.osmtest.visual_objects;

import android.app.AlertDialog;
import android.content.Context;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

/**
 * Diese Klasse soll eine eigene Version des "MarkerOnClickListener" darstellen.
 * Durch diesen angepassten Event-Listener werden die Pop-Up-Fenster beim Klick
 * auf einen Marker erzeugt.
 *
 * Erstellt von Marcel B., am 17.01.2017.
 */
public class CustomClickListener implements Marker.OnMarkerClickListener {

    public CustomClickListener(Context con){
        mContext=con;
    }
    private Context mContext;

    /**
     * Bei Klick auf einen Marker wird ein Pop-Up-Fenster erzeugt, welches
     * alle Relevanten Daten zu diesem Marker bzw. diesem Ort beinhaltet. Die
     * Daten werden aus dem Aufrufer-Objekt bezogen
     *
     * @param marker Aufrufer-Objekt; Daten zur Darstellung werden daraus bezogen
     * @param mapView nicht verwendet
     * @return nicht verwendet
     */
    @Override
    public boolean onMarkerClick(Marker marker, MapView mapView) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
        dialog.setTitle(marker.getTitle());
        dialog.setMessage(marker.getSnippet());
        dialog.show();
        return false;
    }
}
