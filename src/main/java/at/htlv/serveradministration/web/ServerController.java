package at.htlv.serveradministration.web;

import at.htlv.serveradministration.domain.Producerserver;
import at.htlv.serveradministration.domain.Server;
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

@RequestMapping("/servers")
@Controller
public class ServerController {

	@Autowired
    ServerService serverService;

	@Autowired
    RackService rackService;

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid Server server, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, server);
            return "servers/create";
        }
        uiModel.asMap().clear();
        serverService.saveServer(server);
        return "redirect:/servers/" + encodeUrlPathSegment(server.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new Server());
        return "servers/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("server", serverService.findServer(id));
        uiModel.addAttribute("itemId", id);
        return "servers/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("servers", serverService.findServerEntries(firstResult, sizeNo));
            float nrOfPages = (float) serverService.countAllServers() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("servers", serverService.findAllServers());
        }
        return "servers/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid Server server, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, server);
            return "servers/update";
        }
        uiModel.asMap().clear();
        serverService.updateServer(server);
        return "redirect:/servers/" + encodeUrlPathSegment(server.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, serverService.findServer(id));
        return "servers/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Server server = serverService.findServer(id);
        serverService.deleteServer(server);
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/servers";
    }

	void populateEditForm(Model uiModel, Server server) {
        uiModel.addAttribute("server", server);
        uiModel.addAttribute("producerservers", Arrays.asList(Producerserver.values()));
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
