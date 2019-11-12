package myApp.rest;

import myApp.model.DataPacket;
import org.junit.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *  in BeforeClass create TEST schema and T_DICTIONARY table.
 *  in AfterClass delete TEST schema
 *  during all tests the table T_DICTIONARY exists!
 */
public class DatabaseRequestsTest {
    private static Statement stmt;

    private static boolean checkDictionaryExists() throws SQLException {
        return stmt.executeQuery("SELECT 1 FROM INFORMATION_SCHEMA.TABLES " +
                "where TABLE_TYPE='TABLE' and TABLE_NAME='T_DICTIONARY' and TABLE_SCHEMA='TEST'").next();
    }

    //initialise table and schema
    @BeforeClass
    public static void initializeDatabase() throws SQLException {
        Connection connectionDb = DataPacket.getConnectionDb();
        stmt = connectionDb.createStatement();
        stmt.execute("create schema if not exists TEST");
        stmt.execute("create table if not exists TEST.T_DICTIONARY\n" +
                "(\n" +
                "    ID    INTEGER not null,\n" +
                "    NAME  VARCHAR(255),\n" +
                "    VALUE DECIMAL(15, 2),\n" +
                "    constraint TEST.T_DICTIONARY_PK\n" +
                "        primary key (ID)\n" +
                ");\n" +
                "create unique index if not exists TEST.T_DICTIONARY_ID_UINDEX\n" +
                "    on TEST.T_DICTIONARY (ID);");
    }

    @Test
    public void testCreateTableDictionary() throws Exception {
        stmt.execute("drop table if exists TEST.T_DICTIONARY");
        DatabaseRequests.createSchemaAndTableDictionary("test");
        Assert.assertTrue(checkDictionaryExists());
    }

    //insert 1 row and trunc after. Check null
    @Test
    public void testTruncTable() throws Exception {
        stmt.execute("INSERT INTO TEST.T_DICTIONARY(ID, NAME, VALUE) VALUES(0, 'sss', 1.0)");
        DatabaseRequests.truncTable("TEST.T_DICTIONARY");
        Assert.assertFalse(stmt.executeQuery("select 1 from TEST.T_DICTIONARY limit 1").next());
        stmt.execute("truncate table TEST.T_DICTIONARY");
    }

    //check exists t_dictionary and not exists fail tables
    @Test
    public void testCheckTableNotExists() throws SQLException {
        boolean result;
        result = !DatabaseRequests.checkTableNotExists("TEST.T_DICTIONARY");
        result &= DatabaseRequests.checkTableNotExists("nope.T_DICTIONARY");
        result &= DatabaseRequests.checkTableNotExists("TEST.nope");
        result &= DatabaseRequests.checkTableNotExists("nope.nope");
        Assert.assertTrue(result);
    }

    @AfterClass
    public static void clearDatabase() throws SQLException {
        if (checkDictionaryExists())
            stmt.execute("truncate table TEST.T_DICTIONARY");
        stmt.execute("drop table if exists TEST.T_DICTIONARY");
        stmt.execute("drop schema if exists TEST");
        stmt.close();
    }
}
