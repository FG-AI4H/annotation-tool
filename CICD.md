# CICD Definition

##AWS CodePipeline
We are using AWS CodePipeline

https://eu-central-1.console.aws.amazon.com/codesuite/codepipeline/pipelines?region=eu-central-1

###Backend
- Web hook set up after merge on master branch to get GitHub latest master branch source code
- Test code executing UnitTests
- Build artefact (jar) and save it to S3 fg-ai4h-oci-artefacts
- Deploy to Elastic Beanstalk

###Frontend
- Web hook set up after merge on master branch to get GitHub latest master branch source code
- Build artefact
- Deploy on S3 (s3://fg-ai4h-oci-web-app-bucket) with Cloudfront
- Invalidates CloudFront cache

##GitHub Actions
Workflow defined in ./github/workflows/

###Maven
####Sonar
####Tests

##Local build and test
Local build can also add test and code quality. Using Maven 

mvn clean verify sonar:sonar -P fg-ai4h [-Dsonar.qualitygate.wait=true]

Sonar properties have been set into ~/.m2/settings.xml with the following properties:
- sonar.host.url=https://sonarcloud.io
- sonar.login=$SONAR_TOKEN

and we created a new profile fg-ai4h with:
- sonar.projectKey=fg-ai4h
- sonar.organization=fg-ai4h

Optionally you can add -Dsonar.qualitygate.wait=true to make the job wait for the report and fail if the Quality Gates are failing
