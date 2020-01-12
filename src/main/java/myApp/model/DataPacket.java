package myApp.model;

import myApp.rest.DatabaseRequests;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.*;
import java.util.Objects;

/**
 * this class is for loading data into a db. In buffSize, you can set the packet size
 */

public class DataPacket {
    static {
        setConnectionDb();
    }

    private static Connection connectionDb;
    private static final int buffSize = 50000;
    private static final Logger logger = LoggerFactory.getLogger(DataPacket.class);

    public static Connection getConnectionDb() { return connectionDb; }

    private static void setConnectionDb() {
        try {
            Class.forName("org.h2.Driver");
            connectionDb = DriverManager.getConnection(DbConfiguration.getUrl(),
                    DbConfiguration.getUserName(), DbConfiguration.getPassword());
        }
        catch (Exception ex) {
            throw new Error();
        }
    }

    //download all .csv files from input directory to database
    public static boolean readFilesToDictionary(String schema, String input, String output) throws Exception {
        final File folder = new File(DbConfiguration.getInputDirectory());
        boolean read = false;
        if (DatabaseRequests.checkSchemaNotExists(schema))
            throw new Exception("schema not exists!");
        for (final File file : Objects.requireNonNull(folder.listFiles())) {
            if (FilenameUtils.getExtension(file.getName()).equals("csv")) {
                read = true;
                FileReader fileReader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                bufferedReader.readLine();
                while (sendPacketToDictionary(bufferedReader, schema)) {
                    logger.debug("to next package!");
                }
                logger.debug("file read!");
                bufferedReader.close();
                Files.move(Paths.get(input + "/" + file.getName()),
                           Paths.get(output + "/" + file.getName()),
                           StandardCopyOption.REPLACE_EXISTING);
            } else {
                throw new Exception("Invalid file " + file.getName());
            }
        }
        return read;
    }

    //read BUFFSIZE lines from FD, create insert sql query and executes him
    private static boolean sendPacketToDictionary(BufferedReader fileReader, String schema) throws Exception {
        Statement stmt;
        String buffer;
        int i = 0;
        stmt = connectionDb.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        connectionDb.setAutoCommit(false);
        while (i++ < buffSize && (buffer = fileReader.readLine()) != null) {
            String[] line = buffer.split(",");
            stmt.addBatch(String.format("INSERT INTO %s.T_DICTIONARY(ID, NAME, VALUE) VALUES(%s, '%s', %s)",
                    schema, line[0], line[1], line[2]));
        }
        stmt.executeBatch();
        connectionDb.commit();
        stmt.close();
        return i >= buffSize;
    }
}
