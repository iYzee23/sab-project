package rs.etf.sab.student;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.sql.Types;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.OrderOperations;

class Graph {
    private int V;
    private List<List<iPair>> adj;

    Graph(int V) {
        this.V = V;
        adj = new ArrayList<>();
        for (int i = 0; i < V; i++) {
            adj.add(new ArrayList<>());
        }
    }

    void addEdge(int u, int v, int w) {
        adj.get(u).add(new iPair(v, w));
        adj.get(v).add(new iPair(u, w));
    }

    int[] shortestPath(int src) {
        PriorityQueue<iPair> pq = new PriorityQueue<>(V, Comparator.comparingInt(o -> o.first));
        int[] dist = new int[V];
        Arrays.fill(dist, Integer.MAX_VALUE);

        pq.add(new iPair(0, src));
        dist[src] = 0;

        while (!pq.isEmpty()) {
            int u = pq.poll().second;

            for (iPair v : adj.get(u)) {
                if (dist[v.first] > dist[u] + v.second) {
                    dist[v.first] = dist[u] + v.second;
                    pq.add(new iPair(dist[v.first], v.first));
                }
            }
        }

        return dist;
    }
    
    List<Integer> getPath(int src, int dst, int[] dist) {
        List<Integer> path = new ArrayList<>();
        int current = dst;

        while (current != src) {
            path.add(current);
            for (iPair neighbor : adj.get(current)) {
                if (dist[current] == dist[neighbor.first] + neighbor.second) {
                    current = neighbor.first;
                    break;
                }
            }
        }

        path.add(src);
        Collections.reverse(path);

        return path;
    }

    static class iPair {
        int first, second;

        iPair(int first, int second) {
            this.first = first;
            this.second = second;
        }
    }
}


/**
 *
 * @author DjapePC
 */
public class pp200023_OrderOperations implements OrderOperations {
    
    Connection conn = DB.getInstance().getConnection();

