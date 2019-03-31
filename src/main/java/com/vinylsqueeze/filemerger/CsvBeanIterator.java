package com.vinylsqueeze.filemerger;

import java.io.BufferedReader;
import java.lang.reflect.Field;
import java.util.Iterator;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@RequiredArgsConstructor
public class CsvBeanIterator<T> implements Iterator<T> {

	private final @NonNull BufferedReader reader;
	private final @NonNull Class<?> clazz;
	private final @NonNull String separator;
	private final boolean ignoreWhiteSpace;
	private final boolean skipFirstRow;
	private boolean firstRowSkipped;

	@Override
	@SneakyThrows
	public boolean hasNext() {
		if (skipFirstRow && !firstRowSkipped) {
			firstRowSkipped = true;
			if (reader.ready()) {
				reader.readLine();
			}
		}
		return reader.ready();
	}

	@Override
	@SneakyThrows
	public T next() {
		@SuppressWarnings("unchecked")
		T next = (T) clazz.newInstance();
		String line = reader.readLine();
		String[] columns = line.split("\\" + separator);
		if (ignoreWhiteSpace) {
			for (int i = 0; i < columns.length; i++) {
				columns[i] = columns[i].trim();
			}
		}
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			CsvBindByPosition position = field.getAnnotation(CsvBindByPosition.class);
			if (position != null) {
				field.setAccessible(true);
				if (columns.length > position.position()) {
					field.set(next, columns[position.position()]);
				}
			}
		}
		return next;
	}
}
