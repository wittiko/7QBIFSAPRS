package at.htlv.serveradministration.repository;

import at.htlv.serveradministration.domain.Datacenter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DatacenterRepository extends JpaSpecificationExecutor<Datacenter>, JpaRepository<Datacenter, Long> {
}
