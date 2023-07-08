package rs.etf.sab.student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
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
    
    static Map<Integer, Integer> mDaysToAssamble = pp200023_OrderOperations.mDaysToAssamble;
    static Map<Integer, List<Integer>> mPath = pp200023_OrderOperations.mPath;
    static Map<Integer, Integer> mDaysToNextCity = pp200023_OrderOperations.mDaysToNextCity;

    @Override
    public void setInitialTime(Calendar time) {
        calendar.setTime(time.getTime());
    }
    
    // go through mDaysToAssamble and subtract days
    // if remaining days are over 0, just update mDaysToAssamble (assemble city still not reached)
    // if remaining days are under or equal to 0, perform logics (since order reached assemble city)
    // update assemble field for order
    // get path for order
    // if size is 1, that means order has reached it's final city (assemble city is buyer's city)
    // update order's received time and status (this will activate the trigger)
    // remove order from mPath since it is finished
    // if size is over 1, find next distance and update mPath
    // update mDaysToNextCity correspondingly and properly
    // anyways, order has reached it's assemble city and needs to be removed from mDaysToAssamble
    void updateMDaysToAssamble(int days) throws SQLException {
        String query1 = "UPDATE [Order]\n" +
            "SET Status = 'arrived'\n" +
            "WHERE ID = ?";
        String query5 = "UPDATE [Order]\n" +
            "SET ReceivedTime = ?\n" +
            "WHERE ID = ?";
        String query2 = "UPDATE [Order]\n" +
            "SET Assembled = 1\n" +
            "WHERE ID = ?";
        String query4 = "SELECT Distance\n" +
            "FROM Connection\n" +
            "WHERE (CityID1 = ? and CityID2 = ?) or (CityID1 = ? and CityID2 = ?)";
        List<Integer> aToRemove = new ArrayList<>();
        for (Integer aOrderId: mDaysToAssamble.keySet()) {
            int aDays = mDaysToAssamble.get(aOrderId);
            aDays -= days;
            if (aDays > 0) {
                mDaysToAssamble.put(aOrderId, aDays);
            }
            else {
                try (PreparedStatement stmt2 = conn.prepareStatement(query2)) {
                    stmt2.setInt(1, aOrderId);
                    stmt2.executeUpdate();
                    List<Integer> path = mPath.get(aOrderId);
                    if (path.size() == 1) {
                        Calendar cal = (Calendar) calendar.clone();
                        cal.add(Calendar.DATE, days + aDays);
                        try (PreparedStatement stmt5 = conn.prepareStatement(query5)) {
                            stmt5.setTimestamp(1, new Timestamp(cal.getTimeInMillis()));
                            stmt5.setInt(2, aOrderId);
                            stmt5.executeUpdate();
                            try (PreparedStatement stmt1 = conn.prepareStatement(query1)) {
                                stmt1.setTimestamp(1, new Timestamp(cal.getTimeInMillis()));
                                stmt1.setInt(2, aOrderId);
                                stmt1.executeUpdate();
                                mPath.remove(aOrderId);
                            }
                        }
                    }
                    else {
                        int city1 = path.get(0);
                        int city2 = path.get(1);
                        path.remove(0);
                        mPath.put(aOrderId, path);
                        try (PreparedStatement stmt4 = conn.prepareStatement(query4)) {
                            stmt4.setInt(1, city1 + 1);
                            stmt4.setInt(2, city2 + 1);
                            stmt4.setInt(3, city2 + 1);
                            stmt4.setInt(4, city1 + 1);
                            try (ResultSet rs4 = stmt4.executeQuery()) {
                                if (rs4.next()) {
                                    int distance = rs4.getInt(1);
                                    mDaysToNextCity.put(aOrderId, distance + days + aDays);
                                }
                            }
                        }
                    }
                    aToRemove.add(aOrderId);
                }
            }
        }
        for (Integer aOrderId: aToRemove) {
            mDaysToAssamble.remove(aOrderId);
        }
    }
    
    // go through mDaysToNextCity and subtract days
    // if remaining days are over 0, just update mDaysToNextCity (next city still not reached)
    // if remaining days are under or equal to 0, perform logics (since next city is reached)
    // update current city for order
    // get path for order
    // if size is 1, that means order has reached it's final city
    // update order's received time and status (this will activate the trigger)
    // remove order from mPath since it is finished
    // remove order from mDaysToNextCity since there is no next city
    // if size is over 1, find next distance and update mPath
    // update mDaysToNextCity correspondingly and properly
    // since time that passed can be enough to go over more than 1 city, do this in while loop
    void updateMDaysToNextCity(int days) throws SQLException {
        String query1 = "UPDATE [Order]\n" +
            "SET Status = 'arrived'\n" +
            "WHERE ID = ?";
        String query5 = "UPDATE [Order]\n" +
            "SET ReceivedTime = ?\n" +
            "WHERE ID = ?";
        String query3 = "UPDATE [Order]\n" +
            "SET CityID = ?\n" +
            "WHERE ID = ?";
        String query4 = "SELECT Distance\n" +
            "FROM Connection\n" +
            "WHERE (CityID1 = ? and CityID2 = ?) or (CityID1 = ? and CityID2 = ?)";
        List<Integer> cToRemove = new ArrayList<>();
        for (Integer cOrderId: mDaysToNextCity.keySet()) {
            while (true) {
                int cDays = mDaysToNextCity.get(cOrderId);
                cDays -= days;
                if (cDays > 0) {
                    mDaysToNextCity.put(cOrderId, cDays);
                    break;
                }
                else {
                    List<Integer> path = mPath.get(cOrderId);
                    try (PreparedStatement stmt3 = conn.prepareStatement(query3)) {
                        stmt3.setInt(1, path.get(0) + 1);
                        stmt3.setInt(2, cOrderId);
                        stmt3.executeUpdate();
                        if (path.size() == 1) {
                            Calendar cal = (Calendar) calendar.clone();
                            cal.add(Calendar.DATE, days + cDays);
                            try (PreparedStatement stmt5 = conn.prepareStatement(query5)) {
                                stmt5.setTimestamp(1, new Timestamp(cal.getTimeInMillis()));
                                stmt5.setInt(2, cOrderId);
                                stmt5.executeUpdate();
                                try (PreparedStatement stmt1 = conn.prepareStatement(query1)) {
                                    stmt1.setInt(1, cOrderId);
                                    stmt1.executeUpdate();
                                    mPath.remove(cOrderId);
                                    cToRemove.add(cOrderId);
                                    break;
                                }
                            }
                        }
                        else {
                            int city1 = path.get(0);
                            int city2 = path.get(1);
                            path.remove(0);
                            mPath.put(cOrderId, path);
                            try (PreparedStatement stmt4 = conn.prepareStatement(query4)) {
                                stmt4.setInt(1, city1 + 1);
                                stmt4.setInt(2, city2 + 1);
                                stmt4.setInt(3, city2 + 1);
                                stmt4.setInt(4, city1 + 1);
                                try (ResultSet rs4 = stmt4.executeQuery()) {
                                    if (rs4.next()) {
                                        int distance = rs4.getInt(1);
                                        mDaysToNextCity.put(cOrderId, distance + days + cDays);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        for (Integer cOrderId: cToRemove) {
            mDaysToNextCity.remove(cOrderId);
        }
    }

    @Override
    public Calendar time(int days) {
        try {
            conn.setAutoCommit(false);
            updateMDaysToAssamble(days);
            updateMDaysToNextCity(days);
            calendar.add(Calendar.DATE, days);
            conn.commit();
            return calendar;
        } catch (SQLException ex) {
            Logger.getLogger(pp200023_GeneralOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            conn.rollback();
        } catch (SQLException ex) {
            Logger.getLogger(pp200023_OrderOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
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

                        // Reset identity values for tables with identity columns
                        Statement resetIdentityStmt = conn.createStatement();
                        ResultSet tablesResultSet = conn.getMetaData().getTables(null, null, null, new String[]{"TABLE"});
                        while (tablesResultSet.next()) {
                            String tableName = tablesResultSet.getString("TABLE_NAME");
                            ResultSet columnsResultSet = conn.getMetaData().getColumns(null, null, tableName, null);
                            boolean hasIdentityColumn = false;
                            while (columnsResultSet.next()) {
                                if (columnsResultSet.getString("IS_AUTOINCREMENT").equals("YES")) {
                                    hasIdentityColumn = true;
                                    break;
                                }
                            }
                            if (hasIdentityColumn) {
                                String resetIdentityQuery = "DBCC CHECKIDENT ('[" + tableName + "]', RESEED, 0)";
                                resetIdentityStmt.execute(resetIdentityQuery);
                            }
                        }

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
