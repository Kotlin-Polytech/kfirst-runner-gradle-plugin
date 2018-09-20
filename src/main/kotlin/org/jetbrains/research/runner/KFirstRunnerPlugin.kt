package org.jetbrains.research.runner

import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction
import java.io.File


class KFirstRunnerPlugin : Plugin<Project> {
    override fun apply(p: Project) {}
}

open class KFirstRunnerTask : DefaultTask() {
    @get:Input
    var packages: List<String> = emptyList()
    @get:Input
    var authorFile: String = "author.name"
    @get:Input
    var ownerFile: String = "owner.name"
    @get:Input
    var resultFile: String = "results.json"
    @get:Input
    var timeout: Long = 50L

    @TaskAction
    fun kotoedRun() {
        val javaPlugin = project.convention.getPlugin(JavaPluginConvention::class.java)
        val sourceSets = javaPlugin.sourceSets
        val classpath = sourceSets.findByName("test")?.runtimeClasspath
                ?: emptyList<File>()

        val runner = org.jetbrains.research.runner.KFirstRunner()

        val args = RunnerArgs(
                projectDir = "",
                classpathPrefix = classpath.map {
                    when {
                        it.isFile -> "file:$it"
                        else -> "file:$it/"
                    }
                },
                packages = packages,
                authorFile = authorFile,
                ownerFile = ownerFile,
                resultFile = resultFile,
                timeout = timeout
        )

        runner.run(args)
    }
}
