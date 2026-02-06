# SDKMAN Configuration

This project is configured with SDKMAN version management files.

## Files

- `.sdkmanrc` - Main SDKMAN environment file (auto-detected by SDKMAN)
- `sdkman.config` - Additional project configuration reference

## Usage

### Enable automatic switching
To enable automatic version switching when entering this project directory:

```bash
# Enable SDKMAN auto-env feature globally
sdk auto-env enable

# Or manually install the specified versions
sdk env install
```

### Manual version switching
If auto-env is not enabled, you can manually switch versions:

```bash
# Switch Java version
sdk use java 17.0.13-amzn

# Switch Gradle version  
sdk use gradle 8.14

# Install if not available
sdk install java 17.0.13-amzn
sdk install gradle 8.14
```

### Current project settings
- **Java**: 17.0.13-amzn (Amazon Corretto)
- **Gradle**: 8.14

These versions are compatible with Spring Boot 3.x requirements.

## Prerequisites

Ensure you have SDKMAN installed:
```bash
curl -s "https://get.sdkman.io" | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"
```