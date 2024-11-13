package com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SystemAppFeedBack<T> implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private T data;
	private String message;
    private boolean status;

//    public SystemAppFeedBack() {
//        this.message = "An Error occurred while initiating Transaction";
//        this.status = false;
//    }

    public SystemAppFeedBack(T data, String message) {
        this.data = data;
        this.message = message;
        this.status = true;
    }
}
