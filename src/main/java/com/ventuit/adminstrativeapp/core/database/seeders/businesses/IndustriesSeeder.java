package com.ventuit.adminstrativeapp.core.database.seeders.businesses;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import com.ventuit.adminstrativeapp.businesses.models.IndustriesModel;
import com.ventuit.adminstrativeapp.businesses.repositories.IndustriesRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class IndustriesSeeder implements CommandLineRunner {

        private static final Logger logger = LoggerFactory.getLogger(IndustriesSeeder.class);
        private static final String DEFAULT_CREATED_BY = "Gestify solution server";

        private final IndustriesRepository industriesRepository;
        private final Map<String, String> catalogoIndustrias = new HashMap<String, String>() {
                {
                        put("Manufactura",
                                        "Industrias dedicadas a la transformación de materias primas en productos terminados o semielaborados. Incluye sectores como automotriz, textil, y metalúrgico.");

                        put("Tecnología",
                                        "Sector que incluye empresas dedicadas a la innovación y desarrollo de software, hardware, telecomunicaciones, y servicios relacionados con la tecnología.");

                        put("Construcción",
                                        "Industria encargada del diseño, construcción, y mantenimiento de infraestructuras y edificaciones, tanto públicas como privadas.");

                        put("Comercio",
                                        "Sector que abarca la compra y venta de bienes y servicios, tanto al por mayor como al por menor, en diversas áreas de consumo.");

                        put("Finanzas",
                                        "Sector dedicado a la gestión de recursos financieros, incluyendo bancos, aseguradoras, fondos de inversión, y servicios financieros en general.");

                        put("Energía",
                                        "Industrias involucradas en la producción, distribución y comercialización de energía, como el petróleo, gas, electricidad y energías renovables.");

                        put("Salud",
                                        "Sector dedicado a los servicios médicos, farmacéuticos, hospitales, clínicas y empresas relacionadas con la atención y bienestar de las personas.");

                        put("Agricultura",
                                        "Industrias dedicadas al cultivo de alimentos y otros productos agrícolas, así como la producción ganadera y silvicultura.");

                        put("Educación",
                                        "Sector dedicado a la enseñanza y formación en instituciones educativas, desde educación básica hasta niveles universitarios y de posgrado.");

                        put("Transporte y Logística",
                                        "Industrias que se encargan del transporte de bienes y personas, así como la gestión de cadenas de suministro y distribución a nivel local e internacional.");

                        put("Turismo",
                                        "Sector enfocado en la prestación de servicios relacionados con los viajes, alojamiento, recreación y actividades turísticas.");

                        put("Medios y Entretenimiento",
                                        "Industria dedicada a la creación y distribución de contenido mediático, como cine, televisión, música, y producción de eventos de entretenimiento.");

                        put("Alimentos y Bebidas",
                                        "Industrias que producen, procesan y distribuyen productos alimenticios y bebidas, tanto a nivel local como internacional.");
                }
        };

        @Override
        public void run(String... args) throws Exception {
                try {
                        insertIndustries();
                } catch (Exception error) {
                        logger.error("An error has occurred when inserting data into the industries table", error);
                }
        }

        private void insertIndustries() {
                List<IndustriesModel> industries = new ArrayList<IndustriesModel>();
                catalogoIndustrias.forEach((industry, description) -> {
                        IndustriesModel newIndustry = IndustriesModel
                                        .builder()
                                        .industry(industry)
                                        .description(description)
                                        .createdBy(IndustriesSeeder.DEFAULT_CREATED_BY)
                                        .build();
                        industries.add(newIndustry);
                });
                industriesRepository.saveAll(industries);
        }

}
