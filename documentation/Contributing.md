# Open Code Initiative Contribution Guidelines

Thank you for your interest in contributing to the Open Code Initiative! We appreciate your effort and to make the process as smooth as possible, we've outlined a few guidelines below.

## Prerequisites

This project relies on several AWS services. You will need the following prerequisites to contribute to the project:
1. Get a GitHub and AWS account following the guidelines defined in this [documentation](https://github.com/FG-AI4H/Documentation/tree/wikiMaster/Onboarding).

In this project, we want to secure access to AWS Cloud and make sure that long lived credentials are protected with Multifactor authentication. This is the reason that your user will have MFA enabled. This comes with some additional settings that you need to be aware of.

2. Set up your AWS credentials in the credentials file:
First in the ~/.aws/credentials add your user access key and the secret key
```bash
[default]
Aws_access_key_id = xxx
Aws_secret_access_key = xxx
```
3. Then the ~/.aws/config would look something like:
```bash
[default]
Region = eu-west-1

[profile ai4h]
role_arn = arn:aws:iam::<accout_id>:role/<your_new_role_name>
mfa_serial = arn:aws:iam::<accout_id>:mfa/<your_user_name>
source_profile = default
region = eu-central-1
duration_seconds = 43200
```

4. To initiate AWS sessions from the local terminal, use the following command:

```bash
aws s3 ls --profile ai4h
```
5. Get session ID from ~/.aws/cli/cache/<latest-file>
Use the value from AccessKeyId, SecretAccessKey and SessionToken and set them in the environment variables.


## Getting Started

1. **Fork the Repository**: Start by forking the repository to your own GitHub account. This allows you to propose changes without access to the main codebase.

2. **Clone the Repository**: After forking, clone the repository to your local machine to start making changes.

```bash
git clone https://github.com/<your-username>/open-code-initiative.git
```

## Making Changes

1. **Create a Branch**: Always create a new branch for your changes. This keeps the project history clean and your changes isolated.

```bash
git checkout -b <branch-name>
```

2. **Make Your Changes**: Make your changes in the codebase. Please ensure your code adheres to the existing style guidelines.

3. **Commit Your Changes**: Commit your changes regularly with clear, concise conventional commit messages. Follow the [Conventional Commits guidelines](https://www.conventionalcommits.org/).

```bash
git commit -m "feat: Your detailed description of your changes."
```

## Submitting Changes

1. **Push Your Changes**: Once you're happy with your changes, push them to your forked repository.

```bash
git push origin <branch-name>
```
### Quality Checks

Our project uses SonarCloud for automated code quality checks. When you push your changes, they will go through a quality check using SonarCloud. We have set up quality gates to ensure the high standard of our codebase. Here are the criteria:

- No new bugs are introduced.
- Reliability rating is A.
- No new vulnerabilities are introduced.
- Security rating is A.
- New code has limited technical debt.
- Maintainability rating is A.
- All new security hotspots are reviewed.
- New code is sufficiently covered by tests. Coverage is greater than or equal to 80.0%.
- New code has limited duplication. Duplicated Lines (%) is less than or equal to 3.0%.

Please make sure your changes adhere to these quality gates before submitting a pull request. This will speed up the review process and increase the chances of your changes being merged.

2. **Create a Pull Request**: Navigate to your forked repository on GitHub and click the "New pull request" button. Fill out the pull request template with as much detail as possible.

## Code Review

1. **Wait for Review**: Once your pull request is submitted, wait for the project maintainers to review your changes. They may suggest changes or improvements.

2. **Make Requested Changes**: If changes are requested, make them and commit them to the same branch, they'll automatically be added to your pull request.

## Final Steps

1. **Merge**: Once your changes have been approved, the project maintainers will merge your changes into the main codebase.

2. **Celebrate**: Congratulations, you've just contributed to the Open Code Initiative!

Remember, the best way to contribute is to be respectful and understanding in all your interactions with project maintainers and other contributors. Happy coding!