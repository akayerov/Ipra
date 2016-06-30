package developer.remarks.models;

import java.io.Serializable;

import javax.persistence.*;

import java.util.Date;
import java.sql.Timestamp;


/**
 * The persistent class for the prg database table.
 * 
 */
@Entity
//@NamedQuery(name="Prg.findAll", query="SELECT p FROM Prg p")
public class Prg implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Integer id;

	@Temporal(TemporalType.DATE)
	private Date bdate;

	@Temporal(TemporalType.DATE)
	private Date docdt;

	@Column(name="docnum", columnDefinition="bpchar")
	private String docnum;

	@Temporal(TemporalType.DATE)
	private Date dt;

	@Column(name="fname", columnDefinition="bpchar")
	private String fname;

	@Column(name="gndr", columnDefinition="int2")
	private Integer gndr;

	@Column(name="lname", columnDefinition="bpchar")
	private String lname;

	@Column(name="mseid", columnDefinition="bpchar")
	private String mseid;

	@Column(name="nreg", columnDefinition="int2")
	private Integer nreg;

	private Integer oivid;

	@Column(name="okr_id",  columnDefinition="int2")
	private Integer okrId;

	@Column(name="prg",  columnDefinition="int2")
	private Integer prg;

	@Temporal(TemporalType.DATE)
	private Date prgdt;

	@Column(name="prgnum", columnDefinition="bpchar")
	private String prgnum;

	@Column(name="sname", columnDefinition="bpchar")
	private String sname;

	@Column(name="snils", columnDefinition="bpchar")
	private String snils;

	private Timestamp udt;

	public Prg() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getBdate() {
		return this.bdate;
	}

	public void setBdate(Date bdate) {
		this.bdate = bdate;
	}

	public Date getDocdt() {
		return this.docdt;
	}

	public void setDocdt(Date docdt) {
		this.docdt = docdt;
	}

	public String getDocnum() {
		return this.docnum;
	}

	public void setDocnum(String docnum) {
		this.docnum = docnum;
	}

	public Date getDt() {
		return this.dt;
	}

	public void setDt(Date dt) {
		this.dt = dt;
	}

	public String getFname() {
		return this.fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public Integer getGndr() {
		return this.gndr;
	}

	public void setGndr(Integer gndr) {
		this.gndr = gndr;
	}

	public String getLname() {
		return this.lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	public String getMseid() {
		return this.mseid;
	}

	public void setMseid(String mseid) {
		this.mseid = mseid;
	}

	public Integer getNreg() {
		return this.nreg;
	}

	public void setNreg(Integer nreg) {
		this.nreg = nreg;
	}

	public Integer getOivid() {
		return this.oivid;
	}

	public void setOivid(Integer oivid) {
		this.oivid = oivid;
	}

	public Integer getOkrId() {
		return this.okrId;
	}

	public void setOkrId(Integer okrId) {
		this.okrId = okrId;
	}

	public Integer getPrg() {
		return this.prg;
	}

	public void setPrg(Integer prg) {
		this.prg = prg;
	}

	public Date getPrgdt() {
		return this.prgdt;
	}

	public void setPrgdt(Date prgdt) {
		this.prgdt = prgdt;
	}

	public String getPrgnum() {
		return this.prgnum;
	}

	public void setPrgnum(String prgnum) {
		this.prgnum = prgnum;
	}

	public String getSname() {
		return this.sname;
	}

	public void setSname(String sname) {
		this.sname = sname;
	}

	public String getSnils() {
		return this.snils;
	}

	public void setSnils(String snils) {
		this.snils = snils;
	}

	public Timestamp getUdt() {
		return this.udt;
	}

	public void setUdt(Timestamp udt) {
		this.udt = udt;
	}
   @Override
     public String toString(){
	       return "id="+id+", fname="+fname;
   }

}