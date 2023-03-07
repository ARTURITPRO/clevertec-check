//package edu.clevertec.check;
//
//import org.junit.jupiter.api.BeforeAll;
//import org.springframework.test.context.DynamicPropertyRegistry;
//import org.springframework.test.context.DynamicPropertySource;
//import org.springframework.test.context.jdbc.Sql;
//import org.testcontainers.containers.PostgreSQLContainer;
//
////@Transactional
////@ActiveProfiles("test")
//
//@Sql({"src/test/resources/db/migration/schema.sql", "src/test/resources/db/migration/data.sql"})
//
//public abstract class BaseIntegrationTest {
//
//    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:15.1-alpine");
//
//    @BeforeAll
//    static void runContainer() {
//        postgresContainer.start();
//    }
//
//    @DynamicPropertySource
//    static void containerProperties(DynamicPropertyRegistry registry) {
//        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
//    }
//}
