package com.ventuit.adminstrativeapp.shared.services.implementations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

import com.ventuit.adminstrativeapp.bosses.dto.CreateBossesDto;
import com.ventuit.adminstrativeapp.bosses.models.BossesBusinessesModel;
import com.ventuit.adminstrativeapp.bosses.models.BossesModel;
import com.ventuit.adminstrativeapp.bosses.repositories.BossesRepository;
import com.ventuit.adminstrativeapp.bosses.services.implementations.BossesServiceImpl;
import com.ventuit.adminstrativeapp.branches.dto.CreateBranchesDto;
import com.ventuit.adminstrativeapp.branches.models.BranchesModel;
import com.ventuit.adminstrativeapp.branches.models.BranchesProductsModel;
import com.ventuit.adminstrativeapp.branches.repositories.BranchesRepository;
import com.ventuit.adminstrativeapp.branches.services.BranchesService;
import com.ventuit.adminstrativeapp.businesses.dto.CreateBusinessesDto;
import com.ventuit.adminstrativeapp.businesses.dto.ListBusinessesDto;
import com.ventuit.adminstrativeapp.businesses.models.BusinessesModel;
import com.ventuit.adminstrativeapp.businesses.models.BusinessesTypeModel;
import com.ventuit.adminstrativeapp.businesses.models.IndustriesModel;
import com.ventuit.adminstrativeapp.businesses.models.TypesRegimensTaxesModel;
import com.ventuit.adminstrativeapp.businesses.repositories.BusinessesRepository;
import com.ventuit.adminstrativeapp.businesses.repositories.BusinessesTypeRepository;
import com.ventuit.adminstrativeapp.businesses.repositories.IndustriesRepository;
import com.ventuit.adminstrativeapp.businesses.repositories.TypesRegimensTaxesRepository;
import com.ventuit.adminstrativeapp.businesses.services.BusinessesService;
import com.ventuit.adminstrativeapp.keycloak.dto.CreateKeycloakUser;
import com.ventuit.adminstrativeapp.products.dto.CreateProductDto;
import com.ventuit.adminstrativeapp.products.dto.CreateProductImageDto;
import com.ventuit.adminstrativeapp.products.dto.CreateProductsCategoryDto;
import com.ventuit.adminstrativeapp.products.dto.ListProductDto;
import com.ventuit.adminstrativeapp.products.models.ProductsModel;
import com.ventuit.adminstrativeapp.products.repositories.ProductsRepository;
import com.ventuit.adminstrativeapp.products.services.ProductsCategoriesService;
import com.ventuit.adminstrativeapp.products.services.ProductsService;
import com.ventuit.adminstrativeapp.shared.dto.DirectionsDto;
import com.ventuit.adminstrativeapp.shared.helpers.SimpleMultipartFile;
import com.ventuit.adminstrativeapp.shared.services.interfaces.SeedServiceInterface;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;

@Service
@RequiredArgsConstructor
public class SeedServiceImpl implements SeedServiceInterface {

    private static final Logger logger = LoggerFactory.getLogger(SeedServiceImpl.class);
    private final BossesServiceImpl bossesService;
    private final BossesRepository bossesRepository;
    private final BusinessesService businessesService;
    private final BusinessesRepository businessesRepository;
    private final IndustriesRepository industriesRepository;
    private final BusinessesTypeRepository businessesTypeRepository;
    private final TypesRegimensTaxesRepository typesRegimensTaxesRepository;
    private final BranchesService branchesService;
    private final BranchesRepository branchesRepository;
    private final ProductsService productsService;
    private final ProductsRepository productsRepository;
    private final ProductsCategoriesService productsCategoriesService;

    @Override
    @Transactional
    public void seed() {
        logger.info("Seeding database...");
        generateFakeBosses(20);
        generateFakeBusinesses(1);
        generateFakeBranches(1);
        int numberOfCategories = 10;
        generateFakeProductCategories(numberOfCategories);
        generateFakeProducts(5, 3, numberOfCategories);
        logger.info("Database seeding completed.");
    }

