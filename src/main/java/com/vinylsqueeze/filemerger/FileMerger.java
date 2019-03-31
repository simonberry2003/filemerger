package com.vinylsqueeze.filemerger;

import java.io.FileReader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.vinylsqueeze.domain.Note;
import com.vinylsqueeze.domain.Product;
import com.vinylsqueeze.domain.Track;

import lombok.SneakyThrows;
import lombok.val;

public class FileMerger {

	public static void main(String[] args) {
		if (args.length != 4) {
			System.out.println("usage: filemerger <product file> <track file> <note file> <output file>");
			System.exit(1);
		}
		new FileMerger(args[0], args[1], args[2], args[3]).run();
	}

	private final String productFile;
	private final String trackFile;
	private final String noteFile;
	private final String outputFile;

	public FileMerger(String productFile, String trackFile, String noteFile, String outputFile) {
		this.productFile = productFile;
		this.trackFile = trackFile;
		this.noteFile = noteFile;
		this.outputFile = outputFile;
	}

	private void run() {
		System.out.print("Loading products...");
		val products = loadProducts();
		Set<String> skus = products.stream().map(p -> p.getSku()).collect(Collectors.toSet());
		System.out.println(products.size());

		System.out.print("Loading tracks...");
		val tracks = loadTracks(skus);
		System.out.println(tracks.size());

		System.out.print("Loading notes...");
		val notes = loadNotes(skus);
		System.out.println(notes.size());

		for (val product : products) {
			val description = new StringBuilder();
			val trackListing = tracks.get(product.getSku());
			val note = notes.get(product.getSku());
			if (trackListing != null || note != null) {
				description.append("<p>");
			}
			if (trackListing != null) {
				description.append(trackListing.getListing());
			}
			if (note != null) {
				description.append(note.getNote());
			}
			if (trackListing != null || note != null) {
				description.append("</p>");
				product.setDescription(description.toString());
			}
		}
		System.out.print("Writing product file (" + outputFile + ")...");
		writeProducts(outputFile, products);
		System.out.println("done");
	}

	@SneakyThrows
	private void writeProducts(String filename, List<Product> products) {
		try (Writer writer = Files.newBufferedWriter(Paths.get(filename))) {
            StatefulBeanToCsv<Product> beanToCsv = new StatefulBeanToCsvBuilder<Product>(Product.class, writer)
                .withQuotechar("\"")
                .build();
            beanToCsv.write(products);
		}
	}

	@SneakyThrows
	private List<Product> loadProducts() {
		val products = ImmutableList.<Product>builder();
		try (val reader = new FileReader(productFile)) {
            CsvToBean<Product> csvToBean = new CsvToBeanBuilder<Product>(reader)
                .withType(Product.class)
                .withSeparator(",")
                .withIgnoreWhiteSpace(true)
                .skipFirstRow(true)
                .build();
            csvToBean.iterator().forEachRemaining(p -> {
            	if (p.isValid()) {
            		products.add(p);
            	}
            });
        }
		return products.build();
	}

	@SneakyThrows
	private Map<String, Track> loadTracks(Set<String> skus) {
		val tracks = ImmutableMap.<String, Track>builder();
		try (val reader = new FileReader(trackFile)) {
            CsvToBean<Track> csvToBean = new CsvToBeanBuilder<Track>(reader)
            	.withSeparator("|")
                .withType(Track.class)
                .withIgnoreWhiteSpace(true)
                .build();
            csvToBean.iterator().forEachRemaining(t -> {
            	if (t.getSku() != null && skus.contains(t.getSku())) {
            		tracks.put(t.getSku(), t);
            	}
            });
        }
		return tracks.build();
	}

	@SneakyThrows
	private Map<String, Note> loadNotes(Set<String> skus) {
		val notes = ImmutableMap.<String, Note>builder();
		try (val reader = new FileReader(noteFile)) {
            CsvToBean<Note> csvToBean = new CsvToBeanBuilder<Note>(reader)
            	.withSeparator("|")
                .withType(Note.class)
                .withIgnoreWhiteSpace(true)
                .build();
            csvToBean.iterator().forEachRemaining(n -> {
            	if (skus.contains(n.getSku())) {
            		notes.put(n.getSku(), n);
            	}
            });
        }
		return notes.build();
	}
}
