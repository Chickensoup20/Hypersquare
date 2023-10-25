package hypersquare.hypersquare.plot;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static hypersquare.hypersquare.Hypersquare.eventCache;

public class PlotDatabase {
    private static MongoClient mongoClient;
    private static MongoDatabase database;

    private static MongoCollection<Document> plotsCollection;
    private static MongoCollection<Document> additionalCollection;


    public PlotDatabase() {
        Yaml yaml = new Yaml();
        String DBPASS = null;
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("credentials.yml")) {
            if (inputStream != null) {
                // Load the YAML file
                Map<String, String> data = yaml.load(inputStream);
                // Fetch the value of DB_PASS
                DBPASS = data.get("DB_PASS");
            } else {
                System.out.println("File not found!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        mongoClient = MongoClients.create(DBPASS);
        database = mongoClient.getDatabase("chicken_plots");
        plotsCollection = database.getCollection("plots");
        additionalCollection = database.getCollection("additional_info");
    }

    public static void addPlot(int plotID, String ownerUUID, String icon, String name, int node, String tags, int votes, String size,int version) {
        Document plotDocument = new Document("plotID", plotID)
                .append("owner", ownerUUID)
                .append("devs", ownerUUID) // Consider using an array for devs and builders
                .append("builders", ownerUUID) // Consider using an array for devs and builders
                .append("icon", icon)
                .append("name", name)
                .append("node", node)
                .append("tags", tags)
                .append("votes", votes)
                .append("size", size)
                .append("version", version);
        plotsCollection.insertOne(plotDocument);
    }

    public static List<Document> getPlot(String ownerUUID) {
        List<Document> info = new ArrayList<>();

        Document query = new Document("owner", ownerUUID);
        MongoCursor<Document> cursor = plotsCollection.find(query).iterator();

        try {
            while (cursor.hasNext()) {
                Document plotDocument = cursor.next();
                info.add(plotDocument);
            }
        } finally {
            cursor.close();
        }

        return info;
    }

    public static void changePlotName(int plotID, String newName) {
        Document filter = new Document("plotID", plotID);
        Document update = new Document("$set", new Document("name", newName));
        plotsCollection.updateOne(filter, update);
    }

    public static void setRecentPlotID(int plotID) {
        Document update = new Document("$set", new Document("plotID", plotID));
        additionalCollection.updateOne(new Document(), update);
    }

    public static int getRecentPlotID() {
        Document filter = new Document(); // an empty filter to get all documents
        Document result = additionalCollection.find(filter).first();
        if (result != null) {
            return result.getInteger("plotID");
        } else {
            Document newDocument = new Document("plotID", 1);
            additionalCollection.insertOne(newDocument);
            return 1;
        }
    }



    public static void changePlotIcon(int plotID, String newIcon) {
        Document filter = new Document("plotID", plotID);
        Document update = new Document("$set", new Document("icon", newIcon));
        plotsCollection.updateOne(filter, update);
    }

    public static String getPlotName(int plotID) {
        Document query = new Document("plotID", plotID);
        Document result = plotsCollection.find(query).first();
        if (result != null) {
            return result.getString("name");
        }
        return null;
    }
    public static Integer getPlotVersion(int plotID) {
        Document query = new Document("plotID", plotID);
        Document result = plotsCollection.find(query).first();
        if (result != null) {
            return result.getInteger("version");
        }
        return null;
    }

    public static int getPlotNode(int plotID) {
        Document query = new Document("plotID", plotID);
        Document result = plotsCollection.find(query).first();
        if (result != null) {
            return result.getInteger("node", -1);
        }
        return -1;
    }

    public static String getPlotOwner(int plotID) {
        Document query = new Document("plotID", plotID);
        Document result = plotsCollection.find(query).first();
        if (result != null) {
            return result.getString("owner");
        }
        return null;
    }

    public static void updateLocalData(int plotID) {
        Document query = new Document("plotID", plotID);
        Document result = plotsCollection.find(query).first();
        if (result != null) {
            List<Object> data = new ArrayList<>();
            data.add(result.getString("name"));
            data.add(result.getString("owner"));
            data.add(result.getInteger("node", -1));
            // Hypersquare.localPlotData.put(plotID, data); // You can update your local data here.
        }
    }

    public static List<Object> getPlotData(int plotID) {
        Document query = new Document("plotID", plotID);
        Document result = plotsCollection.find(query).first();
        if (result != null) {
            List<Object> data = new ArrayList<>();
            data.add(result.getString("name"));
            data.add(result.getString("owner"));
            data.add(result.getInteger("node", -1));
            return data;
        }
        return null;
    }

    public static List<Document> getPlotsByOwner(String ownerUUID) {
        List<Document> plots = new ArrayList<>();
        MongoCollection<Document> plotsCollection = database.getCollection("plots");

        // Use Filters.eq to find documents with the matching ownerUUID
        FindIterable<Document> plotDocuments = plotsCollection.find(Filters.eq("owner", ownerUUID));

        for (Document plotDocument : plotDocuments) {
            plots.add(plotDocument);
        }

        return plots;
    }

    public static String[] getPlotDevs(int plotID) {
        Document query = new Document("plotID", plotID);
        Document result = plotsCollection.find(query).first();
        if (result != null) {
            String devsString = result.getString("devs");
            if (devsString != null) {
                return devsString.split(",");
            }
        }
        return new String[0];
    }

    public static String getRawDevs(int plotID) {
        Document query = new Document("plotID", plotID);
        Document result = plotsCollection.find(query).first();
        if (result != null) {
            return result.getString("devs");
        }
        return null;
    }

    public static void addDev(int plotID, UUID playerID) {
        Document query = new Document("plotID", plotID);
        Document result = plotsCollection.find(query).first();
        if (result != null) {
            String currentDevs = result.getString("devs");
            String newDevs = currentDevs + "," + playerID.toString();
            Document update = new Document("$set", new Document("devs", newDevs));
            plotsCollection.updateOne(query, update);
        }
    }
    public static void removeDev(int plotID, UUID playerID) {
        Document query = new Document("plotID", plotID);
        Document result = plotsCollection.find(query).first();

        if (result != null) {
            String currentDevs = result.getString("devs");

            // Split the currentDevs string into an array of player IDs
            String[] devsArray = currentDevs.split(",");

            // Create a StringBuilder to construct the newDevs string
            StringBuilder newDevsBuilder = new StringBuilder();

            for (String dev : devsArray) {
                if (!dev.equals(playerID.toString())) {
                    if (newDevsBuilder.length() > 0) {
                        newDevsBuilder.append(",");
                    }
                    newDevsBuilder.append(dev);
                }
            }

            String newDevs = newDevsBuilder.toString();

            // Update the document with the newDevs string
            Document update = new Document("$set", new Document("devs", newDevs));
            plotsCollection.updateOne(query, update);
        }
    }
    public static void deleteAllPlots() {
        Document query = new Document(); // Empty query matches all documents
        plotsCollection.deleteMany(query);
    }

    public static void deletePlot(int plotID) {
        Document query = new Document("plotID", plotID);
        plotsCollection.deleteOne(query);
    }

    public static void addEvents(int plotID, Map<String, String> events) {
        Document filter = new Document("plotID", plotID);
        Document update = new Document();

        for (Map.Entry<String, String> entry : events.entrySet()) {
            String eventKey = entry.getKey();
            String eventValue = entry.getValue();
            update.append("events." + eventKey, eventValue);
        }

        plotsCollection.updateOne(filter, new Document("$set", update));
    }


    public static boolean eventValueExistsInPlot(int plotID, String eventValue) {
        Document query = new Document("plotID", plotID);
        query.append("events", new Document("$elemMatch", new Document("$eq", eventValue)));

        Document result = plotsCollection.find(query).first();

        return result != null;
    }

    public static HashMap<String, String> getAllUniqueEventsInPlot(int plotID) {
        HashMap<String, String> uniqueEvents = new HashMap<>();

        Document query = new Document("plotID", plotID);
        Document result = plotsCollection.find(query).first();

        if (result != null) {
            Document eventsDocument = result.get("events", Document.class);

            if (eventsDocument != null) {
                for (String eventKey : eventsDocument.keySet()) {
                    String eventValue = eventsDocument.getString(eventKey);
                    uniqueEvents.put(eventKey, eventValue);
                }
            }
        }

        return uniqueEvents;
    }


    public static void updateEventsCache(int plotID){
        eventCache.put(plotID, PlotDatabase.getAllUniqueEventsInPlot(plotID));
    }
    public static void removeEventByKey(int plotID, String eventKeyToRemove) {
        Document filter = new Document("plotID", plotID);
        Document update = new Document("$unset", new Document("events." + eventKeyToRemove, ""));

        plotsCollection.updateOne(filter, update);
    }








}