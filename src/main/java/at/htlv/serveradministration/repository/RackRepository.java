package at.htlv.serveradministration.repository;

import at.htlv.serveradministration.domain.Rack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RackRepository extends JpaRepository<Rack, Long>, JpaSpecificationExecutor<Rack> {
}
