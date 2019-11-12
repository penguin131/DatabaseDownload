package myApp.rest;

import myApp.model.DataPacket;
import myApp.model.DictionaryEntity;
import myApp.utils.HibernateUtil;
import org.hibernate.Session;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.regex.Pattern;

//class with SELECT and DELETE query!
public class DatabaseRequests {
    public static void createSchemaAndTableDictionary(String schema) throws Exception {
        if (checkTableNotExists(schema + ".T_DICTIONARY")) {
            schema = schema.toUpperCase();
            if (!Pattern.matches("^[A-Z_0-9]+$", schema))
                throw new Exception("Invalid schema name!");
            Connection connectionDb = DataPacket.getConnectionDb();
            Statement stmt = connectionDb.createStatement();
            stmt.execute("create schema if not exists " + schema);
            stmt.execute(String.format("create table %s.T_DICTIONARY\n" +
                    "(\n" +
                    "    ID    INTEGER not null,\n" +
                    "    NAME  VARCHAR(255),\n" +
                    "    VALUE DECIMAL(15, 2),\n" +
                    "    constraint T_DICTIONARY_PK\n" +
                    "        primary key (ID)\n" +
                    ");\n" +
                    "create unique index %s.T_DICTIONARY_ID_UINDEX\n" +
                    "    on %s.T_DICTIONARY (ID);", schema, schema, schema));
            stmt.close();
        }
    }

    public static boolean checkSchemaNotExists(String schema) throws SQLException {
        Connection connectionDb = DataPacket.getConnectionDb();
        Statement stmt = connectionDb.createStatement();
        ResultSet names = stmt.executeQuery("SELECT SCHEMA_NAME FROM INFORMATION_SCHEMA.SCHEMATA");
        while (names.next()) {
            if (names.getString("SCHEMA_NAME").equals(schema)) {
                stmt.close();
                return false;
            }
        }
        stmt.close();
        return true;
    }

    static boolean checkTableNotExists(String tableSchemaName) throws SQLException {
        Connection connectionDb = DataPacket.getConnectionDb();
        Statement stmt = connectionDb.createStatement();
        ResultSet names = stmt.executeQuery("SELECT TABLE_SCHEMA, TABLE_NAME " +
                "FROM INFORMATION_SCHEMA.TABLES where TABLE_TYPE = 'TABLE'");
        while (names.next()) {
            if (String.format("%s.%s", names.getString("TABLE_SCHEMA"),
                    names.getString("TABLE_NAME")).equals(tableSchemaName)) {
                stmt.close();
                return false;
            }
        }
        stmt.close();
        return true;
    }

    public static List getTableData(String tableSchemaName) throws Exception {
        if (checkTableNotExists(tableSchemaName))
            throw new Exception(String.format("table %s is not exist!", tableSchemaName));
        final Session session = HibernateUtil.getHibernateSession();
        session.beginTransaction();
        List data = session.createSQLQuery(String.format("select * from %s", tableSchemaName))
                .addEntity(DictionaryEntity.class)
                .list();
        session.close();
        return data;
    }

    public static void truncTable(String tableSchemaName) throws Exception {
        if (checkTableNotExists(tableSchemaName))
            throw new Exception(String.format("table %s is not exist!", tableSchemaName));
        Connection connectionDb = DataPacket.getConnectionDb();
        Statement stmt = connectionDb.createStatement();
        stmt.execute(String.format("truncate table %s", tableSchemaName));
        stmt.close();
    }
}
