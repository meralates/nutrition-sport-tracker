# Security Policy

## Supported Version

Security improvements are applied to the latest version of the NutRise backend on the `master` branch.

## Reporting a Vulnerability

Please report suspected security vulnerabilities privately to the repository maintainers instead of opening a public issue containing sensitive details.

Do not include passwords, API keys, access tokens, personal data, or other credentials in public reports.

## Automated Security Checks

This repository uses automated security checks for:

- Maven, Docker and GitHub Actions dependency updates with Dependabot
- Java source code analysis with CodeQL
- Docker image vulnerability scanning with Trivy

Security findings are reviewed according to severity, exploitability, runtime exposure and whether the affected dependency is used in production.

High and Critical findings are investigated before being accepted, mitigated or deferred with documented reasoning.
