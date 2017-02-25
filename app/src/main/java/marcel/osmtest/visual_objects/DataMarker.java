package marcel.osmtest.visual_objects;

import org.osmdroid.util.GeoPoint;

/**
 * Diese Klasse fasst alle Daten, die für einen Marker wichtig sind, zusammen.
 * Mithilfe dieser Klasse werden alle Marker-Objekte erstellt.
 *
 * Erstellt von Marcel B., am 17.01.2017.
 */
public class DataMarker {

    public DataMarker(GeoPoint point, String locationString, String weatherData) {
        this.point = point;
        this.locationString = locationString;
        this.weatherData = weatherData;
    }

    private GeoPoint point;
    private String locationString;
    private String weatherData;

    public GeoPoint getPoint() {
        return point;
    }

    public String getLocationString() {
        return locationString;
    }

    public String getWeatherData() {
        return weatherData;
    }
}
