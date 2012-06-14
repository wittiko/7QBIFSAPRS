package at.htlv.serveradministration.service;

import at.htlv.serveradministration.domain.Rack;
import at.htlv.serveradministration.repository.RackRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class RackServiceImpl implements RackService {

	@Autowired
    RackRepository rackRepository;

	public long countAllRacks() {
        return rackRepository.count();
    }

	public void deleteRack(Rack rack) {
        rackRepository.delete(rack);
    }

	public Rack findRack(Long id) {
        return rackRepository.findOne(id);
    }

	public List<Rack> findAllRacks() {
        return rackRepository.findAll();
    }

	public List<Rack> findRackEntries(int firstResult, int maxResults) {
        return rackRepository.findAll(new org.springframework.data.domain.PageRequest(firstResult / maxResults, maxResults)).getContent();
    }

	public void saveRack(Rack rack) {
        rackRepository.save(rack);
    }

	public Rack updateRack(Rack rack) {
        return rackRepository.save(rack);
    }
}
