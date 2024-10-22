package com.ventuit.adminstrativeapp.core.database.seeders.businesses;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import com.ventuit.adminstrativeapp.businesses.models.BusinessesTypeModel;
import com.ventuit.adminstrativeapp.businesses.repositories.BusinessesTypeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BusinessesTypeSeeder implements CommandLineRunner {

        private static final Logger log = LoggerFactory.getLogger(BusinessesTypeSeeder.class);
        private static final int DEFAULT_CREATED_BY = 1;

        private final BusinessesTypeRepository businessesTypeRepository;

        Map<String, String[]> businessesTypeCatalog = new HashMap<>() {
                {
                        put("PF", new String[] {
                                        "Persona Física",
                                        "Es un individuo que realiza actividades comerciales, empresariales o profesionales. El dueño es responsable de todas las obligaciones fiscales y legales."
                        });

                        put("PM", new String[] {
                                        "Persona Moral",
                                        "Entidad jurídica formada por dos o más personas físicas o morales, constituida legalmente para realizar actividades empresariales con responsabilidad limitada."
                        });

                        put("SA", new String[] {
                                        "Sociedad Anónima (S.A.)",
                                        "Tipo de persona moral con responsabilidad limitada, donde los accionistas responden hasta por el monto de sus aportaciones. Requiere un capital social mínimo."
                        });

                        put("SACV", new String[] {
                                        "Sociedad Anónima de Capital Variable (S.A. de C.V.)",
                                        "Variante de la Sociedad Anónima, donde el capital social puede variar sin necesidad de modificar los estatutos. Los accionistas tienen responsabilidad limitada al capital aportado."
                        });

                        put("SAPI", new String[] {
                                        "Sociedad Anónima Promotora de Inversión (S.A.P.I.)",
                                        "Tipo de sociedad que permite mayor flexibilidad en la emisión de acciones y es ideal para atraer inversionistas."
                        });

                        put("SAS", new String[] {
                                        "Sociedad por Acciones Simplificada (S.A.S.)",
                                        "Sociedad de responsabilidad limitada que puede ser constituida por una o más personas físicas a través de medios electrónicos. Ideal para emprendedores."
                        });

                        put("SC", new String[] {
                                        "Sociedad Civil (S.C.)",
                                        "Tipo de persona moral sin fines de lucro, comúnmente utilizada por profesionales (abogados, arquitectos, etc.) para asociarse con fines comunes."
                        });

                        put("SRL", new String[] {
                                        "Sociedad de Responsabilidad Limitada (S. de R.L.)",
                                        "Sociedad en la que los socios sólo responden hasta por el monto de sus aportaciones y no por deudas de la sociedad. Tiene un capital mínimo establecido."
                        });

                        put("AC", new String[] {
                                        "Asociación Civil (A.C.)",
                                        "Entidad sin fines de lucro constituida por personas físicas o morales con el propósito de realizar actividades de asistencia social, cultural, educativa, etc."
                        });

                        put("RIF", new String[] {
                                        "Régimen de Incorporación Fiscal (RIF)",
                                        "Régimen especial para pequeños contribuyentes que les permite tributar de manera simplificada con beneficios fiscales en los primeros años."
                        });

                        put("SCP", new String[] {
                                        "Sociedad Cooperativa de Producción (S.C. de P.)",
                                        "Sociedad formada por trabajadores que se asocian para producir bienes o servicios y repartir los beneficios entre los socios."
                        });

                        put("SCC", new String[] {
                                        "Sociedad Cooperativa de Consumo (S.C. de C.)",
                                        "Sociedad formada por consumidores que se asocian para comprar bienes o servicios en común, obteniendo mejores condiciones de compra."
                        });
                }
        };

        @Override
        public void run(String... args) throws Exception {
                try {
                        if (businessesTypeRepository.count() == 0) {
                                fillBusinessesTypeTable();
                        }
                } catch (Exception error) {
                        log.error("An error has occurred when inserting data into the business_type table", error);
                }
        }

        private void fillBusinessesTypeTable() {
                List<BusinessesTypeModel> businessTypes = new ArrayList<>();
                businessesTypeCatalog.forEach((key, value) -> {
                        BusinessesTypeModel businessesType = BusinessesTypeModel.builder()
                                        .code(key)
                                        .type(value[0])
                                        .description(value[1])
                                        .createdBy(DEFAULT_CREATED_BY)
                                        .build();
                        businessTypes.add(businessesType);
                });
                businessesTypeRepository.saveAll(businessTypes);
        }
}