    private void generateFakeBosses(int numberOfBosses) {
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
                        .birthdate(randomLocalDate())
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

    private void generateFakeBusinesses(int numberOfBusinessesPeerBoss) {
        Faker faker = new Faker(Locale.ENGLISH);
        List<BossesModel> bosses = bossesRepository.findAll();

        for (BossesModel boss : bosses) {
            Integer[] businessIds = new Integer[numberOfBusinessesPeerBoss];
            for (int i = 0; i < numberOfBusinessesPeerBoss; i++) {
                try {
                    Integer industryId = Long.valueOf(faker.number().numberBetween(1, 13)).intValue();
                    IndustriesModel industry = industriesRepository.findById(industryId)
                            .orElse(industriesRepository.findById(1).orElse(null));

                    Integer businessesTypeId = Long.valueOf(faker.number().numberBetween(1, 12)).intValue();
                    BusinessesTypeModel businessesType = businessesTypeRepository.findById(businessesTypeId)
                            .orElse(businessesTypeRepository.findById(1).orElse(null));

                    Integer taxRegimenId = Long.valueOf(faker.number().numberBetween(1, 10)).intValue();
                    TypesRegimensTaxesModel taxRegimen = typesRegimensTaxesRepository.findById(taxRegimenId)
                            .orElse(typesRegimensTaxesRepository.findById(1).orElse(null));

                    CreateBusinessesDto fakeBusiness = CreateBusinessesDto.builder()
                            .name(faker.company().name())
                            .description(faker.company().catchPhrase())
                            .rfc(faker.regexify("[A-Z]{4}[0-9]{6}[A-Z0-9]{3}"))
                            .establishmentDate(randomLocalDate())
                            .industry(industry)
                            .businessesType(businessesType)
                            .taxRegimen(taxRegimen)
                            .logo(createFakeImage("logo", 200, 200))
                            .build();

                    ListBusinessesDto business = businessesService.create(fakeBusiness);
                    businessIds[i] = business.getId();
                } catch (Exception e) {
                    logger.error("❌ Failed to create fake business for boss " + boss.getId() + ": "
                            + e.getMessage());
                    e.printStackTrace();
                    throw new RuntimeException("Stopping seed due to an error.", e);
                }
            }
            // Link businesses to boss
            Set<BossesBusinessesModel> bossBusinesses = boss.getBossesBusinesses();
            for (Integer businessId : businessIds) {
                BusinessesModel business = businessesRepository.findById(businessId)
                        .orElseThrow(() -> new RuntimeException("Business not found"));
                BossesBusinessesModel bossBusiness = BossesBusinessesModel
                        .builder()
                        .boss(boss)
                        .businesses(business)
                        .build();

                bossBusinesses.add(bossBusiness);
            }
            boss.setBossesBusinesses(bossBusinesses);
            bossesRepository.save(boss);
        }

        logger.info("✅ Successfully generated {} fake businesses for each boss.", numberOfBusinessesPeerBoss);
    }

    private void generateFakeBranches(int numberOfBranchesPerBusiness) {
        Faker faker = new Faker(Locale.ENGLISH);
        List<BusinessesModel> businesses = businessesRepository.findAll();

        for (BusinessesModel business : businesses) {
            for (int i = 0; i < numberOfBranchesPerBusiness; i++) {
                try {
                    // --- Create fake Direction DTO ---
                    DirectionsDto fakeDirection = DirectionsDto.builder()
                            .latitude(faker.address().latitude().replace(',', '.'))
                            .longitude(faker.address().longitude().replace(',', '.'))
                            .street(faker.address().streetName())
                            .exteriorNumber(faker.number().digits(4))
                            .exteriorLetter(faker.letterify("?").toUpperCase())
                            .interiorNumber(faker.number().digits(2))
                            .interiorLetter(faker.letterify("?").toUpperCase())
                            .neighborhood(faker.address().secondaryAddress())
                            .postalCode(faker.address().zipCode().substring(0, 5))
                            .city(faker.address().city())
                            .state(faker.address().state())
                            .country(faker.address().country())
                            .build();

                    // --- Create fake Branch DTO ---
                    String countryCode = "52"; // Mexico country code
                    String phoneNumber = "392" + faker.number().digits(7);
                    String formattedPhone = "+" + countryCode + " " + phoneNumber;

                    CreateBranchesDto fakeBranch = CreateBranchesDto.builder()
                            .name(business.getName() + " " + faker.address().city())
                            .phone(formattedPhone)
                            .email(faker.internet().emailAddress())
                            .openingDate(randomLocalDate())
                            .direction(fakeDirection)
                            .businessId(business.getId())
                            .build();

                    // --- Create Branch using existing logic ---
                    branchesService.create(fakeBranch);

                } catch (Exception e) {
                    logger.error("❌ Failed to create fake branch for business " + business.getId() + ": "
                            + e.getMessage());
                    e.printStackTrace();
                    throw new RuntimeException("Stopping seed due to an error.", e);
                }
            }
        }
        logger.info("✅ Successfully generated {} fake branches for each business.", numberOfBranchesPerBusiness);
    }

    private void generateFakeProductCategories(int numberOfCategories) {
        Faker faker = new Faker(Locale.ENGLISH);
        for (int i = 0; i < numberOfCategories; i++) {
            try {
                // --- Create fake Product Category DTO ---
                String uniqueCategoryName = faker.commerce().department() + "_" + faker.number().digits(8) + "_" + i;
                CreateProductsCategoryDto fakeCategory = CreateProductsCategoryDto.builder()
                        .name(uniqueCategoryName)
                        .description(faker.lorem().sentence())
                        .image(createFakeImage("product_category_" + (i + 1), 600, 600))
                        .build();

                // --- Create Product Category using existing logic ---
                productsCategoriesService.create(fakeCategory);
            } catch (Exception e) {
                logger.error("❌ Failed to create fake product category: " + e.getMessage());
                e.printStackTrace();
                throw new RuntimeException("Stopping seed due to an error.", e);
            }
        }
        logger.info("✅ Successfully generated {} fake product categories.", numberOfCategories);
    }

    private void generateFakeProducts(int numberOfProductsPerBranch, int numberOfImagesPerProduct,
            int numberOfCategories) {
        Faker faker = new Faker(Locale.ENGLISH);
        List<BranchesModel> branches = branchesRepository.findAll();

        for (BranchesModel branch : branches) {
            Integer[] productIds = new Integer[numberOfProductsPerBranch];
            for (int i = 0; i < numberOfProductsPerBranch; i++) {
                try {
                    // --- Create fake Product DTO ---
                    String uniqueProductName = faker.commerce().productName() + "_" + faker.number().digits(8) + "_"
                            + branch.getId() + "_" + i;
                    CreateProductDto fakeProduct = CreateProductDto.builder()
                            .name(uniqueProductName)
                            .description(faker.lorem().paragraph())
                            .price(Double.parseDouble(faker.commerce().price(10.0, 100.0)))
                            .active(true)
                            .categoryId(faker.number().numberBetween(1, numberOfCategories + 1))
                            .images(createProductImages(numberOfImagesPerProduct))
                            .build();

                    // --- Create Product using existing logic ---
                    ListProductDto createdProduct = productsService.create(fakeProduct);
                    productIds[i] = createdProduct.getId();
                } catch (Exception e) {
                    logger.error("❌ Failed to create fake product for branch " + branch.getId() + ": "
                            + e.getMessage());
                    e.printStackTrace();
                    throw new RuntimeException("Stopping seed due to an error.", e);
                }
            }

            // Link products to branch
            Set<BranchesProductsModel> branchesProducts = branch.getBranchesProducts();
            for (Integer productId : productIds) {
                ProductsModel product = productsRepository.findById(productId)
                        .orElseThrow(() -> new RuntimeException("Product not found"));
                BranchesProductsModel branchesProduct = BranchesProductsModel
                        .builder()
                        .branch(branch)
                        .product(product)
                        .build();

                branchesProducts.add(branchesProduct);
            }
            branch.setBranchesProducts(branchesProducts);
            branchesRepository.save(branch);
        }
        logger.info("✅ Successfully generated {} fake products for each branch.", numberOfProductsPerBranch);
    }

    private List<CreateProductImageDto> createProductImages(int numberOfImagesPerProduct) {
        List<CreateProductImageDto> images = new ArrayList<>();
        for (int i = 0; i < numberOfImagesPerProduct; i++) {
            MultipartFile image = createFakeImage("product_image_" + (i + 1), 800, 800);
            boolean isPortrait = (i == 0); // First image is portrait
            CreateProductImageDto productImageDto = CreateProductImageDto.builder()
                    .image(image)
                    .portrait(isPortrait)
                    .build();
            images.add(productImageDto);
        }
        return images;
    }

    private LocalDate randomLocalDate() {
        long minDay = LocalDate.of(1970, 1, 1).toEpochDay();
        long maxDay = LocalDate.of(2000, 12, 31).toEpochDay();
        long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);
        return LocalDate.ofEpochDay(randomDay);
    }

    private MultipartFile createFakeImage(String name, int width, int height) {
        try {
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = image.createGraphics();

            Faker faker = new Faker();
            Color randomColor = Color.decode(faker.color().hex());
            graphics.setColor(randomColor);
            graphics.fillRect(0, 0, width, height);
            graphics.dispose();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            baos.flush();
            byte[] imageInByte = baos.toByteArray();
            baos.close();

            return new SimpleMultipartFile(
                    name,
                    name + ".png",
                    "image/png",
                    imageInByte);

        } catch (Exception e) {
            logger.error("❌ Failed to create image", e);
            return null;
        }
    }
}
