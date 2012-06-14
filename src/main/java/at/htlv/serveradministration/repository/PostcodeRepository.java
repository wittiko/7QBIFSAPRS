package at.htlv.serveradministration.repository;

import at.htlv.serveradministration.domain.Postcode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PostcodeRepository extends JpaSpecificationExecutor<Postcode>, JpaRepository<Postcode, Long> {
}
