package hypersquare.hypersquare.plot;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import hypersquare.hypersquare.Hypersquare;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class PlayerDatabase {

    private static MongoCollection<Document> playerCollection;

    public static void init() {
        MongoDatabase database = Hypersquare.mongoClient.getDatabase(Hypersquare.DB_NAME);
        playerCollection = database.getCollection("players");
    }

    public static void addPlayer(UUID playerUUID, int basicPlots, int largePlots, int hugePlots, int massivePlots, int giganticPlots) {
        Document plotDocument = new Document("player", playerUUID)
            .append("uuid", playerUUID)
            .append("max_basic", basicPlots)
            .append("max_large", largePlots)
            .append("max_huge", hugePlots)
            .append("max_massive", massivePlots)
            .append("max_gigantic", giganticPlots)
            .append("basic", 0)
            .append("large", 0)
            .append("huge", 0)
            .append("massive", 0)
            .append("gigantic", 0);
        playerCollection.insertOne(plotDocument);
        updateLocalPlayerData(Bukkit.getPlayer(playerUUID));
    }

    public static boolean playerExists(UUID playerUUID) {
        Document query = new Document("uuid", playerUUID);
        Document playerDocument = playerCollection.find(query).first();
        return playerDocument != null;
    }

    public static int getMaxPlots(UUID playerUUID, String plotSize) {
        Document query = new Document("uuid", playerUUID);
        Document playerDocument = playerCollection.find(query).first();

        if (playerDocument != null) {
            return playerDocument.getInteger("max_" + plotSize);
        } else {
            return -1;
        }
    }

    public static int getUsedPlots(UUID playerUUID, String plotSize) {
        Document query = new Document("uuid", playerUUID);
        Document playerDocument = playerCollection.find(query).first();

        if (playerDocument != null) {
            return playerDocument.getInteger(plotSize);
        } else {
            return -1;
        }
    }

    public static void addPlot(UUID playerUUID, String plotSize) {
        Document query = new Document("uuid", playerUUID);
        Document playerDocument = playerCollection.find(query).first();

        if (playerDocument != null) {
            int currentPlots = playerDocument.getInteger(plotSize, 0); // Get the current plot count or default to 0
            playerDocument.put(plotSize, currentPlots + 1); // Increment the plot count
            playerCollection.updateOne(query, new Document("$set", playerDocument)); // Update the document in the collection
            updateLocalPlayerData(Bukkit.getPlayer(playerUUID));
        }
    }

    public static void removePlot(UUID playerUUID, String plotSize) {
        Document query = new Document("uuid", playerUUID);
        Document playerDocument = playerCollection.find(query).first();

        if (playerDocument != null) {
            int currentPlots = playerDocument.getInteger(plotSize, 0); // Get the current plot count or default to 0
            if (currentPlots > 0) {
                playerDocument.put(plotSize, currentPlots - 1); // Decrement the plot count (if it's greater than 0)
                playerCollection.updateOne(query, new Document("$set", playerDocument)); // Update the document in the collection
                updateLocalPlayerData(Bukkit.getPlayer(playerUUID));
            }
        }
    }

    public static void updateLocalPlayerData(Player player) {
        Thread thread = new Thread(() -> {
            HashMap<String, Integer> playerData = new HashMap<>();
            playerData.put("used_basic", PlayerDatabase.getUsedPlots(player.getUniqueId(), "basic"));
            playerData.put("used_large", PlayerDatabase.getUsedPlots(player.getUniqueId(), "large"));
            playerData.put("used_huge", PlayerDatabase.getUsedPlots(player.getUniqueId(), "huge"));
            playerData.put("used_massive", PlayerDatabase.getUsedPlots(player.getUniqueId(), "massive"));
            playerData.put("used_gigantic", PlayerDatabase.getUsedPlots(player.getUniqueId(), "gigantic"));
            playerData.put("max_basic", PlayerDatabase.getMaxPlots(player.getUniqueId(), "basic"));
            playerData.put("max_large", PlayerDatabase.getMaxPlots(player.getUniqueId(), "large"));
            playerData.put("max_huge", PlayerDatabase.getMaxPlots(player.getUniqueId(), "huge"));
            playerData.put("max_massive", PlayerDatabase.getMaxPlots(player.getUniqueId(), "massive"));
            playerData.put("max_gigantic", PlayerDatabase.getMaxPlots(player.getUniqueId(), "gigantic"));
            Hypersquare.localPlayerData.put(player.getUniqueId(), playerData);
        });
        thread.start();
    }

    public static void increaseMaxPlots(UUID playerUUID, String plotSize, int additionalPlots) {
        Document query = new Document("uuid", playerUUID);
        Document playerDocument = playerCollection.find(query).first();

        if (playerDocument != null) {
            int currentMaxPlots = playerDocument.getInteger("max_" + plotSize, 0);
            int newMaxPlots = currentMaxPlots + additionalPlots;

            playerDocument.put("max_" + plotSize, newMaxPlots);
            playerCollection.updateOne(query, new Document("$set", playerDocument));
            updateLocalPlayerData(Bukkit.getPlayer(playerUUID));
        }
    }

}
