package at.htlv.serveradministration.web;

import at.htlv.serveradministration.domain.Datacenter;
import at.htlv.serveradministration.domain.Postcode;
import at.htlv.serveradministration.domain.Rack;
import at.htlv.serveradministration.domain.Server;
import at.htlv.serveradministration.service.DatacenterService;
import at.htlv.serveradministration.service.PostcodeService;
import at.htlv.serveradministration.service.RackService;
import at.htlv.serveradministration.service.ServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.support.FormattingConversionServiceFactoryBean;

@Configurable
/**
 * A central place to register application converters and formatters. 
 */
public class ApplicationConversionServiceFactoryBean extends FormattingConversionServiceFactoryBean {

	@Override
	protected void installFormatters(FormatterRegistry registry) {
		super.installFormatters(registry);
		// Register application converters and formatters
	}

	@Autowired
    DatacenterService datacenterService;

	@Autowired
    PostcodeService postcodeService;

	@Autowired
    RackService rackService;

	@Autowired
    ServerService serverService;

	public Converter<Datacenter, String> getDatacenterToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<at.htlv.serveradministration.domain.Datacenter, java.lang.String>() {
            public String convert(Datacenter datacenter) {
                return new StringBuilder().append(datacenter.getName()).append(" ").append(datacenter.getAddress()).append(" ").append(datacenter.getPowerconnection()).append(" ").append(datacenter.getDataconnection()).toString();
            }
        };
    }

	public Converter<Long, Datacenter> getIdToDatacenterConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, at.htlv.serveradministration.domain.Datacenter>() {
            public at.htlv.serveradministration.domain.Datacenter convert(java.lang.Long id) {
                return datacenterService.findDatacenter(id);
            }
        };
    }

	public Converter<String, Datacenter> getStringToDatacenterConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, at.htlv.serveradministration.domain.Datacenter>() {
            public at.htlv.serveradministration.domain.Datacenter convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Datacenter.class);
            }
        };
    }

	public Converter<Postcode, String> getPostcodeToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<at.htlv.serveradministration.domain.Postcode, java.lang.String>() {
            public String convert(Postcode postcode) {
                return new StringBuilder().append(postcode.getPostcode()).append(" ").append(postcode.getCity()).toString();
            }
        };
    }

	public Converter<Long, Postcode> getIdToPostcodeConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, at.htlv.serveradministration.domain.Postcode>() {
            public at.htlv.serveradministration.domain.Postcode convert(java.lang.Long id) {
                return postcodeService.findPostcode(id);
            }
        };
    }

	public Converter<String, Postcode> getStringToPostcodeConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, at.htlv.serveradministration.domain.Postcode>() {
            public at.htlv.serveradministration.domain.Postcode convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Postcode.class);
            }
        };
    }

	public Converter<Rack, String> getRackToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<at.htlv.serveradministration.domain.Rack, java.lang.String>() {
            public String convert(Rack rack) {
                return new StringBuilder().append(rack.getIdentnr()).append(" ").append(rack.getLine()).append(" ").append(rack.getLocation()).append(" ").append(rack.getHe()).toString();
            }
        };
    }

	public Converter<Long, Rack> getIdToRackConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, at.htlv.serveradministration.domain.Rack>() {
            public at.htlv.serveradministration.domain.Rack convert(java.lang.Long id) {
                return rackService.findRack(id);
            }
        };
    }

	public Converter<String, Rack> getStringToRackConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, at.htlv.serveradministration.domain.Rack>() {
            public at.htlv.serveradministration.domain.Rack convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Rack.class);
            }
        };
    }

	public Converter<Server, String> getServerToStringConverter() {
        return new org.springframework.core.convert.converter.Converter<at.htlv.serveradministration.domain.Server, java.lang.String>() {
            public String convert(Server server) {
                return new StringBuilder().append(server.getHostname()).append(" ").append(server.getSerialnumber()).append(" ").append(server.getModel()).append(" ").append(server.getRam()).toString();
            }
        };
    }

	public Converter<Long, Server> getIdToServerConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.Long, at.htlv.serveradministration.domain.Server>() {
            public at.htlv.serveradministration.domain.Server convert(java.lang.Long id) {
                return serverService.findServer(id);
            }
        };
    }

	public Converter<String, Server> getStringToServerConverter() {
        return new org.springframework.core.convert.converter.Converter<java.lang.String, at.htlv.serveradministration.domain.Server>() {
            public at.htlv.serveradministration.domain.Server convert(String id) {
                return getObject().convert(getObject().convert(id, Long.class), Server.class);
            }
        };
    }

	public void installLabelConverters(FormatterRegistry registry) {
        registry.addConverter(getDatacenterToStringConverter());
        registry.addConverter(getIdToDatacenterConverter());
        registry.addConverter(getStringToDatacenterConverter());
        registry.addConverter(getPostcodeToStringConverter());
        registry.addConverter(getIdToPostcodeConverter());
        registry.addConverter(getStringToPostcodeConverter());
        registry.addConverter(getRackToStringConverter());
        registry.addConverter(getIdToRackConverter());
        registry.addConverter(getStringToRackConverter());
        registry.addConverter(getServerToStringConverter());
        registry.addConverter(getIdToServerConverter());
        registry.addConverter(getStringToServerConverter());
    }

	public void afterPropertiesSet() {
        super.afterPropertiesSet();
        installLabelConverters(getObject());
    }
}
