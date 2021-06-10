package svosin.biab.repos;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.bson.Document;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class JobHistoryRepository {

   // @Autowired
    private MongoClient mongo = MongoClients.create();

    public void add(Map<String, Object> keys) {
        // TODO: make configurable / use repository ?
        // TODO: set expire TTL based on 'ts' field (ie. 7 days)
        mongo.getDatabase("jobs-demo").getCollection("job_history")
                .insertOne(new Document(keys));
    }

}
