package com.ventuit.adminstrativeapp.shared.services.implementations;

import java.time.LocalDate;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ventuit.adminstrativeapp.bosses.dto.CreateBossesDto;
import com.ventuit.adminstrativeapp.bosses.services.implementations.BossesServiceImpl;
import com.ventuit.adminstrativeapp.keycloak.dto.CreateKeycloakUser;
import com.ventuit.adminstrativeapp.shared.services.interfaces.SeedServiceInterface;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;

@Service
@RequiredArgsConstructor
public class SeedServiceImpl implements SeedServiceInterface {

    private static final Logger logger = LoggerFactory.getLogger(SeedServiceImpl.class);
    private final BossesServiceImpl bossesService;

    @Override
    @Transactional
    public void seed() {
        logger.info("Seeding database...");
        generateFakeBosses(20);
    }

    public void generateFakeBosses(int numberOfBosses) {
        Faker faker = new Faker(Locale.ENGLISH);
        for (int i = 0; i < numberOfBosses; i++) {
            try {
                // --- Create fake Keycloak user DTO ---
                CreateKeycloakUser fakeUser = CreateKeycloakUser.builder()
                        .username("boss_" + faker.credentials().username() + "_" + faker.number().digits(3))
                        .email(faker.internet().emailAddress())
                        .firstName(faker.name().firstName().replaceAll("[^a-zA-ZñÑ ]", ""))
                        .lastName(faker.name().lastName().replaceAll("[^a-zA-ZñÑ ]", ""))
                        .password("Password123!") // simple default password
                        .build();

                // --- Create fake Boss DTO ---
                String countryCode = "52"; // Mexico country code
                String phoneNumber = "392" + faker.number().digits(7);
                String formattedPhone = "+" + countryCode + " " + phoneNumber;

                CreateBossesDto fakeBoss = CreateBossesDto.builder()
                        .user(fakeUser)
                        .phone(formattedPhone)
                        .birthdate(randomBirthdate())
                        .build();

                // --- Create Boss using existing logic ---
                bossesService.create(fakeBoss);

            } catch (Exception e) {
                logger.error("❌ Failed to create fake boss. See stack trace below.");
                e.printStackTrace(); // Force print the stack trace
                throw new RuntimeException("Stopping seed due to an error.", e);
            }
        }
        logger.info("✅ Successfully generated " + numberOfBosses + " fake bosses.");
    }

    private LocalDate randomBirthdate() {
        long minDay = LocalDate.of(1970, 1, 1).toEpochDay();
        long maxDay = LocalDate.of(2000, 12, 31).toEpochDay();
        long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);
        return LocalDate.ofEpochDay(randomDay);
    }
}
