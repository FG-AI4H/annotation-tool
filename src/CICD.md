# CICD Definition

We are using AWS CodePipeline

https://eu-central-1.console.aws.amazon.com/codesuite/codepipeline/pipelines?region=eu-central-1

##Backend
- Web hook set up after merge on master branch to get GitHub latest master branch source code
- Test code executing UnitTests
- Build artefact (jar) and save it to S3 fg-ai4h-oci-artefacts
- Deploy to Elastic Beanstalk

##Frontend
- Web hook set up after merge on master branch to get GitHub latest master branch source code
- Build artefact
- Deploy on S3 (s3://fg-ai4h-oci-web-app-bucket) with Cloudfront
- Invalidates CloudFront cache
