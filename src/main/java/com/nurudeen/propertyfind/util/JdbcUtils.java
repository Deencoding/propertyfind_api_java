package com.nurudeen.propertyfind.util;


import java.sql.Array;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class JdbcUtils {

    /**
     * Converts a PostgreSQL Array column to a List<String>.
     * Returns an empty list if the array is null.
     */
    public static List<String> pgArrayToList(Array array) throws SQLException {
        if (array == null) {
            return Collections.emptyList();
        }
        return Arrays.asList((String[]) array.getArray());
    }
}

