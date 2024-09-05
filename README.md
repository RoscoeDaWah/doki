<img src="https://git.frzn.dev/fwoppydwisk/doki/raw/branch/master/assets/logo.svg" alt="" height="100"/>
<hr>
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
  "prefix": "oki!",
  "mariadb": {
    "host": "127.0.0.1",
    "user": "db_user",
    "password": "db_password",
    "database": "doki_prod"
  }
}
```

---

## Commands

| Command       | Description                                                                  | Arguments     | Aliases |
|---------------|------------------------------------------------------------------------------|---------------|---------|
| `leaderboard` | Gets the leaderboard for the current guild                                   | None          | `lb`    |
| `rank`        | Gets the rank of the specified user (message author if none specified)       | `[user ping]` | None    |
| `about`       | Gets information about the bot                                               | None          | None    |
| `ping`        | Gets the bot's gateway & rest ping                                           | None          | None    |
| `whois`       | Gets information about the specified user (message author if none specified) | `[user ping]` | None    |