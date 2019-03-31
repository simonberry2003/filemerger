package com.vinylsqueeze.domain;

import com.vinylsqueeze.filemerger.CsvBindByPosition;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Track {

	@CsvBindByPosition(position=0)
	private String id;

	@CsvBindByPosition(position=1)
	private String sku;

	@CsvBindByPosition(position=2)
	private String listing;
}
