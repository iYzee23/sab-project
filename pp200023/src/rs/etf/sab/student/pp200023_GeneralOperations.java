package rs.etf.sab.student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.GeneralOperations;

/**
 *
 * @author DjapePC
 */
public class pp200023_GeneralOperations implements GeneralOperations {
    
    static Connection conn = DB.getInstance().getConnection();
    
    static Calendar calendar = Calendar.getInstance();

    @Override
    public void setInitialTime(Calendar time) {
        calendar.setTime(time.getTime());
    }

    @Override
    public Calendar time(int days) {
        calendar.add(Calendar.DATE, days);
        // TODO
        return calendar;
    }

    @Override
    public Calendar getCurrentTime() {
        return calendar;
    }

    @Override
    public void eraseAll() {
        String query1 = "EXEC sp_MSForEachTable 'DISABLE TRIGGER ALL ON ?'";
        String query2 = "EXEC sp_MSForEachTable 'ALTER TABLE ? NOCHECK CONSTRAINT ALL'";
        String query3 = "EXEC sp_MSForEachTable 'DELETE FROM ?'";
        String query4 = "EXEC sp_MSForEachTable 'ALTER TABLE ? CHECK CONSTRAINT ALL'";
        String query5 = "EXEC sp_MSForEachTable 'ENABLE TRIGGER ALL ON ?'";
        try (PreparedStatement stmt1 = conn.prepareStatement(query1)) {
            stmt1.execute();
            try (PreparedStatement stmt2 = conn.prepareStatement(query2)) {
                stmt2.execute();
                try (PreparedStatement stmt3 = conn.prepareStatement(query3)) {
                    stmt3.execute();
                    try (PreparedStatement stmt4 = conn.prepareStatement(query4)) {
                        stmt4.execute();
                        try (PreparedStatement stmt5 = conn.prepareStatement(query5)) {
                            stmt5.execute();
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(pp200023_GeneralOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
