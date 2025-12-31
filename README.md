# FTC 32473 – Decode

Blocks and OnBot Java code for the 2025-26 season.

## Structure

- `blocks/` – Blocks program exports (.blk)
- `onbotjava/` – OnBot Java files

## Workflow

1. Export from the Control Hub after each meeting
2. Commit with a brief message ("added arm preset positions", not "update")
3. Push

## Programming Options

FTC offers three ways to program your robot. This repo uses the first two:

| Option | Capabilities | Tradeoffs |
|--------|--------------|-----------|
| **Blocks** | Visual drag-and-drop programming. Fast edit-test cycle—connect to Control Hub and go. | Only exposes SDK features converted to visual blocks. |
| **OnBot Java** | Real Java code with custom classes, inheritance, and complex logic. Fast edit-test cycle, browser-based, no install needed. | Difficult to use external libraries. |
| **Android Studio** | Full IDE with external libraries (Road Runner, FTCLib, FTC Dashboard), debugging tools, version control, and offline development. | Slow compile/deploy/reboot cycle. IDE and Gradle issues can waste hours troubleshooting. |

Both Blocks and OnBot Java access the same FTC SDK. OnBot Java gives you direct Java access to the full API.

**Ready for Android Studio?** See the corresponding [32473-sdk-decode](https://github.com/bennington-area-robotics/32473-sdk-decode) repo for the full Android Studio project.

## About

FTC 32473 is part of [Bennington Area Robotics](https://github.com/bennington-area-robotics), based in Bennington, Vermont.

## License

MIT
