package at.htlv.serveradministration.service;

import at.htlv.serveradministration.domain.Rack;
import java.util.List;

public interface RackService {

	public abstract long countAllRacks();


	public abstract void deleteRack(Rack rack);


	public abstract Rack findRack(Long id);


	public abstract List<Rack> findAllRacks();


	public abstract List<Rack> findRackEntries(int firstResult, int maxResults);


	public abstract void saveRack(Rack rack);


	public abstract Rack updateRack(Rack rack);

}
