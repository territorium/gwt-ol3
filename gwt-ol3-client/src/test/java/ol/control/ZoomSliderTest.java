package ol.control;

import ol.*;
import ol.Object;

/**
 * 
 * @author Tino Desjardins
 *
 */
public class ZoomSliderTest extends BaseTestCase {

    public void testZoomSlider() {

        ZoomSlider zoomSlider = OLFactory.createZoomSlider();
        
        assertNotNull(zoomSlider);
        assertTrue(zoomSlider instanceof Object);
        assertTrue(zoomSlider instanceof Control);
        
    }

}
