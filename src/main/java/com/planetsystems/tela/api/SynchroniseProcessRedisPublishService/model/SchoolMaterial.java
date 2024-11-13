package com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name="SchoolMaterials")
public class SchoolMaterial extends ParentEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String code;
	private String materialName;
	
	public SchoolMaterial() {
		
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMaterialName() {
		return materialName;
	}

	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}
	
}
