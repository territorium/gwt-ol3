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
package ol.format.filter;

import jsinterop.annotations.JsType;

/**
 * Abstract class; normally only used for creating subclasses and not
 * instantiated in apps.
 * Base class for WFS GetFeature property comparison filters.
 *
 * @author hmgn
 *
 */
@JsType(isNative = true)
public abstract class Comparison extends Filter {

    /**
     *
     * @param tagName The XML tag name for this filter.
     * @param propertyName Name of the context property to compare.
     */
    public Comparison(String tagName, String propertyName){
        super(tagName);
    }

}
