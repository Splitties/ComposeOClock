#!/usr/bin/env kotlin

@file:DependsOn("io.github.typesafegithub:github-workflows-kt:1.11.0")

import io.github.typesafegithub.workflows.actions.actions.CheckoutV4
import io.github.typesafegithub.workflows.actions.actions.SetupJavaV4
import io.github.typesafegithub.workflows.actions.gradle.GradleBuildActionV3
import io.github.typesafegithub.workflows.actions.stefanzweifel.GitAutoCommitActionV5
import io.github.typesafegithub.workflows.domain.RunnerType.UbuntuLatest
import io.github.typesafegithub.workflows.domain.triggers.WorkflowDispatch
import io.github.typesafegithub.workflows.dsl.expressions.expr
import io.github.typesafegithub.workflows.dsl.workflow
import io.github.typesafegithub.workflows.yaml.writeToFile

workflow(
    name = "Fix workflow",
    on = listOf(
        WorkflowDispatch(),
    ),
    sourceFile = __FILE__.toPath(),
) {
    job(
        id = "fix-branch",
        runsOn = UbuntuLatest,
        // Ensure no auto-commits are ever made on the main/default branch.
        // This is an extra security measure, especially since we don't control what the
        // "stefanzweifel/git-auto-commit-action@v5" GitHub Action could theoretically do.
        `if` = expr { github.ref_name + " != " + github.eventWorkflowDispatch.repository.default_branch }
    ) {
        uses(name = "Check out", action = CheckoutV4())
        uses(
            name = "Setup Java",
            action = SetupJavaV4(distribution = SetupJavaV4.Distribution.Temurin, javaVersion = "17")
        )
        uses(
            name = "Record Screenshots",
            action = GradleBuildActionV3(
                arguments = "verifyAndRecordRoborazziDebug",
            )
        )
        uses(
            name = "Commit Screenshots",
            action = GitAutoCommitActionV5(
                filePattern = "**/src/test/screenshots/*.png",
                disableGlobbing = true,
                commitMessage = "🤖 Updates screenshots"
            )
        )
    }
}.writeToFile()
