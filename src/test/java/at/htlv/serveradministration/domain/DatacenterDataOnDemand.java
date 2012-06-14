package at.htlv.serveradministration.domain;

import at.htlv.serveradministration.repository.DatacenterRepository;
import at.htlv.serveradministration.service.DatacenterService;
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
public class DatacenterDataOnDemand {

	private Random rnd = new SecureRandom();

	private List<Datacenter> data;

	@Autowired
    private PostcodeDataOnDemand postcodeDataOnDemand;

	@Autowired
    DatacenterService datacenterService;

	@Autowired
    DatacenterRepository datacenterRepository;

	public Datacenter getNewTransientDatacenter(int index) {
        Datacenter obj = new Datacenter();
        setAddress(obj, index);
        setDataconnection(obj, index);
        setName(obj, index);
        setPostcode(obj, index);
        setPowerconnection(obj, index);
        return obj;
    }

	public void setAddress(Datacenter obj, int index) {
        String address = "address_" + index;
        obj.setAddress(address);
    }

	public void setDataconnection(Datacenter obj, int index) {
        Integer dataconnection = new Integer(index);
        if (dataconnection < 1) {
            dataconnection = 1;
        }
        obj.setDataconnection(dataconnection);
    }

	public void setName(Datacenter obj, int index) {
        String name = "name_" + index;
        obj.setName(name);
    }

	public void setPostcode(Datacenter obj, int index) {
        Postcode postcode = postcodeDataOnDemand.getRandomPostcode();
        obj.setPostcode(postcode);
    }

	public void setPowerconnection(Datacenter obj, int index) {
        Integer powerconnection = new Integer(index);
        if (powerconnection < 100) {
            powerconnection = 100;
        }
        obj.setPowerconnection(powerconnection);
    }

	public Datacenter getSpecificDatacenter(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        Datacenter obj = data.get(index);
        Long id = obj.getId();
        return datacenterService.findDatacenter(id);
    }

	public Datacenter getRandomDatacenter() {
        init();
        Datacenter obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return datacenterService.findDatacenter(id);
    }

	public boolean modifyDatacenter(Datacenter obj) {
        return false;
    }

	public void init() {
        int from = 0;
        int to = 10;
        data = datacenterService.findDatacenterEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'Datacenter' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<Datacenter>();
        for (int i = 0; i < 10; i++) {
            Datacenter obj = getNewTransientDatacenter(i);
            try {
                datacenterService.saveDatacenter(obj);
            } catch (ConstraintViolationException e) {
                StringBuilder msg = new StringBuilder();
                for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                    ConstraintViolation<?> cv = iter.next();
                    msg.append("[").append(cv.getConstraintDescriptor()).append(":").append(cv.getMessage()).append("=").append(cv.getInvalidValue()).append("]");
                }
                throw new RuntimeException(msg.toString(), e);
            }
            datacenterRepository.flush();
            data.add(obj);
        }
    }
}
