package marcel.osmtest.visual_objects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

/**
 * Diese Klasse überschreibt die Klasse Marker und fügt den Titel über den Marker ein
 *
 * Erstellt von Marcel B., am 17.01.2017.
 * Idee von: http://stackoverflow.com/questions/22250423/osmbonuspack-show-name-of-marker-on-the-map Stand:16.01.2017.
 */
public class MarkerWithLabel extends Marker {
    Paint textPaint = null;
    String mLabel = null;

    public MarkerWithLabel(MapView mapView, String label) {
        super( mapView);
        mLabel = label;
        textPaint = new Paint();
        textPaint.setColor( Color.BLACK);
        textPaint.setTextSize(10f);
        textPaint.setAntiAlias(true);
        textPaint.setTextAlign(Paint.Align.CENTER);
    }

    public void draw( final Canvas c, final MapView osmv, boolean shadow) {
        draw( c, osmv);
    }
    public void draw( final Canvas c, final MapView osmv) {
        super.draw( c, osmv, false);
        Point p = this.mPositionPixels;
        c.drawText( mLabel, p.x, p.y-28, textPaint); //+20 normal -> + = Verschiebung nach unten
    }
}
