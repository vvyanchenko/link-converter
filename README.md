# Link Converter

## Description

Link Converter is a SpringBoot based web application aimed to facilitate conversion between links of different types

## Prerequisites

- JDK of version 11 or higher
- Gradle
- Docker daemon

## Launch steps

```bash
> cd ../projectRootDir
> gradle bootJar
> docker-compose -f docker-compose.yaml up --build
```

## Core API

Endpoint for URL to Deeplink conversion:
```java
POST /link/converter/url/to/deeplink
{
    "url": "url_to_convert"
}
```

Endpoint for URL to Deeplink conversion:
```java
POST /link/converter/deeplink/to/url
{
    "deeplink": "deeplink_to_convert"
}
```