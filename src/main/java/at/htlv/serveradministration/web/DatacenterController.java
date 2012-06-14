package at.htlv.serveradministration.web;

import at.htlv.serveradministration.domain.Datacenter;
import at.htlv.serveradministration.service.DatacenterService;
import at.htlv.serveradministration.service.PostcodeService;
import at.htlv.serveradministration.service.RackService;
import java.io.UnsupportedEncodingException;
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

@RequestMapping("/datacenters")
@Controller
public class DatacenterController {

	@Autowired
    DatacenterService datacenterService;

	@Autowired
    PostcodeService postcodeService;

	@Autowired
    RackService rackService;

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid Datacenter datacenter, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, datacenter);
            return "datacenters/create";
        }
        uiModel.asMap().clear();
        datacenterService.saveDatacenter(datacenter);
        return "redirect:/datacenters/" + encodeUrlPathSegment(datacenter.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new Datacenter());
        return "datacenters/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("datacenter", datacenterService.findDatacenter(id));
        uiModel.addAttribute("itemId", id);
        return "datacenters/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("datacenters", datacenterService.findDatacenterEntries(firstResult, sizeNo));
            float nrOfPages = (float) datacenterService.countAllDatacenters() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("datacenters", datacenterService.findAllDatacenters());
        }
        return "datacenters/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid Datacenter datacenter, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, datacenter);
            return "datacenters/update";
        }
        uiModel.asMap().clear();
        datacenterService.updateDatacenter(datacenter);
        return "redirect:/datacenters/" + encodeUrlPathSegment(datacenter.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, datacenterService.findDatacenter(id));
        return "datacenters/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Datacenter datacenter = datacenterService.findDatacenter(id);
        datacenterService.deleteDatacenter(datacenter);
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/datacenters";
    }

	void populateEditForm(Model uiModel, Datacenter datacenter) {
        uiModel.addAttribute("datacenter", datacenter);
        uiModel.addAttribute("postcodes", postcodeService.findAllPostcodes());
        uiModel.addAttribute("racks", rackService.findAllRacks());
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
