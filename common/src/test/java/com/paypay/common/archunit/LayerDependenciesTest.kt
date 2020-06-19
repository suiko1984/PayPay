package com.paypay.common.archunit

import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.junit.ArchUnitRunner
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses
import org.junit.runner.RunWith

@RunWith(ArchUnitRunner::class)
@SuppressWarnings("Unused")
@AnalyzeClasses(packages = ["com.paypay.common"])
class LayerDependenciesTest {

    @ArchTest
    fun `domain layer should not depend on presentation layer`(importedClasses: JavaClasses) {
        val rule = noClasses().that().resideInAPackage("..domain..")
            .should().dependOnClassesThat().resideInAPackage("..presentation..")
        rule.check(importedClasses)
    }

    @ArchTest
    fun `domain layer should not depend on data layer`(importedClasses: JavaClasses) {
        val rule = noClasses().that().resideInAPackage("..domain..")
            .should().dependOnClassesThat().resideInAPackage("..data..")
        rule.check(importedClasses)
    }

    @ArchTest
    fun `domain layer should not depend on datasource layer`(importedClasses: JavaClasses) {
        val rule = noClasses().that().resideInAPackage("..domain..")
            .should().dependOnClassesThat().resideInAPackage("..datasource..")
        rule.check(importedClasses)
    }

    @ArchTest
    fun `presentation layer should not depend on data layer`(importedClasses: JavaClasses) {
        val rule = noClasses().that().resideInAPackage("..presentation..")
            .should().dependOnClassesThat().resideInAPackage("..data..")
        rule.check(importedClasses)
    }

    @ArchTest
    fun `presentation layer should not depend on datasource layer`(importedClasses: JavaClasses) {
        val rule = noClasses().that().resideInAPackage("..presentation..")
            .should().dependOnClassesThat().resideInAPackage("..datasource..")
        rule.check(importedClasses)
    }

    @ArchTest
    fun `data layer should not depend on presentation layer`(importedClasses: JavaClasses) {
        val rule = noClasses().that().resideInAPackage("..data..")
            .should().dependOnClassesThat().resideInAPackage("..presentation..")
        rule.check(importedClasses)
    }

    @ArchTest
    fun `data layer should not depend on datasource layer`(importedClasses: JavaClasses) {
        val rule = noClasses().that().resideInAPackage("..data..")
            .should().dependOnClassesThat().resideInAPackage("..datasource..")
        rule.check(importedClasses)
    }

    @ArchTest
    fun `domain layer should not depend on android framework`(importedClasses: JavaClasses) {
        val rule = noClasses().that().resideInAPackage("..domain..")
            .should().dependOnClassesThat().resideInAPackage("..android..")
        rule.check(importedClasses)
    }

    @ArchTest
    fun `data layer should not depend on android framework`(importedClasses: JavaClasses) {
        val rule = noClasses().that().resideInAPackage("..data..")
            .should().dependOnClassesThat().resideInAPackage("..android..")
        rule.check(importedClasses)
    }
}