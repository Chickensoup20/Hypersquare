package hypersquare.hypersquare.plot;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import hypersquare.hypersquare.Hypersquare;
import org.bson.Document;
import org.bukkit.entity.Player;

import java.util.*;

public class PlotStats {
    private static MongoCollection<Document> plotStats;

    public static void init() {
        MongoDatabase database = Hypersquare.mongoClient.getDatabase(Hypersquare.DB_NAME);
        plotStats = database.getCollection("plot_stats");
    }


    public static void addPlayer(int plotID, Player player) {
        String uuid = player.getUniqueId().toString();
        long timestamp = System.currentTimeMillis();
        Document playerDoc = new Document("uuid", uuid)
            .append("timestamp", timestamp)
            .append("totalTimePlayed", 0); // Adding totalTimePlayed with a default value of 0

        Document query = new Document("plotID", plotID).append("players.uuid", uuid);
        Document update = new Document("$set", new Document("players.$.timestamp", timestamp));

        if (plotStats.countDocuments(query) > 0) {
            plotStats.updateOne(query, update);
        } else {
            update = new Document("$push", new Document("players", playerDoc));
            plotStats.updateOne(new Document("plotID", plotID), update, new UpdateOptions().upsert(true));
        }
    }


    public static void deleteAllPlotStats() {
        plotStats.deleteMany(new Document());
    }

    public static long getTotalUniquePlayers(int plotID) {
        List<Object> distinctPlayers = Collections.singletonList(plotStats.distinct("players.uuid", new Document("plotID", plotID), Object.class));
        return distinctPlayers.size();
    }

    public static long getUniquePlayersLastSevenDays(int plotID) {
        // Calculate the date seven days ago from the current date
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -7);
        Date sevenDaysAgo = calendar.getTime();

        // Construct the query to find unique players in the last seven days
        Document query = new Document("plotID", plotID)
            .append("players.timestamp", new Document("$gte", sevenDaysAgo));

        List<Object> distinctPlayers = Collections.singletonList(plotStats.distinct("players.uuid", query, Object.class));
        return distinctPlayers.size();
    }

    public static void updatePlayerTime(int plotID, UUID playerUUID, long timeLeft) {

        Document query = new Document("plotID", plotID).append("players.uuid", playerUUID.toString());
        Document projection = new Document("players.$", 1); // Projection to retrieve the specific player document
        Document result = plotStats.find(query).projection(projection).first();

        if (result != null) {
            Document playerData = (Document) result.get("players", List.class).get(0);
            long timeJoined = playerData.getLong("timestamp");
            long timeDifference = timeLeft - timeJoined;

            Document update = new Document("$inc", new Document("players.$.totalTimePlayed", timeDifference));
            plotStats.updateOne(query, update);
        }
    }

    public static String calculateTotalTimePlayed(int plotID) {
        Document query = new Document("plotID", plotID);
        Document projection = new Document("players.totalTimePlayed", 1); // Projection to retrieve totalTimePlayed for all players
        MongoCursor<Document> cursor = plotStats.find(query).projection(projection).iterator();

        long totalTime = 0;
        while (cursor.hasNext()) {
            Document doc = cursor.next();
            List<Document> players = doc.getList("players", Document.class);
            for (Document player : players) {
                if (player.containsKey("totalTimePlayed")) {
                    totalTime += player.getLong("totalTimePlayed");
                }
            }
        }
        cursor.close();

        long seconds = totalTime / 1000;
        long days = seconds / 86400;
        seconds %= 86400;
        long hours = seconds / 3600;
        seconds %= 3600;
        long minutes = seconds / 60;
        seconds %= 60;

        return String.format("%d Days %d Hours %d Minutes %d Seconds", days, hours, minutes, seconds);
    }
}



