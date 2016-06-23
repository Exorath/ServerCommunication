import com.exorath.servercommunication.api.CommunicationManager;
import com.exorath.servercommunication.api.servers.Server;
import com.exorath.servercommunication.api.servers.ServerProvider;
import redis.clients.jedis.JedisPool;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by Toon Sevrin on 6/23/2016.
 */
public class Main {
    private String publishChannel = "application.servers.filter.subfilter";
    private String id;
    private CommunicationManager communicationManager;

    public Main(String id){
        this.id = id;

        communicationManager = CommunicationManager.create(new JedisPool("localhost"), new Provider());
        communicationManager.subscribe("application.servers.*");
    }


    public Server getFilteredServerExample1(){
        Optional<Map.Entry<String, HashSet<String>>> filtered = communicationManager.getServersByChannel().entrySet().stream()
                .filter((entry) -> entry.getKey().startsWith("application.servers.filter"))
                .findAny();

        return filtered.isPresent() ? communicationManager.getServersById().get(filtered.get().getValue().stream().findAny().orElse(null)) : null;
    }

    public Server getFilteredServerExample2(){
        Optional<Server> filtered = communicationManager.getServers().stream()
                .filter(s -> s.getData().has("key") && s.getData().get("key").equals("value")).findAny();
        return filtered.orElse(null);
    }

    private class Provider implements ServerProvider{
        @Override
        public Server getServer() {
            return Server.create(id);
        }

        @Override
        public String getPublishingChannel() {
            return publishChannel;
        }
    }

}
