package com.vinylsqueeze.filemerger;

import java.io.FileReader;
import java.io.Reader;

import com.google.common.base.Preconditions;

public class CsvToBeanBuilder<T> {

	private Reader reader;
	private Class<?> clazz;
	private String separator;
	private boolean skipFirstRow;
	private boolean keepQuotes;

	public CsvToBeanBuilder(FileReader reader) {
		this.reader = Preconditions.checkNotNull(reader);
	}

	public CsvToBeanBuilder<T> withSeparator(String separator) {
		this.separator = Preconditions.checkNotNull(separator);
		return this;
	}

	public CsvToBeanBuilder<T> withType(Class<?> clazz) {
		this.clazz = Preconditions.checkNotNull(clazz);
		return this;
	}

	public CsvToBeanBuilder<T> skipFirstRow(boolean skipFirstRow) {
		this.skipFirstRow = skipFirstRow;
		return this;
	}

	public CsvToBeanBuilder<T> keepQuotes() {
		this.keepQuotes = true;
		return this;
	}

	public CsvToBean<T> build() {
		return new CsvToBean<T>(reader, clazz, separator, skipFirstRow, keepQuotes);
	}
}
