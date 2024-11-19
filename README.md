<img src="https://git.frzn.dev/fwoppydwisk/doki/raw/branch/master/assets/logo.svg" alt="" height="100"/>
<hr>
<img src="https://git.frzn.dev/fwoppydwisk/doki/badges/release.svg?style=for-the-badge" alt="">
<img src="https://git.frzn.dev/fwoppydwisk/doki/badges/stars.svg?style=for-the-badge" alt="">
<br>
A multipurpose Discord bot written in Java.

## Tested Environments
- Java 21+ (May work on older versions but untested)
- Maven

### Tested Operating Systems
- macOS 14.4.1
- Windows 10 (22H2)

---

## Config file schema
```json
{
  "token": "your_token",
  "prefix": "oki!"
}
```

---

## Commands

| Command | Description                                                                  | Arguments                           | Aliases |
|---------|------------------------------------------------------------------------------|-------------------------------------|---------|
| `about` | Gets information about the bot                                               | None                                | None    |
| `ping`  | Gets the bot's gateway & rest ping                                           | None                                | None    |
| `whois` | Gets information about the specified user (message author if none specified) | `[user ping]`                       | None    |
| `mute`  | Mutes a member for a specified amount of time                                | `[user ping]`, `[timeout duration]` | None    | 