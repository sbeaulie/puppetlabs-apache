import puppet.jenkins.jobdsl.builders.jobs.BaseJobBuilder
import puppet.jenkins.jobdsl.builders.views.SingleProjectNestedView

// 'this' represents a DslFactory, which is available
// automatically within the context of the 'Process DSL script'
// seed job where your groovy script is running
def builder1 = new BaseJobBuilder(this)
builder1.githubProject = "puppetlabs-apache"
builder1.description = "This is a simple job"
def firstJob = builder1.build {} // build creates the job and returns the job object
// dsl can then be added / updated with the job object
firstJob.label('worker')
