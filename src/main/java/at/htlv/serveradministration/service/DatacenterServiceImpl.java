package at.htlv.serveradministration.service;

import at.htlv.serveradministration.domain.Datacenter;
import at.htlv.serveradministration.repository.DatacenterRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class DatacenterServiceImpl implements DatacenterService {

	@Autowired
    DatacenterRepository datacenterRepository;

	public long countAllDatacenters() {
        return datacenterRepository.count();
    }

	public void deleteDatacenter(Datacenter datacenter) {
        datacenterRepository.delete(datacenter);
    }

	public Datacenter findDatacenter(Long id) {
        return datacenterRepository.findOne(id);
    }

	public List<Datacenter> findAllDatacenters() {
        return datacenterRepository.findAll();
    }

	public List<Datacenter> findDatacenterEntries(int firstResult, int maxResults) {
        return datacenterRepository.findAll(new org.springframework.data.domain.PageRequest(firstResult / maxResults, maxResults)).getContent();
    }

	public void saveDatacenter(Datacenter datacenter) {
        datacenterRepository.save(datacenter);
    }

	public Datacenter updateDatacenter(Datacenter datacenter) {
        return datacenterRepository.save(datacenter);
    }
}
