package de.flexusma.jdacmdh.utils;

import de.flexusma.jdacmdh.debug.LogType;
import de.flexusma.jdacmdh.debug.Logger;

import java.lang.reflect.Type;
import java.sql.JDBCType;


public class JAVAToSQLDatatype {

    private static Type type;
    private static final Type[] types = {String.class, Character.class,Integer.class,Boolean.class,Long.class,Double.class,Float.class,int.class,boolean.class,long.class,double.class,float.class};
    private static final JDBCType[] JDBCTypes = {JDBCType.LONGVARCHAR,JDBCType.CHAR, JDBCType.BIGINT,JDBCType.BOOLEAN,JDBCType.FLOAT,JDBCType.FLOAT,JDBCType.FLOAT,JDBCType.BIGINT,JDBCType.BOOLEAN,JDBCType.FLOAT,JDBCType.FLOAT,JDBCType.FLOAT};
    public static JDBCType convertType(Type t){
        type=t;
        int count=0;
        for(Type jDatatypes: types){
            if(check(jDatatypes)) return JDBCTypes[count];
            count++;
        }

        return null;

    }


    private static boolean check(Type c){
        return type.getTypeName().equals(c.getTypeName());
    }

}
