package at.htlv.serveradministration.domain;

import at.htlv.serveradministration.repository.DatacenterRepository;
import at.htlv.serveradministration.service.DatacenterService;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@Configurable
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext*.xml")
@Transactional
public class DatacenterIntegrationTest {

    @Test
    public void testMarkerMethod() {
    }

	@Autowired
    private DatacenterDataOnDemand dod;

	@Autowired
    DatacenterService datacenterService;

	@Autowired
    DatacenterRepository datacenterRepository;

	@Test
    public void testCountAllDatacenters() {
        Assert.assertNotNull("Data on demand for 'Datacenter' failed to initialize correctly", dod.getRandomDatacenter());
        long count = datacenterService.countAllDatacenters();
        Assert.assertTrue("Counter for 'Datacenter' incorrectly reported there were no entries", count > 0);
    }

	@Test
    public void testFindDatacenter() {
        Datacenter obj = dod.getRandomDatacenter();
        Assert.assertNotNull("Data on demand for 'Datacenter' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Datacenter' failed to provide an identifier", id);
        obj = datacenterService.findDatacenter(id);
        Assert.assertNotNull("Find method for 'Datacenter' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'Datacenter' returned the incorrect identifier", id, obj.getId());
    }

	@Test
    public void testFindAllDatacenters() {
        Assert.assertNotNull("Data on demand for 'Datacenter' failed to initialize correctly", dod.getRandomDatacenter());
        long count = datacenterService.countAllDatacenters();
        Assert.assertTrue("Too expensive to perform a find all test for 'Datacenter', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<Datacenter> result = datacenterService.findAllDatacenters();
        Assert.assertNotNull("Find all method for 'Datacenter' illegally returned null", result);
        Assert.assertTrue("Find all method for 'Datacenter' failed to return any data", result.size() > 0);
    }

	@Test
    public void testFindDatacenterEntries() {
        Assert.assertNotNull("Data on demand for 'Datacenter' failed to initialize correctly", dod.getRandomDatacenter());
        long count = datacenterService.countAllDatacenters();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<Datacenter> result = datacenterService.findDatacenterEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'Datacenter' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'Datacenter' returned an incorrect number of entries", count, result.size());
    }

	@Test
    public void testFlush() {
        Datacenter obj = dod.getRandomDatacenter();
        Assert.assertNotNull("Data on demand for 'Datacenter' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Datacenter' failed to provide an identifier", id);
        obj = datacenterService.findDatacenter(id);
        Assert.assertNotNull("Find method for 'Datacenter' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyDatacenter(obj);
        Integer currentVersion = obj.getVersion();
        datacenterRepository.flush();
        Assert.assertTrue("Version for 'Datacenter' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testUpdateDatacenterUpdate() {
        Datacenter obj = dod.getRandomDatacenter();
        Assert.assertNotNull("Data on demand for 'Datacenter' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Datacenter' failed to provide an identifier", id);
        obj = datacenterService.findDatacenter(id);
        boolean modified =  dod.modifyDatacenter(obj);
        Integer currentVersion = obj.getVersion();
        Datacenter merged = datacenterService.updateDatacenter(obj);
        datacenterRepository.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'Datacenter' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testSaveDatacenter() {
        Assert.assertNotNull("Data on demand for 'Datacenter' failed to initialize correctly", dod.getRandomDatacenter());
        Datacenter obj = dod.getNewTransientDatacenter(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'Datacenter' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'Datacenter' identifier to be null", obj.getId());
        datacenterService.saveDatacenter(obj);
        datacenterRepository.flush();
        Assert.assertNotNull("Expected 'Datacenter' identifier to no longer be null", obj.getId());
    }

	@Test
    public void testDeleteDatacenter() {
        Datacenter obj = dod.getRandomDatacenter();
        Assert.assertNotNull("Data on demand for 'Datacenter' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Datacenter' failed to provide an identifier", id);
        obj = datacenterService.findDatacenter(id);
        datacenterService.deleteDatacenter(obj);
        datacenterRepository.flush();
        Assert.assertNull("Failed to remove 'Datacenter' with identifier '" + id + "'", datacenterService.findDatacenter(id));
    }
}
