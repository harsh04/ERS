package internship.ibm.com.ers;

import java.util.List;

/**
 * Created by Harsh Mathur on 17-08-2017.
 */

public interface DirectionFinderListener {
    void onDirectionFinderStart();
    void onDirectionFinderSuccess(List<Route> route);
}
