package rs.etf.sab.student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.ShopOperations;

/**
 *
 * @author DjapePC
 */
public class pp200023_ShopOperations implements ShopOperations {
    
    static Connection conn = DB.getInstance().getConnection();

    @Override
    public int createShop(String name, String cityName) {
        int cityId, shopId;
        String query1 = "SELECT ID\n" +
            "FROM City\n" +
            "WHERE Name = ?";
        String query2 = "INSERT INTO Member (CityID) VALUES (?)";
        String query3 = "INSERT INTO Shop (ID, Name) VALUES (?, ?)";
        try (PreparedStatement stmt1 = conn.prepareStatement(query1)) {
            stmt1.setString(1, cityName);
            try (ResultSet rs1 = stmt1.executeQuery()) {
                if (rs1.next()) {
                    cityId = rs1.getInt(1);
                    try (PreparedStatement stmt2 = conn.prepareStatement(query2, PreparedStatement.RETURN_GENERATED_KEYS)) {
                        stmt2.setInt(1, cityId);
                        stmt2.executeUpdate();
                        try (ResultSet rs2 = stmt2.getGeneratedKeys()) {
                            if (rs2.next()) {
                                shopId = rs2.getInt(1);
                                try (PreparedStatement stmt3 = conn.prepareStatement(query3)) {
                                    stmt3.setInt(1, shopId);
                                    stmt3.setString(2, name);
                                    stmt3.executeUpdate();
                                    return shopId;
                                }
                            }
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(pp200023_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    boolean checkShop(int shopId) {
        String query = "SELECT ID\n" + 
            "FROM Shop\n" + 
            "WHERE ID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, shopId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(pp200023_OrderOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    @Override
    public int setCity(int shopId, String cityName) {
        if (!checkShop(shopId)) return -1;
        int cityId;
        String query1 = "SELECT ID\n" +
            "FROM City\n" +
            "WHERE Name = ?";
        String query2 = "UPDATE Member\n" +
            "SET CityID = ?\n" +
            "WHERE ID = ?";
        try (PreparedStatement stmt1 = conn.prepareStatement(query1)) {
            stmt1.setString(1, cityName);
            try (ResultSet rs1 = stmt1.executeQuery()) {
                if (rs1.next()) {
                    cityId = rs1.getInt(1);
                    try (PreparedStatement stmt2 = conn.prepareStatement(query2)) {
                        stmt2.setInt(1, cityId);
                        stmt2.setInt(2, shopId);
                        int rows = stmt2.executeUpdate();
                        if (rows > 0) return 1;
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(pp200023_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    @Override
    public int getCity(int shopId) {
        if (!checkShop(shopId)) return -1;
        String query = "SELECT CityID\n" +
            "FROM Member\n" +
            "WHERE ID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, shopId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(pp200023_BuyerOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    @Override
    public int setDiscount(int shopId, int discountPercentage) {
        if (discountPercentage < 0 || discountPercentage > 100) return -1;
        if (!checkShop(shopId)) return -1;
        String query = "UPDATE Shop\n" +
            "SET Discount = ?\n" +
            "WHERE ID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, discountPercentage);
            stmt.setInt(2, shopId);
            stmt.executeUpdate();
            return 1;
        } catch (SQLException ex) {
            Logger.getLogger(pp200023_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    @Override
    public int increaseArticleCount(int articleId, int increment) {
        if (increment < 0) return -1;
        String query1 = "UPDATE Catalog\n" +
            "SET Count = Count + ?\n" +
            "WHERE ArticleID = ?";
        String query2 = "SELECT Count\n" +
            "FROM Catalog\n" +
            "WHERE ArticleID = ?";
        try (PreparedStatement stmt1 = conn.prepareStatement(query1)) {
            stmt1.setInt(1, increment);
            stmt1.setInt(2, articleId);
            int rows = stmt1.executeUpdate();
            if (rows > 0) {
                try (PreparedStatement stmt2 = conn.prepareStatement(query2)) {
                    stmt2.setInt(1, articleId);
                    try (ResultSet rs2 = stmt2.executeQuery()) {
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
    public int getArticleCount(int articleId) {
        String query = "SELECT Count\n" +
            "FROM Catalog\n" +
            "WHERE ArticleID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, articleId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(pp200023_BuyerOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    @Override
    public List<Integer> getArticles(int shopId) {
        List<Integer> res = new ArrayList<>();
        String query = "SELECT ArticleID\n" +
            "FROM Catalog\n" +
            "WHERE ShopID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, shopId);
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
    public int getDiscount(int shopId) {
        if (!checkShop(shopId)) return -1;
        String query = "SELECT Discount\n" +
            "FROM Shop\n" +
            "WHERE ID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, shopId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(pp200023_BuyerOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }
    
}
