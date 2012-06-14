package at.htlv.serveradministration.domain;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
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
public class Datacenter {

    @NotNull
    @Column(unique = true)
    private String name;

    @NotNull
    private String address;

    @Min(100L)
    private Integer powerconnection;

    @Min(1L)
    private Integer dataconnection;

    @ManyToOne
    private Postcode postcode;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Rack> racks = new HashSet<Rack>();

	public String getName() {
        return this.name;
    }

	public void setName(String name) {
        this.name = name;
    }

	public String getAddress() {
        return this.address;
    }

	public void setAddress(String address) {
        this.address = address;
    }

	public Integer getPowerconnection() {
        return this.powerconnection;
    }

	public void setPowerconnection(Integer powerconnection) {
        this.powerconnection = powerconnection;
    }

	public Integer getDataconnection() {
        return this.dataconnection;
    }

	public void setDataconnection(Integer dataconnection) {
        this.dataconnection = dataconnection;
    }

	public Postcode getPostcode() {
        return this.postcode;
    }

	public void setPostcode(Postcode postcode) {
        this.postcode = postcode;
    }

	public Set<Rack> getRacks() {
        return this.racks;
    }

	public void setRacks(Set<Rack> racks) {
        this.racks = racks;
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
