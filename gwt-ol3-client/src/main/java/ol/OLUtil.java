package ol;

import javax.annotation.*;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.shared.HandlerRegistration;

import ol.event.*;
import ol.gwt.CollectionWrapper;
import ol.layer.*;
import ol.source.*;
import ol.tilegrid.TileGrid;

/**
 * Utility functions.
 *
 * @author sbaumhekel
 */
@ParametersAreNonnullByDefault
public final class OLUtil {

    // prevent instantiating this class
    @Deprecated
    private OLUtil() {
    }

    /**
     * Adds a click listener for the given map.
     *
     * @param map
     *            {@link Map}
     * @param singleClicksOnly
     *            wait fortrue single click with no dragging and no double
     *            click? Note that this event is delayed by 250 ms to ensure
     *            that it is not a double click.
     * @param listener
     *            {@link ClickListener}
     * @return {@link HandlerRegistration}
     */
    public static HandlerRegistration addClickListener(Map map, boolean singleClicksOnly,
	    final ClickListener listener) {
	String type;
	if (singleClicksOnly) {
	    type = "singleclick";
	} else {
	    type = "click";
	}
	return observe(map, type, new EventListener<MapBrowserEvent>() {

	    @Override
	    public void onEvent(MapBrowserEvent event) {
		listener.onClick(event);
	    }
	});
    }

    /**
     * Adds a click listener for the given map.
     *
     * @param map
     *            {@link Map}
     * @param listener
     *            {@link DoubleClickListener}
     * @return {@link HandlerRegistration}
     */
    public static HandlerRegistration addDoubleClickListener(Map map, final DoubleClickListener listener) {
	return observe(map, "dblclick", new EventListener<MapBrowserEvent>() {

	    @Override
	    public void onEvent(MapBrowserEvent event) {
		listener.onDoubleClick(event);
	    }
	});
    }

    /**
     * Adds a map move listener for the given map.
     *
     * @param map
     *            {@link Map}
     * @param listener
     *            {@link MapMoveListener}
     * @return {@link HandlerRegistration}
     */
    public static HandlerRegistration addMapMoveListener(Map map, final MapMoveListener listener) {
	return observe(map, "moveend", new EventListener<MapEvent>() {

	    @Override
	    public void onEvent(MapEvent event) {
		listener.onMapMove(event);
	    }
	});
    }

    /**
     * Adds a map zoom listener for the given map.
     *
     * @param map
     *            {@link Map}
     * @param listener
     *            {@link MapZoomListener}
     * @return {@link HandlerRegistration}
     */
    public static HandlerRegistration addMapZoomListener(final Map map, final MapZoomListener listener) {
	return observe(map.getView(), ObjectEvent.TYPE, new EventListener<ObjectEvent>() {

	    @Override
	    public void onEvent(ObjectEvent event) {
		if ("resolution".equals(event.getKey())) {
		    Event e2 = createLinkedEvent(event, "zoom", (JavaScriptObject) map);
		    MapEvent me = initMapEvent(e2, map);
		    listener.onMapZoom(me);
		}
	    }
	});
    }

    /**
     * Links to {@link Event}s by delegating the childs methods to the parents
     * methods.
     * 
     * @param eParent
     *            parent {@link Event} (is not changed)
     * @param type
     *            type of the event
     * @param currentTarget
     *            current target of the child event
     * @return child {@link Event} (gets its methods linked to the parent event)
     */
    public static native Event createLinkedEvent(Event eParent, String type, JavaScriptObject currentTarget)
    /*-{
		var eChild = {};
		eChild.preventDefault = function() {
			eParent.preventDefault();
			eChild.defaultPrevented = eParent.defaultPrevented;
		};
		eChild.stopPropagation = function() {
			eParent.stopPropagation();
		};
		eChild.currentTarget = currentTarget;
		eChild.defaultPrevented = eParent.defaultPrevented;
		eChild.target = eParent.target;
		eChild.type = type;
		return eChild;
    }-*/;

    /**
     * Initializes a {@link MapEvent}, can be used for creating a new
     * {@link MapEvent} as it does not work with ol.js in release mode using the
     * OpenLayers API.
     * 
     * @param e
     *            base {@link Event}
     * @param map
     *            {@link Map}
     * @return {@link MapEvent}
     */
    public static native MapEvent initMapEvent(Event e, Map map)
    /*-{
		e.map = map;
		e.framestate = null;
		return e;
    }-*/;

    /**
     * Adds a listener for tile loading errors.
     * 
     * @param source
     *            source
     *
     * @param listener
     *            {@link TileLoadErrorListener}
     * @return {@link HandlerRegistration}
     */
    public static HandlerRegistration addTileLoadErrorListener(UrlTile source, final TileLoadErrorListener listener) {
	return observe(source, "tileloaderror", new EventListener<TileEvent>() {

	    @Override
	    public void onEvent(TileEvent event) {
		listener.onTileLoadError(event);
	    }
	});
    }

    /**
     * Creates a JavaScript function calling the given event listener.
     *
     * @param listener
     *            listener
     * @return JavaScript function
     */
    public static native <E extends Event> JavaScriptObject createEventListenerFunction(EventListener<E> listener)
    /*-{
		return function(evt) {
			listener.onEvent(evt);
		};
    }-*/;

    /**
     * Gets the geometry layout string for the given dimension.
     *
     * @param dim
     *            dimension (i.e. 2 or 3)
     * @return geometry layout string
     */
    public static String getGeometryLayout(int dim) {
	if (dim > 2) {
	    return "XYZ";
	}
	return "XY";
    }

