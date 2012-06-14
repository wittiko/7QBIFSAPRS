package at.htlv.serveradministration.domain;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Version;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity
public class Rack {

    @NotNull
    @Column(unique = true)
    private Integer identnr;

    @NotNull
    @Min(1L)
    private Integer line;

    @NotNull
    @Min(1L)
    private Integer location;

    @NotNull
    @Min(24L)
    private Integer he;

    @Enumerated(EnumType.STRING)
    private Producerrack producer;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Server> servers = new HashSet<Server>();

    @ManyToOne
    private Datacenter datacenter;

	public Integer getIdentnr() {
        return this.identnr;
    }

	public void setIdentnr(Integer identnr) {
        this.identnr = identnr;
    }

	public Integer getLine() {
        return this.line;
    }

	public void setLine(Integer line) {
        this.line = line;
    }

	public Integer getLocation() {
        return this.location;
    }

	public void setLocation(Integer location) {
        this.location = location;
    }

	public Integer getHe() {
        return this.he;
    }

	public void setHe(Integer he) {
        this.he = he;
    }

	public Producerrack getProducer() {
        return this.producer;
    }

	public void setProducer(Producerrack producer) {
        this.producer = producer;
    }

	public Set<Server> getServers() {
        return this.servers;
    }

	public void setServers(Set<Server> servers) {
        this.servers = servers;
    }

	public Datacenter getDatacenter() {
        return this.datacenter;
    }

	public void setDatacenter(Datacenter datacenter) {
        this.datacenter = datacenter;
    }

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

	@Version
    @Column(name = "version")
    private Integer version;

	public Long getId() {
        return this.id;
    }

	public void setId(Long id) {
        this.id = id;
    }

	public Integer getVersion() {
        return this.version;
    }

	public void setVersion(Integer version) {
        this.version = version;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
