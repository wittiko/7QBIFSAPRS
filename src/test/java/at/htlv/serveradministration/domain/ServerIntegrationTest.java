package at.htlv.serveradministration.domain;

import at.htlv.serveradministration.repository.ServerRepository;
import at.htlv.serveradministration.service.ServerService;
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
public class ServerIntegrationTest {

    @Test
    public void testMarkerMethod() {
    }

	@Autowired
    private ServerDataOnDemand dod;

	@Autowired
    ServerService serverService;

	@Autowired
    ServerRepository serverRepository;

	@Test
    public void testCountAllServers() {
        Assert.assertNotNull("Data on demand for 'Server' failed to initialize correctly", dod.getRandomServer());
        long count = serverService.countAllServers();
        Assert.assertTrue("Counter for 'Server' incorrectly reported there were no entries", count > 0);
    }

	@Test
    public void testFindServer() {
        Server obj = dod.getRandomServer();
        Assert.assertNotNull("Data on demand for 'Server' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Server' failed to provide an identifier", id);
        obj = serverService.findServer(id);
        Assert.assertNotNull("Find method for 'Server' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'Server' returned the incorrect identifier", id, obj.getId());
    }

	@Test
    public void testFindAllServers() {
        Assert.assertNotNull("Data on demand for 'Server' failed to initialize correctly", dod.getRandomServer());
        long count = serverService.countAllServers();
        Assert.assertTrue("Too expensive to perform a find all test for 'Server', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<Server> result = serverService.findAllServers();
        Assert.assertNotNull("Find all method for 'Server' illegally returned null", result);
        Assert.assertTrue("Find all method for 'Server' failed to return any data", result.size() > 0);
    }

	@Test
    public void testFindServerEntries() {
        Assert.assertNotNull("Data on demand for 'Server' failed to initialize correctly", dod.getRandomServer());
        long count = serverService.countAllServers();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<Server> result = serverService.findServerEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'Server' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'Server' returned an incorrect number of entries", count, result.size());
    }

	@Test
    public void testFlush() {
        Server obj = dod.getRandomServer();
        Assert.assertNotNull("Data on demand for 'Server' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Server' failed to provide an identifier", id);
        obj = serverService.findServer(id);
        Assert.assertNotNull("Find method for 'Server' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyServer(obj);
        Integer currentVersion = obj.getVersion();
        serverRepository.flush();
        Assert.assertTrue("Version for 'Server' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testUpdateServerUpdate() {
        Server obj = dod.getRandomServer();
        Assert.assertNotNull("Data on demand for 'Server' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Server' failed to provide an identifier", id);
        obj = serverService.findServer(id);
        boolean modified =  dod.modifyServer(obj);
        Integer currentVersion = obj.getVersion();
        Server merged = serverService.updateServer(obj);
        serverRepository.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'Server' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testSaveServer() {
        Assert.assertNotNull("Data on demand for 'Server' failed to initialize correctly", dod.getRandomServer());
        Server obj = dod.getNewTransientServer(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'Server' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'Server' identifier to be null", obj.getId());
        serverService.saveServer(obj);
        serverRepository.flush();
        Assert.assertNotNull("Expected 'Server' identifier to no longer be null", obj.getId());
    }

	@Test
    public void testDeleteServer() {
        Server obj = dod.getRandomServer();
        Assert.assertNotNull("Data on demand for 'Server' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Server' failed to provide an identifier", id);
        obj = serverService.findServer(id);
        serverService.deleteServer(obj);
        serverRepository.flush();
        Assert.assertNull("Failed to remove 'Server' with identifier '" + id + "'", serverService.findServer(id));
    }
	
	@Test
	public void testFindingServerByProducerValid() {
		Server s1 = new Server();
		
		s1.setProducer(Producerserver.IBM);
		s1.setComment("irgendwas");
		s1.setCpucount(1);
		s1.setCpufreq(2000);
		s1.setHdd(1000);
		s1.setModel("dl380");
		s1.setHostname("pmx-00");
		s1.setRam(2048);
		s1.setSerialnumber("123asdas1231");
		serverService.saveServer(s1);
		List<Server> l1 = serverService.findServerByProducer(Producerserver.IBM);
		Assert.assertEquals(new Integer(1), new Integer(l1.size()));

	}
	
	@Test
	public void testFindingServerByProducerInvalid() {
		List<Server> l1 = serverService.findServerByProducer(Producerserver.HP);
		Assert.assertEquals(new Integer(0), new Integer(l1.size()));
	}
	
	@Test
	public void testFindingServerByModelValid() {
		Server s1 = new Server();
		
		s1.setProducer(Producerserver.IBM);
		s1.setComment("irgendwas");
		s1.setCpucount(1);
		s1.setCpufreq(2000);
		s1.setHdd(1000);
		s1.setModel("dl380");
		s1.setHostname("pmx-00");
		s1.setRam(2048);
		s1.setSerialnumber("123asdas1231");
		serverService.saveServer(s1);
		
		List<Server> l1 = serverService.findServerByModel("dl380");
		Assert.assertEquals(new Integer(1), new Integer(l1.size()));
	}
	
	@Test
	public void testFindingServerByModelInvalid() {
		
		List<Server> l1 = serverService.findServerByModel("dashkasdhgas");
		
		Assert.assertEquals(new Integer(0), new Integer(l1.size()));
	}
}
