package com.alibaba.csp.sentinel.adapter.gateway.common.api;

import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Eric Zhao
 */
public class GatewayApiDefinitionManagerTest {

    @Test
    public void testIsValidApi() {
        ApiDefinition bad1 = new ApiDefinition();
        ApiDefinition bad2 = new ApiDefinition("foo");
        ApiDefinition good1 = new ApiDefinition("foo")
            .setPredicateItems(Collections.<ApiPredicateItem>singleton(new ApiPathPredicateItem()
                .setPattern("/abc")
            ));

        assertFalse(GatewayApiDefinitionManager.isValidApi(bad1));
        assertFalse(GatewayApiDefinitionManager.isValidApi(bad2));
        assertTrue(GatewayApiDefinitionManager.isValidApi(good1));
    }
}