    @Override
    public int addArticle(int orderId, int articleId, int count) {
        int dbCount, itemId = -1;
        double price;
        String query1 = "SELECT Count\n" +
            "FROM Catalog\n" +
            "WHERE ID = ?";
        String query2 = "SELECT Price\n" +
            "FROM Article\n" +
            "WHERE ID = ?";
        String query3 = "SELECT ID\n" +
            "FROM Item\n" +
            "WHERE ArticleID = ? and OrderID = ?";
        String query4 = "UPDATE Item\n" +
            "SET Count = Count + ?,\n" +
            "	 Price = Price + ?\n" +
            "WHERE ArticleID = ?";
        String query5 = "INSERT INTO Item (Count, Price, ArticleID, OrderID)\n" +
            "VALUES (?, ?, ?, ?)";
        String query6 = "UPDATE Catalog\n" +
            "SET Count = Count - ?\n" +
            "WHERE ArticleID = ?";
        try (PreparedStatement stmt1 = conn.prepareStatement(query1)) {
            stmt1.setInt(1, articleId);
            try (ResultSet rs1 = stmt1.executeQuery()) {
                if (rs1.next()) {
                    dbCount = rs1.getInt(1);
                    if (dbCount >= count) {
                        try (PreparedStatement stmt2 = conn.prepareStatement(query2)) {
                            stmt2.setInt(1, articleId);
                            try (ResultSet rs2 = stmt2.executeQuery()) {
                                if (rs2.next()) {
                                    price = rs2.getDouble(1);
                                    try (PreparedStatement stmt3 = conn.prepareStatement(query3)) {
                                        stmt3.setInt(1, articleId);
                                        stmt3.setInt(2, orderId);
                                        try (ResultSet rs3 = stmt3.executeQuery()) {
                                            if (rs3.next()) {
                                                itemId = rs3.getInt(1);
                                                try (PreparedStatement stmt4 = conn.prepareStatement(query4)) {
                                                    stmt4.setInt(1, count);
                                                    stmt4.setDouble(2, count * price);
                                                    stmt4.setInt(3, articleId);
                                                    stmt4.executeUpdate();
                                                }
                                            }
                                            else {
                                                try (PreparedStatement stmt5 = conn.prepareStatement(query5, PreparedStatement.RETURN_GENERATED_KEYS)) {
                                                    stmt5.setInt(1, count);
                                                    stmt5.setDouble(2, count * price);
                                                    stmt5.setInt(3, articleId);
                                                    stmt5.setInt(4, orderId);
                                                    stmt5.executeUpdate();
                                                    try (ResultSet rs5 = stmt5.getGeneratedKeys()) {
                                                        if (rs5.next()) itemId = rs5.getInt(1);
                                                    }
                                                }
                                            }
                                            try (PreparedStatement stmt6 = conn.prepareStatement(query6)) {
                                                stmt6.setInt(1, count);
                                                stmt6.setInt(2, articleId);
                                                stmt6.executeUpdate();
                                                return itemId;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(pp200023_OrderOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    @Override
    public int removeArticle(int orderId, int articleId) {
        int cnt;
        String query1 = "SELECT Count\n" +
            "FROM Item\n" +
            "WHERE ArticleID = ? and OrderID = ?";
        String query2 = "DELETE FROM Item\n" +
            "WHERE ArticleID = ? and OrderID = ?";
        String query3 = "UPDATE Catalog\n" +
            "SET Count = Count + ?\n" +
            "WHERE ArticleID = ?";
        try (PreparedStatement stmt1 = conn.prepareStatement(query1)) {
            stmt1.setInt(1, articleId);
            stmt1.setInt(2, orderId);
            try (ResultSet rs1 = stmt1.executeQuery()) {
                if (rs1.next()) {
                    cnt = rs1.getInt(1);
                    try (PreparedStatement stmt2 = conn.prepareStatement(query2)) {
                        stmt2.setInt(1, articleId);
                        stmt2.setInt(2, orderId);
                        stmt2.executeUpdate();
                        try (PreparedStatement stmt3 = conn.prepareStatement(query3)) {
                            stmt3.setInt(1, cnt);
                            stmt3.setInt(1, articleId);
                            stmt3.executeUpdate();
                            return 1;
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(pp200023_OrderOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    @Override
    public List<Integer> getItems(int orderId) {
        List<Integer> res = new ArrayList<>();
        String query = "SELECT ID\n" +
            "FROM Item\n" +
            "WHERE OrderID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, orderId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) res.add(rs.getInt(1));
                return res;
            }
        } catch (SQLException ex) {
            Logger.getLogger(pp200023_OrderOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public int completeOrder(int orderId) {
        // TODO
        return -1;
    }

    @Override
    public BigDecimal getFinalPrice(int orderId) {
        String query = "{ CALL dbo.SP_FINAL_PRICE(?, ?) }";
        try (CallableStatement stmt = conn.prepareCall(query)) {
            stmt.setInt(1, orderId);
            stmt.registerOutParameter(2, Types.DECIMAL);
            stmt.execute();
            return stmt.getBigDecimal(2).setScale(3);
        } catch (SQLException ex) {
            Logger.getLogger(pp200023_OrderOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new BigDecimal(-1).setScale(3);
    }

    @Override
    public BigDecimal getDiscountSum(int orderId) {
        String status;
        String query1 = "SELECT Status\n" +
            "FROM [Order]\n" +
            "WHERE ID = ?";
        String query2 = "SELECT COALESCE(SUM(Price * Discount / 100.0), 0)\n" +
            "FROM Item\n" +
            "WHERE OrderId = ?";
        try (PreparedStatement stmt1 = conn.prepareStatement(query1)) {
            stmt1.setInt(1, orderId);
            try (ResultSet rs1 = stmt1.executeQuery()) {
                if (rs1.next()) {
                    status = rs1.getString(1);
                    if (!status.equals("created")) {
                        try (PreparedStatement stmt2 = conn.prepareStatement(query2)) {
                            stmt2.setInt(1, orderId);
                            try (ResultSet rs2 = stmt2.executeQuery()) {
                                if (rs2.next()) return rs2.getBigDecimal(1).setScale(3);
                            }
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(pp200023_OrderOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new BigDecimal(-1).setScale(3);
    }

    @Override
    public String getState(int orderId) {
        String query = "SELECT Status\n" +
            "FROM [Order]\n" +
            "WHERE ID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, orderId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getString(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(pp200023_OrderOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Calendar getSentTime(int orderId) {
        Calendar cal = Calendar.getInstance();
        String query = "SELECT SentTime\n" +
            "FROM [Order]\n" +
            "WHERE ID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, orderId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    cal.setTime(rs.getTimestamp(1));
                    return cal;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(pp200023_OrderOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Calendar getRecievedTime(int orderId) {
        Calendar cal = Calendar.getInstance();
        String query = "SELECT ReceivedTime\n" +
            "FROM [Order]\n" +
            "WHERE ID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, orderId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    cal.setTime(rs.getTimestamp(1));
                    return cal;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(pp200023_OrderOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public int getBuyer(int orderId) {
        String query = "SELECT BuyerID\n" +
            "FROM [Order]\n" +
            "WHERE ID = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, orderId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(pp200023_OrderOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    @Override
    public int getLocation(int orderId) {
        String status;
        String query1 = "SELECT Status\n" +
            "FROM [Order]\n" +
            "WHERE ID = ?";
        String query2 = "SELECT CityID\n" +
            "FROM [Order]\n" +
            "WHERE ID = ?";
        try (PreparedStatement stmt1 = conn.prepareStatement(query1)) {
            stmt1.setInt(1, orderId);
            try (ResultSet rs1 = stmt1.executeQuery()) {
                if (rs1.next()) {
                    status = rs1.getString(1);
                    if (!status.equals("created")) {
                        try (PreparedStatement stmt2 = conn.prepareStatement(query2)) {
                            stmt2.setInt(1, orderId);
                            try (ResultSet rs2 = stmt2.executeQuery()) {
                                if (rs2.next()) return rs2.getInt(1);
                            }
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(pp200023_OrderOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }
    
}
