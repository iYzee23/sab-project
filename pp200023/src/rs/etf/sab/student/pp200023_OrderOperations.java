package rs.etf.sab.student;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.sql.Types;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
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
        // Collections.reverse(path);

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
    
    static Connection conn = DB.getInstance().getConnection();
    
    static Map<Integer, Integer> mDaysToAssamble = new HashMap<>();
    static Map<Integer, List<Integer>> mPath = new HashMap<>();
    static Map<Integer, Integer> mDaysToNextCity = new HashMap<>();

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

    // check if status is created
    boolean checkStatus(int orderId) throws SQLException {
        String query0 = "SELECT Status\n" +
                "FROM [Order]\n" +
                "WHERE ID = ?";
        try (PreparedStatement stmt0 = conn.prepareStatement(query0)) {
            stmt0.setInt(1, orderId);
            try (ResultSet rs0 = stmt0.executeQuery()) {
                if (rs0.next()) {
                    if (!rs0.getString(1).equals("created")) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
    // find buyerId
    // find paid sum for past month
    // if paid sum is over 10000, set discount for order
    // if paid sum is over 10000, set discount for each item in order
    int updateSystemDiscount(int orderId) throws SQLException {
        int buyerId = -1;
        double buyerPrice;
        String query1 = "SELECT BuyerID\n" +
                "FROM [Order]\n" +
                "WHERE ID = ?";
        String query2 = "SELECT COALESCE(SUM(Price), 0)\n" +
                "FROM [Order]\n" +
                "WHERE BuyerID = ? and Status = 'arrived' and ReceivedTime >= ?";
        String query3 = "UPDATE [Order]\n" +
                "SET Discount = 2\n" +
                "WHERE ID = ?";
        String query6 = "UPDATE Item\n" +
                "SET Discount = Discount + 2\n" +
                "WHERE OrderID = ?";
        try (PreparedStatement stmt1 = conn.prepareStatement(query1)) {
            stmt1.setInt(1, orderId);
            try (ResultSet rs1 = stmt1.executeQuery()) {
                if (rs1.next()) {
                    buyerId = rs1.getInt(1);
                    Calendar cal = (Calendar) pp200023_GeneralOperations.calendar.clone();
                    cal.add(Calendar.MONTH, -1);
                    try (PreparedStatement stmt2 = conn.prepareStatement(query2)) {
                        stmt2.setInt(1, buyerId);
                        stmt2.setTimestamp(2, new Timestamp(cal.getTimeInMillis()));
                        try (ResultSet rs2 = stmt2.executeQuery()) {
                            if (rs2.next()) {
                                buyerPrice = rs2.getDouble(1);
                                if (buyerPrice > 10000) {
                                    try (PreparedStatement stmt3 = conn.prepareStatement(query3)) {
                                        stmt3.setInt(1, orderId);
                                        stmt3.executeUpdate();
                                        try (PreparedStatement stmt6 = conn.prepareStatement(query6)) {
                                            stmt6.setInt(1, orderId);
                                            stmt6.executeUpdate();
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return buyerId;
    }
    
    // find all shops included in order
    // find discount for each shop
    // if discount for current shop exists, find all articles in order from that shop
    // set discount for each item containing that article in order
    void updateShopDiscount(int orderId) throws SQLException {
        int discount;
        String query4 = "SELECT DISTINCT(S.ID)\n" +
            "FROM Item I join Catalog C on I.ArticleID = C.ArticleID join Shop S on C.ShopID = S.ID\n" +
            "WHERE I.OrderID = ?";
        String query5 = "SELECT Discount\n" +
            "FROM Shop\n" +
            "WHERE ID = ?";
        String query7 = "SELECT I.ArticleID\n" +
            "FROM Catalog C join Item I on C.ArticleID = I.ArticleID\n" +
            "WHERE C.ShopID = ? and I.OrderID = ?";
        String query8 = "UPDATE Item\n" +
            "SET Discount = Discount + ?\n" +
            "WHERE ArticleID = ? and OrderID = ?";
        try (PreparedStatement stmt4 = conn.prepareStatement(query4)) {
            stmt4.setInt(1, orderId);
            try (ResultSet rs4 = stmt4.executeQuery()) {
                List<Integer> ListShopId = new ArrayList<>();
                while (rs4.next()) ListShopId.add(rs4.getInt(1));
                for (Integer shopId: ListShopId) {
                    try (PreparedStatement stmt5 = conn.prepareStatement(query5)) {
                        stmt5.setInt(1, shopId);
                        try (ResultSet rs5 = stmt5.executeQuery()) {
                            if (rs5.next()) {
                                discount = rs5.getInt(1);
                                if (discount > 0) {
                                    try (PreparedStatement stmt7 = conn.prepareStatement(query7)) {
                                        stmt7.setInt(1, shopId);
                                        stmt7.setInt(2, orderId);
                                        try (ResultSet rs7 = stmt7.executeQuery()) {
                                            List<Integer> ListArticleId = new ArrayList<>();
                                            while (rs7.next()) ListArticleId.add(rs7.getInt(1));
                                            for (Integer articleId: ListArticleId) {
                                                try (PreparedStatement stmt8 = conn.prepareStatement(query8)) {
                                                    stmt8.setInt(1, discount);
                                                    stmt8.setInt(2, articleId);
                                                    stmt8.setInt(3, orderId);
                                                    stmt8.executeUpdate();
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    // find total price that should be paid
    // check if buyer has enough credit to perform payment
    // if can, update the price of order (price that buyer has to pay)
    // if can, update the credit for buyer
    // if everything allright, create transaction for buyer
    boolean updatePricesAndCredit(int orderId, int buyerId) throws SQLException {
        double totalPrice;
        int tranId;
        String query9 = "SELECT COALESCE(SUM(Price * (100 - Discount) / 100.0), 0)\n" +
            "FROM Item\n" +
            "WHERE OrderID = ?";
        String query90 = "SELECT Credit\n" +
            "FROM Buyer\n" +
            "WHERE ID = ?";
        String query10 = "UPDATE [Order]\n" +
            "SET Price = ?\n" +
            "WHERE ID = ?";
        String query11 = "UPDATE Buyer\n" +
            "SET Credit = Credit - ?\n" +
            "WHERE ID = ?";
        String query12 = "INSERT INTO [Transaction] (Amount, ExecutionTime, OrderID) VALUES (?, ?, ?)";
        String query13 = "INSERT INTO TransactionBuyer (ID) VALUES(?)";
        try (PreparedStatement stmt9 = conn.prepareStatement(query9)) {
            stmt9.setInt(1, orderId);
            try (ResultSet rs9 = stmt9.executeQuery()) {
                if (rs9.next()) {
                    totalPrice = rs9.getDouble(1);
                    try (PreparedStatement stmt90 = conn.prepareStatement(query90)) {
                        stmt90.setInt(1, buyerId);
                        try (ResultSet rs90 = stmt90.executeQuery()) {
                            if (rs90.next()) {
                                if (rs90.getDouble(1) < totalPrice) {
                                    return false;
                                }
                            }
                        }
                    }
                    try (PreparedStatement stmt10 = conn.prepareStatement(query10)) {
                        stmt10.setDouble(1, totalPrice);
                        stmt10.setInt(2, orderId);
                        stmt10.executeUpdate();
                        try (PreparedStatement stmt11 = conn.prepareStatement(query11)) {
                            stmt11.setDouble(1, totalPrice);
                            stmt11.setInt(2, buyerId);
                            stmt11.executeUpdate();
                            Calendar calll = (Calendar) pp200023_GeneralOperations.calendar.clone();
                            try (PreparedStatement stmt12 = conn.prepareStatement(query12, PreparedStatement.RETURN_GENERATED_KEYS)) {
                                stmt12.setDouble(1, totalPrice);
                                stmt12.setTimestamp(2, new Timestamp(calll.getTimeInMillis()));
                                stmt12.setInt(3, orderId);
                                stmt12.executeUpdate();
                                try (ResultSet rs12 = stmt12.getGeneratedKeys()) {
                                    if (rs12.next()) {
                                        tranId = rs12.getInt(1);
                                        try (PreparedStatement stmt13 = conn.prepareStatement(query13)) {
                                            stmt13.setInt(1, tranId);
                                            stmt13.executeUpdate();
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
    
    // update status for order to sent
    // update sent time for order to the current time
    void updateStatus(int orderId) throws SQLException {
        String query14 = "UPDATE [Order]\n" +
            "SET Status = 'sent'\n" +
            "WHERE ID = ?";
        String query15 = "UPDATE [Order]\n" +
            "SET SentTime = ?\n" +
            "WHERE ID = ?";
        try (PreparedStatement stmt14 = conn.prepareStatement(query14)) {
            stmt14.setInt(1, orderId);
            stmt14.executeUpdate();
            Calendar call = (Calendar) pp200023_GeneralOperations.calendar.clone();
            try (PreparedStatement stmt15 = conn.prepareStatement(query15)) {
                stmt15.setTimestamp(1, new Timestamp(call.getTimeInMillis()));
                stmt15.setInt(2, orderId);
                stmt15.executeUpdate();
            }
        }
    }
    
    // find size of graph (number of cities)
    // find all connections and make graph
    Graph makeGraph() throws SQLException {
        int cityCnt, city1, city2, dist;
        String query16 = "SELECT COUNT(*)\n" +
            "FROM City";
        String query17 = "SELECT CityID1, CityID2, Distance\n" +
            "FROM Connection";
        try (PreparedStatement stmt16 = conn.prepareStatement(query16)) {
            try (ResultSet rs16 = stmt16.executeQuery()) {
                if (rs16.next()) {
                    cityCnt = rs16.getInt(1);
                    Graph g = new Graph(cityCnt);
                    try (PreparedStatement stmt17 = conn.prepareStatement(query17)) {
                        try (ResultSet rs17 = stmt17.executeQuery()) {
                            while (rs17.next()) {
                                city1 = rs17.getInt(1) - 1;
                                city2 = rs17.getInt(2) - 1;
                                dist = rs17.getInt(3);
                                g.addEdge(city1, city2, dist);
                            }
                            return g;
                        }
                    }
                }
            }
        }
        return null;
    }
    
    // find city of buyer
    // perform dijkstra for buyer's city
    // find cities of all shops
    // find minimum length between buyer's city and shop's city
    // shop's city that is closest to buyer's city is assemble city
    // update current city for order (assemble city)
    // update mPath
    int getAssembleCityAndUpdateMPath(int orderId, int buyerId, Graph g) throws SQLException {
        int buyerCity, assembleCity = -1;
        int minn = Integer.MAX_VALUE;
        List<Integer> ListShopCityID = new ArrayList<>();
        String query18 = "UPDATE [Order]\n" +
            "SET CityID = ?\n" +
            "WHERE ID = ?";
        String query19 = "SELECT CityID\n" +
            "FROM Member\n" +
            "WHERE ID = ?";
        String query20 = "SELECT DISTINCT(M.CityID)\n" +
            "FROM Member M join Shop S on M.ID = S.ID";
        try (PreparedStatement stmt19 = conn.prepareStatement(query19)) {
            stmt19.setInt(1, buyerId);
            try (ResultSet rs19 = stmt19.executeQuery()) {
                if (rs19.next()) {
                    buyerCity = rs19.getInt(1) - 1;
                    int[] path = g.shortestPath(buyerCity);
                    try (PreparedStatement stmt20 = conn.prepareStatement(query20)) {
                        try (ResultSet rs20 = stmt20.executeQuery()) {
                            while (rs20.next()) ListShopCityID.add(rs20.getInt(1) - 1);
                            for (Integer cityId: ListShopCityID) {
                                if (path[cityId] < minn) {
                                    minn = path[cityId];
                                    assembleCity = cityId;
                                }
                            }
                            try (PreparedStatement stmt18 = conn.prepareStatement(query18)) {
                                stmt18.setInt(1, assembleCity + 1);
                                stmt18.setInt(2, orderId);
                                stmt18.executeUpdate();
                            }
                            List<Integer> bPath = g.getPath(buyerCity, assembleCity, path);
                            mPath.put(orderId, bPath);
                        }
                    }
                }
            }
        }
        return assembleCity;
    }
    
    // find cities of all items in order
    // perform dijkstra for assemble city
    // find maximum length between assemble city and city in order
    // update mDaysToAssamble
    void updateMDaysToAssamble(int orderId, int assembleCity, Graph g) throws SQLException {
        int maxx = Integer.MIN_VALUE;
        List<Integer> ListOrderCityID = new ArrayList<>();
        String query21 = "SELECT DISTINCT(M.CityID)\n" +
            "FROM Item I join Catalog C on I.ArticleID = C.ArticleID join Member M on C.ShopID = M.ID\n" +
            "WHERE I.OrderID = ?";
        int[] assPath = g.shortestPath(assembleCity);
        try (PreparedStatement stmt21 = conn.prepareStatement(query21)) {
            stmt21.setInt(1, orderId);
            try (ResultSet rs21 = stmt21.executeQuery()) {
                while (rs21.next()) ListOrderCityID.add(rs21.getInt(1) - 1);
                for (Integer cityId: ListOrderCityID) {
                    if (assPath[cityId] > maxx) maxx = assPath[cityId];
                }
                mDaysToAssamble.put(orderId, maxx);
            }
        }
    }
    
    @Override
    public int completeOrder(int orderId) {
        try {
            conn.setAutoCommit(false);
            int buyerId;
            if (!checkStatus(orderId)) {
                conn.rollback();
                return -1;
            }
            buyerId = updateSystemDiscount(orderId);
            updateShopDiscount(orderId);
            if (!updatePricesAndCredit(orderId, buyerId)) {
                conn.rollback();
                return -1;
            }
            updateStatus(orderId);
            Graph g = makeGraph();
            if (g != null) {
                int assembleCity = getAssembleCityAndUpdateMPath(orderId, buyerId, g);
                updateMDaysToAssamble(orderId, assembleCity, g);
                conn.commit();
                return 1;
            }
        } catch (SQLException ex) {
            Logger.getLogger(pp200023_OrderOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            conn.rollback();
        } catch (SQLException ex) {
            Logger.getLogger(pp200023_OrderOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
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
                if (rs.next() && rs.getTimestamp(1) != null) {
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
                if (rs.next() && rs.getTimestamp(1) != null) {
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
