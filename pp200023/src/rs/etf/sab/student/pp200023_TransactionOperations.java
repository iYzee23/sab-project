package rs.etf.sab.student;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.TransactionOperations;

/**
 *
 * @author DjapePC
 */
public class pp200023_TransactionOperations implements TransactionOperations {
    
    Connection conn = DB.getInstance().getConnection();

    @Override
    public BigDecimal getBuyerTransactionsAmmount(int buyerId) {
        String query = "SELECT COALESCE(SUM(T.Amount), 0)\n" +
            "FROM Transaction T join TransactionBuyer TB on T.ID = TB.ID\n" +
            "WHERE OrderID in (\n" +
            "	SELECT O.ID\n" +
            "	FROM Order O\n" +
            "	WHERE O.BuyerID = ?\n" +
            ")";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, buyerId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getBigDecimal(1).setScale(3);
            }
        } catch (SQLException ex) {
            Logger.getLogger(pp200023_TransactionOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new BigDecimal(-1).setScale(3);
    }

    @Override
    public BigDecimal getShopTransactionsAmmount(int shopId) {
        String query = "SELECT COALESCE(SUM(T.Amount), 0)\n" +
            "FROM Transaction T join TransactionShop TS on T.ID = TS.ID\n" +
            "WHERE TS.ShopID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, shopId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getBigDecimal(1).setScale(3);
            }
        } catch (SQLException ex) {
            Logger.getLogger(pp200023_TransactionOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new BigDecimal(-1).setScale(3);
    }

    @Override
    public List<Integer> getTransationsForBuyer(int buyerId) {
        List<Integer> res = new ArrayList<>();
        String query = "SELECT T.ID\n" +
            "FROM Transaction T join TransactionBuyer TB on T.ID = TB.ID\n" +
            "WHERE OrderID in (\n" +
            "	SELECT O.ID\n" +
            "	FROM Order O\n" +
            "	WHERE O.BuyerID = ?\n" +
            ")";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, buyerId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) res.add(rs.getInt(1));
                return res;
            }
        } catch (SQLException ex) {
            Logger.getLogger(pp200023_TransactionOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public int getTransactionForBuyersOrder(int orderId) {
        String query = "SELECT T.ID\n" +
            "FROM Transaction T join TransactionBuyer TB on T.ID = TB.ID\n" +
            "WHERE OrderID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, orderId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(pp200023_TransactionOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    @Override
    public int getTransactionForShopAndOrder(int orderId, int shopId) {
        String query = "SELECT T.ID\n" +
            "FROM Transaction T join TransactionShop TS on T.ID = TS.ID\n" +
            "WHERE TS.ShopID = ? and T.OrderID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, shopId);
            stmt.setInt(2, orderId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(pp200023_TransactionOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    @Override
    public List<Integer> getTransationsForShop(int shopId) {
        List<Integer> res = new ArrayList<>();
        String query = "SELECT ID\n" +
            "FROM TransactionShop\n" +
            "WHERE ShopID = ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, shopId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) res.add(rs.getInt(1));
                return res;
            }
        } catch (SQLException ex) {
            Logger.getLogger(pp200023_TransactionOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Calendar getTimeOfExecution(int transactionId) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        String query1 = "SELECT O.ReceivedTime\n" +
            "FROM Order O join Transaction T on O.ID = T.OrderID\n" +
            "WHERE T.ID = ?";
        String query2 = "SELECT ExecutionTime\n" +
            "FROM Transaction\n" +
            "WHERE ID = ?";
        try (PreparedStatement stmt1 = conn.prepareStatement(query1)) {
            stmt1.setInt(1, transactionId);
            try (ResultSet rs1 = stmt1.executeQuery()) {
                if (rs1.next()) {
                    cal1.setTime(rs1.getTimestamp(1));
                    try (PreparedStatement stmt2 = conn.prepareStatement(query2)) {
                        stmt2.setInt(1, transactionId);
                        try (ResultSet rs2 = stmt2.executeQuery()) {
                            if (rs2.next()) {
                                cal2.setTime(rs2.getTimestamp(1));
                                if (cal1.compareTo(cal2) == 0) return cal2;
                            }
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(pp200023_TransactionOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public BigDecimal getAmmountThatBuyerPayedForOrder(int orderId) {
        String query = "SELECT Price\n" +
            "FROM Order\n" +
            "WHERE ID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, orderId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getBigDecimal(1).setScale(3);
            }
        } catch (SQLException ex) {
            Logger.getLogger(pp200023_TransactionOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new BigDecimal(-1).setScale(3);
    }

    @Override
    public BigDecimal getAmmountThatShopRecievedForOrder(int shopId, int orderId) {
        String query = "SELECT T.Amount\n" +
            "FROM Transaction T join TransactionShop TS on T.ID = TS.ID\n" +
            "WHERE TS.ShopID = ? and T.OrderID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, shopId);
            stmt.setInt(2, orderId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getBigDecimal(1).setScale(3);
            }
        } catch (SQLException ex) {
            Logger.getLogger(pp200023_TransactionOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new BigDecimal(-1).setScale(3);
    }

    @Override
    public BigDecimal getTransactionAmount(int transactionId) {
        String query = "SELECT Amount\n" +
            "FROM Transaction\n" +
            "WHERE ID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, transactionId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getBigDecimal(1).setScale(3);
            }
        } catch (SQLException ex) {
            Logger.getLogger(pp200023_TransactionOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new BigDecimal(-1).setScale(3);
    }

    @Override
    public BigDecimal getSystemProfit() {
        String query = "SELECT COALESCE(SUM(Price * (5 - Discount) / 100.0), 0)\n" +
            "FROM Order\n" +
            "WHERE Status = 'arrived'";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getBigDecimal(1).setScale(3);
            }
        } catch (SQLException ex) {
            Logger.getLogger(pp200023_TransactionOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new BigDecimal(-1).setScale(3);
    }
    
}
