package rs.etf.sab.student;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.BuyerOperations;

/**
 *
 * @author DjapePC
 */
public class pp200023_BuyerOperations implements BuyerOperations {
    
    static Connection conn = DB.getInstance().getConnection();

    @Override
    public int createBuyer(String name, int cityId) {
        int buyerId;
        String query1 = "INSERT INTO Member (CityID) VALUES (?)";
        String query2 = "INSERT INTO Buyer (ID, Name, Credit) VALUES (?, ?, 0)";
        try (PreparedStatement stmt1 = conn.prepareStatement(query1, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt1.setInt(1, cityId);
            stmt1.executeUpdate();
            try (ResultSet rs1 = stmt1.getGeneratedKeys()) {
                if (rs1.next()) {
                    buyerId = rs1.getInt(1);
                    try (PreparedStatement stmt2 = conn.prepareStatement(query2)) {
                        stmt2.setInt(1, buyerId);
                        stmt2.setString(2, name);
                        stmt2.executeUpdate();
                        return buyerId;
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(pp200023_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    @Override
    public int setCity(int buyerId, int cityId) {
        String query = "UPDATE Member\n" +
            "SET CityID = ?\n" +
            "WHERE ID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, cityId);
            stmt.setInt(2, buyerId);
            stmt.executeUpdate();
            return 1;
        } catch (SQLException ex) {
            Logger.getLogger(pp200023_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    @Override
    public int getCity(int buyerId) {
        String query = "SELECT CityID\n" +
            "FROM Member\n" +
            "WHERE ID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, buyerId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(pp200023_BuyerOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    @Override
    public BigDecimal increaseCredit(int buyerId, BigDecimal credit) {
        String query1 = "UPDATE Buyer\n" +
            "SET Credit = Credit + ?\n" +
            "WHERE ID = ?";
        String query2 = "SELECT Credit\n" +
            "FROM Buyer\n" +
            "WHERE ID = ?";
        try (PreparedStatement stmt1 = conn.prepareStatement(query1)) {
            stmt1.setBigDecimal(1, credit);
            stmt1.setInt(2, buyerId);
            stmt1.executeUpdate();
            try (PreparedStatement stmt2 = conn.prepareStatement(query2)) {
                stmt2.setInt(1, buyerId);
                try (ResultSet rs2 = stmt2.executeQuery()) {
                    if (rs2.next()) return rs2.getBigDecimal(1).setScale(3);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(pp200023_BuyerOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new BigDecimal(-1).setScale(3);
    }

    @Override
    public int createOrder(int buyerId) {
        String query = "INSERT INTO [Order] (BuyerID) VALUES (?)";
        try (PreparedStatement stmt = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, buyerId);
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
    public List<Integer> getOrders(int buyerId) {
        List<Integer> res = new ArrayList<>();
        String query = "SELECT ID\n" +
            "FROM [Order]\n" +
            "WHERE BuyerID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, buyerId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) res.add(rs.getInt(1));
                return res;
            }
        } catch (SQLException ex) {
            Logger.getLogger(pp200023_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public BigDecimal getCredit(int buyerId) {
        String query = "SELECT Credit\n" +
            "FROM Buyer\n" +
            "WHERE ID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, buyerId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getBigDecimal(1).setScale(3);
            }
        } catch (SQLException ex) {
            Logger.getLogger(pp200023_BuyerOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new BigDecimal(-1).setScale(3);
    }
    
}
