package at.htlv.serveradministration.service;

import at.htlv.serveradministration.domain.Datacenter;
import java.util.List;

public interface DatacenterService {

	public abstract long countAllDatacenters();


	public abstract void deleteDatacenter(Datacenter datacenter);


	public abstract Datacenter findDatacenter(Long id);


	public abstract List<Datacenter> findAllDatacenters();


	public abstract List<Datacenter> findDatacenterEntries(int firstResult, int maxResults);


	public abstract void saveDatacenter(Datacenter datacenter);


	public abstract Datacenter updateDatacenter(Datacenter datacenter);

}
