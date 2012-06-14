package at.htlv.serveradministration.domain;

import at.htlv.serveradministration.repository.RackRepository;
import at.htlv.serveradministration.service.RackService;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext*.xml")
@Transactional
@Configurable
public class RackIntegrationTest {

    @Test
    public void testMarkerMethod() {
    }

	@Autowired
    private RackDataOnDemand dod;

	@Autowired
    RackService rackService;

	@Autowired
    RackRepository rackRepository;

	@Test
    public void testCountAllRacks() {
        Assert.assertNotNull("Data on demand for 'Rack' failed to initialize correctly", dod.getRandomRack());
        long count = rackService.countAllRacks();
        Assert.assertTrue("Counter for 'Rack' incorrectly reported there were no entries", count > 0);
    }

	@Test
    public void testFindRack() {
        Rack obj = dod.getRandomRack();
        Assert.assertNotNull("Data on demand for 'Rack' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Rack' failed to provide an identifier", id);
        obj = rackService.findRack(id);
        Assert.assertNotNull("Find method for 'Rack' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'Rack' returned the incorrect identifier", id, obj.getId());
    }

	@Test
    public void testFindAllRacks() {
        Assert.assertNotNull("Data on demand for 'Rack' failed to initialize correctly", dod.getRandomRack());
        long count = rackService.countAllRacks();
        Assert.assertTrue("Too expensive to perform a find all test for 'Rack', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<Rack> result = rackService.findAllRacks();
        Assert.assertNotNull("Find all method for 'Rack' illegally returned null", result);
        Assert.assertTrue("Find all method for 'Rack' failed to return any data", result.size() > 0);
    }

	@Test
    public void testFindRackEntries() {
        Assert.assertNotNull("Data on demand for 'Rack' failed to initialize correctly", dod.getRandomRack());
        long count = rackService.countAllRacks();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<Rack> result = rackService.findRackEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'Rack' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'Rack' returned an incorrect number of entries", count, result.size());
    }

	@Test
    public void testFlush() {
        Rack obj = dod.getRandomRack();
        Assert.assertNotNull("Data on demand for 'Rack' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Rack' failed to provide an identifier", id);
        obj = rackService.findRack(id);
        Assert.assertNotNull("Find method for 'Rack' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyRack(obj);
        Integer currentVersion = obj.getVersion();
        rackRepository.flush();
        Assert.assertTrue("Version for 'Rack' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testUpdateRackUpdate() {
        Rack obj = dod.getRandomRack();
        Assert.assertNotNull("Data on demand for 'Rack' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Rack' failed to provide an identifier", id);
        obj = rackService.findRack(id);
        boolean modified =  dod.modifyRack(obj);
        Integer currentVersion = obj.getVersion();
        Rack merged = rackService.updateRack(obj);
        rackRepository.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'Rack' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testSaveRack() {
        Assert.assertNotNull("Data on demand for 'Rack' failed to initialize correctly", dod.getRandomRack());
        Rack obj = dod.getNewTransientRack(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'Rack' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'Rack' identifier to be null", obj.getId());
        rackService.saveRack(obj);
        rackRepository.flush();
        Assert.assertNotNull("Expected 'Rack' identifier to no longer be null", obj.getId());
    }

	@Test
    public void testDeleteRack() {
        Rack obj = dod.getRandomRack();
        Assert.assertNotNull("Data on demand for 'Rack' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Rack' failed to provide an identifier", id);
        obj = rackService.findRack(id);
        rackService.deleteRack(obj);
        rackRepository.flush();
        Assert.assertNull("Failed to remove 'Rack' with identifier '" + id + "'", rackService.findRack(id));
    }
}
