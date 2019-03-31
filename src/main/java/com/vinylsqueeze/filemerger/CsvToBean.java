package com.vinylsqueeze.filemerger;

import java.io.BufferedReader;
import java.io.Reader;
import java.util.Iterator;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CsvToBean<T> implements Iterable<T> {

	private final @NonNull Reader reader;
	private final @NonNull Class<?> clazz;
	private final @NonNull String separator;
	private final boolean ignoreWhiteSpace;
	private final boolean skipFirstRow;

	@Override
	public Iterator<T> iterator() {
		return new CsvBeanIterator<T>(new BufferedReader(reader), clazz, separator, ignoreWhiteSpace, skipFirstRow);
	}
}
