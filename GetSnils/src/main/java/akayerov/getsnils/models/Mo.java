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
@Table(name="rhb_exc")
public class Mo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	int id;

	@Column(name = "name", columnDefinition = "bpchar")
	private String name;
	@Column(name = "scode", columnDefinition = "bpchar")
	private String scode;
	@Column(name = "inn", columnDefinition = "bpchar")
	private String inn;
	@Column(name = "ogrn", columnDefinition = "bpchar")
	private String ogrn;

	public Mo() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setScode(String scode) {
		this.scode = scode;
	}

	public void setInn(String inn) {
		this.inn = inn;
	}

	public void setOgrn(String ogrn) {
		this.ogrn = ogrn;
	}

	public String getName() {
		return name;
	}

	public String getScode() {
		return scode;
	}

	public String getInn() {
		return inn;
	}

	public String getOgrn() {
		return ogrn;
	}

	@Override
	public String toString() {
		return "id=" + id + ", scode=" + scode + " ogrn=" + ogrn;
	}

}