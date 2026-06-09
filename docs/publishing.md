---
title: Publishing to Maven Central
nav_order: 9
---

# Publishing to Maven Central
{: .no_toc }

Step-by-step guide to publish `jofrantoba-model-jpa` to Maven Central using the Sonatype Central Portal.

<details open markdown="block">
  <summary>Contents</summary>
  {: .text-delta }
- TOC
{:toc}
</details>

---

## Prerequisites

The `pom.xml` must include the following sections. This project already has them configured.

| Requirement | Element |
|---|---|
| Coordinates | `groupId`, `artifactId`, `version` |
| Project info | `name`, `description`, `url` |
| License | `<licenses>` |
| Developer info | `<developers>` |
| Source control | `<scm>` |
| Sources JAR | `maven-source-plugin` |
| Javadoc JAR | `maven-javadoc-plugin` (in `release` profile) |
| GPG signing | `maven-gpg-plugin` (in `release` profile) |
| Upload plugin | `central-publishing-maven-plugin` (in `release` profile) |

---

## Step 1 — Register and verify your namespace

1. Create an account at [central.sonatype.com](https://central.sonatype.com)
2. Go to your account → **View Namespaces**
3. Add your namespace (e.g. `com.example` for domain `example.com`)
4. Copy the **Verification Key** shown next to the namespace

### Verify via DNS TXT record

In your DNS provider's control panel, add a TXT record:

| Field | Value |
|---|---|
| Type | `TXT` |
| Host | `@` |
| Value | The Verification Key from the portal |
| TTL | 300 (or minimum available) |

Back in the portal, click **Verify Namespace**. DNS propagation is usually fast but can take up to 1 hour.

---

## Step 2 — Generate a user token

1. In [central.sonatype.com](https://central.sonatype.com), go to your account → **Generate User Token**
2. Copy the `username` and `password` values

Create `C:\Users\<your-user>\.m2\settings.xml`:

```xml
<settings>
  <servers>
    <server>
      <id>central</id>
      <username>YOUR_TOKEN_USERNAME</username>
      <password>YOUR_TOKEN_PASSWORD</password>
    </server>
  </servers>
</settings>
```

The `id` must match `publishingServerId` in the `pom.xml` (`central`).

---

## Step 3 — Generate a GPG key (Windows)

Open PowerShell and run:

```powershell
& "C:\Program Files\Git\usr\bin\gpg.exe" --gen-key
```

Enter your name, email, and a passphrase when prompted.

> **Tip:** For automated/CI deployments you can generate a key without passphrase using `--batch` mode.

### Upload your public key to keyservers

```powershell
# List your key ID
& "C:\Program Files\Git\usr\bin\gpg.exe" --list-secret-keys --keyid-format=long

# Upload to the three keyservers supported by Sonatype
& "C:\Program Files\Git\usr\bin\gpg.exe" --keyserver keyserver.ubuntu.com --send-keys YOUR_KEY_ID
& "C:\Program Files\Git\usr\bin\gpg.exe" --keyserver pgp.mit.edu --send-keys YOUR_KEY_ID
& "C:\Program Files\Git\usr\bin\gpg.exe" --keyserver keys.openpgp.org --send-keys YOUR_KEY_ID
```

Wait a few minutes before deploying so the keys propagate.

---

## Step 4 — Configure the GPG executable path

On Windows, Maven cannot find the Git-bundled `gpg.exe` automatically. The `release` profile
reads the executable from the overridable `gpg.executable` property, which defaults to the
Git-for-Windows location:

```xml
<properties>
    <gpg.executable>C:\Program Files\Git\usr\bin\gpg.exe</gpg.executable>
</properties>

<!-- in the release profile -->
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-gpg-plugin</artifactId>
    <configuration>
        <executable>${gpg.executable}</executable>
    </configuration>
    ...
</plugin>
```

On Linux/macOS or CI where `gpg` is on the `PATH`, override the default at deploy time:

```bash
mvn clean deploy -P release -DskipTests -Dgpg.executable=gpg
```

---

## Step 5 — Deploy

Run the following command from the project root:

```powershell
mvn clean deploy -P release -DskipTests
```

This will:

1. Compile and package the project
2. Generate sources and Javadoc JARs
3. Sign all artifacts with GPG
4. Bundle and upload to the Sonatype portal
5. Wait for validation

A successful run ends with:

```
[INFO] Deployment <id> has been validated.
[INFO] To finish publishing visit https://central.sonatype.com/publishing/deployments
[INFO] BUILD SUCCESS
```

---

## Step 6 — Publish from the portal

1. Go to [central.sonatype.com/publishing/deployments](https://central.sonatype.com/publishing/deployments)
2. Find your deployment in **Validated** state
3. Click **Publish**

The artifact becomes available on Maven Central within 15–30 minutes.

---

## Using the published artifact

```xml
<dependency>
    <groupId>com.jofrantoba.model.jpa</groupId>
    <artifactId>jofrantoba-model-jpa</artifactId>
    <version>2.0.1</version>
</dependency>
```

---

## Troubleshooting

### `Could not determine gpg version`

Maven cannot find the GPG executable. Check that the `gpg.executable` property in `pom.xml` (or the `-Dgpg.executable=...` override) points to a valid GPG binary. On Windows with Git for Windows the default location is `C:\Program Files\Git\usr\bin\gpg.exe`.

### `no default secret key: No secret key`

No GPG key has been generated. Follow [Step 3](#step-3--generate-a-gpg-key-windows).

### `Could not find a public key by the key fingerprint`

The public key has not propagated to the keyservers yet, or was never uploaded. Run the upload commands in [Step 3](#step-3--generate-a-gpg-key-windows) and wait a few minutes before retrying the deploy.

### `Namespace not verified`

The DNS TXT record has not been picked up yet, or the wrong domain was entered. Verify that the TXT record matches the Verification Key exactly and that the host is set to `@` (root domain).

---

## Automatic publishing (optional)

By default `autoPublish` is set to `false` so you can review the deployment before it goes live. To publish automatically without portal intervention, update `pom.xml`:

```xml
<configuration>
    <publishingServerId>central</publishingServerId>
    <autoPublish>true</autoPublish>
</configuration>
```
