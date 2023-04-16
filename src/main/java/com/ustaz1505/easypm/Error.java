package com.ustaz1505.easypm;

import java.util.logging.Level;

public class Error {
    public static void execute(EasyPM epm, Exception ex){
        epm.getLogger().log(Level.SEVERE, "Couldn't execute MySQL statement: ", ex);
    }
    public static void close(EasyPM epm, Exception ex){
        epm.getLogger().log(Level.SEVERE, "Failed to close MySQL connection: ", ex);
    }
}
 