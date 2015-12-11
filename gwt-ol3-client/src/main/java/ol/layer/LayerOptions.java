package ol.layer;

import com.google.gwt.core.client.js.JsProperty;
import com.google.gwt.core.client.js.JsType;

import ol.Options;
import ol.source.Source;

/**
 * 
 * @author Tino Desjardins
 *
 */
@JsType
public interface LayerOptions extends Options {

    @JsProperty
    void setSource(Source source);

    @JsProperty
    void setOpacity(float opacity);

    @JsProperty
    void setSaturation(float saturation);

    @JsProperty
    void setVisible(boolean visible);

}
