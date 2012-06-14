package at.htlv.serveradministration.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity
public class Server {

    @NotNull
    @Column(unique = true)
    private String hostname;

    @Enumerated(EnumType.STRING)
    private Producerserver producer;

    private String serialnumber;

    @NotNull
    private String model;

    @Min(1024L)
    @Max(262144L)
    private Integer ram;

    @Min(200L)
    @Max(6000L)
    private Integer cpufreq;

    @Min(1L)
    @Max(200L)
    private Integer cpucount;

    @Min(32L)
    private Integer hdd;

    private String comment;

    @ManyToOne
    private Rack rack;

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

	public String getHostname() {
        return this.hostname;
    }

	public void setHostname(String hostname) {
        this.hostname = hostname;
    }

	public Producerserver getProducer() {
        return this.producer;
    }

	public void setProducer(Producerserver producer) {
        this.producer = producer;
    }

	public String getSerialnumber() {
        return this.serialnumber;
    }

	public void setSerialnumber(String serialnumber) {
        this.serialnumber = serialnumber;
    }

	public String getModel() {
        return this.model;
    }

	public void setModel(String model) {
        this.model = model;
    }

	public Integer getRam() {
        return this.ram;
    }

	public void setRam(Integer ram) {
        this.ram = ram;
    }

	public Integer getCpufreq() {
        return this.cpufreq;
    }

	public void setCpufreq(Integer cpufreq) {
        this.cpufreq = cpufreq;
    }

	public Integer getCpucount() {
        return this.cpucount;
    }

	public void setCpucount(Integer cpucount) {
        this.cpucount = cpucount;
    }

	public Integer getHdd() {
        return this.hdd;
    }

	public void setHdd(Integer hdd) {
        this.hdd = hdd;
    }

	public String getComment() {
        return this.comment;
    }

	public void setComment(String comment) {
        this.comment = comment;
    }

	public Rack getRack() {
        return this.rack;
    }

	public void setRack(Rack rack) {
        this.rack = rack;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
