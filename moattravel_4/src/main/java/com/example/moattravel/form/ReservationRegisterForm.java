package com.example.moattravel.form;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

public class ReservationRegisterForm {
	private Integer houseId;

	private Integer usseId;

	private String checkinDate;

	private String checkoutDate;

	private Integer numberOfPeople;

	private Integer amount;

}
