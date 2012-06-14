package at.htlv.serveradministration.web;

import at.htlv.serveradministration.domain.Producerrack;
import at.htlv.serveradministration.domain.Rack;
import at.htlv.serveradministration.service.DatacenterService;
import at.htlv.serveradministration.service.RackService;
import at.htlv.serveradministration.service.ServerService;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

@RequestMapping("/racks")
@Controller
public class RackController {

	@Autowired
    RackService rackService;

	@Autowired
    DatacenterService datacenterService;

	@Autowired
    ServerService serverService;

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid Rack rack, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, rack);
            return "racks/create";
        }
        uiModel.asMap().clear();
        rackService.saveRack(rack);
        return "redirect:/racks/" + encodeUrlPathSegment(rack.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new Rack());
        return "racks/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("rack", rackService.findRack(id));
        uiModel.addAttribute("itemId", id);
        return "racks/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("racks", rackService.findRackEntries(firstResult, sizeNo));
            float nrOfPages = (float) rackService.countAllRacks() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("racks", rackService.findAllRacks());
        }
        return "racks/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid Rack rack, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, rack);
            return "racks/update";
        }
        uiModel.asMap().clear();
        rackService.updateRack(rack);
        return "redirect:/racks/" + encodeUrlPathSegment(rack.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, rackService.findRack(id));
        return "racks/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Rack rack = rackService.findRack(id);
        rackService.deleteRack(rack);
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/racks";
    }

	void populateEditForm(Model uiModel, Rack rack) {
        uiModel.addAttribute("rack", rack);
        uiModel.addAttribute("datacenters", datacenterService.findAllDatacenters());
        uiModel.addAttribute("producerracks", Arrays.asList(Producerrack.values()));
        uiModel.addAttribute("servers", serverService.findAllServers());
    }

	String encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
        String enc = httpServletRequest.getCharacterEncoding();
        if (enc == null) {
            enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
        }
        try {
            pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
        } catch (UnsupportedEncodingException uee) {}
        return pathSegment;
    }
}
