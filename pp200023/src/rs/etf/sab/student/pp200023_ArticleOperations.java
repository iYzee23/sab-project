package rs.etf.sab.student;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.ArticleOperations;

/**
 *
 * @author DjapePC
 */
public class pp200023_ArticleOperations implements ArticleOperations {
    
    static Connection conn = DB.getInstance().getConnection();
    
    @Override
    public int createArticle(int shopId, String articleName, int articlePrice) {
        if (articlePrice < 0) return -1;
        int articleId;
        String query1 = "INSERT INTO Article (Name, Price) VALUES (?, ?)";
        String query2 = "INSERT INTO Catalog (Count, ArticleID, ShopID) VALUES (0, ?, ?)";
        try (PreparedStatement stmt1 = conn.prepareStatement(query1, PreparedStatement.RETURN_GENERATED_KEYS)) {
            conn.setAutoCommit(false);
            stmt1.setString(1, articleName);
            stmt1.setInt(2, articlePrice);
            stmt1.executeUpdate();
            try (ResultSet rs1 = stmt1.getGeneratedKeys()) {
                if (rs1.next()) {
                    articleId = rs1.getInt(1);
                    try (PreparedStatement stmt2 = conn.prepareStatement(query2)) {
                        stmt2.setInt(1, articleId);
                        stmt2.setInt(2, shopId);
                        stmt2.executeUpdate();
                        conn.commit();
                        return articleId;
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(pp200023_ArticleOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            conn.rollback();
        } catch (SQLException ex) {
            Logger.getLogger(pp200023_OrderOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }
    
}
