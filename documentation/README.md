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

## Local Development with AWS Resources

This project relies on several AWS services. You 

To initiate AWS sessions from the local terminal, use the following command:

```bash
aws s3 ls --profile ai4h
```

## Further Information

For more detailed information about the project, please refer to the [Data annotation](Data-annotation.md).

## Contributing

If you wish to contribute or fork this project, please read the [documentation](./documentation/Contributing.md).
