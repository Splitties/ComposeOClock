#!/usr/bin/env kotlin

@file:DependsOn("io.github.typesafegithub:github-workflows-kt:1.11.0")

import io.github.typesafegithub.workflows.actions.actions.CheckoutV4
import io.github.typesafegithub.workflows.actions.actions.SetupJavaV4
import io.github.typesafegithub.workflows.actions.gradle.GradleBuildActionV3
import io.github.typesafegithub.workflows.domain.RunnerType.UbuntuLatest
import io.github.typesafegithub.workflows.domain.triggers.PullRequest
import io.github.typesafegithub.workflows.domain.triggers.Push
import io.github.typesafegithub.workflows.dsl.workflow
import io.github.typesafegithub.workflows.yaml.writeToFile

workflow(
    name = "Build workflow",
    on = listOf(
        Push(branches = listOf("main")),
        PullRequest(),
    ),
    sourceFile = __FILE__.toPath(),
) {
    job(id = "build-and-test", runsOn = UbuntuLatest) {
        uses(name = "Check out", action = CheckoutV4())
        uses(
            name = "Setup Java",
            action = SetupJavaV4(distribution = SetupJavaV4.Distribution.Adopt, javaVersion = "17")
        )
        uses(
            name = "Build",
            action = GradleBuildActionV3(
                arguments = "test",
            )
        )
    }
}.writeToFile()