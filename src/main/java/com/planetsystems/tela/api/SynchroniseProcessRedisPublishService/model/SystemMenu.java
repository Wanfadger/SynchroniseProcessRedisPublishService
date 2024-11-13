package com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.model;

import com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.model.enums.NavigationMenu;
import com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.model.enums.Status;
import com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.model.enums.SubMenuItem;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * rename to SideNavMenu(SideNavTitle , SideNaveItem)
 */
@Entity
@Table(name="SystemMenus")
@Setter
@Getter
public class SystemMenu extends ParentEntity {
	private NavigationMenu navigationMenu;
	private SubMenuItem subMenuItem;

	private Status activationStatus;

	public SystemMenu() { }

	public SystemMenu(String id) {
		super(id);
	}


	public SystemMenu(NavigationMenu navigationMenu, SubMenuItem subMenuItem) {
		this.navigationMenu = navigationMenu;
		this.subMenuItem = subMenuItem;
		this.activationStatus = Status.ACTIVE;
	}

	@PrePersist()
	void onPersist(){
		this.setCreatedDateTime(LocalDateTime.now());
		this.setUpdatedDateTime(LocalDateTime.now());
		this.activationStatus = Status.ACTIVE;
	}


	@Override
	public String toString() {
		return "SystemMenu{" +
				"navigationMenu=" + navigationMenu +
				", subMenuItem=" + subMenuItem +
				", activationStatus=" + activationStatus +
				'}';
	}
}
