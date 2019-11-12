package myApp.model;

import org.junit.Test;

import static org.junit.Assert.*;
import static myApp.model.DbConfiguration.*;

public class DbConfigurationTest {

    //???
    @Test
    public void configurationTest() {
        assertTrue(getInputDirectory().length() > 0 && getOutputDirectory().length() > 0
                && getUserName().length() > 0 && getUrl().length() > 0);
    }
}
