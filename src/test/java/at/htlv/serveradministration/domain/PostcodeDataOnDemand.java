package at.htlv.serveradministration.domain;

import at.htlv.serveradministration.repository.PostcodeRepository;
import at.htlv.serveradministration.service.PostcodeService;
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
public class PostcodeDataOnDemand {

	private Random rnd = new SecureRandom();

	private List<Postcode> data;

	@Autowired
    PostcodeService postcodeService;

	@Autowired
    PostcodeRepository postcodeRepository;

	public Postcode getNewTransientPostcode(int index) {
        Postcode obj = new Postcode();
        setCity(obj, index);
        setPostcode(obj, index);
        return obj;
    }

	public void setCity(Postcode obj, int index) {
        String city = "city_" + index;
        obj.setCity(city);
    }

	public void setPostcode(Postcode obj, int index) {
        Integer postcode = new Integer(index);
        obj.setPostcode(postcode);
    }

	public Postcode getSpecificPostcode(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        Postcode obj = data.get(index);
        Long id = obj.getId();
        return postcodeService.findPostcode(id);
    }

	public Postcode getRandomPostcode() {
        init();
        Postcode obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return postcodeService.findPostcode(id);
    }

	public boolean modifyPostcode(Postcode obj) {
        return false;
    }

	public void init() {
        int from = 0;
        int to = 10;
        data = postcodeService.findPostcodeEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'Postcode' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<Postcode>();
        for (int i = 0; i < 10; i++) {
            Postcode obj = getNewTransientPostcode(i);
            try {
                postcodeService.savePostcode(obj);
            } catch (ConstraintViolationException e) {
                StringBuilder msg = new StringBuilder();
                for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                    ConstraintViolation<?> cv = iter.next();
                    msg.append("[").append(cv.getConstraintDescriptor()).append(":").append(cv.getMessage()).append("=").append(cv.getInvalidValue()).append("]");
                }
                throw new RuntimeException(msg.toString(), e);
            }
            postcodeRepository.flush();
            data.add(obj);
        }
    }
}
