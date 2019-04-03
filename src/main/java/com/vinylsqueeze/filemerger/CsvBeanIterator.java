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
	private final boolean skipFirstRow;
	private final boolean keepQuotes;
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
		String[] columns = separator.equals(",") ? line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)") : line.split("\\" + separator);
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			CsvBindByPosition position = field.getAnnotation(CsvBindByPosition.class);
			if (position != null) {
				field.setAccessible(true);
				if (columns.length > position.position()) {
					String value = columns[position.position()];
					if (!keepQuotes) {
						value = value.replace("\"", "").replace("'", "");
					}
					field.set(next, value.trim());
				}
			}
		}
		return next;
	}
}
