package myApp.model;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class DataPacketTest {

    @Test
    public void getConnectionDbTest() {
        assertNotNull(DataPacket.getConnectionDb());
    }
}
