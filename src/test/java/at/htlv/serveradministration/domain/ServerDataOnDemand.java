package at.htlv.serveradministration.domain;

import at.htlv.serveradministration.repository.ServerRepository;
import at.htlv.serveradministration.service.ServerService;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

@Configurable
@Component
public class ServerDataOnDemand {

	private Random rnd = new SecureRandom();

	private List<Server> data;

	@Autowired
    private RackDataOnDemand rackDataOnDemand;

	@Autowired
    ServerService serverService;

	@Autowired
    ServerRepository serverRepository;

	public Server getNewTransientServer(int index) {
        Server obj = new Server();
        setComment(obj, index);
        setCpucount(obj, index);
        setCpufreq(obj, index);
        setHdd(obj, index);
        setHostname(obj, index);
        setModel(obj, index);
        setProducer(obj, index);
        setRack(obj, index);
        setRam(obj, index);
        setSerialnumber(obj, index);
        return obj;
    }

	public void setComment(Server obj, int index) {
        String comment = "comment_" + index;
        obj.setComment(comment);
    }

	public void setCpucount(Server obj, int index) {
        Integer cpucount = new Integer(index);
        if (cpucount < 1 || cpucount > 200) {
            cpucount = 200;
        }
        obj.setCpucount(cpucount);
    }

	public void setCpufreq(Server obj, int index) {
        Integer cpufreq = new Integer(index);
        if (cpufreq < 200 || cpufreq > 6000) {
            cpufreq = 6000;
        }
        obj.setCpufreq(cpufreq);
    }

	public void setHdd(Server obj, int index) {
        Integer hdd = new Integer(index);
        if (hdd < 32) {
            hdd = 32;
        }
        obj.setHdd(hdd);
    }

	public void setHostname(Server obj, int index) {
        String hostname = "hostname_" + index;
        obj.setHostname(hostname);
    }

	public void setModel(Server obj, int index) {
        String model = "model_" + index;
        obj.setModel(model);
    }

	public void setProducer(Server obj, int index) {
        Producerserver producer = Producerserver.class.getEnumConstants()[0];
        obj.setProducer(producer);
    }

	public void setRack(Server obj, int index) {
        Rack rack = rackDataOnDemand.getRandomRack();
        obj.setRack(rack);
    }

	public void setRam(Server obj, int index) {
        Integer ram = new Integer(index);
        if (ram < 1024 || ram > 262144) {
            ram = 262144;
        }
        obj.setRam(ram);
    }

	public void setSerialnumber(Server obj, int index) {
        String serialnumber = "serialnumber_" + index;
        obj.setSerialnumber(serialnumber);
    }

	public Server getSpecificServer(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        Server obj = data.get(index);
        Long id = obj.getId();
        return serverService.findServer(id);
    }

	public Server getRandomServer() {
        init();
        Server obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return serverService.findServer(id);
    }

	public boolean modifyServer(Server obj) {
        return false;
    }

	public void init() {
        int from = 0;
        int to = 10;
        data = serverService.findServerEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'Server' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<Server>();
        for (int i = 0; i < 10; i++) {
            Server obj = getNewTransientServer(i);
            try {
                serverService.saveServer(obj);
            } catch (ConstraintViolationException e) {
                StringBuilder msg = new StringBuilder();
                for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                    ConstraintViolation<?> cv = iter.next();
                    msg.append("[").append(cv.getConstraintDescriptor()).append(":").append(cv.getMessage()).append("=").append(cv.getInvalidValue()).append("]");
                }
                throw new RuntimeException(msg.toString(), e);
            }
            serverRepository.flush();
            data.add(obj);
        }
    }
}
