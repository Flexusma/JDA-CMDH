/*
 * Copyright 2019-2020 Lucas Regh | Flexusma
 * This Project is licensed under
 * MPL-2.0
 * A copy of the complete license can be found in the root folder of this project in a file called License
 */
package de.flexusma.jdacmdh.utils;

import java.lang.reflect.Type;
import java.sql.JDBCType;


public class JAVAToSQLDatatype {

    private static Type type;
    private static final Type[] types = {String.class, Character.class, Integer.class, Boolean.class, Long.class, Double.class, Float.class, int.class, boolean.class, long.class, double.class, float.class};
    private static final JDBCType[] JDBCTypes = {JDBCType.LONGVARCHAR, JDBCType.CHAR, JDBCType.BIGINT, JDBCType.BOOLEAN, JDBCType.FLOAT, JDBCType.FLOAT, JDBCType.FLOAT, JDBCType.BIGINT, JDBCType.BOOLEAN, JDBCType.FLOAT, JDBCType.FLOAT, JDBCType.FLOAT};

    public static JDBCType convertType(Type t) {
        type = t;
        int count = 0;
        for (Type jDatatypes : types) {
            if (check(jDatatypes)) return JDBCTypes[count];
            count++;
        }

        return null;
    }

    private static boolean check(Type c) {
        return type.getTypeName().equalsIgnoreCase(c.getTypeName());
    }

}
