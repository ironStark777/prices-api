package dev.alexmunoz.prices;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

/**
 * Guards the hexagonal architecture boundaries: dependencies only point inwards
 * (infrastructure -> application -> domain) and the core stays framework-free.
 */
@AnalyzeClasses(packages = "dev.alexmunoz.prices", importOptions = ImportOption.DoNotIncludeTests.class)
class HexagonalArchitectureTest {

    private static final String DOMAIN = "..prices.domain..";
    private static final String APPLICATION = "..prices.application..";
    private static final String INFRASTRUCTURE = "..prices.infrastructure..";

    @ArchTest
    static final ArchRule dependenciesPointInwards = layeredArchitecture()
            .consideringOnlyDependenciesInLayers()
            .layer("Domain").definedBy(DOMAIN)
            .layer("Application").definedBy(APPLICATION)
            .layer("Infrastructure").definedBy(INFRASTRUCTURE)
            .whereLayer("Infrastructure").mayNotBeAccessedByAnyLayer()
            .whereLayer("Application").mayOnlyBeAccessedByLayers("Infrastructure")
            .whereLayer("Domain").mayOnlyBeAccessedByLayers("Application", "Infrastructure");

    @ArchTest
    static final ArchRule domainOnlyDependsOnTheJdk = classes()
            .that().resideInAPackage(DOMAIN)
            .should().onlyDependOnClassesThat().resideInAnyPackage(DOMAIN, "java..");

    @ArchTest
    static final ArchRule applicationIsFrameworkAgnostic = classes()
            .that().resideInAPackage(APPLICATION)
            .should().onlyDependOnClassesThat().resideInAnyPackage(APPLICATION, DOMAIN, "java..");
}
