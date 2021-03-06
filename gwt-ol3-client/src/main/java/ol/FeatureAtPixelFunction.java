/*******************************************************************************
 * Copyright 2014, 2020 gwt-ol
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package ol;

import jsinterop.annotations.JsFunction;
import ol.layer.Base;

/**
 * Callback for {@link PluggableMap#forEachFeatureAtPixel(Pixel, FeatureAtPixelFunction, FeatureAtPixelOptions)}
 *
 * @author gkresic
 */
@FunctionalInterface
@JsFunction
public interface FeatureAtPixelFunction {

    /**
     * <p><b>Note:</b>Due to missing type info in latest supported OpenLayers version (5.3.0), <code>layerFilter</code> param can not accept {@link ol.layer.Layer},
     * but it accepts {@link ol.layer.Base} instead. This is fixed in OpenLayers 6 so it will change in gwt-ol at some point, too.</p>
     */
    boolean call(Feature feature, Base layer);

}
