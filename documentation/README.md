# Developer Documentation

## Project Overview

This project is a Spring Boot application written in Java, with a focus on annotation backend. It uses Maven for dependency management and build automation. The project also uses AWS SDK for interacting with AWS services like Cognito Identity Provider, Secrets Manager, S3 Control, STS, Glue, and Athena.

## Prerequisites

- Java 17
- Maven
- IntelliJ IDEA 2023.2.1 or any preferred IDE
- AWS account and access to the mentioned AWS services
- MySQL database

## Setup

1. Fork the repository to your own GitHub account.

2. Clone the forked repository to your local machine.

```bash
git clone https://github.com/<your-username>/<repository-name>.git
```

3. Open the project in your IDE.

4. Update the `application.yml` file with your local or AWS MySQL database details.

5. If you are using AWS resources, make sure to set up your AWS credentials in your environment or in the AWS credentials file.

## Running the Application

You can run the application using the following command in the terminal:

```bash
mvn spring-boot:run
```

## Testing

Unit tests can be run with the following command:

```bash
mvn test
```

## Building

To build the application, use the following command:

```bash
mvn clean install
```

## Contributing

When contributing to this project, please use the following process:

1. Create a new branch for your feature or bug fix.
2. Make your changes and write tests if necessary.
3. Commit your changes using the Conventional Commits standard.
4. Push your changes to your fork.
5. Open a pull request against the main repository.

Please note that this project uses SonarCloud for code quality checks, and AWS CodePipeline for continuous integration and deployment. Make sure your changes pass all checks before opening a pull request.

## Deployment

The project is deployed to AWS Elastic Beanstalk using AWS CodePipeline. The pipeline is triggered by a webhook on merge to the master branch. The pipeline tests the code, builds the artifact, and deploys it to Elastic Beanstalk.

## Local Development with AWS Resources

To initiate AWS sessions from the local terminal, use the following command:

```bash
aws s3 ls --profile ai4h
```

## Further Information

For more detailed information about the project, please refer to the `CICD.md` file in the repository.