# Profiles
The profile is changed like this in application.yml:

        spring:
            profiles:
                active: dev

## Dev
application-dev.yml

## Prod
application-prod.yml

The production profile defines a MySQL database (Bean defined in DataSourceConfig):

          jpa:
            database: mysql

Prod environment is using AWS Elastic Beanstalk with IAM instance profile: aws-elasticbeanstalk-ec2-role

# CICD Definition

##Conventional Commits
We enforced Conventional Commits on the repo be able to track changes and generate CHANGELOG files.
https://github.com/qoomon/git-conventional-commits

Pre-commit hooks defined in .git-hooks

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

### Build and Analyze Pipeline
./github/workflows/maven.yml

        mvn -B verify org.sonarsource.scanner.maven:sonar-maven-plugin:sonar -Dsonar.qualitygate.wait=true -Dsonar.organization=fg-ai4h -Dsonar.projectKey=fg-ai4h

####Tests
####Sonar
Once executed, the result of the scan is available here:
https://sonarcloud.io/summary/overall?id=fg-ai4h



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

#Misc Security
Programmatic user arn:aws:iam::601883093460:user/amplify_prog_user is MFA enabled which should be removed

We are using a MySQL database and associated a security group (sg-0a783f9a234401c53) and opened port 3306 to be reachable for dev
