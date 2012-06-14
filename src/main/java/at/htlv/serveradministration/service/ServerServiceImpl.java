package at.htlv.serveradministration.service;

import at.htlv.serveradministration.domain.Producerserver;
import at.htlv.serveradministration.domain.Server;
import at.htlv.serveradministration.repository.ServerRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class ServerServiceImpl implements ServerService {

	@Autowired
    ServerRepository serverRepository;

	public long countAllServers() {
        return serverRepository.count();
    }

	public void deleteServer(Server server) {
        serverRepository.delete(server);
    }

	public Server findServer(Long id) {
        return serverRepository.findOne(id);
    }

	public List<Server> findAllServers() {
        return serverRepository.findAll();
    }

	public List<Server> findServerEntries(int firstResult, int maxResults) {
        return serverRepository.findAll(new org.springframework.data.domain.PageRequest(firstResult / maxResults, maxResults)).getContent();
    }

	public boolean saveServer(Server server) {
        serverRepository.save(server);
        return true;
    }

	public Server updateServer(Server server) {
        return serverRepository.save(server);
    }
	
	public List<Server> findServerByProducer(Producerserver producer) {
		return serverRepository.findByProducer(producer);
	}
	
	public List<Server> findServerByModel(String model) {
		return serverRepository.findByModel(model);
	}

	
}
