# jenkins-template
Template for use of puppet CI with JobDSL. It is a simple two step process

1. Merge this template in your repo's `jenkins` directory
2. Register your repo in `ci-job-configs`

## Merge the template
We recommend using `subtree` (for more info see [this blog post][subtree])
```
cd <your-repo>
git subtree add --prefix jenkins git@github.com:puppetlabs/jenkins-template.git master --squash
```
When you need to update with the latest in this jenkins-template
```
git subtree pull --prefix jenkins git@github.com:puppetlabs/jenkins-template.git master --squash
```

### Jenkins template
Copy this repo over your repository's `jenkins` sub-directory to get the layout that our CI system expects. There is one `mother seed job` per jenkins master that will scan and create the jobs based on this directory structure.

    (root of your repo)
     +- jenkins                     # main directory for CI
         +- seeds                   # job dsl directory that will get scanned to create pipelines
         |     +- exampleJobs.groovy         # script using the job DSL to create the CI pipeline
         |     +- anotherExampleJob.groovy   # you can have more than one groovy file
         +- gradle.properties       # set the shared Puppet JobDsl Library Version here
         +- build.gradle            # gradle tasks like 'test' and 'debugXML'

**note** The files outside of the seeds directory are for enabling local development and test

## Decentralized CI repositories
Previously the job definitions were all centralized and saved in the puppetlabs/ci-job-config repo with scripts written in YAML for consumption by JJB. We are now supporting decentralized scripts written in groovy and using Job DSL.

### Registering your repo
Registering your repo is a simple step that will let the system know it needs to scan your repo and all the 'valid' branches as defined by a regular expression and automatically create the necessary jobs and views on one of the jenkins masters. See registering your repo [registering repo][registering-repo]

### Using shared libraries
#### Job DSL libraries
Abstractions on top of the default JobDSL language for puppet specific jobs are created and maintained in a puppetlabs github repo [puppet-jobdsl-lib][jenkins-jobdsl-lib] that library is distributed in our nexus instance and can be pinned by your project by setting the value of `puppetJobDslLibVersion` in the file `gradle.properties` 

## Local Development aka what is all that Gradle stuff?
The gradle build tool is used to test the validity of the groovy files created locally without having to commit or access a jenkins instance. 
This also gives confidence that the scripts are valid and/or can use the shared libraries. See [gradle doc][gradle-wrapper]
### gradlew
Gradle provides a thin library called a wrapper that anyone can use in linux or windows by calling `./gradlew.sh` or `gradlew.bat`
They do *not* require gradle to be installed on the target system and do not need to be changed.
### Using the `test` task
Run the `test` task to check that all the jobs in `seeds/*.groovy` are using valid jobDSL syntax.
```
./gradlew test
:compileJava UP-TO-DATE
:compileGroovy UP-TO-DATE
:processResources UP-TO-DATE
:classes UP-TO-DATE
:compileTestJava UP-TO-DATE
:compileTestGroovy
:processTestResources UP-TO-DATE
:testClasses
:test

SeedsValidityTest > Generates XML for exampleJob.groovy without exception STANDARD_OUT
    Processing provided DSL script
    Test generated 2 jobs
    production_jenkins-jobdsl-plugin_standard_master
    production_jenkins-jobdsl-plugin_nostandard_2016.5.x

BUILD SUCCESSFUL

Total time: 5.387 secs
```
### Using the `debugXML` task
Run the `debugXML` task with `-Psource=seeds/<myfile>.groovy` to generate the job XML locally in the present working directory
```
./gradlew debugXML -Psource=seeds/exampleJob.groovy

:compileJava UP-TO-DATE
:compileGroovy UP-TO-DATE
:processResources UP-TO-DATE
:classes UP-TO-DATE
:debugXML
Processing DSL script seeds/exampleJob.groovy
Sep 19, 2016 3:38:08 PM org.codehaus.groovy.runtime.callsite.PojoMetaMethodSite$PojoCachedMethodSiteNoUnwrap invoke
INFO: From seeds/exampleJob.groovy, Generated item: GeneratedJob{name='production_jenkins-jobdsl-plugin_standard_master'}
Sep 19, 2016 3:38:08 PM org.codehaus.groovy.runtime.callsite.PojoMetaMethodSite$PojoCachedMethodSiteNoUnwrap invoke
INFO: From seeds/exampleJob.groovy, Generated item: GeneratedJob{name='production_jenkins-jobdsl-plugin_nostandard_2016.5.x'}

BUILD SUCCESSFUL

Total time: 3.249 secs
```

### Using the `dslLibs` task
This task is used by the CI system to copy locally the shared Puppet JobDSL libraries

[registering-repo]: https://github.com/puppetlabs/ci-job-configs/blob/master/doc/cinext/registering-repos.md
[puppet-jobdsl-lib]: https://github.com/puppetlabs/puppet-jobdsl-lib
[gradle-wrapper]: https://docs.gradle.org/current/userguide/gradle_wrapper.html
[subtree]: http://blogs.atlassian.com/2013/05/alternatives-to-git-submodule-git-subtree/
