package at.htlv.serveradministration.domain;

import at.htlv.serveradministration.repository.PostcodeRepository;
import at.htlv.serveradministration.service.PostcodeService;
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
public class PostcodeIntegrationTest {

    @Test
    public void testMarkerMethod() {
    }

	@Autowired
    private PostcodeDataOnDemand dod;

	@Autowired
    PostcodeService postcodeService;

	@Autowired
    PostcodeRepository postcodeRepository;

	@Test
    public void testCountAllPostcodes() {
        Assert.assertNotNull("Data on demand for 'Postcode' failed to initialize correctly", dod.getRandomPostcode());
        long count = postcodeService.countAllPostcodes();
        Assert.assertTrue("Counter for 'Postcode' incorrectly reported there were no entries", count > 0);
    }

	@Test
    public void testFindPostcode() {
        Postcode obj = dod.getRandomPostcode();
        Assert.assertNotNull("Data on demand for 'Postcode' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Postcode' failed to provide an identifier", id);
        obj = postcodeService.findPostcode(id);
        Assert.assertNotNull("Find method for 'Postcode' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'Postcode' returned the incorrect identifier", id, obj.getId());
    }

	@Test
    public void testFindAllPostcodes() {
        Assert.assertNotNull("Data on demand for 'Postcode' failed to initialize correctly", dod.getRandomPostcode());
        long count = postcodeService.countAllPostcodes();
        Assert.assertTrue("Too expensive to perform a find all test for 'Postcode', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<Postcode> result = postcodeService.findAllPostcodes();
        Assert.assertNotNull("Find all method for 'Postcode' illegally returned null", result);
        Assert.assertTrue("Find all method for 'Postcode' failed to return any data", result.size() > 0);
    }

	@Test
    public void testFindPostcodeEntries() {
        Assert.assertNotNull("Data on demand for 'Postcode' failed to initialize correctly", dod.getRandomPostcode());
        long count = postcodeService.countAllPostcodes();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<Postcode> result = postcodeService.findPostcodeEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'Postcode' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'Postcode' returned an incorrect number of entries", count, result.size());
    }

	@Test
    public void testFlush() {
        Postcode obj = dod.getRandomPostcode();
        Assert.assertNotNull("Data on demand for 'Postcode' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Postcode' failed to provide an identifier", id);
        obj = postcodeService.findPostcode(id);
        Assert.assertNotNull("Find method for 'Postcode' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyPostcode(obj);
        Integer currentVersion = obj.getVersion();
        postcodeRepository.flush();
        Assert.assertTrue("Version for 'Postcode' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testUpdatePostcodeUpdate() {
        Postcode obj = dod.getRandomPostcode();
        Assert.assertNotNull("Data on demand for 'Postcode' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Postcode' failed to provide an identifier", id);
        obj = postcodeService.findPostcode(id);
        boolean modified =  dod.modifyPostcode(obj);
        Integer currentVersion = obj.getVersion();
        Postcode merged = postcodeService.updatePostcode(obj);
        postcodeRepository.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'Postcode' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testSavePostcode() {
        Assert.assertNotNull("Data on demand for 'Postcode' failed to initialize correctly", dod.getRandomPostcode());
        Postcode obj = dod.getNewTransientPostcode(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'Postcode' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'Postcode' identifier to be null", obj.getId());
        postcodeService.savePostcode(obj);
        postcodeRepository.flush();
        Assert.assertNotNull("Expected 'Postcode' identifier to no longer be null", obj.getId());
    }

	@Test
    public void testDeletePostcode() {
        Postcode obj = dod.getRandomPostcode();
        Assert.assertNotNull("Data on demand for 'Postcode' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Postcode' failed to provide an identifier", id);
        obj = postcodeService.findPostcode(id);
        postcodeService.deletePostcode(obj);
        postcodeRepository.flush();
        Assert.assertNull("Failed to remove 'Postcode' with identifier '" + id + "'", postcodeService.findPostcode(id));
    }
}
