package at.htlv.serveradministration.service;

import at.htlv.serveradministration.domain.Producerserver;
import at.htlv.serveradministration.domain.Server;
import java.util.List;

public interface ServerService {

	public abstract long countAllServers();


	public abstract void deleteServer(Server server);


	public abstract Server findServer(Long id);


	public abstract List<Server> findAllServers();


	public abstract List<Server> findServerEntries(int firstResult, int maxResults);


	public abstract boolean saveServer(Server server);


	public abstract Server updateServer(Server server);
	
	public abstract List<Server> findServerByProducer(Producerserver producer);
	
	public abstract List<Server> findServerByModel(String model);

}
