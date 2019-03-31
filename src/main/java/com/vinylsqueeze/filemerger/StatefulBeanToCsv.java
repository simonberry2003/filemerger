package com.vinylsqueeze.filemerger;

import java.io.Writer;
import java.lang.reflect.Field;
import java.util.List;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@RequiredArgsConstructor
public class StatefulBeanToCsv<T> {

	private final @NonNull Class<T> type;
	private final @NonNull Writer writer;
	private final @NonNull String quoteChar;

	@SneakyThrows
	public void write(List<T> rows) {
		// Headers
		StringBuilder sb = new StringBuilder();
		Field[] fields = type.getDeclaredFields();
		String separator = "";
		for (Field field : fields) {
			CsvBindByName binder = field.getAnnotation(CsvBindByName.class);
			sb.append(binder.column());
			sb.append(separator);
			separator = ",";
		}
		sb.append("\n");
		writer.write(sb.toString());

		separator = "";
		for (T row : rows) {
			sb = new StringBuilder();
			for (Field field : fields) {
				field.setAccessible(true);
				Object rowValue = field.get(row);
				if (rowValue instanceof String && !((String) rowValue).startsWith(quoteChar)) {
					sb.append(quoteChar);
				}
				sb.append(rowValue);
				if (rowValue instanceof String && !((String) rowValue).endsWith(quoteChar)) {
					sb.append(quoteChar);
				}
				sb.append(separator);
				separator = ",";
			}
			sb.append("\n");
			writer.write(sb.toString());
		}
	}
}
