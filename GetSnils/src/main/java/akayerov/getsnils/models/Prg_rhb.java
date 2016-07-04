package akayerov.getsnils.models;

import java.io.Serializable;

import javax.persistence.*;

import java.util.Date;
import java.sql.Timestamp;


/**
 * The persistent class for the prg database table.
 * 
 */
@Entity
@Table(name="prg_rhb")
//@NamedQuery(name="Prg.findAll", query="SELECT p FROM Prg p")
public class Prg_rhb implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private Integer prgid;
	private Integer typeid;
	private Integer evntid;
	private Integer dicid;
	private Integer tsrid;
	@Column(name="name", columnDefinition="bpchar")
	private String name;
	@Temporal(TemporalType.DATE)
	private Date dt_exc;
	private Integer excid;
	@Column(name="execut", columnDefinition="bpchar")
	private String execut;
	private Integer resid;
	private Integer par1;
	private Integer par2;
	private Integer par3;
	@Column(name="result", columnDefinition="bpchar")
	private String result;
	@Column(name="note", columnDefinition="bpchar")
	private String note;
	private Timestamp udt;

	public Prg_rhb() {
	}

	
   public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public Integer getPrgid() {
		return prgid;
	}


	public void setPrgid(Integer prgid) {
		this.prgid = prgid;
	}


	public Integer getTypeid() {
		return typeid;
	}


	public void setTypeid(Integer typeid) {
		this.typeid = typeid;
	}


	public Integer getEvntid() {
		return evntid;
	}


	public void setEvntid(Integer evntid) {
		this.evntid = evntid;
	}


	public Integer getDicid() {
		return dicid;
	}


	public void setDicid(Integer dicid) {
		this.dicid = dicid;
	}


	public Integer getTsrid() {
		return tsrid;
	}


	public void setTsrid(Integer tsrid) {
		this.tsrid = tsrid;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public Date getDt_exc() {
		return dt_exc;
	}


	public void setDt_exc(Date dt_exc) {
		this.dt_exc = dt_exc;
	}


	public Integer getExcid() {
		return excid;
	}


	public void setExcid(Integer excid) {
		this.excid = excid;
	}


	public String getExecut() {
		return execut;
	}


	public void setExecut(String execut) {
		this.execut = execut;
	}


	public Integer getResid() {
		return resid;
	}


	public void setResid(Integer resid) {
		this.resid = resid;
	}


	public Integer getPar1() {
		return par1;
	}


	public void setPar1(Integer par1) {
		this.par1 = par1;
	}


	public Integer getPar2() {
		return par2;
	}


	public void setPar2(Integer par2) {
		this.par2 = par2;
	}


	public Integer getPar3() {
		return par3;
	}


	public void setPar3(Integer par3) {
		this.par3 = par3;
	}


	public String getResult() {
		return result;
	}


	public void setResult(String result) {
		this.result = result;
	}


	public String getNote() {
		return note;
	}


	public void setNote(String note) {
		this.note = note;
	}


@Override
     public String toString(){
	       return "id="+id+", result="+result;
   }

}