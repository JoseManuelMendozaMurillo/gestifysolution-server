package com.ventuit.adminstrativeapp.core.database.seeders.businesses;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import com.ventuit.adminstrativeapp.businesses.models.TypesRegimensTaxesModel;
import com.ventuit.adminstrativeapp.businesses.repositories.TypesRegimensTaxesRepository;

import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequiredArgsConstructor
@Service
public class TypesRegimensTaxesSeeder implements CommandLineRunner {

        private static final Logger log = LoggerFactory.getLogger(BusinessesTypeSeeder.class);
        private static final String DEFAULT_CREATED_BY = "Gestify solution server";
        private static final Map<String, String> regimensTaxes = new HashMap<String, String>() {
                {
                        put("Régimen General de Ley de Personas Morales",
                                        "Régimen aplicable a empresas constituidas como personas morales, donde se declaran los ingresos, deducciones, y se calculan los impuestos conforme a la ley general del ISR.");

                        put("Régimen de Incorporación Fiscal (RIF)",
                                        "Régimen especial para pequeños contribuyentes, que les permite tributar de manera simplificada con reducción progresiva de impuestos durante los primeros 10 años.");

                        put("Régimen de Actividades Empresariales y Profesionales",
                                        "Régimen aplicable a personas físicas que realizan actividades comerciales, industriales, de autotransporte, o prestan servicios profesionales de manera independiente.");

                        put("Régimen de Arrendamiento",
                                        "Régimen para personas físicas que obtienen ingresos por el alquiler o arrendamiento de bienes inmuebles (propiedades como casas, oficinas o terrenos).");

                        put("Régimen de Sueldos y Salarios",
                                        "Régimen para personas físicas que reciben ingresos por su trabajo personal subordinado, es decir, trabajadores que reciben un salario de un empleador.");

                        put("Régimen de Dividendos",
                                        "Régimen aplicable a los accionistas de una empresa que reciben utilidades o dividendos, sujetos al pago de impuestos por estas percepciones.");

                        put("Régimen de Actividades Agrícolas, Ganaderas, Silvícolas y Pesqueras",
                                        "Régimen especial para personas físicas y morales dedicadas a actividades del sector primario, como agricultura, ganadería, silvicultura o pesca.");

                        put("Régimen de Enajenación de Bienes",
                                        "Régimen para personas físicas que obtienen ingresos por la venta o enajenación de bienes, como propiedades inmuebles o vehículos.");

                        put("Régimen de Personas Morales con Fines no Lucrativos",
                                        "Régimen aplicable a asociaciones civiles, fundaciones, y organizaciones sin fines de lucro, que están exentas del pago de ISR sobre sus ingresos.");

                        put("Régimen de Asalariados",
                                        "Régimen específico para personas físicas que trabajan por cuenta ajena (empleados), donde los impuestos son retenidos directamente por el empleador.");
                }
        };

        private final TypesRegimensTaxesRepository typesRegimensTaxesRepository;

        @Override
        public void run(String... args) throws Exception {
                try {
                        insertRegimensTaxes();
                } catch (Exception error) {
                        log.error("An error has occurred when inserting data into the tax_regimens table", error);
                }
        }

        private void insertRegimensTaxes() {
                List<TypesRegimensTaxesModel> regimens = new ArrayList<TypesRegimensTaxesModel>();
                regimensTaxes.forEach((regimen, description) -> {
                        TypesRegimensTaxesModel newRegimen = TypesRegimensTaxesModel
                                        .builder()
                                        .regimen(regimen)
                                        .description(description)
                                        .createdBy(TypesRegimensTaxesSeeder.DEFAULT_CREATED_BY)
                                        .build();
                        regimens.add(newRegimen);
                });
                typesRegimensTaxesRepository.saveAll(regimens);
                log.info("Successfully inserted {} tax regimens into the tax_regimens table", regimens.size());
        }

}
