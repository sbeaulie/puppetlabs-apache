/**
 * Test that the example JobDSL scripts does not raise exceptions when run.
 */

import groovy.io.FileType

import javaposse.jobdsl.dsl.*
import spock.lang.*

class SeedsValidityTest extends Specification {

  @Unroll
  def 'Generates XML for #file.name without exception'(File file) {
    setup:
    JobManagement jm = new MemoryJobManagement()

    when:
    GeneratedItems generatedItems = new DslScriptLoader(jm).runScript(file.text)
    printJobInfo(generatedItems)

    then:
    noExceptionThrown()

    where:
    file << jobFiles
  }

  static List<File> getJobFiles() {
    List<File> files = []
    new File('seeds').eachFileRecurse(FileType.FILES) {
      if (it.name.endsWith('.groovy')) {
        files << it
      }
    }
    files
  }

  static void printJobInfo(GeneratedItems generatedItems) {
    def jobs = generatedItems.jobs
    println "Test generated " + jobs.size() + " jobs"
    jobs.each {
      println it.jobName
    }
  }
}
