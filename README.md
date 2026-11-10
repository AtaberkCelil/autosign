# Auto Sign Text

A tiny Fabric client mod for **Minecraft 26.3** that automatically writes text into signs the moment you edit them. Place a sign, and your configured text is applied instantly — no typing required.

## Features

- **Auto text** — a configurable text is sent to the server automatically whenever a sign edit screen opens
- **ModMenu integration** — change all settings from the ModMenu config screen (via Cloth Config)
- **Configurable options**
  - Enable/disable the whole mod
  - Custom sign text (default: `Welcome!`)
  - Auto-close the sign GUI after the text is applied

## How it works

The mod injects into `AbstractSignEditScreen` and, as soon as the screen initializes, sends a `ServerboundSignUpdatePacket` with your configured text for whichever side (front/back) the sign editor targets. Works on servers and singleplayer.

## Installation

Requires:

- **Minecraft 26.3** with **Fabric Loader ≥ 0.20.1**
- **Java 25+**
- [Fabric API](https://modrinth.com/mod/fabric-api)
- [Cloth Config](https://modrinth.com/mod/cloth-config)
- [ModMenu](https://modrinth.com/mod/modmenu) (optional, for the config screen)

Drop `autosign-1.0.0.jar` into your `mods` folder.

## Configuration

Open ModMenu → find **Auto Sign Text** → config, and set:

| Option | Description | Default |
|---|---|---|
| Enable Auto Sign | Master on/off switch | `true` |
| Sign Text | The text written onto the sign | `Welcome!` |
| Auto Close GUI | Close the sign screen after applying | `true` |

Settings are stored in `config/autosign.json`.

## Building

```
./gradlew build
```

Requires Java 25 (the wrapper will not run on older JDKs). The built jar is placed in `build/libs/`. Tag a release with `v*` and GitHub Actions will build and attach the jar to a GitHub release automatically.

## License

MIT
