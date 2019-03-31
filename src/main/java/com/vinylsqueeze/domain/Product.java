package com.vinylsqueeze.domain;

import com.vinylsqueeze.filemerger.CsvBindByName;
import com.vinylsqueeze.filemerger.CsvBindByPosition;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Product {

	@CsvBindByName(column = "Product ID")
	@CsvBindByPosition(position=0)
	private String id;

	@CsvBindByName(column = "Product SKU")
	@CsvBindByPosition(position=1)
	private String sku;

	@CsvBindByName(column = "Product Name")
	@CsvBindByPosition(position=2)
	private String name;

	@CsvBindByName(column = "Category String")
	@CsvBindByPosition(position=3)
	private String category;

	@CsvBindByName(column = "Description")
	@CsvBindByPosition(position=4)
	private String description;

	@CsvBindByName(column = "Track Inventory")
	@CsvBindByPosition(position=5)
	private String trackInventory;

	public boolean isValid() {
		return id != null && sku != null;
	}
}
