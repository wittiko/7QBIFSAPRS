package at.htlv.serveradministration.domain;

import at.htlv.serveradministration.repository.RackRepository;
import at.htlv.serveradministration.service.RackService;
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

@Component
@Configurable
public class RackDataOnDemand {

	private Random rnd = new SecureRandom();

	private List<Rack> data;

	@Autowired
    private DatacenterDataOnDemand datacenterDataOnDemand;

	@Autowired
    RackService rackService;

	@Autowired
    RackRepository rackRepository;

	public Rack getNewTransientRack(int index) {
        Rack obj = new Rack();
        setDatacenter(obj, index);
        setHe(obj, index);
        setIdentnr(obj, index);
        setLine(obj, index);
        setLocation(obj, index);
        setProducer(obj, index);
        return obj;
    }

	public void setDatacenter(Rack obj, int index) {
        Datacenter datacenter = datacenterDataOnDemand.getRandomDatacenter();
        obj.setDatacenter(datacenter);
    }

	public void setHe(Rack obj, int index) {
        Integer he = new Integer(index);
        if (he < 24) {
            he = 24;
        }
        obj.setHe(he);
    }

	public void setIdentnr(Rack obj, int index) {
        Integer identnr = new Integer(index);
        obj.setIdentnr(identnr);
    }

	public void setLine(Rack obj, int index) {
        Integer line = new Integer(index);
        if (line < 1) {
            line = 1;
        }
        obj.setLine(line);
    }

	public void setLocation(Rack obj, int index) {
        Integer location = new Integer(index);
        if (location < 1) {
            location = 1;
        }
        obj.setLocation(location);
    }

	public void setProducer(Rack obj, int index) {
        Producerrack producer = Producerrack.class.getEnumConstants()[0];
        obj.setProducer(producer);
    }

	public Rack getSpecificRack(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        Rack obj = data.get(index);
        Long id = obj.getId();
        return rackService.findRack(id);
    }

	public Rack getRandomRack() {
        init();
        Rack obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return rackService.findRack(id);
    }

	public boolean modifyRack(Rack obj) {
        return false;
    }

	public void init() {
        int from = 0;
        int to = 10;
        data = rackService.findRackEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'Rack' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<Rack>();
        for (int i = 0; i < 10; i++) {
            Rack obj = getNewTransientRack(i);
            try {
                rackService.saveRack(obj);
            } catch (ConstraintViolationException e) {
                StringBuilder msg = new StringBuilder();
                for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                    ConstraintViolation<?> cv = iter.next();
                    msg.append("[").append(cv.getConstraintDescriptor()).append(":").append(cv.getMessage()).append("=").append(cv.getInvalidValue()).append("]");
                }
                throw new RuntimeException(msg.toString(), e);
            }
            rackRepository.flush();
            data.add(obj);
        }
    }
}
