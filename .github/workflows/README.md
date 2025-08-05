# GitHub Actions Workflow Documentation

This project contains two GitHub Actions workflows for automated building, testing, and releasing. The workflows are designed to work with both Maven and Makefile build systems.

## Workflow Files

### 1. CI Workflow (`ci.yml`)

**Triggers:**
- Push to `main` or `master` branch
- Pull Request to `main` or `master` branch

**Features:**
- **Dual Build System Support:**
  - Maven-based builds for standard CI/CD
  - Makefile-based builds for full product packaging
- Parallel builds for Java 8 and Java 11 (Maven only)
- Automatic Maven configuration validation
- Project compilation and testing
- Full product build with external dependencies (Makefile)
- Upload build artifacts as GitHub Artifacts

**Build Jobs:**
1. **maven-build:** Standard Maven build and test (runs on all events)
2. **full-build:** Complete product build using Makefile (runs only on main/master push)

**Build Artifacts:**
- Maven artifacts: `chaosblade-jvm-maven-{java-version}`
- Full build artifacts: `chaosblade-jvm-full-build`
- Artifacts are retained for 7 days

### 2. Release Workflow (`release.yml`)

**Triggers:**
- Push version tags (format: `v*`, e.g., `v1.7.4`)

**Features:**
- Full product build using Makefile
- Automatic version detection from git tag
- Create release packages (tar.gz and zip formats)
- Automatically create GitHub Release
- Generate release notes

## Build System Integration

### Makefile vs GitHub Actions

This project uses a hybrid approach combining both Makefile and GitHub Actions:

**Makefile (Local Development):**
- Complex build process with external dependencies
- Downloads and configures JVM Sandbox
- Creates complete product packages
- Version management and artifact organization

**GitHub Actions (CI/CD):**
- Standardized Maven builds for quick feedback
- Multi-environment testing (Java 8/11)
- Automated testing and validation
- Artifact management and release automation

### Build Strategy

1. **PR and Development:** Maven-only builds for fast feedback
2. **Main Branch:** Full Makefile builds for complete product testing
3. **Release:** Makefile builds with version-specific packaging

## Usage

### Daily Development

1. **For PRs:** Only Maven builds run for quick feedback
2. **For main branch pushes:** Both Maven and full Makefile builds run
3. Check build status on GitHub Actions page
4. If build fails, review detailed logs for fixes

### Release New Version

1. Ensure code is merged to main branch
2. Create and push version tag:
   ```bash
   git tag v1.7.5
   git push origin v1.7.5
   ```
3. Release workflow runs automatically and creates GitHub Release

### Local Development

For local development, you can use either approach:

```bash
# Standard Maven build
mvn clean install

# Full Makefile build (includes external dependencies)
make build

# Quick Java-only build
make build-java-only
```

## Build Environment

- **Operating System:** Ubuntu Latest
- **Java Versions:** 8, 11 (CI) / 11 (Release)
- **Maven:** Latest version
- **Cache:** Maven dependency cache enabled

## Troubleshooting

### Common Issues

1. **Build Failures:**
   - Check Java version compatibility
   - Review test failure details
   - Verify Maven dependencies

2. **Test Failures:**
   - Check specific test error messages
   - Verify test environment configuration

3. **Release Failures:**
   - Ensure tag format is correct (v*)
   - Check GitHub Token permissions

### Local Testing

Before pushing, it's recommended to run the same build commands locally:

```bash
# Validate configuration
mvn validate

# Compile
mvn compile

# Test
mvn test

# Package
mvn package -Dmaven.test.skip=true

# Install
mvn install -Dmaven.test.skip=true
```

## Configuration

### Environment Variables

- `GITHUB_TOKEN`: Automatically provided for GitHub API access

### Cache Configuration

- Maven dependency cache enabled for faster subsequent builds
- Cache key based on pom.xml file content

### Matrix Build

CI workflow uses matrix strategy to test both Java 8 and Java 11 simultaneously, ensuring compatibility. 