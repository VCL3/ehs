/**
 * Created by wliu on 1/31/18.
 */
package vcl3.health;

import com.codahale.metrics.health.HealthCheck;

public class EhsHealthCheck extends HealthCheck {

    @Override
    public Result check() throws Exception { // NOPMD
        return 2 + 2 == 4 ? Result.healthy() : Result.unhealthy("2 + 2 != 4 !!!");
    }

}
