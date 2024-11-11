package com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//@NamedEntityGraph(name = "systemUserGroup-systemMenu-detail-graph" , attributeNodes = {
//		@NamedAttributeNode(value = "systemUserGroup"),
//		@NamedAttributeNode(value = "systemMenu"),
//		@NamedAttributeNode(value = "createdBy"),
//		@NamedAttributeNode(value = "updatedBy"),
//})
@Entity
@Table(name="SystemUserGroupSystemMenus")
//@Cache(region = "SystemUserGroupSystemMenuCache", usage = CacheConcurrencyStrategy.READ_WRITE)
@Setter
@Getter
@NoArgsConstructor
public class SystemUserGroupSystemMenu extends ParentEntity {

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	private SystemUserGroup systemUserGroup;

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	private SystemMenu systemMenu;

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	private SystemUserProfile createdBy;

	@ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
	private SystemUserProfile updatedBy;

	public SystemUserGroupSystemMenu(SystemUserGroup systemUserGroup, SystemMenu systemMenu) {
		this.systemUserGroup = systemUserGroup;
		this.systemMenu = systemMenu;
	}
}
