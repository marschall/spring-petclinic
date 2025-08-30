package org.springframework.samples.petclinic.db;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static java.nio.file.StandardOpenOption.WRITE;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.Year;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class GenerateLargeOwner {

	@Disabled("generates sources")
	@Test
	void generateTestData() throws IOException {
		Path output = Path.of("src/main/resources/db/h2/large-owner.sql");
		try (Writer writer = Files.newBufferedWriter(output, WRITE, TRUNCATE_EXISTING, CREATE)) {
			var context = new CreationContext(11, 13, 4);
			generateData(context, writer);
		}
	}

	private static void generateData(CreationContext context, Writer writer) throws IOException {
		generateOwner(context, writer);
		for (int i = 0; i < 500; i++) {
			generatePet(context, writer);
			generateVisit(context, writer, 500);
		}

	}

	private static void generateOwner(CreationContext context, Writer writer) throws IOException {

		writer.append("INSERT INTO owners VALUES (default, 'Cat', 'Davis', 'Catstreet', 'Gotham City', '1112223334');");
		writer.append("\n");
	}

	private static void generatePet(CreationContext context, Writer writer) throws IOException {
		LocalDate baseDate = Year.now().minusYears(1L).atDay(1);
		int petIndex = context.lastPetId() + 1;
		int typeCat = 1;
		writer.append("INSERT INTO pets VALUES (default, 'Cat%d', '%tF', %d, %d);".formatted(petIndex,
				baseDate.plusDays(petIndex), typeCat, context.getOwnerId()));
		writer.append("\n");
		context.incrementLastPetId();
	}

	private static void generateVisit(CreationContext context, Writer writer, int count) throws IOException {
		LocalDate baseDate = Year.now().atDay(1);
		// writer.append("INSERT INTO visits VALUES (default, %d, '%tF',
		// 'worms');".formatted(context.lastPetId(),
		// baseDate.plusDays(context.lastVisitId())));
		// writer.append("\n");
		// context.incrementLastVisitId();
		// writer.append("INSERT INTO visits VALUES (default, %d, '%tF',
		// 'worms');".formatted(context.lastPetId(),
		writer.append("INSERT INTO visits VALUES \n");
		for (int i = 0; i < count; i++) {
			writer.append("(default, %d, '%tF', 'worms')".formatted(context.lastPetId(),
					baseDate.plusDays(context.lastVisitId())));
			context.incrementLastVisitId();
			if (i == count - 1) {
				writer.append(";\n");
			}
			else {
				writer.append(",\n");
			}
		}
	}

	static final class CreationContext {

		int ownerId;

		int lastPetId;

		int lastVisitId;

		CreationContext(int ownerId, int lastPetId, int lastVisitId) {
			this.ownerId = ownerId;
			this.lastPetId = lastPetId;
			this.lastVisitId = lastVisitId;
		}

		int lastVisitId() {
			return this.lastVisitId;
		}

		void incrementLastVisitId() {
			this.lastVisitId += 1;
		}

		int getOwnerId() {
			return this.ownerId;
		}

		int lastPetId() {
			return this.lastPetId;
		}

		void incrementLastPetId() {
			this.lastPetId += 1;

		}

	}

}