    /**
     * Gets the geometry layout string for the given dimension.
     *
     * @param dim
     *            dimension (i.e. 2 or 3)
     * @param measure
     *            include measure 'M' coordinate?
     * @return geometry layout string
     */
    public static String getGeometryLayout(int dim, boolean measure) {
	if (measure) {
	    if (dim > 2) {
		return "XYZM";
	    }
	    return "XYM";
	} else {
	    if (dim > 2) {
		return "XYZ";
	    }
	    return "XY";
	}
    }

    /**
     * Gets a layer by the given name.
     *
     * @param map
     *            {@link Map}
     * @param name
     *            name of the layer
     * @return {@link Layer} on success, else null
     */
    @Nullable
    public static Base getLayerByName(Map map, String name) {
	CollectionWrapper<Base> layers = new CollectionWrapper<Base>(map.getLayers());
	for (Base l : layers) {
	    if (name.equals(getName(l))) {
		return l;
	    }
	}
	return null;
    }

    /**
     * Gets the name of the given {@link Layer}.
     *
     * @param layer
     *            {@link Layer}
     * @return name on success, else null
     */
    @Nullable
    public static String getName(Base layer) {
	return layer.get("name");
    }

    /**
     * Gets the current zoomlevel of the given {@link Map}.
     * 
     * @param map
     *            {@link Map}
     * @return zoomlevel on success, else -1
     */
    public static int getZoomLevel(Map map) {
	View v = map.getView();
	// try to get zoom
	int z = getZoom(v);
	if (z >= 0) {
	    return z;
	}
	// zoom is undefined, so check resolution
	double zoomResolution = v.getResolution();
	// walk layers to find resolution
	CollectionWrapper<Base> layers = new CollectionWrapper<Base>(map.getLayers());
	for (Base l : layers) {
	    // get source if layer instance has it
	    Source source = l.get("source");
	    if (source != null) {
		// try to get a tilegrid from the source
		TileGrid tg = getTileGrid((JavaScriptObject) source);
		if (tg != null) {
		    // check resolutions
		    double[] resolutions = tg.getResolutions();
		    if (resolutions != null) {
			double dPreviousResolution = 0;
			for (int i = 0; i < resolutions.length; i++) {
			    // resolutions are sorted in descending order, so
			    // compare with actual one
			    double resolution = resolutions[i];
			    if (resolution <= zoomResolution) {
				if (i > 1) {
				    // check to which zoomlevel resolution is
				    // nearer and prefer the larger number by
				    // (75%:25%=3)
				    if ((zoomResolution - resolution) / (dPreviousResolution - zoomResolution) < 3) {
					return i;
				    } else {
					return i - 1;
				    }
				} else {
				    return 0;
				}
			    }
			    dPreviousResolution = resolution;
			}
		    }
		}
	    }
	}
	return -1;
    }

    /**
     * Gets the current zoom level of the given {@link View}.
     * 
     * @param v
     *            {@link View}
     * @return Zoom on success, else -1
     */
    private static native int getZoom(View v)
    /*-{
		return v.getZoom() || -1;
    }-*/;

    /**
     * Gets a {@link TileGrid} from the given object, if the property is set
     * 
     * @param o
     *            {@link JavaScriptObject}
     * @return {@link TileGrid} on success, else null
     */
    private static native TileGrid getTileGrid(JavaScriptObject o)
    /*-{
		return o.tileGrid || null;
    }-*/;

    /**
     * Determines the ground resolution (in meters per pixel) at a specified
     * latitude and level of detail.
     *
     * @param latitude
     *            latitude
     * @param zoomLevel
     *            zoomlevel
     * @return ground resolution
     */
    public static double getGroundResolutionInMeters(double latitude, int zoomLevel) {
	return Math.cos(latitude * Math.PI / 180) * 2 * Math.PI * dEarthRadius / getMapSizeInPixels(zoomLevel);
    }

    private static final double dEarthRadius = 6378137;

    /**
     * Determines the map width and height (in pixels) at a specified level of
     * detail.
     *
     * @param zoomLevel
     *            Level of detail, from 1 (lowest detail) to 23 (highest
     *            detail).
     * @return The map width and height in pixels.
     */
    public static double getMapSizeInPixels(int zoomLevel) {
	return ((double) (1 << zoomLevel)) * 256;
    }

    /**
     * Registers the given event listener to listen for the given event type on
     * the given {@link Observable}.
     *
     * @param o
     *            {@link Observable}
     * @param eventType
     *            event type
     * @param listener
     *            listener
     * @return {@link HandlerRegistration}
     */
    public static <E extends Event> HandlerRegistration observe(Observable o, String eventType,
	    EventListener<E> listener) {
	JavaScriptObject handler = OLUtil.createEventListenerFunction(listener);
	JavaScriptObject key = o.on(eventType, handler);
	return new OLHandlerRegistration(o, key);
    }

    /**
     * Registers the given event listener to listen once for the given event
     * type on the given {@link Observable}.
     *
     * @param o
     *            {@link Observable}
     * @param eventType
     *            event type
     * @param listener
     *            listener
     * @return {@link HandlerRegistration}
     */
    public static <E extends Event> HandlerRegistration observeOnce(Observable o, String eventType,
	    EventListener<E> listener) {
	JavaScriptObject handler = OLUtil.createEventListenerFunction(listener);
	JavaScriptObject key = o.once(eventType, handler);
	return new OLHandlerRegistration(o, key);
    }

    /**
     * Sets the name of the given {@link Layer}.
     *
     * @param layer
     *            {@link Layer}
     * @param name
     *            name
     */
    public static void setName(Base layer, String name) {
	layer.set("name", name);
    }

}
