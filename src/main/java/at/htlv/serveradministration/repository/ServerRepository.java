package at.htlv.serveradministration.repository;

import java.util.List;

import at.htlv.serveradministration.domain.Producerserver;
import at.htlv.serveradministration.domain.Server;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ServerRepository extends JpaRepository<Server, Long>, JpaSpecificationExecutor<Server> {
	
	List<Server> findByProducer(Producerserver producer);
	List<Server> findByModel(String model);
}
