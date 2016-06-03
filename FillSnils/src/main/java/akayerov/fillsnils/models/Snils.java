package akayerov.fillsnils.models;

import java.io.Serializable;

import javax.persistence.*;

import java.util.Date;
import java.sql.Timestamp;


/**
 * The persistent class for the prg database table.
 * 
 */
@Entity
public class Snils implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="snils", columnDefinition="bpchar")
	private String snils;
	@Column(name="ogrn", columnDefinition="bpchar")
	private String ogrn;


	public Snils() {
	}

	public String getSnils() {
		return this.snils;
	}
	public void setSnils(String snils) {
		this.snils =  snils;
	}

	public String getOgrn() {
		return this.ogrn;
	}
	public void setOgrn(String ogrn) {
		this.ogrn =  ogrn;
	}
   @Override
     public String toString(){
	       return "Snils="+snils+", Ogrn="+ogrn;
   }

}