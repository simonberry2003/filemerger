package com.vinylsqueeze.filemerger;

import java.io.Writer;

public class StatefulBeanToCsvBuilder<T> {

	private Class<T> type;
	private Writer writer;
	private String quoteChar;

	public StatefulBeanToCsvBuilder(Class<T> type, Writer writer) {
		this.type = type;
		this.writer = writer;
	}

	public StatefulBeanToCsvBuilder<T> withQuotechar(String quoteChar) {
		this.quoteChar = quoteChar;
		return this;
	}

	public StatefulBeanToCsv<T> build() {
		return new StatefulBeanToCsv<T>(type, writer, quoteChar);
	}
}
