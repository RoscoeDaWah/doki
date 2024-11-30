<img src="https://git.frzn.dev/fwoppydwisk/doki/raw/branch/master/assets/logo.svg" alt="" height="100"/>
<hr>
<a href="https://git.frzn.dev/fwoppydwisk/doki/releases/latest">
  <img src="https://git.frzn.dev/fwoppydwisk/doki/badges/release.svg?style=for-the-badge" alt="">
</a>
<img src="https://git.frzn.dev/fwoppydwisk/doki/badges/stars.svg?style=for-the-badge" alt="">
<br>
A multipurpose Discord bot written in Java.

## Required Environment
- Java 17+ (May work on older versions but untested)
- Maven

### Tested Operating Systems
- macOS 14 & 15 (arm64)
- Windows 10 22H2 (x86_64)
- Debian 12 (x86_64)

---

## Config file schema
```json
{
  "token": "your_token"
}
```

---

## Commands

| Command           | Description                                                                  | Arguments                                                 |
|-------------------|------------------------------------------------------------------------------|-----------------------------------------------------------|
| `/ping`           | Gets the bot's gateway & rest ping                                           | None                                                      |
| `/whois`          | Gets information about the specified user (message author if none specified) | `[member]`                                                |
| `/timeout set`    | Times out a member for a specified amount of time                            | `[member]`, `[duration]`, `(reason)`                      |
| `/timeout get`    | Gets the current timeout status of the specified member                      | `[member]`                                                |
| `/timeout cancel` | Cancels the specified users timeout                                          | `[member]`, `(reason)`                                    |
| `/poll`           | Creates a poll                                                               | `[title]`, `[duration]`, `[options]`, `(multiple-choice)` | 
