package rs.etf.sab.student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.CityOperations;

/**
 *
 * @author DjapePC
 */
public class pp200023_CityOperations implements CityOperations {
    
    static Connection conn = DB.getInstance().getConnection();

    @Override
    public int createCity(String name) {
        String query = "INSERT INTO City (Name) VALUES (?)";
        try (PreparedStatement stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, name);
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(pp200023_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    @Override
    public List<Integer> getCities() {
        List<Integer> res = new ArrayList<>();
        String query = "SELECT ID\n" +
            "FROM City";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) res.add(rs.getInt(1));
                if (!res.isEmpty()) return res;
            }
        } catch (SQLException ex) {
            Logger.getLogger(pp200023_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public int connectCities(int cityId1, int cityId2, int distance) {
        String query1 = "SELECT ID\n" +
            "FROM Connection\n" +
            "WHERE (CityID1 = ? and CityID2 = ?) or (CityID1 = ? and CityID2 = ?)";
        String query2 = "INSERT INTO Connection (Distance, CityID1, CityID2) VALUES (?, ?, ?)";
        try (PreparedStatement stmt1 = conn.prepareStatement(query1)) {
            stmt1.setInt(1, cityId1);
            stmt1.setInt(2, cityId2);
            stmt1.setInt(3, cityId2);
            stmt1.setInt(4, cityId1);
            try (ResultSet rs1 = stmt1.executeQuery()) {
                if (!rs1.next()) {
                    try (PreparedStatement stmt2 = conn.prepareStatement(query2, PreparedStatement.RETURN_GENERATED_KEYS)) {
                        stmt2.setInt(1, distance);
                        stmt2.setInt(2, cityId1);
                        stmt2.setInt(3, cityId2);
                        stmt2.executeUpdate();
                        ResultSet rs2 = stmt2.getGeneratedKeys();
                        if (rs2.next()) return rs2.getInt(1);
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(pp200023_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    @Override
    public List<Integer> getConnectedCities(int cityId) {
        List<Integer> res = new ArrayList<>();
        String query1 = "SELECT CityID1\n" +
            "FROM Connection\n" +
            "WHERE CityID2 = ?";
        String query2 = "SELECT CityID2\n" +
            "FROM Connection\n" +
            "WHERE CityID1 = ?";
        try (PreparedStatement stmt1 = conn.prepareStatement(query1)) {
            stmt1.setInt(1, cityId);
            try (ResultSet rs1 = stmt1.executeQuery()) {
                while (rs1.next()) res.add(rs1.getInt(1));
                try (PreparedStatement stmt2 = conn.prepareStatement(query2)) {
                    stmt2.setInt(1, cityId);
                    try (ResultSet rs2 = stmt2.executeQuery()) {
                        while (rs2.next()) res.add(rs2.getInt(1));
                        if (!res.isEmpty()) return res;
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(pp200023_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public List<Integer> getShops(int cityId) {
        List<Integer> res = new ArrayList<>();
        String query = "SELECT M.ID\n" +
            "FROM Member M join Shop S on M.ID = S.ID\n" +
            "WHERE M.CityID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, cityId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) res.add(rs.getInt(1));
                if (!res.isEmpty()) return res;
            }
        } catch (SQLException ex) {
            Logger.getLogger(pp200023_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
}
