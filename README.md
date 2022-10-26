# Pi4j Samples for Freenove Smart Car

This repository contains Java "SDK" for working with Freenove Smart Car for Raspberry Pi

> ⚠️ Due to inner workings of Pi4J library that enables controlling Raspberry Pi hardware peripherals, this project requires to be ran with elevated permissions as `sudo`

## Project setup

### Prerequisites

This project assumes you already followed Raspberry Pi OS setup from [this](https://github.com/artemy/Freenove_4WD_Smart_Car_Kit_for_Raspberry_Pi/blob/master/docs/INSTALLATION.md) guide.
To install required dependencies, run the following command on your Raspberry Pi:

```shell
sudo apt-get install openjdk-17-jdk-headless maven libpigpio1 -y
```

## Enabling remote development

Quarkus supports [remote development mode](https://quarkus.io/guides/maven-tooling#remote-development-mode), so that you can run Quarkus in a remote environment (such as on Raspberry Pi) and have changes made to your local files become immediately visible.

### Raspberry Pi

To enable remote development, follow the next steps on your Raspberry Pi:

1. Check out this project on your Raspberry Pi:
   ```shell
   git clone https://github.com/artemy/pi4j-freenove-smart-car-samples.git
   ```
2. From the project directory, build the project with maven:
   ```shell
   ./mvnw clean package
   ```
3. Run the project with `QUARKUS_LAUNCH_DEVMODE` and `-Dquarkus.http.host=0.0.0.0` flag so that you can access the project remotely:
   ```shell
   sudo QUARKUS_LAUNCH_DEVMODE=true java -jar target/quarkus-app/quarkus-run.jar -Dquarkus.http.host=0.0.0.0
   ```

### Your local computer

1. Make sure you have the project cloned out locally
   ```shell
   git clone https://github.com/artemy/pi4j-freenove-smart-car-samples.git
   ```
2. Set correct remote host in [application.properties](src/main/resources/application.properties) file:
   ```properties
   quarkus.live-reload.url=http://raspberrypi.local:8080
   ```
   URL should point to the correct host and port of your quarkus instance HTTP server.
3. To connect to remote instance of Quarkus app, run the following maven command from your local source code directory:

   ```shell
   maven quarkus:remote-dev
   ```

If you followed the steps correctly, each time you access HTTP server of your remote quarkus instance, the code will be reloaded and any changes you made locally will be picked up on a remote Quarkus instance. For more details, see [Remote Development Mode](https://quarkus.io/guides/maven-tooling#remote-development-mode) Quarkus documentation.

# Dependencies

- [Quarkus](https://quarkus.io)
- [Pi4J](https://pi4j.com)
